package org.apache.tools.ant.util.regexp;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.tools.ant.BuildException;

/**
 * Implementation of RegexpMatcher for the built-in regexp matcher of
 * JDK 1.4. UNIX_LINES option is enabled as a default.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 * @author Matthew Inger <a href="mailto:mattinger@mindless.com">mattinger@mindless.com</a>
 */
public class Jdk14RegexpMatcher implements RegexpMatcher {

    private String pattern;

    public Jdk14RegexpMatcher() {}

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

    protected Pattern getCompiledPattern(int options)
        throws BuildException {
        int cOptions = getCompilerOptions(options);
        try {
            Pattern p = Pattern.compile(this.pattern, cOptions);
            return p;
        } catch (PatternSyntaxException e) {
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
        try {
            Pattern p = getCompiledPattern(options);
            return p.matcher(input).find();
        } catch (Exception e) {
            throw new BuildException(e);
        }
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

    /**
     * Returns a Vector of matched groups found in the argument.
     *
     * <p>Group 0 will be the full match, the rest are the
     * parenthesized subexpressions</p>.
     */
    public Vector getGroups(String input, int options)
        throws BuildException {
        Pattern p = getCompiledPattern(options);
        Matcher matcher = p.matcher(input);
        if (!matcher.find()) {
            return null;
        }
        Vector v = new Vector();
        int cnt = matcher.groupCount();
        for (int i = 0; i <= cnt; i++) {
            v.addElement(matcher.group(i));
        }
        return v;
    }

    protected int getCompilerOptions(int options) {
        int cOptions = Pattern.UNIX_LINES;

        if (RegexpUtil.hasFlag(options, MATCH_CASE_INSENSITIVE)) {
            cOptions |= Pattern.CASE_INSENSITIVE;
        }
        if (RegexpUtil.hasFlag(options, MATCH_MULTILINE)) {
            cOptions |= Pattern.MULTILINE;
        }
        if (RegexpUtil.hasFlag(options, MATCH_SINGLELINE)) {
            cOptions |= Pattern.DOTALL;
        }

        return cOptions;
    }

}
