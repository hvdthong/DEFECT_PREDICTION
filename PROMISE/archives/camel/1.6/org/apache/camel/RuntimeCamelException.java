package org.apache.camel;

/**
 * Base class for all Camel unchecked exceptions.
 *
 * @version $Revision: 659422 $
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
