package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;

/**
 * Simple String comparison condition.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @since Ant 1.4
 * @version $Revision: 274041 $
 */
public class Equals implements Condition {

    private String arg1, arg2;
    private boolean trim = false;
    private boolean caseSensitive = true;

    public void setArg1(String a1) {
        arg1 = a1;
    }

    public void setArg2(String a2) {
        arg2 = a2;
    }

    /**
     * Should we want to trim the arguments before comparing them?
     *
     * @since Revision: 1.3, Ant 1.5
     */
    public void setTrim(boolean b) {
        trim = b;
    }

    /**
     * Should the comparison be case sensitive?
     *
     * @since Revision: 1.3, Ant 1.5
     */
    public void setCasesensitive(boolean b) {
        caseSensitive = b;
    }

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
