package org.apache.tools.ant.util.regexp;

import org.apache.oro.text.regex.*;

import org.apache.tools.ant.BuildException;
import java.util.Vector;

/**
 * Implementation of RegexpMatcher for Jakarta-ORO.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 */
public class JakartaOroMatcher implements RegexpMatcher {

    protected Perl5Matcher reg = new Perl5Matcher();
    protected Perl5Compiler comp = new Perl5Compiler();
    private Pattern pattern;

    /**
     * Set the regexp pattern from the String description.
     */
    public void setPattern(String pattern) throws BuildException {
        try {
            this.pattern = comp.compile(pattern);
        } catch (MalformedPatternException e) {
            throw new BuildException(e);
        }
    }

    /**
     * Get a String representation of the regexp pattern
     */
    public String getPattern() {
        return pattern.getPattern();
    }

    /**
     * Does the given argument match the pattern?
     */
    public boolean matches(String argument) {
        return reg.contains(argument, pattern);
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
        MatchResult mr = reg.getMatch();
        for (int i=0; i<mr.groups(); i++) {
            v.addElement(mr.group(i));
        }
        return v;
    }

}
