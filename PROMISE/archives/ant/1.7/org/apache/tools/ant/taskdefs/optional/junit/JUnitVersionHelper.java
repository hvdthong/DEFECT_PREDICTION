package org.apache.tools.ant.taskdefs.optional.junit;

import java.lang.reflect.Method;
import junit.framework.Test;
import junit.framework.TestCase;

/**
 * Work around for some changes to the public JUnit API between
 * different JUnit releases.
 * @since Ant 1.7
 */
public class JUnitVersionHelper {

    private static Method testCaseName = null;

    /**
     * Name of the JUnit4 class we look for.
     * {@value}
     * @since Ant 1.7.1
     */
    public static final String JUNIT_FRAMEWORK_JUNIT4_TEST_CASE_FACADE
        = "junit.framework.JUnit4TestCaseFacade";
    private static final String UNKNOWN_TEST_CASE_NAME = "unknown";

    static {
        try {
            testCaseName = TestCase.class.getMethod("getName", new Class[0]);
        } catch (NoSuchMethodException e) {
            try {
                testCaseName = TestCase.class.getMethod("name", new Class[0]);
            } catch (NoSuchMethodException ignored) {
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
     *
     * <p>Since Ant 1.7 also checks for JUnit4TestCaseFacade explicitly.
     * This is used by junit.framework.JUnit4TestAdapter.</p>
     * @param t the test.
     * @return the name of the test.
     */
    public static String getTestCaseName(Test t) {
        if (t == null) {
            return UNKNOWN_TEST_CASE_NAME;
        }
        if (t.getClass().getName().equals(JUNIT_FRAMEWORK_JUNIT4_TEST_CASE_FACADE)) {
            String name = t.toString();
            if (name.endsWith(")")) {
                int paren = name.lastIndexOf('(');
                return name.substring(0, paren);
            } else {
                return name;
            }
        }
        if (t instanceof TestCase && testCaseName != null) {
            try {
                return (String) testCaseName.invoke(t, new Object[0]);
            } catch (Throwable ignored) {
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
            } catch (Throwable ignored) {
            }
        }
        return UNKNOWN_TEST_CASE_NAME;
    }

    /**
     * Tries to find the name of the class which a test represents
     * across JUnit 3 and 4. For Junit4 it parses the toString() value of the
     * test, and extracts it from there.
     * @since Ant 1.7.1 (it was private until then)
     * @param test test case to look at
     * @return the extracted class name.
     */
    public static String getTestCaseClassName(Test test) {
        String className = test.getClass().getName();
        if (test instanceof JUnitTaskMirrorImpl.VmExitErrorTest) {
            className = ((JUnitTaskMirrorImpl.VmExitErrorTest) test).getClassName();
        } else
        if (className.equals(JUNIT_FRAMEWORK_JUNIT4_TEST_CASE_FACADE)) {
            String name = test.toString();
            int paren = name.lastIndexOf('(');
            if (paren != -1 && name.endsWith(")")) {
                className = name.substring(paren + 1, name.length() - 1);
            }
        }
        return className;
    }

}
