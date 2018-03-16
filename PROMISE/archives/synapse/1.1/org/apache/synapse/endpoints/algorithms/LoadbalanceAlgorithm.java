package org.apache.synapse.endpoints.algorithms;

import org.apache.synapse.MessageContext;
import org.apache.synapse.endpoints.Endpoint;

/**
 * All load balance algorithms must implement this interface. Implementations of this interface can
 * be registered in LoadbalanceManagers.
 */
public interface LoadbalanceAlgorithm {

    /**
     * This method returns the next node according to the algorithm implementation.
     *
     * @param synapseMessageContext SynapseMessageContext of the current message
     * @return Next node for directing the message
     */
    public Endpoint getNextEndpoint(MessageContext synapseMessageContext);

    /**
     * Resets the algorithm to its initial position. Initial position depends on the implementation.
     */
    public void reset();
}
