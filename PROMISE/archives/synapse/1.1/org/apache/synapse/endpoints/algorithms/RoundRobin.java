package org.apache.synapse.endpoints.algorithms;

import org.apache.synapse.MessageContext;
import org.apache.synapse.endpoints.Endpoint;

import java.util.ArrayList;

/**
 * This is the implementation of the round robin load balancing algorithm. It simply iterates through
 * the endpoint list one by one for until an active endpoint is found.
 */
public class RoundRobin implements LoadbalanceAlgorithm {

    private ArrayList endpoints = null;
    private int currentEPR = 0;

    public RoundRobin(ArrayList endpoints) {
        this.endpoints = endpoints;
    }

    /**
     * Choose an active endpoint using the round robin algorithm. If there are no active endpoints
     * available, returns null.
     *
     * @param synapseMessageContext
     * @return endpoint to send the next message
     */
    public Endpoint getNextEndpoint(MessageContext synapseMessageContext) {

        Endpoint nextEndpoint = null;
        int attempts = 0;

        do {
            synchronized(this) {
                nextEndpoint = (Endpoint) endpoints.get(currentEPR);

                if(currentEPR == endpoints.size() - 1) {
                    currentEPR = 0;
                } else {
                    currentEPR++;
                }
            }

            attempts++;
            if (attempts > endpoints.size()) {
                return null;
            }

        } while (!nextEndpoint.isActive(synapseMessageContext));

        return nextEndpoint;
    }

    public void reset() {
        currentEPR = 0;
    }
}
