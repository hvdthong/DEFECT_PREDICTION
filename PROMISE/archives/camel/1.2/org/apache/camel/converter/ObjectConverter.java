package org.apache.camel.converter;

import org.apache.camel.Converter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Some core java.lang based <a
 * 
 * @version $Revision: 571489 $
 */
@Converter
public class ObjectConverter {
    
    /**
     * Utility classes should not have a public constructor.
     */
    private ObjectConverter() {        
    }
    
    public static boolean isCollection(Object value) {
        return value instanceof Collection || (value != null && value.getClass().isArray());
    }

    /**
     * Creates an iterator over the value if the value is a collection, an
     * Object[] or a primitive type array; otherwise to simplify the caller's
     * code, we just create a singleton collection iterator over a single value
     */
    @Converter
    public static Iterator iterator(Object value) {
        if (value == null) {
            return Collections.EMPTY_LIST.iterator();
        } else if (value instanceof Collection) {
            Collection collection = (Collection)value;
            return collection.iterator();
        } else if (value.getClass().isArray()) {
            List<Object> list = Arrays.asList((Object[]) value);
            return list.iterator();
        } else if (value instanceof NodeList) {
            final NodeList nodeList = (NodeList) value;
            return new Iterator<Node>() {
                int idx = -1;

                public boolean hasNext() {
                    return ++idx < nodeList.getLength();
                }

                public Node next() {
                    return nodeList.item(idx);
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        } else {
            return Collections.singletonList(value).iterator();
        }
    }

    /**
     * Converts the given value to a boolean, handling strings or Boolean
     * objects; otherwise returning false if the value could not be converted to
     * a boolean
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
     * Converts the given value to a Boolean, handling strings or Boolean
     * objects; otherwise returning null if the value cannot be converted to a
     * boolean
     */
    @Converter
    public static Boolean toBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean)value;
        }
        if (value instanceof String) {
            return "true".equalsIgnoreCase(value.toString()) ? Boolean.TRUE : Boolean.FALSE;
        }
        return null;
    }

    /**
     * Returns the boolean value, or null if the value is null
     */
    @Converter
    public static Boolean toBoolean(Boolean value) {
        if (value != null) {
            return value.booleanValue();
        }
        return false;
    }

}
