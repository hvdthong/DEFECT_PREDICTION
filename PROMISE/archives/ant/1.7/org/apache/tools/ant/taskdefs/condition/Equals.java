package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;

/**
 * Simple String comparison condition.
 *
 * @since Ant 1.4
 */
public class Equals implements Condition {

    private String arg1, arg2;
    private boolean trim = false;
    private boolean caseSensitive = true;

    /**
     * Set the first string
     *
     * @param a1 the first string
     */
    public void setArg1(String a1) {
        arg1 = a1;
    }

    /**
     * Set the second string
     *
     * @param a2 the second string
     */
    public void setArg2(String a2) {
        arg2 = a2;
    }

    /**
     * Should we want to trim the arguments before comparing them?
     * @param b if true trim the arguments
     * @since Ant 1.5
     */
    public void setTrim(boolean b) {
        trim = b;
    }

    /**
     * Should the comparison be case sensitive?
     * @param b if true use a case sensitive comparison (this is the
     *          default)
     * @since Ant 1.5
     */
    public void setCasesensitive(boolean b) {
        caseSensitive = b;
    }

    /**
     * @return true if the two strings are equal
     * @exception BuildException if the attributes are not set correctly
     */
    public boolean eval() throws BuildException {
        if (arg1 == null || arg2 == null) {
            throw new BuildException("both arg1 and arg2 are required in "
                                     + "equals");
        }

        if (trim) {
            arg1 = arg1.trim();
            arg2 = arg2.trim();
        }

        return caseSensitive ? arg1.equals(arg2) : arg1.equalsIgnoreCase(arg2);
    }
}
