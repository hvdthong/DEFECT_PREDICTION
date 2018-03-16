package org.apache.synapse.core.axis2;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.AddressingConstants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.context.ServiceGroupContext;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.AxisServiceGroup;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.sandesha2.client.SandeshaClientConstants;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.SynapseException;
import org.apache.synapse.endpoints.utils.EndpointDefinition;
import org.apache.synapse.util.MessageHelper;

import javax.xml.namespace.QName;
import java.util.ArrayList;

/**
 * This is a simple client that handles both in only and in out
 */
public class Axis2FlexibleMEPClient {

    private static final Log log = LogFactory.getLog(Axis2FlexibleMEPClient.class);

    /**
     * Based on the Axis2 client code. Sends the Axis2 Message context out and returns
     * the Axis2 message context for the response.
     *
     * Here Synapse works as a Client to the service. It would expect 200 ok, 202 ok and
     * 500 internal server error as possible responses.
     *
     * @param endpoint the endpoint being sent to, maybe null
     * @param synapseOutMessageContext the outgoing synapse message
     * @throws AxisFault on errors
     */
    public static void send(

        EndpointDefinition endpoint,
        org.apache.synapse.MessageContext synapseOutMessageContext) throws AxisFault {

        boolean separateListener    = false;
        boolean wsSecurityEnabled   = false;
        String wsSecPolicyKey       = null;
        boolean wsRMEnabled         = false;
        String wsRMPolicyKey        = null;
        boolean wsAddressingEnabled = false;

        if (endpoint != null) {
            separateListener    = endpoint.isUseSeparateListener();
            wsSecurityEnabled   = endpoint.isSecurityOn();
            wsSecPolicyKey      = endpoint.getWsSecPolicyKey();
            wsRMEnabled         = endpoint.isReliableMessagingOn();
            wsRMPolicyKey       = endpoint.getWsRMPolicyKey();
            wsAddressingEnabled = endpoint.isAddressingOn() || wsSecurityEnabled || wsRMEnabled;
        }

        if (log.isDebugEnabled()) {
            log.debug(
                "Sending [add = " + wsAddressingEnabled +
                "] [sec = " + wsSecurityEnabled +
                "] [rm = " + wsRMEnabled +
                (endpoint != null ?
                    "] [ mtom = " + endpoint.isUseMTOM() +
                    "] [ swa = " + endpoint.isUseSwa() +
                    "] [ format = " + endpoint.getFormat() +                    
                    "] [ force soap11=" + endpoint.isForceSOAP11() +
                    "] [ force soap12=" + endpoint.isForceSOAP12() +
                    "; pox=" + endpoint.isForcePOX() : "") +
                "] [ to " + synapseOutMessageContext.getTo() + "]");
        }

        MessageContext originalInMsgCtx
            = ((Axis2MessageContext) synapseOutMessageContext).getAxis2MessageContext();

        MessageContext axisOutMsgCtx = cloneForSend(originalInMsgCtx);

        if (endpoint != null) {

            if (SynapseConstants.FORMAT_POX.equals(endpoint.getFormat())) {
                axisOutMsgCtx.setDoingREST(true);
                
            } else if (SynapseConstants.FORMAT_SOAP11.equals(endpoint.getFormat())) {
                axisOutMsgCtx.setDoingREST(false);
                if (axisOutMsgCtx.getSoapAction() == null && axisOutMsgCtx.getWSAAction() != null) {
                    axisOutMsgCtx.setSoapAction(axisOutMsgCtx.getWSAAction());
                }
                if(!axisOutMsgCtx.isSOAP11()) {
                    SOAPUtils.convertSOAP12toSOAP11(axisOutMsgCtx);
                }
                
            } else if (SynapseConstants.FORMAT_SOAP12.equals(endpoint.getFormat())) {
                axisOutMsgCtx.setDoingREST(false);
                if (axisOutMsgCtx.getSoapAction() == null && axisOutMsgCtx.getWSAAction() != null) {
                    axisOutMsgCtx.setSoapAction(axisOutMsgCtx.getWSAAction());
                }
                if(axisOutMsgCtx.isSOAP11()) {
                    SOAPUtils.convertSOAP11toSOAP12(axisOutMsgCtx);
                }                
                
            }

            if (endpoint.isUseMTOM()) {
                axisOutMsgCtx.setDoingMTOM(true);
                axisOutMsgCtx.setProperty(
                        org.apache.axis2.Constants.Configuration.ENABLE_MTOM,
                        org.apache.axis2.Constants.VALUE_TRUE);
                axisOutMsgCtx.setDoingMTOM(true);

            } else if (endpoint.isUseSwa()) {
                axisOutMsgCtx.setDoingSwA(true);
                axisOutMsgCtx.setProperty(
                        org.apache.axis2.Constants.Configuration.ENABLE_SWA,
                        org.apache.axis2.Constants.VALUE_TRUE);
                axisOutMsgCtx.setDoingSwA(true);
            }

            if (endpoint.getAddress() != null) {
                axisOutMsgCtx.setTo(new EndpointReference(endpoint.getAddress()));
            }

            if (endpoint.isUseSeparateListener()) {
                axisOutMsgCtx.getOptions().setUseSeparateListener(true);
            }
        }

        if (wsAddressingEnabled) {
            axisOutMsgCtx.setProperty
                    (AddressingConstants.DISABLE_ADDRESSING_FOR_OUT_MESSAGES, Boolean.FALSE);

        } else {
            axisOutMsgCtx.setProperty
                    (AddressingConstants.DISABLE_ADDRESSING_FOR_OUT_MESSAGES, Boolean.TRUE);
        }

        ConfigurationContext axisCfgCtx = axisOutMsgCtx.getConfigurationContext();
        AxisConfiguration axisCfg       = axisCfgCtx.getAxisConfiguration();

        AxisService anoymousService =
            AnonymousServiceFactory.getAnonymousService(synapseOutMessageContext.getConfiguration(),
            axisCfg, wsAddressingEnabled, wsRMEnabled, wsSecurityEnabled);
        ServiceGroupContext sgc = new ServiceGroupContext(
            axisCfgCtx, (AxisServiceGroup) anoymousService.getParent());
        ServiceContext serviceCtx = sgc.getServiceContext(anoymousService);

        boolean outOnlyMessage = "true".equals(
            synapseOutMessageContext.getProperty(SynapseConstants.OUT_ONLY));

        AxisOperation axisAnonymousOperation = anoymousService.getOperation(
            outOnlyMessage ?
                new QName(AnonymousServiceFactory.OUT_ONLY_OPERATION) :
                new QName(AnonymousServiceFactory.OUT_IN_OPERATION));

        Options clientOptions = new Options();
        clientOptions.setUseSeparateListener(separateListener);
        if (wsRMEnabled) {
            if (wsRMPolicyKey != null) {
                clientOptions.setProperty(
                    SynapseConstants.SANDESHA_POLICY,
                    getPolicy(synapseOutMessageContext, wsRMPolicyKey));
            }
            copyRMOptions(originalInMsgCtx, clientOptions);
        }

        if (wsSecurityEnabled) {
            if (wsSecPolicyKey != null) {
                clientOptions.setProperty(
                    SynapseConstants.RAMPART_POLICY,
                    getPolicy(synapseOutMessageContext, wsSecPolicyKey));
            }
            if (axisOutMsgCtx.getEnvelope().getHeader() == null) {
                SOAPFactory fac = axisOutMsgCtx.isSOAP11() ?
                    OMAbstractFactory.getSOAP11Factory() : OMAbstractFactory.getSOAP12Factory();
                fac.createSOAPHeader(axisOutMsgCtx.getEnvelope());
            }
        }

        OperationClient mepClient = axisAnonymousOperation.createClient(serviceCtx, clientOptions);
        mepClient.addMessageContext(axisOutMsgCtx);
        axisOutMsgCtx.setAxisMessage(
            axisAnonymousOperation.getMessage(WSDLConstants.MESSAGE_LABEL_OUT_VALUE));

        if (!outOnlyMessage) {
            AsyncCallback callback = new AsyncCallback(synapseOutMessageContext);
            if (endpoint != null) {
                callback.setTimeOutOn(System.currentTimeMillis() + endpoint.getTimeoutDuration());
                callback.setTimeOutAction(endpoint.getTimeoutAction());
            } else {
                callback.setTimeOutOn(System.currentTimeMillis());
            }
            mepClient.setCallback(callback);
        }

        mepClient.execute(true);

        originalInMsgCtx.getOperationContext().setProperty(
            org.apache.axis2.Constants.RESPONSE_WRITTEN, "SKIP");
   }

