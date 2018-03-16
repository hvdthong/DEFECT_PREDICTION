package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.classfile.Field;
import org.apache.bcel.generic.ACONST_NULL;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.DCONST;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.IntType;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.RealType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class Variable extends VariableBase {

    public int getIndex() {
	return (_local != null) ? _local.getIndex() : -1;
    }

    /**
     * Parse the contents of the variable
     */
    public void parseContents(Parser parser) {
	super.parseContents(parser);

	SyntaxTreeNode parent = getParent();
	if (parent instanceof Stylesheet) {
	    _isLocal = false;
	    Variable var = parser.getSymbolTable().lookupVariable(_name);
	    if (var != null) {
		final int us = this.getImportPrecedence();
		final int them = var.getImportPrecedence();
		if (us == them) {
		    final String name = _name.toString();
		    reportError(this, parser, ErrorMsg.VARIABLE_REDEF_ERR,name);
		}
		else if (them > us) {
		    _ignore = true;
		    return;
		}
		else {
		    var.disable();
		}
	    }
	    ((Stylesheet)parent).addVariable(this);
	    parser.getSymbolTable().addVariable(this);
	}
	else {
	    _isLocal = true;
	}
    }

    /**
     * Runs a type check on either the variable element body or the
     * expression in the 'select' attribute
     */
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {

	if (_select != null) {
	    _type = _select.typeCheck(stable);
	}
	else if (hasContents()) {
	    typeCheckContents(stable);
	    _type = Type.ResultTree;
	}
	else {
	    _type = Type.Reference;
	}
	return Type.Void;
    }

    /**
     * This method is part of a little trick that is needed to use local
     * variables inside nested for-each loops. See the initializeVariables()
     * method in the ForEach class for an explanation
     */
    public void initialize(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	if (isLocal() && !_refs.isEmpty()) {
	    if (_local == null) {
		_local = methodGen.addLocalVariable2(getEscapedName(),
						     _type.toJCType(),
						     il.getEnd());
	    }
	    if ((_type instanceof IntType) ||
		(_type instanceof NodeType) ||
		(_type instanceof BooleanType))
	    else if (_type instanceof RealType)
	    else
	    il.append(_type.STORE(_local.getIndex()));
	}
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

        if (_refs.isEmpty()) {
            _ignore = true;
        }

	if (_ignore) return;
	_ignore = true;

	final String name = getEscapedName();

	if (isLocal()) {
	    translateValue(classGen, methodGen);

	    if (_local == null) {
                mapRegister(methodGen);
            }
	    il.append(_type.STORE(_local.getIndex()));
	}
	else {
	    String signature = _type.toSignature();

	    if (classGen.containsField(name) == null) {
		classGen.addField(new Field(ACC_PUBLIC, 
					    cpg.addUtf8(name),
					    cpg.addUtf8(signature),
					    null, cpg.getConstantPool()));

		il.append(classGen.loadTranslet());
		translateValue(classGen, methodGen);
		il.append(new PUTFIELD(cpg.addFieldref(classGen.getClassName(),
						       name, signature)));
	    }
	}
    }
}
