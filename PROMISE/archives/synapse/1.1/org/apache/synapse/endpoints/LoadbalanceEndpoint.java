package org.apache.synapse.endpoints;

import org.apache.synapse.FaultHandler;
import org.apache.synapse.MessageContext;
import org.apache.synapse.endpoints.algorithms.LoadbalanceAlgorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Load balance endpoint can have multiple endpoints. It will route messages according to the
 * specified loadbalance algorithm. This will assume that all immediate child endpoints are identical
 * in state (state is replicated) or state is not maintained at those endpoints. If an endpoint is
 * failing, the failed endpoint is marked as inactive and the message to the next endpoint obtained
 * using the loadbalance algorithm. If all the endpoints have failed and the parent endpoint is
 * available, onChildEndpointFail(...) methos of parent endpoint is called. If parent is not
 * avialable, this will call next FaultHandler for the message context.
 */
public class LoadbalanceEndpoint implements Endpoint {

    /**
     * Name of the endpoint. Used for named endpoints which can be referred using the key attribute
     * of indirect endpoints.
     */
    private String name = null;

    /**
     * List of endpoints among which the load is distributed. Any object implementing the Endpoint
     * interface could be used.
     */
    private List endpoints = null;

    /**
     * Algorithm used for selecting the next endpoint to direct the load. Default is RoundRobin.
     */
    private LoadbalanceAlgorithm algorithm = null;

    /**
     * Determine whether this endpoint is active or not. This is active iff all child endpoints of
     * this endpoint is active. This is always loaded from the memory as it could be accessed from
     * multiple threads simultaneously.
     */
    private volatile boolean active = true;

    /**
     * If this supports load balancing with failover. If true, request will be directed to the next
     * endpoint if the current one is failing.
     */
    private boolean failover = true;

    /**
     * Parent endpoint of this endpoint if this used inside another endpoint. Possible parents are
     * LoadbalanceEndpoint, SALoadbalanceEndpoint and FailoverEndpoint objects.
     */
    private Endpoint parentEndpoint = null;

    public void send(MessageContext synMessageContext) {

        Endpoint endpoint = algorithm.getNextEndpoint(synMessageContext);
        if (endpoint != null) {

            if (failover) {
                synMessageContext.getEnvelope().build();
            }

            endpoint.send(synMessageContext);

        } else {
            setActive(false, synMessageContext);

            if (parentEndpoint != null) {
                parentEndpoint.onChildEndpointFail(this, synMessageContext);
            } else {
                Object o = synMessageContext.getFaultStack().pop();
                if (o != null) {
                    ((FaultHandler) o).handleFault(synMessageContext);
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public LoadbalanceAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(LoadbalanceAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * If this endpoint is in inactive state, checks if all immediate child endpoints are still
     * failed. If so returns false. If at least one child endpoint is in active state, sets this
     * endpoint's state to active and returns true. As this a sessionless load balancing endpoint
     * having one active child endpoint is enough to consider this as active.
     *
     * @param synMessageContext MessageContext of the current message. This is not used here.
     *
     * @return true if active. false otherwise.
     */
    public boolean isActive(MessageContext synMessageContext) {

        if (!active) {
            for (int i = 0; i < endpoints.size(); i++) {
                Endpoint endpoint = (Endpoint) endpoints.get(i);
                if (endpoint.isActive(synMessageContext)) {
                    active = true;

                }
            }
        }

        return active;
    }

    public void setActive(boolean active, MessageContext synMessageContext) {
        this.active = active;
    }

    public boolean isFailover() {
        return failover;
    }

    public void setFailover(boolean failover) {
        this.failover = failover;
    }

    public List getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List endpoints) {
        this.endpoints = endpoints;
    }

    public void setParentEndpoint(Endpoint parentEndpoint) {
        this.parentEndpoint = parentEndpoint;
    }

    public void onChildEndpointFail(Endpoint endpoint, MessageContext synMessageContext) {

        if (failover) {
            send(synMessageContext);
        } else {
            Object o = synMessageContext.getFaultStack().pop();
            if (o != null) {
                ((FaultHandler) o).handleFault(synMessageContext);
            }
        }
    }
}
