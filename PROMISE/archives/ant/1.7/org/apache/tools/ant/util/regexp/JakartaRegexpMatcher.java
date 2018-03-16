package org.apache.tools.ant.util.regexp;

import java.util.Vector;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;
import org.apache.tools.ant.BuildException;

/**
 * Implementation of RegexpMatcher for Jakarta-Regexp.
 *
 */
public class JakartaRegexpMatcher implements RegexpMatcher {

    private String pattern;

    /**
     * Set the regexp pattern from the String description.
     * @param pattern the pattern to match
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Get a String representation of the regexp pattern
     * @return the pattern
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Compile the pattern.
     *
     * @param options the ant regexp options
     * @return a compiled pattern
     * @exception BuildException if an error occurs
     */
    protected RE getCompiledPattern(int options)
        throws BuildException {
        int cOptions = getCompilerOptions(options);
        try {
            RE reg = new RE(pattern);
            reg.setMatchFlags(cOptions);
            return reg;
        } catch (RESyntaxException e) {
            throw new BuildException(e);
        }
    }

    /**
     * Does the given argument match the pattern?
     * @param argument the string to match against
     * @return true if the pattern matches
     * @throws BuildException on error
     */
    public boolean matches(String argument) throws BuildException {
        return matches(argument, MATCH_DEFAULT);
    }

    /**
     * Does the given argument match the pattern?
     * @param input the string to match against
     * @param options the regex options to use
     * @return true if the pattern matches
     * @throws BuildException on error
     */
    public boolean matches(String input, int options)
        throws BuildException {
        return matches(input, getCompiledPattern(options));
    }

    private boolean matches(String input, RE reg) {
        return reg.match(input);
    }

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
    public Vector getGroups(String argument) throws BuildException {
        return getGroups(argument, MATCH_DEFAULT);
    }

    /**
     * Returns a Vector of matched groups found in the argument.
     *
     * <p>Group 0 will be the full match, the rest are the
     * parenthesized subexpressions</p>.
     *
     * @param input the string to match against
     * @param options the regex options to use
     * @return the vector of groups
     * @throws BuildException on error
     */
    public Vector getGroups(String input, int options)
        throws BuildException {
        RE reg = getCompiledPattern(options);
        if (!matches(input, reg)) {
            return null;
        }
        Vector v = new Vector();
        int cnt = reg.getParenCount();
        for (int i = 0; i < cnt; i++) {
            String match = reg.getParen(i);
            if (match == null) {
                match = "";
            }
            v.addElement(match);
        }
        return v;
    }

    /**
     * Convert the generic options to the regex compiler specific options.
     * @param options the generic options
     * @return the specific options
     */
    protected int getCompilerOptions(int options) {
        int cOptions = RE.MATCH_NORMAL;

        if (RegexpUtil.hasFlag(options, MATCH_CASE_INSENSITIVE)) {
            cOptions |= RE.MATCH_CASEINDEPENDENT;
        }
        if (RegexpUtil.hasFlag(options, MATCH_MULTILINE)) {
            cOptions |= RE.MATCH_MULTILINE;
        }
        if (RegexpUtil.hasFlag(options, MATCH_SINGLELINE)) {
            cOptions |= RE.MATCH_SINGLELINE;
        }

        return cOptions;
    }

}
