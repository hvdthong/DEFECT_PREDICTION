package org.apache.tools.ant.taskdefs.condition;

import java.util.Enumeration;
import org.apache.tools.ant.BuildException;

/**
 * &lt;or&gt; condition container.
 *
 * <p>Iterates over all conditions and returns true as soon as one
 * evaluates to true.</p>
 *
 * @since Ant 1.4
 */
public class Or extends ConditionBase implements Condition {

    /**
     * @return true if any of the contained conditions evaluate to true
     * @exception BuildException if an error occurs
     */
    public boolean eval() throws BuildException {
        Enumeration e = getConditions();
        while (e.hasMoreElements()) {
            Condition c = (Condition) e.nextElement();
            if (c.eval()) {
                return true;
            }
        }
        return false;
    }

}