    private static MessageContext cloneForSend(MessageContext ori) throws AxisFault {

        MessageContext newMC = MessageHelper.clonePartially(ori);

        newMC.setEnvelope(ori.getEnvelope());        
        removeAddressingHeaders(newMC);

        newMC.setProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS,
            ori.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS));

        return newMC;
    }

    private static void copyRMOptions(MessageContext oriContext, Options targetOptions) {
        Options oriOptions = oriContext.getOptions();
        if (oriOptions.getProperty(SynapseConstants.SANDESHA_LAST_MESSAGE) != null) {
            targetOptions.setProperty(SynapseConstants.SANDESHA_LAST_MESSAGE,
                    oriOptions.getProperty(SynapseConstants.SANDESHA_LAST_MESSAGE));
        }
        if (oriOptions.getProperty(SynapseConstants.SANDESHA_SPEC_VERSION) != null) {
            targetOptions.setProperty(SynapseConstants.SANDESHA_SPEC_VERSION,
                    oriOptions.getProperty(SynapseConstants.SANDESHA_SPEC_VERSION));
        }
        if (oriOptions.getProperty(SynapseConstants.SANDESHA_SEQUENCE_KEY) != null) {
            targetOptions.setProperty(SynapseConstants.SANDESHA_SEQUENCE_KEY,
                    oriOptions.getProperty(SynapseConstants.SANDESHA_SEQUENCE_KEY));
        }
        if (oriOptions.getProperty(SandeshaClientConstants.OFFERED_SEQUENCE_ID) != null) {
            targetOptions.setProperty(SandeshaClientConstants.OFFERED_SEQUENCE_ID,
                    oriOptions.getProperty(SandeshaClientConstants.OFFERED_SEQUENCE_ID));
        }
    }

    /**
     * Get the Policy object for the given name from the Synapse configuration at runtime
     * @param synCtx the current synapse configuration to get to the synapse configuration
     * @param propertyKey the name of the property which holds the Policy required
     * @return the Policy object with the given name, from the configuration
     */
    private static Policy getPolicy(org.apache.synapse.MessageContext synCtx, String propertyKey) {
        Object property = synCtx.getEntry(propertyKey);
        if (property != null && property instanceof OMElement) {
            return PolicyEngine.getPolicy((OMElement) property);
        } else {
            handleException("Cannot locate policy from the property : " + propertyKey);
        }
        return null;
    }

    private static void handleException(String msg) {
        log.error(msg);
        throw new SynapseException(msg);
    }

    /**
     * Removes Submission and Final WS-Addressing headers and return the SOAPEnvelope
     * from the given message context
     * @param axisMsgCtx the Axis2 Message context
     * @return the resulting SOAPEnvelope
     */
    public static SOAPEnvelope removeAddressingHeaders(MessageContext axisMsgCtx) {

        SOAPEnvelope env = axisMsgCtx.getEnvelope();
        SOAPHeader soapHeader = env.getHeader();
        ArrayList addressingHeaders;

        if (soapHeader != null) {
            addressingHeaders = soapHeader.getHeaderBlocksWithNSURI(
                AddressingConstants.Submission.WSA_NAMESPACE);

            if (addressingHeaders != null && addressingHeaders.size() != 0) {
                detachAddressingInformation(addressingHeaders);

            } else {
                addressingHeaders = soapHeader.getHeaderBlocksWithNSURI(
                    AddressingConstants.Final.WSA_NAMESPACE);
                if (addressingHeaders != null && addressingHeaders.size() != 0) {
                    detachAddressingInformation(addressingHeaders);
                }
            }
        }
        return env;
    }

    /**
     * Remove WS-A headers
     * @param headerInformation headers to be removed
     */
    private static void detachAddressingInformation(ArrayList headerInformation) {
        for (Object o : headerInformation) {
            if (o instanceof SOAPHeaderBlock) {
                SOAPHeaderBlock headerBlock = (SOAPHeaderBlock) o;
                headerBlock.detach();
            } else if (o instanceof OMElement) {
                OMElement om = (OMElement) o;
                OMNamespace ns = om.getNamespace();
                if (ns != null && (
                    AddressingConstants.Submission.WSA_NAMESPACE.equals(ns.getNamespaceURI()) ||
                        AddressingConstants.Final.WSA_NAMESPACE.equals(ns.getNamespaceURI()))) {
                    om.detach();
                }
            }
        }
    }
}
