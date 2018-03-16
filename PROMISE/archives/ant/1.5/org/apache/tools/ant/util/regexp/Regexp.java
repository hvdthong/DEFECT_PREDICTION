package org.apache.tools.ant.util.regexp;

import org.apache.tools.ant.BuildException;

/***
 * Interface which represents a regular expression, and the operations
 * that can be performed on it.
 *
 * @author <a href="mailto:mattinger@mindless.com">Matthew Inger</a>
 */
public interface Regexp extends RegexpMatcher {

    /**
     * Replace only the first occurance of the regular expression
     */
    int REPLACE_FIRST          = 0x00000001;

    /**
     * Replace all occurances of the regular expression
     */
    int REPLACE_ALL            = 0x00000010;

    /**
     * Perform a substitution on the regular expression.
     * @param input The string to substitute on
     * @param argument The string which defines the substitution
     * @param options The list of options for the match and replace. See the
     *                MATCH_ and REPLACE_ constants above.
     */
    String substitute(String input, String argument, int options)
        throws BuildException;
}
