package org.apache.tools.ant.util;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Method;
import org.apache.tools.ant.BuildException;

/**
 * ClassLoader utility methods
 *
 * @author Conor MacNeill
 */
public class LoaderUtils {
    /** The getContextClassLoader method */
    private static Method getContextClassLoader;
    /** The setContextClassLoader method */
    private static Method setContextClassLoader;

    static {
        try {
            getContextClassLoader
                 = Thread.class.getMethod("getContextClassLoader",
                new Class[0]);
            Class[] setContextArgs = new Class[]{ClassLoader.class};
            setContextClassLoader
                 = Thread.class.getMethod("setContextClassLoader",
                setContextArgs);
        } catch (Exception e) {
        }
    }

    /**
     * JDK1.1 compatible access to get the context class loader. Has no
     * effect on JDK 1.1
     *
     * @param loader the ClassLoader to be used as the context class loader
     *      on the current thread.
     */
    public static void setContextClassLoader(ClassLoader loader) {
        if (setContextClassLoader == null) {
            return;
        }

        try {
            Thread currentThread = Thread.currentThread();
            setContextClassLoader.invoke(currentThread,
                new Object[]{loader});
        } catch (IllegalAccessException e) {
            throw new BuildException
                ("Unexpected IllegalAccessException", e);
        } catch (InvocationTargetException e) {
            throw new BuildException
                ("Unexpected InvocationTargetException", e);
        }

    }


    /**
     * JDK1.1 compatible access to set the context class loader.
     *
     * @return the ClassLoader instance being used as the context
     *      classloader on the current thread. Returns null on JDK 1.1
     */
    public static ClassLoader getContextClassLoader() {
        if (getContextClassLoader == null) {
            return null;
        }

        try {
            Thread currentThread = Thread.currentThread();
            return (ClassLoader) getContextClassLoader.invoke(currentThread,
                new Object[0]);
        } catch (IllegalAccessException e) {
            throw new BuildException
                ("Unexpected IllegalAccessException", e);
        } catch (InvocationTargetException e) {
            throw new BuildException
                ("Unexpected InvocationTargetException", e);
        }
    }

    /**
     * Indicates if the context class loader methods are available
     *
     * @return true if the get and set methods dealing with the context
     *      classloader are available.
     */
    public static boolean isContextLoaderAvailable() {
        return getContextClassLoader != null &&
            setContextClassLoader != null;
    }
}

