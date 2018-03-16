package org.apache.camel.impl.converter;

import org.apache.camel.TypeConverter;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.util.ObjectHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * A {@link TypeConverter} implementation which invokes a static method to convert from a type to another type
 *
 * @version $Revision: 525236 $
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
