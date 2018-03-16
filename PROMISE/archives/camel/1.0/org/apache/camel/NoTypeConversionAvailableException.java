package org.apache.camel;

/**
 * An exception thrown if a value could not be converted to the required type
 *
 * @version $Revision: 525122 $
 */
public class NoTypeConversionAvailableException extends RuntimeCamelException {
    private final Object value;
    private final Class type;

    public NoTypeConversionAvailableException(Object value, Class type) {
        super("No converter available to convert value: " + value + " to the required type: " + type.getName());
        this.value = value;
        this.type = type;
    }

    /**
     * Returns the value which could not be converted
     *
     * @return the value that could not be converted
     */
    public Object getValue() {
        return value;
    }

    /**
     * Returns the required type
     *
     * @return the required type
     */
    public Class getType() {
        return type;
    }
}
