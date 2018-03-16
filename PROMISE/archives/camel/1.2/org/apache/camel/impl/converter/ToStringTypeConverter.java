package org.apache.camel.impl.converter;

import org.apache.camel.TypeConverter;

/**
 * A simple converter that can convert any object to a String type by using the
 * toString() method of the object.
 * 
 * @version $Revision: 523731 $
 */
public class ToStringTypeConverter implements TypeConverter {

    public <T> T convertTo(Class<T> toType, Object value) {
        if (value != null) {
            if (toType.equals(String.class)) {
                return (T)value.toString();
            }
        }
        return null;
    }

}
