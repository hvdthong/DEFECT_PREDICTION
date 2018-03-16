package org.apache.camel.component.bean;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Represents a {@link Serializable} version of a {@link Method}
 *
 * @version $Revision: 640438 $
 */
public class MethodBean implements Serializable {
    private String name;
    private Class<?> type;
    private Class<?>[] parameterTypes;

    public MethodBean() {
    }

    public MethodBean(Method method) {
        this.name = method.getName();
        this.type = method.getDeclaringClass();
        this.parameterTypes = method.getParameterTypes();
    }

    public Method getMethod() throws NoSuchMethodException {
        return type.getMethod(name, parameterTypes);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }
}
