package org.apache.camel.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.w3c.dom.NodeList;

/**
 * A number of helper methods for working with collections
 *
 * @version $Revision: 640438 $
 */
public final class CollectionHelper {

    /**
     * Utility classes should not have a public constructor.
     */
    private CollectionHelper() {
    }

    /**
     * Returns the size of the collection if it can be determined to be a collection
     */
    public static Integer size(Object value) {
        if (value != null) {
            if (value instanceof Collection) {
                Collection collection = (Collection)value;
                return collection.size();
            } else if (value instanceof Map) {
                Map map = (Map)value;
                return map.size();
            } else if (value instanceof Object[]) {
                Object[] array = (Object[])value;
                return array.length;
            } else if (value.getClass().isArray()) {
                return Array.getLength(value);
            } else if (value instanceof NodeList) {
                NodeList nodeList = (NodeList)value;
                return nodeList.getLength();
            }
        }
        return null;
    }

    /**
     * Sets the value of the entry in the map for the given key, though if the
     * map already contains a value for the given key then the value is appended
     * to a list of values.
     *
     * @param map the map to add the entry to
     * @param key the key in the map
     * @param value the value to put in the map
     */
    public static void appendValue(Map map, Object key, Object value) {

        Object oldValue = map.get(key);
        if (oldValue != null) {
            List list;
            if (oldValue instanceof List) {
                list = (List)oldValue;
            } else {
                list = new ArrayList();
                list.add(oldValue);
            }
            list.add(value);
        } else {
            map.put(key, value);
        }
    }
}
