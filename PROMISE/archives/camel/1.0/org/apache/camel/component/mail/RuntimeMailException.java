package org.apache.camel.component.mail;

/**
 * @version $Revision:520964 $
 */
public class RuntimeMailException extends RuntimeException {
    private static final long serialVersionUID = -2141493732308871761L;

    public RuntimeMailException(String message, Throwable cause) {
        super(message, cause);
    }
}
