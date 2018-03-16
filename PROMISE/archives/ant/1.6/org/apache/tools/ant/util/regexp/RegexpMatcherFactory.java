package org.apache.tools.ant.util.regexp;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

/**
 * Simple Factory Class that produces an implementation of
 * RegexpMatcher based on the system property
 * <code>ant.regexp.matcherimpl</code> and the classes
 * available.
 *
 * <p>In a more general framework this class would be abstract and
 * have a static newInstance method.</p>
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
    public RegexpMatcher newRegexpMatcher(Project p)
        throws BuildException {
        String systemDefault = null;
        if (p == null) {
            systemDefault = System.getProperty("ant.regexp.regexpimpl");
        } else {
            systemDefault = p.getProperty("ant.regexp.regexpimpl");
        }

        if (systemDefault != null) {
            return createInstance(systemDefault);
        }

        try {
            testAvailability("java.util.regex.Matcher");
            return createInstance("org.apache.tools.ant.util.regexp.Jdk14RegexpMatcher");
        } catch (BuildException be) {
        }

        try {
            testAvailability("org.apache.oro.text.regex.Pattern");
            return createInstance("org.apache.tools.ant.util.regexp.JakartaOroMatcher");
        } catch (BuildException be) {
        }

        try {
            testAvailability("org.apache.regexp.RE");
            return createInstance("org.apache.tools.ant.util.regexp.JakartaRegexpMatcher");
        } catch (BuildException be) {
        }

        throw new BuildException("No supported regular expression matcher found");
   }

    /**
     * Create an instance of a matcher from a classname.
     *
     * @param className a <code>String</code> value
     * @return a <code>RegexpMatcher</code> value
     * @exception BuildException if an error occurs
     */
    protected RegexpMatcher createInstance(String className)
        throws BuildException {
        try {
            Class implClass = Class.forName(className);
            return (RegexpMatcher) implClass.newInstance();
        } catch (Throwable t) {
            throw new BuildException(t);
        }
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
}
