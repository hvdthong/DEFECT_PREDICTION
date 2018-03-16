package org.apache.camel.impl.converter;

import java.lang.reflect.Method;

import org.apache.camel.TypeConverter;
import org.apache.camel.util.ObjectHelper;

/**
 * A {@link TypeConverter} implementation which invokes a static method to convert from a type to another type
 *
 * @version $Revision: 563607 $
 */
public class StaticMethodTypeConverter implements TypeConverter {
    private final Method method;

    public StaticMethodTypeConverter(Method method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "StaticMethodTypeConverter: " + method;
    }

    public <T> T convertTo(Class<T> type, Object value) {
        return (T) ObjectHelper.invokeMethod(method, null, value);
    }
}
