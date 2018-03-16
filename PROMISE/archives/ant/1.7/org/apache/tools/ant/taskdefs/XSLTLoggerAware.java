package org.apache.tools.ant.taskdefs;

/**
 * Interface for a class that one can set an XSLTLogger on.
 * @since Ant 1.5
 */
public interface XSLTLoggerAware {
    /**
     * Set the logger for this class.
     * @param l the logger
     */
    void setLogger(XSLTLogger l);
}
