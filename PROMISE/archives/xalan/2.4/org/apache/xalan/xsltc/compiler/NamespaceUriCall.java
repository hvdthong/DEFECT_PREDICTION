package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.util.*;

final class NamespaceUriCall extends NameBase {

    /**
     * Handles calls with no parameter (current node is implicit parameter).
     */
    public NamespaceUriCall(QName fname) {
	super(fname);
    }

    /**
     * Handles calls with one parameter (either node or node-set).
     */
    public NamespaceUriCall(QName fname, Vector arguments) {
	super(fname, arguments);
    }

    /**
     * Translate code that leaves a node's namespace URI (as a String)
     * on the stack
     */
    public void translate(ClassGenerator classGen,
			  MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	final int getNamespace = cpg.addInterfaceMethodref(DOM_INTF,
							   "getNamespaceName",
							   "(I)"+STRING_SIG);
	super.translate(classGen, methodGen);
	il.append(new INVOKEINTERFACE(getNamespace, 2));
    }
}
