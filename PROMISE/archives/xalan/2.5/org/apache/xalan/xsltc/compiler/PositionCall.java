package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.CompareGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.TestGenerator;

final class PositionCall extends FunctionCall {

    public PositionCall(QName fname) {
	super(fname);
    }

    public boolean hasPositionCall() {
	return true;
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final InstructionList il = methodGen.getInstructionList();

	if (methodGen instanceof CompareGenerator) {
	    il.append(((CompareGenerator)methodGen).loadCurrentNode());
	}
	else if (methodGen instanceof TestGenerator) {
	    il.append(new ILOAD(POSITION_INDEX));
	}
	else {
	    final ConstantPoolGen cpg = classGen.getConstantPool();
            final int index =
                    cpg.addMethodref(BASIS_LIBRARY_CLASS, "positionF",
                                     "("+NODE_ITERATOR_SIG+")I");

	    il.append(methodGen.loadIterator());
            il.append(new INVOKESTATIC(index));
	}
    }
}
