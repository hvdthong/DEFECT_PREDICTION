package org.apache.synapse.core.axis2;

import org.apache.axis2.AxisFault;
import org.apache.axis2.util.Utils;
import org.apache.axis2.transport.nhttp.NhttpConstants;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.engine.AxisEngine;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.Constants;
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

    public static void sendOn(
            EndpointDefinition endpoint,
            org.apache.synapse.MessageContext synapseInMessageContext) {

        try {
                Axis2FlexibleMEPClient.send(
                    endpoint,

                    synapseInMessageContext);

        } catch (Exception e) {
            handleException("Unexpected error during Sending message onwards", e);
        }
    }

    public static void sendBack(org.apache.synapse.MessageContext smc) {

        MessageContext messageContext = ((Axis2MessageContext) smc).getAxis2MessageContext();

        if (Utils.isExplicitlyTrue(messageContext, NhttpConstants.SC_ACCEPTED) &&
            messageContext.getTransportOut() != null &&
            !messageContext.getTransportOut().getName().startsWith("http")) {
                return;
        }

        AxisEngine ae = new AxisEngine(messageContext.getConfigurationContext());

        try {
            messageContext.setProperty(Constants.ISRESPONSE_PROPERTY, Boolean.TRUE);
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
            handleException("Unexpected error during Sending message back", e);
        }
    }

    private static void handleException(String msg, Exception e) {
        log.error(msg, e);
        throw new SynapseException(msg, e);
    }
}
