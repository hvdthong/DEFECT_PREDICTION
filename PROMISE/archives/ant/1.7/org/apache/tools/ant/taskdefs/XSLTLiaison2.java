package org.apache.tools.ant.taskdefs;

/**
 * Extended Proxy interface for XSLT processors.
 *
 * @see XSLTProcess
 * @since Ant 1.6
 */
public interface XSLTLiaison2 extends XSLTLiaison {
    /**
     * Configure the liasion from the XSLTProcess task
     * @param xsltTask the XSLTProcess task
     */
    void configure(XSLTProcess xsltTask);
}
