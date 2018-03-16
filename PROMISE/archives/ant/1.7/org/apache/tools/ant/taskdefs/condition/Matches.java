package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.util.regexp.Regexp;
import org.apache.tools.ant.types.RegularExpression;
import org.apache.tools.ant.util.regexp.RegexpMatcher;

/**
 * Simple regular expression condition.
 *
 * @since Ant 1.7
 */
public class Matches extends ProjectComponent implements Condition {

    private String  string;
    private boolean caseSensitive = true;
    private boolean multiLine = false;
    private boolean singleLine = false;
    private RegularExpression regularExpression;

    /**
     * Set the string
     *
     * @param string the string to match
     */
    public void setString(String string) {
        this.string = string;
    }

    /**
     * Set the regular expression to match against
     *
     * @param pattern the regular expression pattern
     */
    public void setPattern(String pattern) {
        if (regularExpression != null) {
            throw new BuildException(
                "Only one regular expression is allowed.");
        }
        regularExpression = new RegularExpression();
        regularExpression.setPattern(pattern);
    }

    /**
     * A regular expression.
     * You can use this element to refer to a previously
     * defined regular expression datatype instance
     * @param regularExpression the regular expression object
     *                          to be configured as an element
     */
    public void addRegexp(RegularExpression regularExpression) {
        if (this.regularExpression != null) {
            throw new BuildException(
                "Only one regular expression is allowed.");
        }
        this.regularExpression = regularExpression;
    }

    /**
     * Whether to ignore case or not.
     * @param b if false, ignore case.
     * @since Ant 1.7
     */
    public void setCasesensitive(boolean b) {
        caseSensitive = b;
    }

    /**
     * Whether to match should be multiline.
     * @param b the value to set.
     */
    public void setMultiline(boolean b) {
        multiLine = b;
    }

    /**
     * Whether to treat input as singleline ('.' matches newline).
     * Corresponsds to java.util.regex.Pattern.DOTALL.
     * @param b the value to set.
     */
    public void setSingleLine(boolean b) {
        singleLine = b;
    }

    /**
     * @return true if the string matches the regular expression pattern
     * @exception BuildException if the attributes are not set correctly
     */
    public boolean eval() throws BuildException {
        if (string == null) {
            throw new BuildException(
                "Parameter string is required in matches.");
        }
        if (regularExpression == null) {
            throw new BuildException("Missing pattern in matches.");
        }
        int options = RegexpMatcher.MATCH_DEFAULT;
        if (!caseSensitive) {
            options = options | RegexpMatcher.MATCH_CASE_INSENSITIVE;
        }
        if (multiLine) {
            options = options | RegexpMatcher.MATCH_MULTILINE;
        }
        if (singleLine) {
            options = options | RegexpMatcher.MATCH_SINGLELINE;
        }
        Regexp regexp = regularExpression.getRegexp(getProject());
        return regexp.matches(string, options);
    }
}
