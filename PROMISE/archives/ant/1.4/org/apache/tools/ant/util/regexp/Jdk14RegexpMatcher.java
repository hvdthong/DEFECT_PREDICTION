package org.apache.tools.ant.util.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.tools.ant.BuildException;
import java.util.Vector;

/**
 * Implementation of RegexpMatcher for the built-in regexp matcher of
 * JDK 1.4.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 */
public class Jdk14RegexpMatcher implements RegexpMatcher {

    private Pattern pattern;

    /**
     * Set the regexp pattern from the String description.
     */
    public void setPattern(String pattern) throws BuildException {
        try {
            this.pattern = Pattern.compile(pattern);
        } catch (PatternSyntaxException e) {
            throw new BuildException(e);
        }
    }

    /**
     * Get a String representation of the regexp pattern
     */
    public String getPattern() {
        return pattern.pattern();
    }

    /**
     * Does the given argument match the pattern?
     */
    public boolean matches(String argument) {
        return pattern.matcher(argument).find();
    }

    /**
     * Returns a Vector of matched groups found in the argument.
     *
     * <p>Group 0 will be the full match, the rest are the
     * parenthesized subexpressions</p>.
     */
    public Vector getGroups(String argument) {
        Matcher matcher = pattern.matcher(argument);
        if (!matcher.find()) {
            return null;
        }
        Vector v = new Vector();
        for (int i=0; i<=matcher.groupCount(); i++) {
            v.addElement(matcher.group(i));
        }
        return v;
    }

}
