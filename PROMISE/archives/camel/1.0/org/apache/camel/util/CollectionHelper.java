package org.apache.camel.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A number of helper methods for working with collections
 *
 * @version $Revision: 1.1 $
 */
public class CollectionHelper {
    /**
     * Sets the value of the entry in the map for the given key, though if the map already contains a value for the
     * given key then the value is appended to a list of values.
     *
     * @param map   the map to add the entry to
     * @param key   the key in the map
     * @param value the value to put in the map
     */
    public static void appendValue(Map map, Object key, Object value) {

        Object oldValue = map.get(key);
        if (oldValue != null) {
            List list;
            if (oldValue instanceof List) {
                list = (List) oldValue;
            }
            else {
                list = new ArrayList();
                list.add(oldValue);
            }
            list.add(value);
        }
        else {
            map.put(key, value);
        }
    }
}
