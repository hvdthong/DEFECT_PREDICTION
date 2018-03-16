package org.apache.synapse.startup;

import org.apache.synapse.Startup;

/**
 * 
 */
public abstract class AbstractStartup implements Startup {

    /**
     * Holds the name of a Startup
     */
    protected String name = null;

    /**
     * This will return the name of the startup
     *
     * @return String representing the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * This will set the name of a Startup
     *
     * @param name
     *          String name to be set to the startup
     */
    public void setName(String name) {
        this.name = name;
    }
}
