package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;

final class FloorCall extends FunctionCall {
    public FloorCall(QName fname, Vector arguments) {
	super(fname, arguments);
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	argument().translate(classGen, methodGen);
	methodGen.getInstructionList()
	    .append(new INVOKESTATIC(classGen.getConstantPool()
				     .addMethodref(MATH_CLASS,
						   "floor", "(D)D")));
    }
}
