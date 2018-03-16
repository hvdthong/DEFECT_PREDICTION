package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;

/**
 * &lt;not&gt; condition.
 *
 * Evaluates to true if the single condition nested into it is false
 * and vice versa.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @since Ant 1.4
 * @version $Revision: 274041 $
 */
public class Not extends ConditionBase implements Condition {

    public boolean eval() throws BuildException {
        if (countConditions() > 1) {
            throw new BuildException("You must not nest more than one condition into <not>");
        }
        if (countConditions() < 1) {
            throw new BuildException("You must nest a condition into <not>");
        }
        return !((Condition) getConditions().nextElement()).eval();
    }

}
