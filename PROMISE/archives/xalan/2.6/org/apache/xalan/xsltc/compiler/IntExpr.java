package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
final class IntExpr extends Expression {
    private final int _value;

    public IntExpr(int value) {
        _value = value;
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	return _type = Type.Int;
    }

    public String toString() {
	return "int-expr(" + _value + ')';
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	ConstantPoolGen cpg = classGen.getConstantPool();
	InstructionList il = methodGen.getInstructionList();
	il.append(new PUSH(cpg, _value));
    }
}
