package org.apache.camel.impl.converter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.camel.TypeConverter;

/**
 * A type converter which is used to convert to and from array types
 * particularly for derived types of array component types and dealing with
 * primitive array types.
 * 
 * @version $Revision: 660275 $
 */
public class ArrayTypeConverter implements TypeConverter {

    public <T> T convertTo(Class<T> type, Object value) {
        if (type.isArray()) {
            if (value instanceof Collection) {
                Collection collection = (Collection)value;
                Object array = Array.newInstance(type.getComponentType(), collection.size());
                if (array instanceof Object[]) {
                    collection.toArray((Object[])array);
                } else {
                    int index = 0;
                    for (Object element : collection) {
                        Array.set(array, index++, element);
                    }
                }
                return (T)array;
            } else if (value != null && value.getClass().isArray()) {
                int size = Array.getLength(value);
                Object answer = Array.newInstance(type.getComponentType(), size);
                for (int i = 0; i < size; i++) {
                    Array.set(answer, i, Array.get(value, i));
                }
                return (T)answer;
            }
        } else if (Collection.class.isAssignableFrom(type)) {
            if (value != null) {
                if (value instanceof Object[]) {
                    return (T)Arrays.asList((Object[])value);
                } else if (value.getClass().isArray()) {
                    int size = Array.getLength(value);
                    List answer = new ArrayList(size);
                    for (int i = 0; i < size; i++) {
                        answer.add(Array.get(value, i));
                    }
                    return (T)answer;
                }
            }
        }
        return null;
    }
}
