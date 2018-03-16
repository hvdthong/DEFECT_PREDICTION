package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.IFNE;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;

final class RoundCall extends FunctionCall {

    public RoundCall(QName fname, Vector arguments) {
	super(fname, arguments);
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	argument().translate(classGen, methodGen);
	il.append(DUP2);

	il.append(new INVOKESTATIC(cpg.addMethodref("java.lang.Double",
						    "isNaN", "(D)Z")));
	final BranchHandle skip = il.append(new IFNE(null));
	il.append(new INVOKESTATIC(cpg.addMethodref(MATH_CLASS,
						    "round", "(D)J")));
	il.append(L2D);
	skip.setTarget(il.append(NOP));
    }
}
