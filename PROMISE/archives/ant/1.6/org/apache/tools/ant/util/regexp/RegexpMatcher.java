package org.apache.tools.ant.util.regexp;

import java.util.Vector;
import org.apache.tools.ant.BuildException;

/**
 * Interface describing a regular expression matcher.
 *
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
     * @param pattern the pattern to match
     * @throws BuildException on error
     */
    void setPattern(String pattern) throws BuildException;

    /**
     * Get a String representation of the regexp pattern
     * @return the pattern
     * @throws BuildException on error
     */
    String getPattern() throws BuildException;

    /**
     * Does the given argument match the pattern?
     * @param argument the string to match against
     * @return true if the pattern matches
     * @throws BuildException on error
     */
    boolean matches(String argument) throws BuildException;

    /**
     * Returns a Vector of matched groups found in the argument
     * using default options.
     *
     * <p>Group 0 will be the full match, the rest are the
     * parenthesized subexpressions</p>.
     *
     * @param argument the string to match against
     * @return the vector of groups
     * @throws BuildException on error
     */
    Vector getGroups(String argument) throws BuildException;

    /***
     * Does this regular expression match the input, given
     * certain options
     * @param input The string to check for a match
     * @param options The list of options for the match. See the
     *                MATCH_ constants above.
     * @return true if the pattern matches
     * @throws BuildException on error
     */
    boolean matches(String input, int options) throws BuildException;

    /***
     * Get the match groups from this regular expression.  The return
     * type of the elements is always String.
     * @param input The string to check for a match
     * @param options The list of options for the match. See the
     *                MATCH_ constants above.
     * @return the vector of groups
     * @throws BuildException on error
     */
    Vector getGroups(String input, int options) throws BuildException;

}
