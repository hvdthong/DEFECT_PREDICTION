package org.apache.tools.ant.util.regexp;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.ClasspathUtils;
import org.apache.tools.ant.util.JavaEnvUtils;

/**
 * Simple Factory Class that produces an implementation of RegexpMatcher based on the system
 * property <code>ant.regexp.regexpimpl</code> and the classes available.
 *
 * <p>
 * In a more general framework this class would be abstract and have a static newInstance method.
 * </p>
 *
 */
public class RegexpMatcherFactory {

    /** Constructor for RegexpMatcherFactory. */
    public RegexpMatcherFactory() {
    }

    /***
     * Create a new regular expression instance.
     * @return the matcher
     * @throws BuildException on error
     */
    public RegexpMatcher newRegexpMatcher() throws BuildException {
        return newRegexpMatcher(null);
    }

    /***
     * Create a new regular expression instance.
     *
     * @param p Project whose ant.regexp.regexpimpl property will be used.
     * @return the matcher
     * @throws BuildException on error
     */
    public RegexpMatcher newRegexpMatcher(Project p) throws BuildException {
        String systemDefault = null;
        if (p == null) {
            systemDefault = System.getProperty(MagicNames.REGEXP_IMPL);
        } else {
            systemDefault = p.getProperty(MagicNames.REGEXP_IMPL);
        }

        if (systemDefault != null) {
            return createInstance(systemDefault);
        }

        Throwable cause = null;

        try {
            testAvailability("java.util.regex.Matcher");
            return createInstance("org.apache.tools.ant.util.regexp.Jdk14RegexpMatcher");
        } catch (BuildException be) {
            cause = orCause(
                cause, be,
                JavaEnvUtils.getJavaVersionNumber() < JavaEnvUtils.VERSION_1_4);
        }

        try {
            testAvailability("org.apache.oro.text.regex.Pattern");
            return createInstance("org.apache.tools.ant.util.regexp.JakartaOroMatcher");
        } catch (BuildException be) {
            cause = orCause(cause, be, true);
        }

        try {
            testAvailability("org.apache.regexp.RE");
            return createInstance("org.apache.tools.ant.util.regexp.JakartaRegexpMatcher");
        } catch (BuildException be) {
            cause = orCause(cause, be, true);
        }
        throw new BuildException("No supported regular expression matcher found"
                + (cause != null ? ": " + cause : ""), cause);
    }

    static Throwable orCause(Throwable deflt, BuildException be, boolean ignoreCnfe) {
        if (deflt != null) {
            return deflt;
        }
        Throwable t = be.getException();
        return ignoreCnfe && t instanceof ClassNotFoundException ? null : t;
    }

    /**
     * Create an instance of a matcher from a classname.
     *
     * @param className a <code>String</code> value
     * @return a <code>RegexpMatcher</code> value
     * @exception BuildException if an error occurs
     */
    protected RegexpMatcher createInstance(String className) throws BuildException {
        return (RegexpMatcher) ClasspathUtils.newInstance(className, RegexpMatcherFactory.class
                .getClassLoader(), RegexpMatcher.class);
    }

    /**
     * Test if a particular class is available to be used.
     *
     * @param className a <code>String</code> value
     * @exception BuildException if an error occurs
     */
    protected void testAvailability(String className) throws BuildException {
        try {
            Class.forName(className);
        } catch (Throwable t) {
            throw new BuildException(t);
        }
    }

    /**
     * Checks if a RegExp-Matcher is available.
     * @param project  The project to check for (may be <code>null</code>)
     * @return <code>true</code> if available otherwise <code>false</code>
     */
    public static boolean regexpMatcherPresent(Project project) {
        try {
            new RegexpMatcherFactory().newRegexpMatcher(project);
            return true;
        } catch (Throwable ex) {
            return false;
        }
    }
}
