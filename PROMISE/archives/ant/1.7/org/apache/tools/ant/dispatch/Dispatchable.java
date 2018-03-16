package org.apache.tools.ant.dispatch;

/**
 * Classes implementing this interface specify the
 * name of the parameter that contains the name
 * of the task's method to execute.
 */
public interface Dispatchable {
    /**
     * Get the name of the parameter.
     * @return the name of the parameter that contains the name of the method.
     */
    String getActionParameterName();
}
