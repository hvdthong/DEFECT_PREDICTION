package org.apache.camel;

/**
 * An exception thrown if a value could not be converted to the required type
 *
 * @version $Revision: 655720 $
 */
public class NoTypeConversionAvailableException extends RuntimeCamelException {
    private final Object value;
    private final Class type;

    public NoTypeConversionAvailableException(Object value, Class type) {
        super("No type converter available to convert from type: " + (value != null ? value.getClass() : null)
              + " to the required type " + type.getName() + " with value " + value);
        this.value = value;
        this.type = type;
    }

    /**
     * Returns the value which could not be converted
     */
    public Object getValue() {
        return value;
    }

    /**
     * Returns the required <tt>to</tt> type
     */
    public Class getType() {
        return type;
    }

    /**
     * Returns the required <tt>from</tt> type.
     * Returns <tt>null</tt> if the provided value was null.
     */
    public Class getFromType() {
        if (value != null) {
            return value.getClass();
        } else {
            return null;
        }
    }

}
