package org.apache.synapse;

import javax.xml.namespace.QName;

/**
 * This startup interface will be instantiated to create startup tasks.
 */
public interface Startup extends ManagedLifecycle {

    /**
     * This will return the configuration tag QName of the implemented startup
     *
     * @return QName representing the configuraiton element for the startup
     */
    public abstract QName getTagQName();

    /**
     * This will return the name of the startup
     *
     * @return String representing the name
     */
    public String getName();

    /**
     * This will set the name of a Startup
     *
     * @param id String name to be set to the startup
     */
    public void setName(String id);
}
