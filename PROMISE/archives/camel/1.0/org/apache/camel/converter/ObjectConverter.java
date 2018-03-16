package org.apache.camel.converter;

import org.apache.camel.Converter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * Some core java.lang based
 *
 * @version $Revision: 544115 $
 */
@Converter
public class ObjectConverter {
    public static boolean isCollection(Object value) {
        return value instanceof Collection || (value != null && value.getClass().isArray());
    }

    /**
     * Creates an iterator over the value if the value is a collection, an Object[] or a primitive type array; otherwise
     * to simplify the caller's code, we just create a singleton collection iterator over a single value
     */
    @Converter
    public static Iterator iterator(Object value) {
        if (value == null) {
            return Collections.EMPTY_LIST.iterator();
        }
        else if (value instanceof Collection) {
            Collection collection = (Collection) value;
            return collection.iterator();
        }
        else if (value.getClass().isArray()) {
            return Arrays.asList(value).iterator();
        }
        else {
            return Collections.singletonList(value).iterator();
        }
    }

    /**
     * Converts the given value to a boolean, handling strings or Boolean objects;
     * otherwise returning false if the value could not be converted to a boolean
     */
    @Converter
    public static boolean toBool(Object value) {
        Boolean answer = toBoolean(value);
        if (answer != null) {
            return answer.booleanValue();
        }
        return false;
    }

    /**
     * Converts the given value to a Boolean, handling strings or Boolean objects;
     * otherwise returning null if the value cannot be converted to a boolean
     */
    @Converter
    public static Boolean toBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return "true".equalsIgnoreCase(value.toString()) ? Boolean.TRUE : Boolean.FALSE;
        }
        return null;
    }
}
