package org.apache.camel;

import org.apache.camel.util.ExchangeHelper;

/**
 * An exception caused when a mandatory header is not available on a message
 * {@link Exchange}
 *
 * @see ExchangeHelper#getMandatoryHeader(Exchange, String, Class)
 *
 * @version $Revision: 633440 $
 */
public class NoSuchHeaderException extends CamelExchangeException {
    private static final long serialVersionUID = -8721487431101572630L;
    private final String headerName;
    private final Class<?> type;

    public NoSuchHeaderException(Exchange exchange, String headerName, Class<?> type) {
        super("No '" + headerName + "' header available of type: " + type.getName()
              + reason(exchange, headerName), exchange);
        this.headerName = headerName;
        this.type = type;
    }

    public String getHeaderName() {
        return headerName;
    }

    public Class<?> getType() {
        return type;
    }

    protected static String reason(Exchange exchange, String propertyName) {
        Object value = exchange.getProperty(propertyName);
        return valueDescription(value);
    }

    static String valueDescription(Object value) {
        if (value == null) {
            return "";
        }
        return " but has value: " + value + " of type: " + value.getClass().getName();
    }
}
