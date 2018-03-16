package org.apache.synapse;

import org.apache.synapse.core.SynapseEnvironment;

/**
 * This interface defines all the managed stateful parts of Synapse
 * including the configuration itself.
 */
public interface ManagedLifecycle {

    /**
     * This method should implement the initialization of the
     * implemented parts of the configuration.
     *
     * @param se SynapseEnvironment to be used for initialization
     */
    public void init(SynapseEnvironment se);

    /**
     * This method should implement the destroying of the
     * implemented parts of the configuration.
     */
    public void destroy();
}
