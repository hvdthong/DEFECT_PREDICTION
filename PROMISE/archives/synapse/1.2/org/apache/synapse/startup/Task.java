package org.apache.synapse.startup;

/**
 * Defines the Task for a SimpleQuartzStartup.
 */
public interface Task {

    /**
     * Execute method will be invoked by the SimpleQuartzStartup.
     */
    public void execute();
}
