package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;

/**
 * Is one string part of another string?
 *
 *
 * @since Ant 1.5
 */
public class Contains implements Condition {

    private String string, subString;
    private boolean caseSensitive = true;

    /**
     * The string to search in.
     * @param string the string to search in
     * @since Ant 1.5
     */
    public void setString(String string) {
        this.string = string;
    }

    /**
     * The string to search for.
     * @param subString the string to search for
     * @since Ant 1.5
     */
    public void setSubstring(String subString) {
        this.subString = subString;
    }

    /**
     * Whether to search ignoring case or not.
     * @param b if true, ignore case
     * @since Ant 1.5
     */
    public void setCasesensitive(boolean b) {
        caseSensitive = b;
    }

    /**
     * @since Ant 1.5
     * @return true if the substring is within the string
     * @exception BuildException if the attributes are not set correctly
     */
    public boolean eval() throws BuildException {
        if (string == null || subString == null) {
            throw new BuildException("both string and substring are required "
                                     + "in contains");
        }

        return caseSensitive
            ? string.indexOf(subString) > -1
            : string.toLowerCase().indexOf(subString.toLowerCase()) > -1;
    }
}
