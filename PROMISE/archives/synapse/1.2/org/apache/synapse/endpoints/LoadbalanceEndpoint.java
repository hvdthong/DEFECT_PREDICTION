package org.apache.synapse.endpoints;

import org.apache.axis2.clustering.ClusterManager;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.FaultHandler;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.endpoints.algorithms.AlgorithmContext;
import org.apache.synapse.endpoints.algorithms.LoadbalanceAlgorithm;

import java.util.List;

/**
 * Load balance endpoint can have multiple endpoints. It will route messages according to the
 * specified load balancing algorithm. This will assume that all immediate child endpoints are
 * identical in state (state is replicated) or state is not maintained at those endpoints. If an
 * endpoint is failing, the failed endpoint is marked as inactive and the message to the next
 * endpoint obtained using the load balancing algorithm. If all the endpoints have failed and the
 * parent endpoint is available, onChildEndpointFail(...) method of parent endpoint is called. If
 * parent is not available, this will call next FaultHandler for the message context.
 */
public class LoadbalanceEndpoint implements Endpoint {

    private static final Log log = LogFactory.getLog(LoadbalanceEndpoint.class);
    /**
     * Name of the endpoint. Used for named endpoints which can be referred using the key attribute
     * of indirect endpoints.
     */
    private String name = null;

    /**
     * List of endpoints among which the load is distributed. Any object implementing the Endpoint
     * interface could be used.
     */
    private List<Endpoint> endpoints = null;

    /**
     * Algorithm used for selecting the next endpoint to direct the load. Default is RoundRobin.
     */
    private LoadbalanceAlgorithm algorithm = null;

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

    /**
     * The endpoint context , place holder for keep any runtime states related to the endpoint
     */
    private final EndpointContext endpointContext = new EndpointContext();

    /**
     * The algorithm context , place holder for keep any runtime states related to the load balance
     * algorithm
     */
    private final AlgorithmContext algorithmContext = new AlgorithmContext();

    public void send(MessageContext synMessageContext) {

        if (log.isDebugEnabled()) {
            log.debug("Start : Load-balance Endpoint");
        }

        boolean isClusteringEnable = false;
        org.apache.axis2.context.MessageContext axisMC =
                ((Axis2MessageContext) synMessageContext).getAxis2MessageContext();
        ConfigurationContext cc = axisMC.getConfigurationContext();


        ClusterManager clusterManager = cc.getAxisConfiguration().getClusterManager();
        if (clusterManager != null &&
                clusterManager.getContextManager() != null) {
            isClusteringEnable = true;
        }

        String endPointName = this.getName();
        if (endPointName == null) {

            if (isClusteringEnable) {
                log.warn("In a clustering environment , the endpoint  name should be specified" +
                        "even for anonymous endpoints. Otherwise , the clustering would not be " +
                        "functioned correctly if there are more than one anonymous endpoints. ");
            }
            endPointName = SynapseConstants.ANONYMOUS_ENDPOINT;
        }

        if (isClusteringEnable) {

            if (endpointContext.getConfigurationContext() == null) {
                endpointContext.setConfigurationContext(cc);
                endpointContext.setContextID(endPointName);

            }
            if (algorithmContext.getConfigurationContext() == null) {
                algorithmContext.setConfigurationContext(cc);
                algorithmContext.setContextID(endPointName);
            }
        }

        Endpoint endpoint = algorithm.getNextEndpoint(synMessageContext, algorithmContext);
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
     * @return true if active. false otherwise.
     */
    public boolean isActive(MessageContext synMessageContext) {
        boolean active = endpointContext.isActive();
        if (!active && endpoints != null) {
            for (Endpoint endpoint : endpoints) {
                if (endpoint.isActive(synMessageContext)) {
                    active = true;
                    endpointContext.setActive(true);

                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Endpoint  '" + name + "' is in state ' " + active + " '");
        }

        return active;
    }

    public void setActive(boolean active, MessageContext synMessageContext) {
        endpointContext.setActive(active);
    }

    public boolean isFailover() {
        return failover;
    }

    public void setFailover(boolean failover) {
        this.failover = failover;
    }

    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<Endpoint> endpoints) {
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
