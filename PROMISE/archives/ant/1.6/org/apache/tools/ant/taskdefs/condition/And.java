package org.apache.tools.ant.taskdefs.condition;

import java.util.Enumeration;
import org.apache.tools.ant.BuildException;

/**
 * &lt;and&gt; condition container.
 *
 * <p>Iterates over all conditions and returns false as soon as one
 * evaluates to false.</p>
 *
 * @since Ant 1.4
 */
public class And extends ConditionBase implements Condition {

    /**
     * @return true if all the contained conditions evaluates to true
     * @exception BuildException if an error occurs
     */
    public boolean eval() throws BuildException {
        Enumeration e = getConditions();
        while (e.hasMoreElements()) {
            Condition c = (Condition) e.nextElement();
            if (!c.eval()) {
                return false;
            }
        }
        return true;
    }

}
