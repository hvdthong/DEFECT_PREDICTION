package org.apache.tools.ant;

/**
 * BuildException + exit status.
 *
 * @since Ant 1.7
 */
public class ExitStatusException extends BuildException {

    private static final long serialVersionUID = 7760846806886585968L;

    /** Status code */
    private int status;

    /**
     * Constructs an <code>ExitStatusException</code>.
     * @param status the associated status code
     */
    public ExitStatusException(int status) {
        super();
        this.status = status;
    }

    /**
     * Constructs an <code>ExitStatusException</code>.
     * @param msg the associated message
     * @param status the associated status code
     */
    public ExitStatusException(String msg, int status) {
        super(msg);
        this.status = status;
    }

    /**
     * Construct an exit status exception with location information too
     * @param message error message
     * @param status exit status
     * @param location exit location
     */
    public ExitStatusException(String message, int status, Location location) {
        super(message, location);
        this.status = status;
    }

    /**
     * Get the status code.
     * @return <code>int</code>
     */
    public int getStatus() {
        return status;
    }
}
