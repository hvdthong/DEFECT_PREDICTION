package org.apache.tools.ant.taskdefs.condition;

import java.util.Enumeration;

import org.apache.tools.ant.BuildException;

/**
 * &lt;or&gt; condition container.
 *
 * <p>Iterates over all conditions and returns true as soon as one
 * evaluates to true.</p>
 * 
 * @author <a href="mailto:stefan.bodewig@epost.de>Stefan Bodewig</a>
 * @version $Revision: 269456 $
 */
public class Or extends ConditionBase implements Condition {

    public boolean eval() throws BuildException {
        Enumeration enum = getConditions();
        while (enum.hasMoreElements()) {
            Condition c = (Condition) enum.nextElement();
            if (c.eval()) {
                return true;
            }
        }
        return false;
    }

}
