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
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @version $Revision: 274041 $
 *
 * @since Ant 1.4
 *
 * @ant.task category="control"
 */
public class ConditionTask extends ConditionBase {

    private String property = null;
    private String value = "true";

    /**
     * The name of the property to set. Required.
     *
     * @since Ant 1.4
     */
    public void setProperty(String p) {property = p;}

    /**
     * The value for the property to set, if condition evaluates to true.
     * Defaults to "true".
     *
     * @since Ant 1.4
     */
    public void setValue(String v) {value = v;}

    /**
     * See whether our nested condition holds and set the property.
     *
     * @since Ant 1.4
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
            log("Condition true; setting "+property+" to "+value,
                Project.MSG_DEBUG);
            getProject().setNewProperty(property, value);
        } else {
            log("Condition false; not setting "+property,
                Project.MSG_DEBUG);
        }
    }
}
