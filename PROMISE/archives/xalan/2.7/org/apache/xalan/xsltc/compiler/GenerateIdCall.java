package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
final class GenerateIdCall extends FunctionCall {
    public GenerateIdCall(QName fname, Vector arguments) {
	super(fname, arguments);
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final InstructionList il = methodGen.getInstructionList();
	if (argumentCount() == 0) {
	   il.append(methodGen.loadContextNode());
	}
	    argument().translate(classGen, methodGen);
	}
	final ConstantPoolGen cpg = classGen.getConstantPool();
	il.append(new INVOKESTATIC(cpg.addMethodref(BASIS_LIBRARY_CLASS,
						    "generate_idF",
						    GET_NODE_NAME_SIG)));
    }
}
