package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

/**
 * This class implements inlined calls to the XSLT standard functions 
 * true() and false().
 */
final class BooleanExpr extends Expression {
    private boolean _value;

    public BooleanExpr(boolean value) {
	_value = value;
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	_type = Type.Boolean;
	return _type;
    }

    public String toString() {
	return _value ? "true()" : "false()";
    }

    public boolean getValue() {
	return _value;
    }

    public boolean contextDependent() {
	return false;
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	ConstantPoolGen cpg = classGen.getConstantPool();
	InstructionList il = methodGen.getInstructionList();
	il.append(new PUSH(cpg, _value));
    }

    public void translateDesynthesized(ClassGenerator classGen,
				       MethodGenerator methodGen) {
	final InstructionList il = methodGen.getInstructionList();
	if (_value) {
	}
	else {
	    _falseList.add(il.append(new GOTO(null)));
	}
    }
}
