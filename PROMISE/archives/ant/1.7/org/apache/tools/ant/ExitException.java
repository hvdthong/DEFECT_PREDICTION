package org.apache.tools.ant;

/**
 * Used to report exit status of classes which call System.exit().
 *
 * @see org.apache.tools.ant.util.optional.NoExitSecurityManager
 * @see org.apache.tools.ant.types.Permissions
 *
 */
public class ExitException extends SecurityException {

    private static final long serialVersionUID = 2772487854280543363L;

    /** Status code */
    private int status;

    /**
     * Constructs an exit exception.
     * @param status the status code returned via System.exit()
     */
    public ExitException(int status) {
        super("ExitException: status " + status);
        this.status = status;
    }

    /**
     * Constructs an exit exception.
     * @param msg the message to be displayed.
     * @param status the status code returned via System.exit()
     */
    public ExitException(String msg, int status) {
        super(msg);
        this.status = status;
    }

    /**
     * The status code returned by System.exit()
     *
     * @return the status code returned by System.exit()
     */
    public int getStatus() {
        return status;
    }
}
