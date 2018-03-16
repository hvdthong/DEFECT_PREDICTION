package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.*;
import org.apache.xalan.xsltc.compiler.Stylesheet;

/**
 * This class implements auxliary classes needed to compile 
 * filters (predicates). These classes defined a single method
 * of type <tt>TestGenerator</tt>.
 */
public final class FilterGenerator extends ClassGenerator {
    private final Instruction _aloadTranslet;

    public FilterGenerator(String className, String superClassName,
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
    public final Instruction loadTranslet() {
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
