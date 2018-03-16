package org.apache.tools.ant.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.tools.ant.BuildException;
import java.lang.reflect.Field;

/**
 * Utility class to handle reflection on java objects.
 * The class contains static methods to call reflection
 * methods, catch any exceptions, converting them
 * to BuildExceptions.
 */
public class ReflectUtil {

    /**  private constructor */
    private ReflectUtil() {
    }

    /**
     * Call a method on the object with no parameters.
     * @param obj  the object to invoke the method on.
     * @param methodName the name of the method to call
     * @return the object returned by the method
     */
    public static Object invoke(Object obj, String methodName) {
        try {
            Method method;
            method = obj.getClass().getMethod(
                        methodName, (Class[]) null);
            return method.invoke(obj, (Object[]) null);
        } catch (Exception t) {
            throwBuildException(t);
        }
    }

    /**
     * Call a method on the object with no parameters.
     * Note: Unlike the invoke method above, this
     * calls class or static methods, not instance methods.
     * @param obj  the object to invoke the method on.
     * @param methodName the name of the method to call
     * @return the object returned by the method
     */
    public static Object invokeStatic(Object obj, String methodName) {
        try {
            Method method;
            method = ((Class) obj).getMethod(
                    methodName, (Class[]) null);
            return method.invoke(obj, (Object[]) null);
        }  catch (Exception t) {
            throwBuildException(t);
        }
    }

    /**
     * Call a method on the object with one argument.
     * @param obj  the object to invoke the method on.
     * @param methodName the name of the method to call
     * @param argType    the type of argument.
     * @param arg        the value of the argument.
     * @return the object returned by the method
     */
    public static Object invoke(
        Object obj, String methodName, Class argType, Object arg) {
        try {
            Method method;
            method = obj.getClass().getMethod(
                methodName, new Class[] {argType});
            return method.invoke(obj, new Object[] {arg});
        } catch (Exception t) {
            throwBuildException(t);
        }
    }

    /**
     * Call a method on the object with two argument.
     * @param obj  the object to invoke the method on.
     * @param methodName the name of the method to call
     * @param argType1   the type of the first argument.
     * @param arg1       the value of the first argument.
     * @param argType2   the type of the second argument.
     * @param arg2       the value of the second argument.
     * @return the object returned by the method
     */
    public static Object invoke(
        Object obj, String methodName, Class argType1, Object arg1,
        Class argType2, Object arg2) {
        try {
            Method method;
            method = obj.getClass().getMethod(
                methodName, new Class[] {argType1, argType2});
            return method.invoke(obj, new Object[] {arg1, arg2});
        } catch (Exception t) {
            throwBuildException(t);
        }
    }

    /**
     * Get the value of a field in an object.
     * @param obj the object to look at.
     * @param fieldName the name of the field in the object.
     * @return the value of the field.
     * @throws BuildException if there is an error.
     */
    public static Object getField(Object obj, String fieldName)
        throws BuildException {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception t) {
            throwBuildException(t);
        }
    }

    /**
     * A method to convert an invocationTargetException to
     * a buildexception and throw it.
     * @param t the invocation target exception.
     * @throws BuildException the converted exception.
     */
    public static void throwBuildException(Exception t)
        throws BuildException {
        throw toBuildException(t);
    }

    /**
     * A method to convert an invocationTargetException to
     * a buildexception.
     * @param t the invocation target exception.
     * @return the converted exception.
     * @since ant 1.7.1
     */
    public static BuildException toBuildException(Exception t) {
        if (t instanceof InvocationTargetException) {
            Throwable t2 = ((InvocationTargetException) t)
                .getTargetException();
            if (t2 instanceof BuildException) {
                return (BuildException) t2;
            }
            return new BuildException(t2);
        } else {
            return new BuildException(t);
        }
    }

    /**
     * A method to test if an object responds to a given
     * message (method call)
     * @param o the object
     * @param methodName the method to check for
     * @return true if the object has the method.
     * @throws BuildException if there is a problem.
     */
    public static boolean respondsTo(Object o, String methodName)
        throws BuildException {
        try {
            Method[] methods = o.getClass().getMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equals(methodName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception t) {
            throw toBuildException(t);
        }
    }
}
