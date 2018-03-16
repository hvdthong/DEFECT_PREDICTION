package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

final class RealExpr extends Expression {
    private double _value;

    public RealExpr(double value) {
	_value = value;
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	return _type = Type.Real;
    }

    public String toString() {
	return "real-expr(" + _value + ')';
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	ConstantPoolGen cpg = classGen.getConstantPool();
	InstructionList il = methodGen.getInstructionList();
	il.append(new PUSH(cpg, _value));
    }
}
