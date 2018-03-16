package org.apache.tools.ant.types;

import org.apache.tools.ant.Project;

/**
 * The enumerated values for Ant's log level.
 */
public class LogLevel extends EnumeratedAttribute {

    /** ERR loglevel constant. */
    public static final LogLevel ERR = new LogLevel("error");

    /** WARN loglevel constant. */
    public static final LogLevel WARN = new LogLevel("warn");

    /** INFO loglevel constant. */
    public static final LogLevel INFO = new LogLevel("info");

    /** VERBOSE loglevel constant. */
    public static final LogLevel VERBOSE = new LogLevel("verbose");

    /** DEBUG loglevel constant. */
    public static final LogLevel DEBUG = new LogLevel("debug");

    /**
     * Public constructor.
     */
    public LogLevel() {
    }

    private LogLevel(String value) {
        this();
        setValue(value);
    }

    /**
     * @see EnumeratedAttribute#getValues
     * @return the strings allowed for the level attribute
     */
    public String[] getValues() {
        return new String[] {
            "error",
            "warn",
            "warning",
            "info",
            "verbose",
            "debug"};
    }

    /**
     * mapping of enumerated values to log levels
     */
    private static int[] levels = {
        Project.MSG_ERR,
        Project.MSG_WARN,
        Project.MSG_WARN,
        Project.MSG_INFO,
        Project.MSG_VERBOSE,
        Project.MSG_DEBUG
    };

    /**
     * get the level of the echo of the current value
     * @return the level
     */
    public int getLevel() {
        return levels[getIndex()];
    }
}
