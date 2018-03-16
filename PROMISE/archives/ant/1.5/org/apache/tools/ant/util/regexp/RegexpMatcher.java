package org.apache.tools.ant.util.regexp;

import org.apache.tools.ant.BuildException;
import java.util.Vector;

/**
 * Interface describing a regular expression matcher.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 * @author <a href="mailto:mattinger@mindless.com">Matthew Inger</a>
 */
public interface RegexpMatcher {

    /***
     * Default Mask (case insensitive, neither multiline nor
     * singleline specified).
     */
    int MATCH_DEFAULT          = 0x00000000;

    /***
     * Perform a case insenstive match
     */
    int MATCH_CASE_INSENSITIVE = 0x00000100;
    
    /***
     * Treat the input as a multiline input
     */
    int MATCH_MULTILINE        = 0x00001000;

    /***
     * Treat the input as singleline input ('.' matches newline)
     */
    int MATCH_SINGLELINE       = 0x00010000;


    /**
     * Set the regexp pattern from the String description.
     */
    void setPattern(String pattern) throws BuildException;

    /**
     * Get a String representation of the regexp pattern
     */
    String getPattern() throws BuildException;

    /**
     * Does the given argument match the pattern?
     */
    boolean matches(String argument) throws BuildException;

    /**
     * Returns a Vector of matched groups found in the argument.
     *
     * <p>Group 0 will be the full match, the rest are the
     * parenthesized subexpressions</p>.
     */
    Vector getGroups(String argument) throws BuildException;

    /***
     * Does this regular expression match the input, given
     * certain options
     * @param input The string to check for a match
     * @param options The list of options for the match. See the
     *                MATCH_ constants above.
     */
    boolean matches(String input, int options) throws BuildException;

    /***
     * Get the match groups from this regular expression.  The return
     * type of the elements is always String.
     * @param input The string to check for a match
     * @param options The list of options for the match. See the
     *                MATCH_ constants above.
     */
    Vector getGroups(String input, int options) throws BuildException;

}
