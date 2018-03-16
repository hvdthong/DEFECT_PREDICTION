package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.Type;

import org.apache.bcel.generic.*;
import org.apache.bcel.classfile.JavaClass;

import org.apache.xalan.xsltc.compiler.util.*;

final class Message extends Instruction {
    private boolean _terminate = false;
	
    public void parseContents(Parser parser) {
	String termstr = getAttribute("terminate");
	if (termstr != null) {
            _terminate = termstr.equals("yes");
	}
	parseChildren(parser);
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	typeCheckContents(stable);
	return Type.Void;
    }

    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	il.append(classGen.loadTranslet());

	compileResultTree(classGen, methodGen);
	final int toStr = cpg.addInterfaceMethodref(DOM_INTF,
						    "getTreeString",
						    "()"+STRING_SIG);
	il.append(new INVOKEINTERFACE(toStr, 1));

	il.append(new INVOKEVIRTUAL(cpg.addMethodref(TRANSLET_CLASS,
						     "displayMessage",
						     "("+STRING_SIG+")V")));

	if (_terminate == true) {
	    final int einit = cpg.addMethodref("java.lang.RuntimeException",
					       "<init>",
					       "(Ljava/lang/String;)V");
	    il.append(new NEW(cpg.addClass("java.lang.RuntimeException")));
	    il.append(DUP);
	    il.append(new PUSH(cpg,"Termination forced by an " +
			           "xsl:message instruction"));
	    il.append(new INVOKESPECIAL(einit));
	    il.append(ATHROW);
	}
    }
    
}
