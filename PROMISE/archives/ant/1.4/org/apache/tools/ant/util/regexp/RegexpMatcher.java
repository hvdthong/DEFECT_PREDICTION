package org.apache.tools.ant.util.regexp;

import org.apache.tools.ant.BuildException;
import java.util.Vector;

/**
 * Interface describing a regular expression matcher.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 */
public interface RegexpMatcher {

    /**
     * Set the regexp pattern from the String description.
     */
    public void setPattern(String pattern) throws BuildException;

    /**
     * Get a String representation of the regexp pattern
     */
    public String getPattern();

    /**
     * Does the given argument match the pattern?
     */
    public boolean matches(String argument);

    /**
     * Returns a Vector of matched groups found in the argument.
     *
     * <p>Group 0 will be the full match, the rest are the
     * parenthesized subexpressions</p>.
     */
    public Vector getGroups(String argument);
}
