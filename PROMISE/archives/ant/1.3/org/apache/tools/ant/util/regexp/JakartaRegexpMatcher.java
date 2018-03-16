package org.apache.tools.ant.util.regexp;

import org.apache.regexp.*;

import org.apache.tools.ant.BuildException;
import java.util.Vector;

/**
 * Implementation of RegexpMatcher for Jakarta-Regexp.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 */
public class JakartaRegexpMatcher implements RegexpMatcher {

    protected RE reg = null;
    private String pattern;

    /**
     * Set the regexp pattern from the String description.
     */
    public void setPattern(String pattern) throws BuildException {
        try {
            this.pattern = pattern;
            reg = new RE(pattern);
        } catch (RESyntaxException e) {
            throw new BuildException(e);
        }
    }

    /**
     * Get a String representation of the regexp pattern
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Does the given argument match the pattern?
     */
    public boolean matches(String argument) {
        return reg.match(argument);
    }

    /**
     * Returns a Vector of matched groups found in the argument.
     *
     * <p>Group 0 will be the full match, the rest are the
     * parenthesized subexpressions</p>.
     */
    public Vector getGroups(String argument) {
        if (!matches(argument)) {
            return null;
        }
        Vector v = new Vector();
        for (int i=0; i<reg.getParenCount(); i++) {
            v.addElement(reg.getParen(i));
        }
        return v;
    }

}
