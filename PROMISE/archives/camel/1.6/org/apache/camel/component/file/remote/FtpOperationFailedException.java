package org.apache.camel.component.file.remote;

import org.apache.camel.RuntimeCamelException;

/**
 * Exception thrown in case of last FTP operation failed.
 *
 * @version $Revision: 695030 $
 */
public class FtpOperationFailedException extends RuntimeCamelException {
    private final int code;
    private final String reason;

    public FtpOperationFailedException(int code, String reason) {
        super("Ftp operation failed: " + reason + ". Code: " + code);
        this.code = code;
        this.reason = reason;
    }

    public FtpOperationFailedException(int code, String reason, String message) {
        this(code, reason + " " + message);
    }

    /**
     * Return the FTP failure code
     */
    public int getCode() {
        return code;
    }

    /**
     * Return the FTP failure reason
     */
    public String getReason() {
        return reason;
    }
}
