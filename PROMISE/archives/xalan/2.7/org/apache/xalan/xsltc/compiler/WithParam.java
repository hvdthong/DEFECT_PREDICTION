package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.utils.XML11Char;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 * @author John Howard <JohnH@schemasoft.com>
 */
final class WithParam extends Instruction {

    /**
     * Parameter's name.
     */
    private QName _name;
    
    /**
     * The escaped qname of the with-param.
     */
    protected String _escapedName;
    
    /**
     * Parameter's default value.
     */
    private Expression _select;

    /**
     * %OPT% This is set to true when the WithParam is used in a CallTemplate
     * for a simple named template. If this is true, the parameters are 
     * passed to the named template through method arguments rather than
     * using the expensive Translet.addParameter() call.
     */
    private boolean _doParameterOptimization = false;

    /**
     * Displays the contents of this element
     */
    public void display(int indent) {
	indent(indent);
	Util.println("with-param " + _name);
	if (_select != null) {
	    indent(indent + IndentIncrement);
	    Util.println("select " + _select.toString());
	}
	displayContents(indent + IndentIncrement);
    }
    
    /**
     * Returns the escaped qname of the parameter
     */
    public String getEscapedName() {
	return _escapedName;
    }    
    
    /**
     * Return the name of this WithParam.
     */
    public QName getName() {
        return _name;	
    }
    
    /**
     * Set the name of the variable or paremeter. Escape all special chars.
     */
    public void setName(QName name) {
	_name = name;
	_escapedName = Util.escape(name.getStringRep());
    }    
    
    /**
     * Set the do parameter optimization flag
     */
    public void setDoParameterOptimization(boolean flag) {
    	_doParameterOptimization = flag;
    }
    
    /**
     * The contents of a <xsl:with-param> elements are either in the element's
     * 'select' attribute (this has precedence) or in the element body.
     */
    public void parseContents(Parser parser) {
	final String name = getAttribute("name");
	if (name.length() > 0) {
            if (!XML11Char.isXML11ValidQName(name)) {
                ErrorMsg err = new ErrorMsg(ErrorMsg.INVALID_QNAME_ERR, name,
                                            this);
                parser.reportError(Constants.ERROR, err);
            }
	    setName(parser.getQNameIgnoreDefaultNs(name));
	}
        else {
	    reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, "name");
        }
	
	final String select = getAttribute("select");
	if (select.length() > 0) {
	    _select = parser.parseExpression(this, "select", null);
	}
	
	parseChildren(parser);
    }

    /**
     * Type-check either the select attribute or the element body, depending
     * on which is in use.
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	if (_select != null) {
	    final Type tselect = _select.typeCheck(stable);
	    if (tselect instanceof ReferenceType == false) {
		_select = new CastExpr(_select, Type.Reference);
	    }
	}
	else {
	    typeCheckContents(stable);
	}
	return Type.Void;
    }

    /**
     * Compile the value of the parameter, which is either in an expression in
     * a 'select' attribute, or in the with-param element's body
     */
    public void translateValue(ClassGenerator classGen,
			       MethodGenerator methodGen) {
	if (_select != null) {
	    _select.translate(classGen, methodGen);
	    _select.startIterator(classGen, methodGen);
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

    /**
     * This code generates a sequence of bytecodes that call the
     * addParameter() method in AbstractTranslet. The method call will add
     * (or update) the parameter frame with the new parameter value.
     */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	if (_doParameterOptimization) {
	    translateValue(classGen, methodGen);
	    return;
	}
	
	String name = Util.escape(getEscapedName());

	il.append(classGen.loadTranslet());

	translateValue(classGen, methodGen);
	il.append(new PUSH(cpg, false));
	il.append(new INVOKEVIRTUAL(cpg.addMethodref(TRANSLET_CLASS,
						     ADD_PARAMETER,
						     ADD_PARAMETER_SIG)));
    }
}
