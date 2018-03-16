package org.apache.tools.ant.util.regexp;

import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

import org.apache.tools.ant.BuildException;
import java.util.Vector;

/**
 * Implementation of RegexpMatcher for Jakarta-Regexp.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 * @author Matthew Inger <a href="mailto:mattinger@mindless.com">mattinger@mindless.com</a>
 */
public class JakartaRegexpMatcher implements RegexpMatcher {

    private String pattern;

    /**
     * Set the regexp pattern from the String description.
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Get a String representation of the regexp pattern
     */
    public String getPattern() {
        return pattern;
    }

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
     */
    public boolean matches(String argument) throws BuildException {
        return matches(argument, MATCH_DEFAULT);
    }

    /**
     * Does the given argument match the pattern?
     */
    public boolean matches(String input, int options)
        throws BuildException {
        return matches(input, getCompiledPattern(options));
    }

    private boolean matches(String input, RE reg) {
        return reg.match(input);
    }

    /**
     * Returns a Vector of matched groups found in the argument.
     *
     * <p>Group 0 will be the full match, the rest are the
     * parenthesized subexpressions</p>.
     */
    public Vector getGroups(String argument) throws BuildException {
        return getGroups(argument, MATCH_DEFAULT);
    }

    public Vector getGroups(String input, int options)
        throws BuildException {
        RE reg = getCompiledPattern(options);
        if (!matches(input, reg)) {
            return null;
        }
        Vector v = new Vector();
        int cnt = reg.getParenCount();
        for (int i = 0; i < cnt; i++) {
            v.addElement(reg.getParen(i));
        }
        return v;
    }

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
