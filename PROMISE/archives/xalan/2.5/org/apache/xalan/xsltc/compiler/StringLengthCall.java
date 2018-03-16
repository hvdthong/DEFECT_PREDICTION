package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;

final class StringLengthCall extends FunctionCall {
    public StringLengthCall(QName fname, Vector arguments) {
	super(fname, arguments);
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();
	if (argumentCount() > 0) {
	    argument().translate(classGen, methodGen);
	}
	else {
	    il.append(methodGen.loadContextNode());
	    Type.Node.translateTo(classGen, methodGen, Type.String);
	}
	il.append(new INVOKEVIRTUAL(cpg.addMethodref(STRING_CLASS,
						     "length", "()I")));
    }
}
