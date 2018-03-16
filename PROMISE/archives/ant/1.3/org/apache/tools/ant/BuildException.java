package org.apache.tools.ant;


import java.io.*;

/**
 * Signals an error condition during a build.
 *
 * @author James Duncan Davidson
 */
public class BuildException extends RuntimeException {

    /** Exception that might have caused this one. */
    private Throwable cause;

    /** Location in the build file where the exception occured */
    private Location location = Location.UNKNOWN_LOCATION;

    /**
     * Constructs a build exception with no descriptive information.
     */
    public BuildException() {
        super();
    }

    /**
     * Constructs an exception with the given descriptive message.
     * @param msg Description of or information about the exception.
     */
    public BuildException(String msg) {
        super(msg);
    }

    /**
     * Constructs an exception with the given message and exception as
     * a root cause.
     * @param msg Description of or information about the exception.
     * @param cause Throwable that might have cause this one.
     */
    public BuildException(String msg, Throwable cause) {
        super(msg);
        this.cause = cause;
    }

    /**
     * Constructs an exception with the given message and exception as
     * a root cause and a location in a file.
     * @param msg Description of or information about the exception.
     * @param cause Exception that might have cause this one.
     * @param location Location in the project file where the error occured.
     */
    public BuildException(String msg, Throwable cause, Location location) {
        this(msg, cause);
        this.location = location;
    }

    /**
     * Constructs an exception with the given exception as a root cause.
     * @param cause Exception that might have caused this one.
     */
    public BuildException(Throwable cause) {
        super(cause.toString());
        this.cause = cause;
    }

    /**
     * Constructs an exception with the given descriptive message and a location
     * in a file.
     * @param msg Description of or information about the exception.
     * @param location Location in the project file where the error occured.
     */
    public BuildException(String msg, Location location) {
        super(msg);
        this.location = location;
    }

    /**
     * Constructs an exception with the given exception as
     * a root cause and a location in a file.
     * @param cause Exception that might have cause this one.
     * @param location Location in the project file where the error occured.
     */
    public BuildException(Throwable cause, Location location) {
        this(cause);
        this.location = location;
    }

    /**
     * Returns the nested exception.
     */
    public Throwable getException() {
        return cause;
    }

    /**
     * Returns the location of the error and the error message.
     */
    public String toString() {
        return location.toString() + getMessage();
    }

    /**
     * Sets the file location where the error occured.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Returns the file location where the error occured.
     */
    public Location getLocation() {
        return location;
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }
    
    public void printStackTrace(PrintStream ps) {
        synchronized (ps) {
            ps.println(this);
            if (cause != null) {
                ps.println("--- Nested Exception ---");
                cause.printStackTrace(ps);
            }
        }
    }
    
    public void printStackTrace(PrintWriter pw) {
        synchronized (pw) {
            pw.println(this);
            if (cause != null) {
                pw.println("--- Nested Exception ---");
                cause.printStackTrace(pw);
            }
        }
    }
}
