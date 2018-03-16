package org.apache.camel;

/**
 * @version $Revision: 563607 $
 */
public class RuntimeCamelException extends RuntimeException {
    private static final long serialVersionUID = 8046489554418284257L;

    public RuntimeCamelException() {
    }

    public RuntimeCamelException(String message) {
        super(message);
    }

    public RuntimeCamelException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeCamelException(Throwable cause) {
        super(cause);
    }
}
