package org.apache.camel;

/**
 * @version $Revision: 520124 $
 */
public class InvalidHeaderTypeException extends RuntimeCamelException {

	private static final long serialVersionUID = -8417806626073055262L;
	private Object headerValue;

    public InvalidHeaderTypeException(Throwable cause, Object headerValue) {
        super(cause.getMessage() + " headerValue is: " + headerValue + " of type: "
                + typeName(headerValue), cause);
        this.headerValue = headerValue;
    }

    public InvalidHeaderTypeException(String message, Object headerValue) {
        super(message);
        this.headerValue = headerValue;
    }


    /**
     * Returns the actual header value
     */
    public Object getHeaderValue() {
        return headerValue;
    }

    protected static String typeName(Object headerValue) {
        return (headerValue != null) ? headerValue.getClass().getName() : "null";
    }
}
