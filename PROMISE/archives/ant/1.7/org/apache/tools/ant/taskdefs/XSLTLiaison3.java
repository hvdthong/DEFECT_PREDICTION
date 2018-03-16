package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.types.Resource;

/**
 * Extends Proxy interface for XSLT processors.
 *
 * @see XSLTProcess
 * @since Ant 1.7
 */
public interface XSLTLiaison3 extends XSLTLiaison2 {
    /**
     * sets the stylesheet to use as a resource
     * @param stylesheet the stylesheet to use as a resource
     * @throws Exception if the stylesheet cannot be loaded
     */
    void setStylesheet(Resource stylesheet) throws Exception;
}
