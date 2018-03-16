package org.apache.tools.ant;

/**
 * Used to report exit status of classes which call System.exit()
 *
 * @author Conor MacNeill
 */
public class ExitException extends SecurityException {

    private int status;
    
    /**
     * Constructs an exit exception.
     */
    public ExitException(int status) {
        super("ExitException: status " + status);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
