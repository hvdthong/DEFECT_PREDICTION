package org.apache.camel.spring.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectionUtils extends org.springframework.util.ReflectionUtils {
    public static <T extends Annotation> void callLifecycleMethod(final Object bean, final Class<T> annotation) {
        ReflectionUtils.doWithMethods(bean.getClass(), new ReflectionUtils.MethodCallback() {
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                if (method.getAnnotation(annotation) != null) {
                    try {
                        method.invoke(bean, (Object[]) null);
                    }
                    catch (IllegalArgumentException ex) {
                        throw new IllegalStateException("Failure to invoke " + method + " on "
                                + bean.getClass() + ": args=[]", ex);
                    }
                    catch (IllegalAccessException ex) {
                        throw new UnsupportedOperationException(ex.toString());
                    }
                    catch (InvocationTargetException ex) {
                        throw new UnsupportedOperationException("PostConstruct method on bean threw exception",
                                ex.getTargetException());
                    }
                }
            }
        });
    }

    public static void setField(Field f, Object instance, Object value) {
        try {
            boolean oldAccessible = f.isAccessible();
            boolean shouldSetAccessible = !Modifier.isPublic(f.getModifiers()) && !oldAccessible;
            if (shouldSetAccessible) {
                f.setAccessible(true);
            }
            f.set(instance, value);
            if (shouldSetAccessible) {
                f.setAccessible(oldAccessible);
            }
        }
        catch (IllegalArgumentException ex) {
            throw new UnsupportedOperationException("Cannot inject value of class '"
                    + value.getClass() + "' into " + f);
        }
        catch (IllegalAccessException ex) {
            ReflectionUtils.handleReflectionException(ex);
        }
    }
}
