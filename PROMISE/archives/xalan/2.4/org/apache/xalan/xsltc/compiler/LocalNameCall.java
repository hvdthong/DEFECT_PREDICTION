package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

final class LocalNameCall extends NameBase {

    /**
     * Handles calls with no parameter (current node is implicit parameter).
     */
    public LocalNameCall(QName fname) {
	super(fname);
    }

    /**
     * Handles calls with one parameter (either node or node-set).
     */
    public LocalNameCall(QName fname, Vector arguments) {
	super(fname, arguments);
    }

    /**
     * This method is called when the constructor is compiled in
     * Stylesheet.compileConstructor() and not as the syntax tree is traversed.
     */
    public void translate(ClassGenerator classGen,
			  MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	final int getNodeName = cpg.addInterfaceMethodref(DOM_INTF,
							  "getNodeName",
							  "(I)"+STRING_SIG);

	final int getLocalName = cpg.addMethodref(BASIS_LIBRARY_CLASS,
						  "getLocalName",
						  "(Ljava/lang/String;)"+
						  "Ljava/lang/String;");
	super.translate(classGen, methodGen);
	il.append(new INVOKEINTERFACE(getNodeName, 2));
	il.append(new INVOKESTATIC(getLocalName));
    }
}
