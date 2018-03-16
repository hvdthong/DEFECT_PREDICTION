package org.apache.camel;

/**
 * Thrown if a message transformation fails
 * 
 * @version $Revision: 521156 $
 */
public class RuntimeTransformException extends RuntimeCamelException {

	private static final long serialVersionUID = -8417806626073055262L;

    public RuntimeTransformException(String message) {
        super(message);
    }

    public RuntimeTransformException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeTransformException(Throwable cause) {
        super(cause);
    }
}
