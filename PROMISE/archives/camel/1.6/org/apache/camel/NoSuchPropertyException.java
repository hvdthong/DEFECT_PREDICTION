package org.apache.camel;

import org.apache.camel.util.ExchangeHelper;

/**
 * An exception caused when a mandatory property is not available on a message
 * {@link Exchange}
 * 
 * @see ExchangeHelper#getMandatoryProperty(Exchange, String, Class)
 * 
 * @version $Revision: 633440 $
 */
public class NoSuchPropertyException extends CamelExchangeException {
    private static final long serialVersionUID = -8721487431101572630L;
    private final String propertyName;
    private final Class<?> type;

    public NoSuchPropertyException(Exchange exchange, String propertyName, Class<?> type) {
        super("No '" + propertyName + "' property available of type: " + type.getName()
              + reason(exchange, propertyName), exchange);
        this.propertyName = propertyName;
        this.type = type;
    }

    public String getPropertyName() {
        return propertyName;
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
