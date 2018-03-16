package org.apache.tools.ant;

/**
 * Used to report exit status of classes which call System.exit().
 *
 * @see org.apache.tools.ant.util.optional.NoExitSecurityManager
 *
 * @author Conor MacNeill
 */
public class ExitException extends SecurityException {

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
     * The status code returned by System.exit()
     *
     * @return the status code returned by System.exit()
     */
    public int getStatus() {
        return status;
    }
}
