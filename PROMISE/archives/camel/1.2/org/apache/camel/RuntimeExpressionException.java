package org.apache.camel;

/**
 * Thrown if an expression evaluation fails
 * 
 * @version $Revision: 563607 $
 */
public class RuntimeExpressionException extends RuntimeCamelException {

    private static final long serialVersionUID = -8417806626073055262L;

    public RuntimeExpressionException(String message) {
        super(message);
    }

    public RuntimeExpressionException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeExpressionException(Throwable cause) {
        super(cause);
    }
}
