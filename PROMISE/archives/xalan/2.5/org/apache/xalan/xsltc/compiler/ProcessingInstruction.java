package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class ProcessingInstruction extends Instruction {

    
    public void parseContents(Parser parser) {
	final String name  = getAttribute("name");
	_name = AttributeValue.create(this, name, parser);
	if (name.equals("xml")) {
	    reportError(this, parser, ErrorMsg.ILLEGAL_PI_ERR, "xml");
	}
	parseChildren(parser);
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	_name.typeCheck(stable);
	typeCheckContents(stable);
	return Type.Void;
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
						     "getValueOfPI",
						     "()" + STRING_SIG)));
	final int processingInstruction =
	    cpg.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE,
				      "processingInstruction", 
				      "(" + STRING_SIG + STRING_SIG + ")V");
	il.append(new INVOKEINTERFACE(processingInstruction, 3));
	il.append(methodGen.storeHandler());
    }
}
