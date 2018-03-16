package org.apache.synapse.core.axis2;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.util.Utils;
import org.apache.synapse.transport.nhttp.NhttpConstants;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.engine.AxisEngine;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.SynapseException;
import org.apache.synapse.endpoints.utils.EndpointDefinition;
import org.apache.synapse.statistics.StatisticsUtils;
import org.apache.synapse.util.UUIDGenerator;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.rampart.handler.WSSHandlerConstants;

/**
 * This class helps the Axis2SynapseEnvironment implement the send method
 */
public class Axis2Sender {

    private static final Log log = LogFactory.getLog(Axis2Sender.class);

    /**
     * Send a message out from the Synapse engine to an external service
     * @param endpoint the endpoint definition where the message should be sent
     * @param synapseInMessageContext the Synapse message context
     */
    public static void sendOn(EndpointDefinition endpoint,
        org.apache.synapse.MessageContext synapseInMessageContext) {

        try {
            Axis2FlexibleMEPClient.send(
                endpoint,
                synapseInMessageContext);

        } catch (Exception e) {
            handleException("Unexpected error during sending message out", e);
        }
    }

    /**
     * Send a response back to a client of Synapse
     * @param smc the Synapse message context sent as the response
     */
    public static void sendBack(org.apache.synapse.MessageContext smc) {

        MessageContext messageContext = ((Axis2MessageContext) smc).getAxis2MessageContext();

        if (messageContext.isPropertyTrue(NhttpConstants.SC_ACCEPTED) &&
            messageContext.getTransportOut() != null &&
            !messageContext.getTransportOut().getName().startsWith(Constants.TRANSPORT_HTTP)) {
                return;
        }

        AxisEngine ae = new AxisEngine(messageContext.getConfigurationContext());

        try {
            messageContext.setProperty(SynapseConstants.ISRESPONSE_PROPERTY, Boolean.TRUE);
            if (smc.isResponse()) {
                StatisticsUtils.processEndPointStatistics(smc);
                StatisticsUtils.processProxyServiceStatistics(smc);
                StatisticsUtils.processAllSequenceStatistics(smc);
            }
            Axis2FlexibleMEPClient.removeAddressingHeaders(messageContext);
            messageContext.setMessageID(UUIDGenerator.getUUID());

            if (messageContext.isEngaged(WSSHandlerConstants.SECURITY_MODULE_NAME) &&
                messageContext.getEnvelope().getHeader() == null) {
                SOAPFactory fac = messageContext.isSOAP11() ?
                    OMAbstractFactory.getSOAP11Factory() : OMAbstractFactory.getSOAP12Factory();
                fac.createSOAPHeader(messageContext.getEnvelope());
            }
            ae.send(messageContext);

        } catch (AxisFault e) {
            handleException("Unexpected error sending message back", e);
        }
    }

    private static void handleException(String msg, Exception e) {
        log.error(msg, e);
        throw new SynapseException(msg, e);
    }
}
