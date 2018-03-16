package org.apache.synapse.core.axis2;

import org.apache.axis2.engine.MessageReceiver;
import org.apache.axis2.client.async.Callback;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.AxisFault;
import org.apache.axis2.util.Utils;
import org.apache.axis2.transport.nhttp.NhttpConstants;
import org.apache.axis2.addressing.RelatesTo;
import org.apache.axis2.addressing.AddressingConstants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.Constants;
import org.apache.synapse.FaultHandler;
import org.apache.synapse.SynapseException;
import org.apache.synapse.endpoints.Endpoint;
import org.apache.axiom.soap.SOAPFault;
import org.apache.sandesha2.client.SandeshaClientConstants;

import java.util.*;

public class SynapseCallbackReceiver implements MessageReceiver {

    private static final Log log = LogFactory.getLog(SynapseCallbackReceiver.class);


    /**
     * Timer to schedule the timeout task.
     */
    private Timer timeOutTimer = null;

    public SynapseCallbackReceiver() {
        callbackStore = Collections.synchronizedMap(new HashMap());

        TimeoutHandler timeoutHandler = new TimeoutHandler(callbackStore);
        timeOutTimer = new Timer(true);
        timeOutTimer.schedule(timeoutHandler, 0, Constants.TIMEOUT_HANDLER_INTERVAL);
    }

    public void addCallback(String MsgID, Callback callback) {
        callbackStore.put(MsgID, callback);
    }

    public void receive(MessageContext messageCtx) throws AxisFault {

        String messageID = null;

        if (messageCtx.getOptions() != null && messageCtx.getOptions().getRelatesTo() != null) {
            messageID = messageCtx.getOptions().getRelatesTo().getValue();
        } else if (messageCtx.getProperty(SandeshaClientConstants.SEQUENCE_KEY) == null) {
            messageID = (String) messageCtx.getProperty(Constants.RELATES_TO_FOR_POX);
        }

        if (messageID != null) {
            Callback callback = (Callback) callbackStore.remove(messageID);

            RelatesTo[] relates = messageCtx.getRelationships();
            if (relates != null && relates.length > 1) {
                removeDuplicateRelatesTo(messageCtx, relates);
            }
            
            if (callback != null) {
                handleMessage(messageCtx, ((AsyncCallback) callback).getSynapseOutMsgCtx());
            } else {
                log.warn("Synapse received a response for the request with message Id : " +
                        messageID + " But a callback has not been registered to process this response");
            }

        } else if (!Utils.isExplicitlyTrue(messageCtx, NhttpConstants.SC_ACCEPTED)){
            log.warn("Synapse received a response message without a message Id");
        }
    }

