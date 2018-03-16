package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class Comment extends Instruction {

    public void parseContents(Parser parser) {
	parseChildren(parser);
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	typeCheckContents(stable);
	return Type.String;
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	il.append(methodGen.loadHandler());

	il.append(classGen.loadTranslet());
	il.append(new GETFIELD(cpg.addFieldref(TRANSLET_CLASS,
					       "stringValueHandler",
					       STRING_VALUE_HANDLER_SIG)));
	il.append(DUP);
	il.append(methodGen.storeHandler());

	translateContents(classGen, methodGen);

	il.append(new INVOKEVIRTUAL(cpg.addMethodref(STRING_VALUE_HANDLER,
						     "getValue",
						     "()" + STRING_SIG)));
	final int comment =
	    cpg.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE,
				      "comment", 
				      "(" + STRING_SIG + ")V");
	il.append(new INVOKEINTERFACE(comment, 2));
	il.append(methodGen.storeHandler());
    }
}
