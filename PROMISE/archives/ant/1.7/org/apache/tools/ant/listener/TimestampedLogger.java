package org.apache.tools.ant.listener;

import org.apache.tools.ant.DefaultLogger;

/**
 * Like a normal logger, except with timed outputs
 */
public class TimestampedLogger extends DefaultLogger {

    /**
     * what appears between the old message and the new
     */
    public static final String SPACER = " - at ";


    /**
     * This is an override point: the message that indicates whether a build failed.
     * Subclasses can change/enhance the message.
     *
     * @return The classic "BUILD FAILED" plus a timestamp
     */
    protected String getBuildFailedMessage() {
        return super.getBuildFailedMessage() + SPACER + getTimestamp();
    }

    /**
     * This is an override point: the message that indicates that a build succeeded.
     * Subclasses can change/enhance the message.
     *
     * @return The classic "BUILD SUCCESSFUL" plus a timestamp
     */
    protected String getBuildSuccessfulMessage() {
        return super.getBuildSuccessfulMessage() + SPACER + getTimestamp();
    }

}
