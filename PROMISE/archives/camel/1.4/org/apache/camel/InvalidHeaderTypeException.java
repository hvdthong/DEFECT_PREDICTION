package org.apache.camel;

/**
 * @deprecated use {@link InvalidTypeException}. Will be removed in Camel 2.0.
 * @version $Revision: 663882 $
 */
@Deprecated
public class InvalidHeaderTypeException extends RuntimeCamelException {

    private static final long serialVersionUID = -8417806626073055262L;
    private final Object headerValue;

    public InvalidHeaderTypeException(Throwable cause, Object headerValue) {
        super(cause.getMessage() + " headerValue is: " + headerValue + " of type: " + typeName(headerValue),
              cause);
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
