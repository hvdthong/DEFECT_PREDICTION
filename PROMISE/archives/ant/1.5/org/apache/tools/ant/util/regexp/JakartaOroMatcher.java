package org.apache.tools.ant.util.regexp;

import org.apache.tools.ant.BuildException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;



import java.util.Vector;

/**
 * Implementation of RegexpMatcher for Jakarta-ORO.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 * @author <a href="mailto:mattinger@mindless.com">Matthew Inger</a>
 */
public class JakartaOroMatcher implements RegexpMatcher {

    private String pattern;
    protected final Perl5Compiler compiler = new Perl5Compiler();
    protected final Perl5Matcher matcher = new Perl5Matcher();

    public JakartaOroMatcher() {}

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
        return this.pattern;
    }

    /**
     * Get a compiled representation of the regexp pattern
     */
    protected Pattern getCompiledPattern(int options)
        throws BuildException {
        try {
            Pattern p = compiler.compile(pattern, getCompilerOptions(options));
            return p;
        } catch (Exception e) {
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
        Pattern p = getCompiledPattern(options);
        return matcher.contains(input, p);
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
        if (!matches(input, options)) {
            return null;
        }
        Vector v = new Vector();
        MatchResult mr = matcher.getMatch();
        int cnt = mr.groups();
        for (int i = 0; i < cnt; i++) {
            v.addElement(mr.group(i));
        }
        return v;
    }

    protected int getCompilerOptions(int options) {
        int cOptions = Perl5Compiler.DEFAULT_MASK;

        if (RegexpUtil.hasFlag(options, MATCH_CASE_INSENSITIVE)) {
            cOptions |= Perl5Compiler.CASE_INSENSITIVE_MASK;
        }
        if (RegexpUtil.hasFlag(options, MATCH_MULTILINE)) {
            cOptions |= Perl5Compiler.MULTILINE_MASK;
        }
        if (RegexpUtil.hasFlag(options, MATCH_SINGLELINE)) {
            cOptions |= Perl5Compiler.SINGLELINE_MASK;
        }
        
        return cOptions;
    }

}
