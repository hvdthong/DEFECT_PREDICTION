package org.apache.camel;

/**
 * Base class for all Camel checked exceptions typically thrown by a
 * {@link Processor}
 *
 * @version $Revision: $
 */
public class CamelException extends Exception {

    public CamelException() {
    }

    public CamelException(String message) {
        super(message);
    }

    public CamelException(String message, Throwable cause) {
        super(message, cause);
    }

    public CamelException(Throwable cause) {
        super(cause);
    }
}
