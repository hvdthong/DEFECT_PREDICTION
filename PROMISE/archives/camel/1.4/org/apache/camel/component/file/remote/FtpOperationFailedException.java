package org.apache.camel.component.file.remote;

import org.apache.camel.RuntimeCamelException;

/**
 * @version $Revision: 630574 $
 */
public class FtpOperationFailedException extends RuntimeCamelException {
    private final int code;
    private final String reason;

    public FtpOperationFailedException(int code, String reason) {
        super("Ftp Operation failed: " + reason.trim() + " Code: " + code);
        this.code = code;
        this.reason = reason;
    }

    /**
     * Return the failure code
     */
    public int getCode() {
        return code;
    }

    /**
     * Return the failure reason
     */
    public String getReason() {
        return reason;
    }
}
