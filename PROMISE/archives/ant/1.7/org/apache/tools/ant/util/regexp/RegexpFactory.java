package org.apache.tools.ant.util.regexp;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.ClasspathUtils;
import org.apache.tools.ant.util.JavaEnvUtils;

/***
 * Regular expression factory, which will create Regexp objects.  The
 * actual implementation class depends on the System or Ant Property:
 * <code>ant.regexp.regexpimpl</code>.
 *
 */
public class RegexpFactory extends RegexpMatcherFactory {

    /** Constructor for RegexpFactory */
    public RegexpFactory() {
    }

    /***
     * Create a new regular expression matcher instance.
     * @return the matcher instance
     * @throws BuildException on error
     */
    public Regexp newRegexp() throws BuildException {
        return (Regexp) newRegexp(null);
    }

    /***
     * Create a new regular expression matcher instance.
     *
     * @param p Project whose ant.regexp.regexpimpl property will be used.
     * @return the matcher instance
     * @throws BuildException on error
     */
    public Regexp newRegexp(Project p) throws BuildException {
        String systemDefault = null;
        if (p == null) {
            systemDefault = System.getProperty(MagicNames.REGEXP_IMPL);
        } else {
            systemDefault = p.getProperty(MagicNames.REGEXP_IMPL);
        }

        if (systemDefault != null) {
            return createRegexpInstance(systemDefault);
        }

        Throwable cause = null;

        try {
            testAvailability("java.util.regex.Matcher");
            return createRegexpInstance("org.apache.tools.ant.util.regexp.Jdk14RegexpRegexp");
        } catch (BuildException be) {
            cause = orCause(
                cause, be,
                JavaEnvUtils.getJavaVersionNumber() < JavaEnvUtils.VERSION_1_4);
        }

        try {
            testAvailability("org.apache.oro.text.regex.Pattern");
            return createRegexpInstance("org.apache.tools.ant.util.regexp.JakartaOroRegexp");
        } catch (BuildException be) {
            cause = orCause(cause, be, true);
        }

        try {
            testAvailability("org.apache.regexp.RE");
            return createRegexpInstance("org.apache.tools.ant.util.regexp.JakartaRegexpRegexp");
        } catch (BuildException be) {
            cause = orCause(cause, be, true);
        }
        throw new BuildException("No supported regular expression matcher found"
                + (cause != null ? ": " + cause : ""), cause);
    }

    /**
     * Wrapper over RegexpMatcherFactory.createInstance that ensures that
     * we are dealing with a Regexp implementation.
     * @param classname the name of the class to use.
     * @return the instance.
     * @throws BuildException if there is a problem.
     * @since 1.3
     *
     * @see RegexpMatcherFactory#createInstance(String)
     */
    protected Regexp createRegexpInstance(String classname) throws BuildException {
        return (Regexp) ClasspathUtils.newInstance(classname, RegexpFactory.class.getClassLoader(),
                Regexp.class);
    }

}
