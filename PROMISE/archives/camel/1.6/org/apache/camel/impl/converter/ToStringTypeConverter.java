package org.apache.camel.impl.converter;

import org.apache.camel.Exchange;
import org.apache.camel.TypeConverter;

/**
 * A simple converter that can convert any object to a String type by using the
 * toString() method of the object.
 *
 * @version $Revision: 687545 $
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

    public <T> T convertTo(Class<T> type, Exchange exchange, Object value) {
        return convertTo(type, value);
    }
}
