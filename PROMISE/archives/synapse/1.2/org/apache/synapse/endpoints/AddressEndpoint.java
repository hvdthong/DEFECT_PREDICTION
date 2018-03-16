package org.apache.synapse.endpoints;

import org.apache.axis2.clustering.ClusterManager;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.endpoints.utils.EndpointDefinition;
import org.apache.synapse.statistics.impl.EndPointStatisticsStack;

/**
 * This class represents an actual endpoint to send the message. It is responsible for sending the
 * message, performing retries if a failure occurred and informing the parent endpoint if a failure
 * couldn't be recovered.
 */
public class AddressEndpoint extends DefaultEndpoint {

    /**
     * The endpoint context , place holder for keep any runtime states related to the endpoint
     */
    private final EndpointContext endpointContext = new EndpointContext();

    /**
     * Checks if the endpoint is active (failed or not). If endpoint is in failed state and
     * suspendOnFailDuration has elapsed, it will be set to active.
     *
     * @param synMessageContext MessageContext of the current message. This is not used here.
     * @return true if endpoint is active. false otherwise.
     */
    public boolean isActive(MessageContext synMessageContext) {

        boolean active = endpointContext.isActive();
        if (!active) {

            long recoverOn = endpointContext.getRecoverOn();
            if (System.currentTimeMillis() > recoverOn) {
                active = true;
                endpointContext.setActive(true);
                endpointContext.setRecoverOn(0);                       

            }
        }

        if (log.isDebugEnabled()) {
            log.debug("AddressEndpoint with name '" + getName() + "' is in "
                    + (active ? "active" : "inactive") + " state");
        }

        return active;
    }

    /**
     * Sets if endpoint active or not. if endpoint is set as failed (active = false), the recover on
     * time is calculated so that it will be activated after the recover on time.
     *
     * @param active            true if active. false otherwise.
     * @param synMessageContext MessageContext of the current message. This is not used here.
     */
    public synchronized void setActive(boolean active, MessageContext synMessageContext) {


        if (!active) {
            EndpointDefinition endpoint = getEndpoint();
            if (endpoint.getSuspendOnFailDuration() != -1) {
                endpointContext.setRecoverOn(
                        System.currentTimeMillis() + endpoint.getSuspendOnFailDuration());
            } else {
                endpointContext.setRecoverOn(Long.MAX_VALUE);
            }
        }

        this.endpointContext.setActive(active);
    }

    /**
     * Sends the message through this endpoint. This method just handles statistics related
     * functions and gives the message to the Synapse environment to send. It does not add any
     * endpoint specific details to the message context. These details are added only to the cloned
     * message context by the Axis2FlexibleMepClient. So that we can reuse the original message
     * context for resending through different endpoints.
     *
     * @param synCtx MessageContext sent by client to Synapse
     */
    public void send(MessageContext synCtx) {

        boolean traceOn = isTraceOn(synCtx);
        boolean traceOrDebugOn = isTraceOrDebugOn(traceOn);

        if (traceOrDebugOn) {
            traceOrDebug(traceOn, "Start : Address Endpoint");

            if (traceOn && trace.isTraceEnabled()) {
                trace.trace("Message : " + synCtx.getEnvelope());
            }
        }

        boolean isClusteringEnable = false;
        org.apache.axis2.context.MessageContext axisMC =
                ((Axis2MessageContext) synCtx).getAxis2MessageContext();
        ConfigurationContext cc = axisMC.getConfigurationContext();


        ClusterManager clusterManager = cc.getAxisConfiguration().getClusterManager();
        if (clusterManager != null &&
                clusterManager.getContextManager() != null) {
            isClusteringEnable = true;
        }

        String endPointName = this.getName();
        if (endPointName == null) {

            if (traceOrDebugOn && isClusteringEnable) {
                log.warn(SALoadbalanceEndpoint.WARN_MESSAGE);
            }
            endPointName = SynapseConstants.ANONYMOUS_ENDPOINT;
        }

        if (isClusteringEnable) {

            if (endpointContext.getConfigurationContext() == null) {
                endpointContext.setConfigurationContext(cc);
            }
        }

        EndpointDefinition endpoint = getEndpoint();
        boolean statisticsEnable
                = (SynapseConstants.STATISTICS_ON == endpoint.getStatisticsState());
        if (statisticsEnable) {
            EndPointStatisticsStack endPointStatisticsStack = null;
            Object statisticsStackObj =
                    synCtx.getProperty(org.apache.synapse.SynapseConstants.ENDPOINT_STATS);
            if (statisticsStackObj == null) {
                endPointStatisticsStack = new EndPointStatisticsStack();
                synCtx.setProperty(org.apache.synapse.SynapseConstants.ENDPOINT_STATS,
                        endPointStatisticsStack);
            } else if (statisticsStackObj instanceof EndPointStatisticsStack) {
                endPointStatisticsStack = (EndPointStatisticsStack) statisticsStackObj;
            }
            if (endPointStatisticsStack != null) {
                boolean isFault = synCtx.getEnvelope().getBody().hasFault();
                endPointStatisticsStack.put(endPointName, System.currentTimeMillis(),
                        !synCtx.isResponse(), statisticsEnable, isFault);
            }
        }

        if (endpoint.getAddress() != null) {
            if (traceOrDebugOn) {
                traceOrDebug(traceOn, "Sending message to endpoint : " +
                        endPointName + " resolves to address = " + endpoint.getAddress());
                traceOrDebug(traceOn, "SOAPAction: " + (synCtx.getSoapAction() != null ?
                        synCtx.getSoapAction() : "null"));
                traceOrDebug(traceOn, "WSA-Action: " + (synCtx.getWSAAction() != null ?
                        synCtx.getWSAAction() : "null"));

                if (traceOn && trace.isTraceEnabled()) {
                    trace.trace("Envelope : \n" + synCtx.getEnvelope());
                }
            }
        }

        synCtx.pushFaultHandler(this);

        synCtx.setProperty(SynapseConstants.PROCESSED_ENDPOINT, this);

        synCtx.getEnvironment().send(endpoint, synCtx);
    }

    public void onFault(MessageContext synCtx) {

        setActive(false, synCtx);
        super.onFault(synCtx);
    }
}
