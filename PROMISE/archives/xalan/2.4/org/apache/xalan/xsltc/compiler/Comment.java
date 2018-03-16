package org.apache.xalan.xsltc.compiler;

import javax.xml.parsers.*;

import org.xml.sax.*;

import java.util.Vector;
import java.util.Enumeration;

import org.apache.xalan.xsltc.compiler.util.Type;

import org.apache.bcel.generic.*;
import org.apache.bcel.classfile.JavaClass;

import org.apache.xalan.xsltc.compiler.util.*;

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
