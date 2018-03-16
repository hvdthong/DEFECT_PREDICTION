package org.apache.tools.ant.taskdefs;

/**
 * Interface to log messages for XSLT
 * @since Ant 1.5
 */
public interface XSLTLogger {
    /**
     * Log a message.
     * @param msg the message to log
     */
    void log(String msg);
}
