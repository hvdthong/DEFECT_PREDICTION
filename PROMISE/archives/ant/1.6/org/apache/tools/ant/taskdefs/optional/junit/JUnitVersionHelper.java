package org.apache.tools.ant.taskdefs.optional.junit;

import java.lang.reflect.Method;
import junit.framework.Test;
import junit.framework.TestCase;

/**
 * Work around for some changes to the public JUnit API between
 * different JUnit releases.
 *
 */
public class JUnitVersionHelper {

    private static Method testCaseName = null;
    static {
        try {
            testCaseName = TestCase.class.getMethod("getName", new Class[0]);
        } catch (NoSuchMethodException e) {
            try {
                testCaseName = TestCase.class.getMethod("name", new Class[0]);
            } catch (NoSuchMethodException e2) {
            }
        }
    }

    /**
     * JUnit 3.7 introduces TestCase.getName() and subsequent versions
     * of JUnit remove the old name() method.  This method provides
     * access to the name of a TestCase via reflection that is
     * supposed to work with version before and after JUnit 3.7.
     *
     * <p>since Ant 1.5.1 this method will invoke &quot;<code>public
     * String getName()</code>&quot; on any implementation of Test if
     * it exists.</p>
     */
    public static String getTestCaseName(Test t) {
        if (t instanceof TestCase && testCaseName != null) {
            try {
                return (String) testCaseName.invoke(t, new Object[0]);
            } catch (Throwable e) {
            }
        } else {
            try {
                Method getNameMethod = null;
                try {
                    getNameMethod =
                        t.getClass().getMethod("getName", new Class [0]);
                } catch (NoSuchMethodException e) {
                    getNameMethod = t.getClass().getMethod("name",
                                                           new Class [0]);
                }
                if (getNameMethod != null
                    && getNameMethod.getReturnType() == String.class) {
                    return (String) getNameMethod.invoke(t, new Object[0]);
                }
            } catch (Throwable e) {
            }
        }
        return "unknown";
    }

}
