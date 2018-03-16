package org.apache.xalan.xsltc.compiler;

import java.util.Vector;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 */
final class NameCall extends NameBase {

    /**
     * Handles calls with no parameter (current node is implicit parameter).
     */
    public NameCall(QName fname) {
	super(fname);
    }

    /**
     * Handles calls with one parameter (either node or node-set).
     */
    public NameCall(QName fname, Vector arguments) {
	super(fname, arguments);
    }

    /**
     * Translate code that leaves a node's QName (as a String) on the stack
     */ 
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
	final ConstantPoolGen cpg = classGen.getConstantPool();
	final InstructionList il = methodGen.getInstructionList();

	final int getName = cpg.addInterfaceMethodref(DOM_INTF,
						      GET_NODE_NAME,
						      GET_NODE_NAME_SIG);
	super.translate(classGen, methodGen);
	il.append(new INVOKEINTERFACE(getName, 2));
    }
}
