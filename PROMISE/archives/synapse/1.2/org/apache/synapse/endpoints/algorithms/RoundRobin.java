package org.apache.synapse.endpoints.algorithms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.endpoints.Endpoint;

import java.util.ArrayList;

/**
 * This is the implementation of the round robin load balancing algorithm. It simply iterates
 * through the endpoint list one by one for until an active endpoint is found.
 */
public class RoundRobin implements LoadbalanceAlgorithm {

    private static final Log log = LogFactory.getLog(RoundRobin.class);

    /**
     * Endpoints list for the round robin algorithm
     */
    private ArrayList endpoints = null;

    public RoundRobin(ArrayList endpoints) {
        this.endpoints = endpoints;
    }

    /**
     * Choose an active endpoint using the round robin algorithm. If there are no active endpoints
     * available, returns null.
     *
     * @param synapseMessageContext MessageContext instance which holds all per-message properties
     * @param  algorithmContext The context in which holds run time states related to the algorithm
     * @return endpoint to send the next message
     */
    public Endpoint getNextEndpoint(MessageContext synapseMessageContext,
        AlgorithmContext algorithmContext) {

        if (log.isDebugEnabled()) {
            log.debug("Using the Round Robin loadbalancing algorithm to select the next endpoint");
        }

        Endpoint nextEndpoint;
        int attempts = 0;
        int currentEPR = algorithmContext.getCurrentEndpointIndex();
        do {
            synchronized (this) {
                nextEndpoint = (Endpoint) endpoints.get(currentEPR);

                if (currentEPR == endpoints.size() - 1) {
                    currentEPR = 0;
                } else {
                    currentEPR++;
                }
                algorithmContext.setCurrentEPR(currentEPR);
            }

            attempts++;
            if (attempts > endpoints.size()) {
                log.warn("Couldn't find an endpoint from the Round Robin loadbalancing algorithm");
                return null;
            }

        } while (!nextEndpoint.isActive(synapseMessageContext));

        return nextEndpoint;
    }

    public void reset(AlgorithmContext algorithmContext) {
        if (log.isDebugEnabled()) {
            log.debug("Resetting the Round Robin loadbalancing algorithm ...");
        }
        algorithmContext.setCurrentEPR(0);
    }
}
