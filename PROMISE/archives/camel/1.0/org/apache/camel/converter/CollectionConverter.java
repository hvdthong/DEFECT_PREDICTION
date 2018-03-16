package org.apache.camel.converter;

import org.apache.camel.Converter;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Some core java.util Collection based
 *
 * @version $Revision: 524215 $
 */
@Converter
public class CollectionConverter {

    /**
     * Converts a collection to an array
     */
    @Converter
    public static Object[] toArray(Collection value) {
        if (value == null) {
            return null;
        }
        return value.toArray();
    }

    /**
     * Converts an array to a collection
     */
    @Converter
    public static List toList(Object[] array) {
        return Arrays.asList(array);
    }

    @Converter
    public static Set toSet(Object[] array) {
        Set answer = new HashSet();
        for (Object element : array) {
            answer.add(element);
        }
        return answer;
    }

    @Converter
    public static Set toSet(Collection collection) {
        return new HashSet(collection);
    }

    @Converter
    public static Set toSet(Map map) {
        return map.entrySet();
    }
}
