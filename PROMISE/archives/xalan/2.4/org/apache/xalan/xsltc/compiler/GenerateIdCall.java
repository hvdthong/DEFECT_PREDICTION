package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

final class GenerateIdCall extends FunctionCall {
    public GenerateIdCall(QName fname, Vector arguments) {
	super(fname, arguments);
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final InstructionList il = methodGen.getInstructionList();
	if (argumentCount() == 0) {
	    il.append(new ILOAD(methodGen.getLocalIndex("current")));
	}
	    argument().translate(classGen, methodGen);
	}
	final ConstantPoolGen cpg = classGen.getConstantPool();
	il.append(new INVOKESTATIC(cpg.addMethodref(BASIS_LIBRARY_CLASS,
						    "generate_idF",
						    GET_NODE_NAME_SIG)));
    }
}
