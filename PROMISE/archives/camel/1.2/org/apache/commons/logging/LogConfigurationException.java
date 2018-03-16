package org.apache.commons.logging;

/**
 * An exception that is thrown only if a suitable <code>LogFactory</code>
 * or <code>Log</code> instance cannot be created by the corresponding
 * factory methods.
 *
 * @version $Id: LogConfigurationException.java 1432663 2013-01-13 17:24:18Z tn $
 */
public class LogConfigurationException extends RuntimeException {

    /** Serializable version identifier. */
    private static final long serialVersionUID = 8486587136871052495L;

    /**
     * Construct a new exception with <code>null</code> as its detail message.
     */
    public LogConfigurationException() {
        super();
    }

    /**
     * Construct a new exception with the specified detail message.
     *
     * @param message The detail message
     */
    public LogConfigurationException(String message) {
        super(message);
    }

    /**
     * Construct a new exception with the specified cause and a derived
     * detail message.
     *
     * @param cause The underlying cause
     */
    public LogConfigurationException(Throwable cause) {
        this(cause == null ? null : cause.toString(), cause);
    }

    /**
     * Construct a new exception with the specified detail message and cause.
     *
     * @param message The detail message
     * @param cause The underlying cause
     */
    public LogConfigurationException(String message, Throwable cause) {
        super(message + " (Caused by " + cause + ")");
    }

    /**
     * The underlying cause of this exception.
     */
    protected Throwable cause = null;

    /**
     * Return the underlying cause of this exception (if any).
     */
    public Throwable getCause() {
        return this.cause;
    }
}
