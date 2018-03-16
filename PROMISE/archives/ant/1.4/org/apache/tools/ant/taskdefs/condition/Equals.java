package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;

/**
 * Simple String comparison condition.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de>Stefan Bodewig</a>
 * @version $Revision: 269456 $
 */
public class Equals implements Condition {

    private String arg1, arg2;

    public void setArg1(String a1) {arg1 = a1;}
    public void setArg2(String a2) {arg2 = a2;}

    public boolean eval() throws BuildException {
        if (arg1 == null || arg2 == null) {
            throw new BuildException("both arg1 and arg2 are required in equals");
        }
        return arg1.equals(arg2);
    }
}
