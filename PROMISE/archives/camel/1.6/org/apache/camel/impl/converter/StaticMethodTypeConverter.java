package org.apache.camel.impl.converter;

import java.lang.reflect.Method;

import org.apache.camel.Exchange;
import org.apache.camel.TypeConverter;
import org.apache.camel.util.ObjectHelper;

/**
 * A {@link TypeConverter} implementation which invokes a static method to convert from a type to another type
 *
 * @version $Revision: 687545 $
 */
public class StaticMethodTypeConverter implements TypeConverter {
    private final Method method;
    private final boolean useExchange;

    public StaticMethodTypeConverter(Method method) {
        this.method = method;
        this.useExchange = method.getParameterTypes().length == 2;
    }

    @Override
    public String toString() {
        return "StaticMethodTypeConverter: " + method;
    }

    public <T> T convertTo(Class<T> type, Object value) {
        return convertTo(type, null, value);
    }

    public <T> T convertTo(Class<T> type, Exchange exchange, Object value) {
        return useExchange ? (T)ObjectHelper.invokeMethod(method, null, value, exchange)
            : (T)ObjectHelper.invokeMethod(method, null, value);
    }
}
