package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;

/**
 * Is one string part of another string?
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @version $Revision: 274041 $
 *
 * @since Ant 1.5
 */
public class Contains implements Condition {

    private String string, subString;
    private boolean caseSensitive = true;

    /**
     * The string to search in.
     *
     * @since 1.1, Ant 1.5
     */
    public void setString(String a1) {
        string = a1;
    }

    /**
     * The string to search for.
     *
     * @since 1.1, Ant 1.5
     */
    public void setSubstring(String a2) {
        subString = a2;
    }

    /**
     * Whether to search ignoring case or not.
     * 
     * @since 1.1, Ant 1.5
     */
    public void setCasesensitive(boolean b) {
        caseSensitive = b;
    }

    /** 
     * @since 1.1, Ant 1.5
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
