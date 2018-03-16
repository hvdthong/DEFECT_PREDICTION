package org.apache.tools.ant;

/**
 * BuildException + exit status.
 *
 * @since Ant 1.7
 */
public class ExitStatusException extends BuildException {

    /** Status code */
    private int status;

    /**
     * Constructs an <CODE>ExitStatusException</CODE>.
     * @param status the associated status code
     */
    public ExitStatusException(int status) {
        super();
        this.status = status;
    }

    /**
     * Constructs an <CODE>ExitStatusException</CODE>.
     * @param msg the associated message
     * @param status the associated status code
     */
    public ExitStatusException(String msg, int status) {
        super(msg);
        this.status = status;
    }

    /**
     * Get the status code.
     * @return <CODE>int</CODE>
     */
    public int getStatus() {
        return status;
    }
}
