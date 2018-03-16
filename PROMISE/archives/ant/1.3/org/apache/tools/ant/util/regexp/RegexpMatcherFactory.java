package org.apache.tools.ant.util.regexp;

import org.apache.tools.ant.BuildException;

/**
 * Simple Factory Class that produces an implementation of
 * RegexpMatcher based on the system property
 * <code>ant.regexp.matcherimpl</code> and the classes
 * available.
 * 
 * <p>In a more general framework this class would be abstract and
 * have a static newInstance method.</p>
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 */
public class RegexpMatcherFactory {

    public RegexpMatcherFactory() {}

    public RegexpMatcher newRegexpMatcher() throws BuildException {
        String systemDefault = System.getProperty("ant.regexp.matcherimpl");
        if (systemDefault != null) {
            return createInstance(systemDefault);
        }

        try {
            return createInstance("org.apache.tools.ant.util.regexp.JakartaOroMatcher");
        } catch (BuildException be) {}
        
        try {
            return createInstance("org.apache.tools.ant.util.regexp.JakartaRegexpMatcher");
        } catch (BuildException be) {}

        throw new BuildException("No supported regular expression matcher found");
   }

    protected RegexpMatcher createInstance(String className) 
        throws BuildException {
        try {
            Class implClass = Class.forName(className);
            return (RegexpMatcher) implClass.newInstance();
        } catch (Throwable t) {
            throw new BuildException(t);
        }
    }
}
