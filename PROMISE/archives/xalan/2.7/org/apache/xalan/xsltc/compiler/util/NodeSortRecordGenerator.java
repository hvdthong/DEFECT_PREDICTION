package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.Instruction;
import org.apache.xalan.xsltc.compiler.Stylesheet;

/**
 *
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 */
public final class NodeSortRecordGenerator extends ClassGenerator {
    private final Instruction _aloadTranslet;

    public NodeSortRecordGenerator(String className, String superClassName,
				   String fileName,
				   int accessFlags, String[] interfaces,
				   Stylesheet stylesheet) {
	super(className, superClassName, fileName,
	      accessFlags, interfaces, stylesheet);
	_aloadTranslet = new ALOAD(TRANSLET_INDEX);
    }
    
    /**
     * The index of the translet pointer within the execution of
     * the test method.
     */
    public Instruction loadTranslet() {
	return _aloadTranslet;
    }

    /**
     * Returns <tt>true</tt> since this class is external to the
     * translet.
     */
    public boolean isExternal() {
	return true;
    }

}
