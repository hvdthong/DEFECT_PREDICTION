package org.apache.xalan.xsltc.compiler.util;

import org.apache.xalan.xsltc.compiler.Stylesheet;

/**
 * Generator for subclasses of NodeSortRecordFactory.
 */
public final class NodeSortRecordFactGenerator extends ClassGenerator {

    public NodeSortRecordFactGenerator(String className, String superClassName,
				   String fileName,
				   int accessFlags, String[] interfaces,
				   Stylesheet stylesheet) {
	super(className, superClassName, fileName,
	      accessFlags, interfaces, stylesheet);
    }
    
    /**
     * Returns <tt>true</tt> since this class is external to the
     * translet.
     */
    public boolean isExternal() {
	return true;
    }
}
