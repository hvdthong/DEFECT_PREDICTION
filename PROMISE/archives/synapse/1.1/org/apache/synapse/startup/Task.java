package org.apache.synapse.startup;

/**
 * Defines the Task for a SimpleQuarts Startup
 */
public interface Task {

    /**
     * Esecute method will be invoked by the SimpleQuartzStartup
     */
    public void execute();
}