    /**
     * Handle the response or error (during a failed send) message received for an outgoing request
     *
     * @param response         the Axis2 MessageContext that has been received and has to be handled
     * @param synapseOutMsgCtx the corresponding (outgoing) Synapse MessageContext for the above
     *                         Axis2 MC, that holds Synapse specific information such as the error
     *                         handler stack and local properties etc.
     */
    private void handleMessage(MessageContext response,
                               org.apache.synapse.MessageContext synapseOutMsgCtx) {

        Object o = response.getProperty(NhttpConstants.SENDING_FAULT);
        if (o != null && Boolean.TRUE.equals(o)) {


            Stack faultStack = synapseOutMsgCtx.getFaultStack();
            if (faultStack != null && !faultStack.isEmpty()) {
                SOAPFault fault = response.getEnvelope().getBody().getFault();
                Exception e = fault.getException();
                if (e == null) {
                    e = new Exception(fault.toString());
                }
                synapseOutMsgCtx.setProperty(Constants.ERROR_CODE, Constants.SENDING_FAULT);
                if (fault != null && fault.getReason() != null) {
                    synapseOutMsgCtx.setProperty(Constants.ERROR_MESSAGE, fault.getReason().getText());
                }

                ((FaultHandler) faultStack.pop()).handleFault(synapseOutMsgCtx, e);
            }

        } else {

            Stack faultStack = synapseOutMsgCtx.getFaultStack();
            if (!faultStack.isEmpty() && faultStack.peek() instanceof Endpoint) {
                faultStack.pop();
            }
            if (log.isDebugEnabled()) {
                log.debug("Synapse received an asynchronous response message");
                log.debug("Received To: " +
                        (response.getTo() != null ? response.getTo().getAddress() : "null"));
                log.debug("SOAPAction: " +
                        (response.getSoapAction() != null ? response.getSoapAction() : "null"));
                log.debug("WSA-Action: " +
                        (response.getWSAAction() != null ? response.getWSAAction() : "null"));
                String[] cids = response.getAttachmentMap().getAllContentIDs();
                if (cids != null && cids.length > 0) {
                    for (int i = 0; i < cids.length; i++) {
                        log.debug("Attachment : " + cids[i]);
                    }
                }
                log.debug("Body : \n" + response.getEnvelope());
            }
            MessageContext axisOutMsgCtx =
                    ((Axis2MessageContext) synapseOutMsgCtx).getAxis2MessageContext();

            response.setOperationContext(axisOutMsgCtx.getOperationContext());
            response.getAxisMessage().setParent(
                axisOutMsgCtx.getOperationContext().getAxisOperation());
            response.setAxisService(axisOutMsgCtx.getAxisService());

            response.setServerSide(true);
            response.setProperty(Constants.ISRESPONSE_PROPERTY, Boolean.TRUE);
            response.setProperty(MessageContext.TRANSPORT_OUT,
                    axisOutMsgCtx.getProperty(MessageContext.TRANSPORT_OUT));
            response.setProperty(org.apache.axis2.Constants.OUT_TRANSPORT_INFO,
                    axisOutMsgCtx.getProperty(org.apache.axis2.Constants.OUT_TRANSPORT_INFO));
            response.setTransportIn(axisOutMsgCtx.getTransportIn());
            response.setTransportOut(axisOutMsgCtx.getTransportOut());

            response.setDoingREST(axisOutMsgCtx.isDoingREST());
            if (axisOutMsgCtx.isDoingMTOM()) {
                response.setDoingMTOM(true);
                response.setProperty(
                        org.apache.axis2.Constants.Configuration.ENABLE_MTOM,
                        org.apache.axis2.Constants.VALUE_TRUE);
            }
            if (axisOutMsgCtx.isDoingSwA()) {
                response.setDoingSwA(true);
                response.setProperty(
                        org.apache.axis2.Constants.Configuration.ENABLE_SWA,
                        org.apache.axis2.Constants.VALUE_TRUE);
            }

            if (axisOutMsgCtx.getMessageID() != null) {
                response.setRelationships(
                        new RelatesTo[]{new RelatesTo(axisOutMsgCtx.getMessageID())});
            }

            Axis2MessageContext synapseInMessageContext =
                    new Axis2MessageContext(
                            response,
                            synapseOutMsgCtx.getConfiguration(),
                            synapseOutMsgCtx.getEnvironment());

            synapseInMessageContext.setResponse(true);
            synapseInMessageContext.setTo(
                new EndpointReference(AddressingConstants.Final.WSA_ANONYMOUS_URL));

            Iterator iter = synapseOutMsgCtx.getPropertyKeySet().iterator();

            while (iter.hasNext()) {
                Object key = iter.next();
                synapseInMessageContext.setProperty(
                        (String) key, synapseOutMsgCtx.getProperty((String) key));
            }

            try {
                synapseOutMsgCtx.getEnvironment().injectMessage(synapseInMessageContext);
            } catch (SynapseException syne) {
                if (!synapseInMessageContext.getFaultStack().isEmpty()) {
                    ((FaultHandler) synapseInMessageContext
                            .getFaultStack().pop()).handleFault(synapseInMessageContext, syne);
                } else {
                    log.error("Synapse encountered an exception, " +
                            "No error handlers found - [Message Dropped]\n" + syne.getMessage());
                }
            }
        }
    }

    private void removeDuplicateRelatesTo(MessageContext mc, RelatesTo[] relates) {

        int insertPos = 0;
        RelatesTo[] newRelates = new RelatesTo[relates.length];

        for (int i = 0; i < relates.length; i++) {
            RelatesTo current = relates[i];
            boolean found = false;
            for (int j = 0; j < newRelates.length && j < insertPos; j++) {
                if (newRelates[j].equals(current) ||
                        newRelates[j].getValue().equals(current.getValue())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                newRelates[insertPos++] = current;
            }
        }

        RelatesTo[] trimmedRelates = new RelatesTo[insertPos];
        System.arraycopy(newRelates, 0, trimmedRelates, 0, insertPos);
        mc.setRelationships(trimmedRelates);
    }
}
