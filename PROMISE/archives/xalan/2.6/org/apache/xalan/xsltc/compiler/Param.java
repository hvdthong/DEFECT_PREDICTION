package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.classfile.Field;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.IFNONNULL;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.ObjectType;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.runtime.BasisLibrary;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 * @author Erwin Bolwidt <ejb@klomp.org>
 * @author John Howard <JohnH@schemasoft.com>
 */
final class Param extends VariableBase {

    /**
     * True if this Param is declared in a simple named template.
     * This is used to optimize codegen for parameter passing
     * in named templates.
     */
    private boolean _isInSimpleNamedTemplate = false;

    /**
     * Display variable as single string
     */
    public String toString() {
	return "param(" + _name + ")";
    }

    /**
     * Set the instruction for loading the value of this variable onto the 
     * JVM stack and returns the old instruction.
     */
    public Instruction setLoadInstruction(Instruction instruction) {
        Instruction tmp = _loadInstruction;
        _loadInstruction = instruction;
        return tmp;
    }

    /**
     * Set the instruction for storing a value from the stack into this
     * variable and returns the old instruction.
     */
    public Instruction setStoreInstruction(Instruction instruction) {
        Instruction tmp = _storeInstruction;
        _storeInstruction = instruction;
        return tmp;
    }

    /**
     * Display variable in a full AST dump
     */
    public void display(int indent) {
	indent(indent);
	System.out.println("param " + _name);
	if (_select != null) {
	    indent(indent + IndentIncrement);
	    System.out.println("select " + _select.toString());
	}
	displayContents(indent + IndentIncrement);
    }

    /**
     * Parse the contents of the <xsl:param> element. This method must read
     * the 'name' (required) and 'select' (optional) attributes.
     */
    public void parseContents(Parser parser) {

	super.parseContents(parser);

	final SyntaxTreeNode parent = getParent();
	if (parent instanceof Stylesheet) {
	    _isLocal = false;
	    Param param = parser.getSymbolTable().lookupParam(_name);
	    if (param != null) {
		final int us = this.getImportPrecedence();
		final int them = param.getImportPrecedence();
		if (us == them) {
		    final String name = _name.toString();
		    reportError(this, parser, ErrorMsg.VARIABLE_REDEF_ERR,name);
		}
		else if (them > us) {
		    _ignore = true;
		    return;
		}
		else {
		    param.disable();
		}
	    }
	    ((Stylesheet)parent).addParam(this);
	    parser.getSymbolTable().addParam(this);
	}
	else if (parent instanceof Template) {
            Template template = (Template) parent;
	    _isLocal = true;
            template.addParameter(this);
            if (template.isSimpleNamedTemplate()) {
                _isInSimpleNamedTemplate = true;
            }
	}
    }

    /**
     * Type-checks the parameter. The parameter type is determined by the
     * 'select' expression (if present) or is a result tree if the parameter
     * element has a body and no 'select' expression.
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	if (_select != null) {
	    _type = _select.typeCheck(stable); 
	    if (_type instanceof ReferenceType == false && !(_type instanceof ObjectType)) {
		_select = new CastExpr(_select, Type.Reference);
	    }
	}
	else if (hasContents()) {
	    typeCheckContents(stable);
	}
	_type = Type.Reference;

	return Type.Void;
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	if (_ignore) return;
	_ignore = true;

        /*
         * To fix bug 24518 related to setting parameters of the form
         * {namespaceuri}localName which will get mapped to an instance 
         * variable in the class.
         */
	final String name = BasisLibrary.mapQNameToJavaName(_name.toString());
	final String signature = _type.toSignature();
	final String className = _type.getClassName();

	if (isLocal()) {
            /*
              * If simple named template then generate a conditional init of the 
              * param using its default value: 
              *       if (param == null) param = <default-value>
              */
            if (_isInSimpleNamedTemplate) {
		il.append(loadInstruction());
                BranchHandle ifBlock = il.append(new IFNONNULL(null));
                translateValue(classGen, methodGen);
                il.append(storeInstruction());
                ifBlock.setTarget(il.append(NOP));
                return;
            }
            
	    il.append(classGen.loadTranslet());
	    il.append(new PUSH(cpg, name));
	    translateValue(classGen, methodGen);
	    il.append(new PUSH(cpg, true));

	    il.append(new INVOKEVIRTUAL(cpg.addMethodref(TRANSLET_CLASS,
							 ADD_PARAMETER,
							 ADD_PARAMETER_SIG)));
	    if (className != EMPTYSTRING) {
		il.append(new CHECKCAST(cpg.addClass(className)));
	    }

	    _type.translateUnBox(classGen, methodGen);

		il.append(_type.POP());
		_local = null;
	    }
		_local = methodGen.addLocalVariable2(name,
						     _type.toJCType(),
						     il.getEnd());
		il.append(_type.STORE(_local.getIndex()));
	    }
	}
	else {
	    if (classGen.containsField(name) == null) {
		classGen.addField(new Field(ACC_PUBLIC, cpg.addUtf8(name),
					    cpg.addUtf8(signature),
					    null, cpg.getConstantPool()));
		il.append(classGen.loadTranslet());
		il.append(DUP);
		il.append(new PUSH(cpg, name));
		translateValue(classGen, methodGen);
		il.append(new PUSH(cpg, true));

		il.append(new INVOKEVIRTUAL(cpg.addMethodref(TRANSLET_CLASS,
						     ADD_PARAMETER,
						     ADD_PARAMETER_SIG)));

		_type.translateUnBox(classGen, methodGen);

		if (className != EMPTYSTRING) {
		    il.append(new CHECKCAST(cpg.addClass(className)));
		}
		il.append(new PUTFIELD(cpg.addFieldref(classGen.getClassName(),
						       name, signature)));
	    }
	}
    }
}
