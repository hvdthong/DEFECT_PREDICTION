package org.apache.tools.ant.util.regexp;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

/***
 * Regular expression factory, which will create Regexp objects.  The
 * actual implementation class depends on the System or Ant Property:
 * <code>ant.regexp.regexpimpl</code>.
 *
 * @author Matthew Inger <a href="mailto:mattinger@mindless.com">mattinger@mindless.com</a>
 * @version $Revision: 274041 $
 */
public class RegexpFactory extends RegexpMatcherFactory {
    public RegexpFactory() {
    }

    /***
     * Create a new regular expression matcher instance.
     */
    public Regexp newRegexp() throws BuildException {
        return (Regexp) newRegexp(null);
    }

    /***
     * Create a new regular expression matcher instance.
     *
     * @param p Project whose ant.regexp.regexpimpl property will be used.
     */
    public Regexp newRegexp(Project p) throws BuildException {
        String systemDefault = null;
        if (p == null) {
            systemDefault = System.getProperty("ant.regexp.regexpimpl");
        } else {
            systemDefault = p.getProperty("ant.regexp.regexpimpl");
        }
        
        if (systemDefault != null) {
            return createRegexpInstance(systemDefault);
        }

        try {
            testAvailability("java.util.regex.Matcher");
            return createRegexpInstance("org.apache.tools.ant.util.regexp.Jdk14RegexpRegexp");
        } catch (BuildException be) {}
        
        try {
            testAvailability("org.apache.oro.text.regex.Pattern");
            return createRegexpInstance("org.apache.tools.ant.util.regexp.JakartaOroRegexp");
        } catch (BuildException be) {}
        
        try {
            testAvailability("org.apache.regexp.RE");
            return createRegexpInstance("org.apache.tools.ant.util.regexp.JakartaRegexpRegexp");
        } catch (BuildException be) {}

        throw new BuildException("No supported regular expression matcher found");
    }

    /**
     * Wrapper over RegexpMatcherFactory.createInstance that ensures that 
     * we are dealing with a Regexp implementation.
     *
     * @since 1.3
     * 
     * @see RegexpMatcherFactory#createInstance(String)
     */
    protected Regexp createRegexpInstance(String classname) 
        throws BuildException {

        RegexpMatcher m = createInstance(classname);
        if (m instanceof Regexp) {
            return (Regexp) m;
        } else {
            throw new BuildException(classname + " doesn't implement the Regexp interface");
        }
    }

}
