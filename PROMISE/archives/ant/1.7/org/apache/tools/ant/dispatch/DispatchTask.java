package org.apache.tools.ant.dispatch;

import org.apache.tools.ant.Task;

/**
 * Tasks extending this class may contain multiple actions.
 * The method that is invoked for execution depends upon the
 * value of the action attribute of the task.
 * <br/>
 * Example:<br/>
 * &lt;mytask action=&quot;list&quot;/&gt; will invoke the method
 * with the signature public void list() in mytask's class.
 * If the action attribute is not defined in the task or is empty,
 * the execute() method will be called.
 */
public abstract class DispatchTask extends Task implements Dispatchable {
    private String action;

    /**
     * Get the action parameter name.
     * @return the <code>String</code> "action" by default (can be overridden).
     */
    public String getActionParameterName() {
        return "action";
    }

    /**
     * Set the action.
     * @param action the method name.
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Get the action.
     * @return the action.
     */
    public String getAction() {
        return action;
    }
}
