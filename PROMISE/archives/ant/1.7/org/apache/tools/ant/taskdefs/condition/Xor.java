package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;

import java.util.Enumeration;

/**
 * The <tt>Xor</tt> condition type to exclusive or operations.
 * This does not shortcut stuff.
 * @since Ant 1.7
 */
public class Xor extends ConditionBase implements Condition {

    /**
     * Evaluate the contained conditions.
     * @return the result of xoring the conditions together.
     * @throws org.apache.tools.ant.BuildException
     *          if an error occurs.
     */
    public boolean eval() throws BuildException {
        Enumeration e = getConditions();
        boolean state = false;
        while (e.hasMoreElements()) {
            Condition c = (Condition) e.nextElement();
            state ^= c.eval();
        }
        return state;
    }

}
