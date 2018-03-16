package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.Util;

class VariableBase extends TopLevelElement {


    protected Vector      _refs = new Vector(2); 

    protected Vector      _dependencies = null;

    protected boolean    _ignore = false;

    protected int        _weight = 0;

    /**
     * Disable this variable/parameter
     */
    public void disable() {
	_ignore = true;
    }

    /**
     * Add a reference to this variable. Called by VariableRef when an
     * expression contains a reference to this variable.
     */
    public void addReference(VariableRefBase vref) {
	_refs.addElement(vref);
    }

    /**
     * Remove a reference to this variable. Called by VariableRef when this
     * variable goes out of scope.
     */
    public void removeReference(VariableRefBase vref) {
	_refs.remove(vref);
    }

    /**
     *
     */
    public void addDependency(VariableBase other) {
	if (_dependencies == null) {
	    _dependencies = new Vector();
	}
	if (!_dependencies.contains(other)) {
	    _dependencies.addElement(other);
	}
    }

    /**
     *
     */
    public Vector getDependencies() {
	return _dependencies;
    }

    /**
     * Map this variable to a register
     */
    public void mapRegister(MethodGenerator methodGen) {
        if (_local == null) {
            final InstructionList il = methodGen.getInstructionList();
	    final org.apache.bcel.generic.Type varType = _type.toJCType();
            _local = methodGen.addLocalVariable2(name, varType, il.getEnd());
        }
    }

    /**
     * Remove the mapping of this variable to a register.
     * Called when we leave the AST scope of the variable's declaration
     */
    public void unmapRegister(MethodGenerator methodGen) {
	if (_refs.isEmpty() && (_local != null)) {
	    _local.setEnd(methodGen.getInstructionList().getEnd());
	    methodGen.removeLocalVariable(_local);
	    _refs = null;
	    _local = null;
	}
    }

    /**
     * Returns a handle to the instruction for loading the value of this
     * variable onto the JVM stack.
     */
    public Instruction loadInstruction() {
	final Instruction instr = _loadInstruction;
	if (_loadInstruction == null) 
	    _loadInstruction = _type.LOAD(_local.getIndex());
	return _loadInstruction;
    }

    /**
     * Returns the expression from this variable's select attribute (if any)
     */
    public Expression getExpression() {
	return(_select);
    }

    /**
     * Display variable as single string
     */
    public String toString() {
	return("variable("+_name+")");
    }

    /**
     * Display variable in a full AST dump
     */
    public void display(int indent) {
	indent(indent);
	System.out.println("Variable " + _name);
	if (_select != null) { 
	    indent(indent + IndentIncrement);
	    System.out.println("select " + _select.toString());
	}
	displayContents(indent + IndentIncrement);
    }

    /**
     * Returns the type of the variable
     */
    public Type getType() {
	return _type;
    }

    /**
     * Returns the name of the variable or parameter as it will occur in the
     * compiled translet.
     */
    public QName getName() {
	return _name;
    }

    /**
     * Returns the name of the variable or parameter as it occured in the
     * stylesheet.
     */
    public String getVariable() {
	return _variable;
    }

    /**
     * Set the name of the variable or paremeter. Escape all special chars.
     */
    public void setName(QName name) {
	_name = name;
	_variable = Util.escape(name.getLocalPart());
    }

    /**
     * Returns the true if the variable is local
     */
    public boolean isLocal() {
	return _isLocal;
    }

    /**
     * Parse the contents of the <xsl:decimal-format> element.
     */
    public void parseContents(Parser parser) {
	String name = getAttribute("name");
	if (name == null) name = EMPTYSTRING;

	if (name.length() > 0)
	    setName(parser.getQNameIgnoreDefaultNs(name));
        else
	    reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, "name");

	VariableBase other = parser.lookupVariable(_name);
	if ((other != null) && (other.getParent() == getParent())) {
	    reportError(this, parser, ErrorMsg.VARIABLE_REDEF_ERR, name);
	}
	
	select = getAttribute("select");
	if (select.length() > 0) {
	    _select = getParser().parseExpression(this, "select", null);
	    if (_select.isDummy()) {
		reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, "select");
		return;
	    }
	}

	parseChildren(parser);
    }

    /**
     * Compile the value of the variable, which is either in an expression in
     * a 'select' attribute, or in the variable elements body
     */
    public void translateValue(ClassGenerator classGen,
			       MethodGenerator methodGen) {
	if (_select != null) {
	    _select.translate(classGen, methodGen);
	    _select.startResetIterator(classGen, methodGen);
	}
	else if (hasContents()) {
	    compileResultTree(classGen, methodGen);
	}
	else {
	    final ConstantPoolGen cpg = classGen.getConstantPool();
	    final InstructionList il = methodGen.getInstructionList();
	    il.append(new PUSH(cpg, Constants.EMPTYSTRING));
	}
    }

}
