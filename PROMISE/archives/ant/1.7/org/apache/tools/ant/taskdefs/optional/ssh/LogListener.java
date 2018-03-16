package org.apache.tools.ant.taskdefs.optional.ssh;

/**
 * Interface for ssh log listeners to implement.
 */
public interface LogListener {
    /**
     * Method for the loglistener to implement to recieve log messages.
     * @param message the message to log
     */
    void log(String message);
}
