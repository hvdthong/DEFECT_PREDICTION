package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.taskdefs.condition.ConditionBase;

/**
 * Task to set a property conditionally using &lt;uptodate&gt;, &lt;available&gt;,
 * and many other supported conditions.
 *
 * <p>This task supports boolean logic as well as pluggable conditions
 * to decide, whether a property should be set.</p>
 *
 * <p>This task does not extend Task to take advantage of
 * ConditionBase.</p>
 *
 *
 * @since Ant 1.4
 *
 * @ant.task category="control"
 */
public class ConditionTask extends ConditionBase {

    private String property = null;
    private String value = "true";
    private String alternative = null;

    /**
     * The name of the property to set. Required.
     * @param p the name of the property
     * @since Ant 1.4
     */
    public void setProperty(String p) {
        property = p;
    }

    /**
     * The value for the property to set, if condition evaluates to true.
     * Defaults to "true".
     * @param v the value of the property
     * @since Ant 1.4
     */
    public void setValue(String v) {
        value = v;
    }

    /**
     * The value for the property to set, if condition evaluates to false.
     * If this attribute is not specified, the property will not be set.
     * @param e the alternate value of the property.
     * @since Ant 1.6.3
     */
    public void setElse(String e) {
        alternative = e;
    }

    /**
     * See whether our nested condition holds and set the property.
     *
     * @since Ant 1.4
     * @exception BuildException if an error occurs
     */
    public void execute() throws BuildException {
        if (countConditions() > 1) {
            throw new BuildException("You must not nest more than one "
                + "condition into <condition>");
        }
        if (countConditions() < 1) {
            throw new BuildException("You must nest a condition into "
                + "<condition>");
        }
        if (property == null) {
            throw new BuildException("The property attribute is required.");
        }
        Condition c = (Condition) getConditions().nextElement();
        if (c.eval()) {
            log("Condition true; setting " + property + " to " + value,
                Project.MSG_DEBUG);
            getProject().setNewProperty(property, value);
        } else if (alternative != null) {
            log("Condition false; setting " + property + " to " + alternative,
                Project.MSG_DEBUG);
            getProject().setNewProperty(property, alternative);
        } else {
            log("Condition false; not setting " + property,
                Project.MSG_DEBUG);
        }
    }
}
