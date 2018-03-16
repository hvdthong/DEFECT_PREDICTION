package org.apache.camel;

/**
 * Is thrown if type conversion failed.
 *
 * @version $Revision: 659422 $
 */
public class InvalidTypeException extends CamelExchangeException {
    private final Object value;
    private final Class<?> type;

    public InvalidTypeException(Exchange exchange, Object value, Class<?> type) {
        super("Could not convert value: " + value + " to type: " + type.getName()
              + NoSuchPropertyException.valueDescription(value), exchange);
        this.value = value;
        this.type = type;
    }

    /**
     * The value
     */
    public Object getValue() {
        return value;
    }

    /**
     * The expected type of the value
     */
    public Class<?> getType() {
        return type;
    }
}
