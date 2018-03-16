package org.apache.synapse.endpoints;

import org.apache.axis2.clustering.ClusterManager;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.FaultHandler;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.core.axis2.Axis2MessageContext;

import java.util.List;

/**
 * FailoverEndpoint can have multiple child endpoints. It will always try to send messages to
 * current endpoint. If the current endpoint is failing, it gets another active endpoint from the
 * list and make it the current endpoint. Then the message is sent to the current endpoint and if
 * it fails, above procedure repeats until there are no active endpoints. If all endpoints are
 * failing and parent endpoint is available, this will delegate the problem to the parent endpoint.
 * If parent endpoint is not available it will pop the next FaultHandler and delegate the problem
 * to that.
 */
public class FailoverEndpoint implements Endpoint {

    private static final Log log = LogFactory.getLog(FailoverEndpoint.class);

    /**
     * Name of the endpoint. Used for named endpoints which can be referred using the key attribute
     * of indirect endpoints.
     */
    private String name = null;
    /**
     * List of child endpoints. Failover sending is done among these. Any object implementing the
     * Endpoint interface can be a child.
     */
    private List<Endpoint> endpoints = null;

    /**
     * Endpoint for which currently sending the SOAP traffic.
     */
    private Endpoint currentEndpoint = null;

    /**
     * Parent endpoint of this endpoint if this used inside another endpoint. Possible parents are
     * LoadbalanceEndpoint, SALoadbalanceEndpoint and FailoverEndpoint objects. But use of
     * SALoadbalanceEndpoint as the parent is the logical scenario.
     */
    private Endpoint parentEndpoint = null;

    /**
     * The endpoint context , place holder for keep any runtime states related to the endpoint
     */
    private final EndpointContext endpointContext = new EndpointContext();

    public void send(MessageContext synMessageContext) {

        if (log.isDebugEnabled()) {
            log.debug("Start : Failover Endpoint");
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

            if (log.isDebugEnabled() && isClusteringEnable) {
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
        }

        synMessageContext.getEnvelope().build();

        if (currentEndpoint.isActive(synMessageContext)) {
            currentEndpoint.send(synMessageContext);
        } else {

            boolean foundEndpoint = false;
            for (Endpoint endpoint : endpoints) {
                if (endpoint.isActive(synMessageContext)) {
                    foundEndpoint = true;
                    currentEndpoint = endpoint;
                    currentEndpoint.send(synMessageContext);
                    break;
                }
            }

            if (!foundEndpoint) {
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
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    /**
     * If this endpoint is in inactive state, checks if all immediate child endpoints are still
     * failed. If so returns false. If at least one child endpoint is in active state, sets this
     * endpoint's state to active and returns true.
     *
     * @param synMessageContext MessageContext of the current message. This is not used here.
     * @return true if active. false otherwise.
     */
    public boolean isActive(MessageContext synMessageContext) {
        boolean active = endpointContext.isActive();
        if (!active) {
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
        this.endpointContext.setActive(active);
    }

    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
        if (endpoints.size() > 0) {
            currentEndpoint = endpoints.get(0);
        }
    }

    public void onChildEndpointFail(Endpoint endpoint, MessageContext synMessageContext) {
        send(synMessageContext);
    }

    public void setParentEndpoint(Endpoint parentEndpoint) {
        this.parentEndpoint = parentEndpoint;
    }
}
