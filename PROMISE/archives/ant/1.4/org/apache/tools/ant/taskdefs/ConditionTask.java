package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.taskdefs.condition.ConditionBase;

/**
 * &lt;condition&gt; task as a generalization of &lt;available&gt; and
 * &lt;uptodate&gt;
 *
 * <p>This task supports boolean logic as well as pluggable conditions
 * to decide, whether a property should be set.</p>
 *
 * <p>This task does not extend Task to take advantage of
 * ConditionBase.</p>
 *
 * @author <a href="mailto:stefan.bodewig@epost.de>Stefan Bodewig</a>
 * @version $Revision: 269456 $
 */
public class ConditionTask extends ConditionBase {

    private String property;
    private String value = "true";

    /**
     * The name of the property to set. Required.
     *
     * @since 1.1
     */
    public void setProperty(String p) {property = p;}

    /**
     * The value for the property to set. Defaults to "true".
     *
     * @since 1.1
     */
    public void setValue(String v) {value = v;}

    /**
     * See whether our nested condition holds and set the property.
     *
     * @since 1.1
     */
    public void execute() throws BuildException {
        if (countConditions() > 1) {
            throw new BuildException("You must not nest more than one condition into <condition>");
        }
        if (countConditions() < 1) {
            throw new BuildException("You must nest a condition into <condition>");
        }
        Condition c = (Condition) getConditions().nextElement();
        if (c.eval()) {
            getProject().setProperty(property, value);
        }
    }
}
