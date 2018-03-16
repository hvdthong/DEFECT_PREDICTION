package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.classfile.Field;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class Param extends VariableBase {

    /**
     * Display variable as single string
     */
    public String toString() {
	return("param("+_name+")");
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
	    _isLocal = true;
	    ((Template)parent).hasParams(true);
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
	    if (_type instanceof ReferenceType == false) {
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

	final String name = getVariable();
	final String signature = _type.toSignature();
	final String className = _type.getClassName();

	if (isLocal()) {

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
