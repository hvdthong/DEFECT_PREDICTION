package org.apache.synapse.mediators.eip.splitter;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.context.OperationContext;
import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.mediators.eip.EIPConstants;
import org.apache.synapse.mediators.eip.EIPUtils;
import org.apache.synapse.mediators.eip.Target;
import org.apache.synapse.util.MessageHelper;
import org.apache.synapse.util.xpath.SynapseXPath;
import org.jaxen.JaxenException;

import java.util.Iterator;
import java.util.List;

/**
 * Splits a message using an XPath expression and creates a new message to hold
 * each resulting element. This is very much similar to the clone mediator, and
 * hands over the newly created messages to a target for processing
 */
public class IterateMediator extends AbstractMediator implements ManagedLifecycle {

    /** Continue mediation on the parent message or not? */
    private boolean continueParent = false;

    /**
     * Preserve the payload as a template to create new messages with the selected
     * elements with the rest of the parent, or create new message that contain only
     * the selected element as its payload?
     */
    private boolean preservePayload = false;

    /** The XPath that will list the elements to be splitted */
    private SynapseXPath expression = null;

    /**
     * An XPath expression that specifies where the splitted elements should be attached when
     * the payload is being preserved
     */
    private SynapseXPath attachPath = null;

    /** The target for the newly splitted messages */
    private Target target = null;

    /**
     * Splits the message by iterating over the results of the given XPath expression
     *
     * @param synCtx - MessageContext to be mediated
     * @return boolean false if need to stop processing of the parent message
     */
    public boolean mediate(MessageContext synCtx) {

        boolean traceOn = isTraceOn(synCtx);
        boolean traceOrDebugOn = isTraceOrDebugOn(traceOn);

        if (traceOrDebugOn) {
            traceOrDebug(traceOn, "Start : Iterate mediator");

            if (traceOn && trace.isTraceEnabled()) {
                trace.trace("Message : " + synCtx.getEnvelope());
            }
        }

        try {
            SOAPEnvelope envelope = MessageHelper.cloneSOAPEnvelope(synCtx.getEnvelope());

            List splitElements = EIPUtils.getDetachedMatchingElements(envelope, expression);

            if (traceOrDebugOn) {
                traceOrDebug(traceOn, "Splitting with XPath : " + expression + " resulted in " +
                    splitElements.size() + " elements");
            }

            if (!preservePayload && envelope.getBody() != null) {
                for (Iterator itr = envelope.getBody().getChildren(); itr.hasNext();) {
                    ((OMNode) itr.next()).detach();
                }
            }

            int msgCount = splitElements.size();
            int msgNumber = 0;

            for (Object o : splitElements) {

                if (!(o instanceof OMNode)) {
                    handleException("Error splitting message with XPath : "
                        + expression + " - result not an OMNode", synCtx);
                }

                if (traceOrDebugOn) {
                    traceOrDebug(traceOn, "Submitting " + (msgNumber+1) + " of " + msgNumber +
                        " messages for processing in parallel");
                }

                target.mediate(
                    getIteratedMessage(synCtx, msgNumber++, msgCount, envelope, (OMNode) o));
            }

        } catch (JaxenException e) {
            handleException("Error evaluating split XPath expression : " + expression, e, synCtx);
        } catch (AxisFault af) {
            handleException("Error creating an iterated copy of the message", af, synCtx);
        }

        OperationContext opCtx
            = ((Axis2MessageContext) synCtx).getAxis2MessageContext().getOperationContext();
        if (!continueParent && opCtx != null) {
            opCtx.setProperty(Constants.RESPONSE_WRITTEN,"SKIP");
        }

        if (traceOrDebugOn) {
            traceOrDebug(traceOn, "End : Iterate mediator");
        }

        return continueParent;
    }

    /**
     * Create a new message context using the given original message context, the envelope
     * and the split result element.
     *
     * @param synCtx    - original message context
     * @param msgNumber - message number in the iteration
     * @param msgCount  - total number of messages in the split
     * @param envelope  - envelope to be used in the iteration
     * @param o         - element which participates in the iteration replacement
     * @return newCtx created by the iteration
     * @throws AxisFault if there is a message creation failure
     * @throws JaxenException if the expression evauation failure
     */
    private MessageContext getIteratedMessage(MessageContext synCtx, int msgNumber, int msgCount,
        SOAPEnvelope envelope, OMNode o) throws AxisFault, JaxenException {
        
        MessageContext newCtx = MessageHelper.cloneMessageContext(synCtx);

        newCtx.setProperty(
            EIPConstants.MESSAGE_SEQUENCE,
            msgNumber + EIPConstants.MESSAGE_SEQUENCE_DELEMITER + msgCount);

        SOAPEnvelope newEnvelope = MessageHelper.cloneSOAPEnvelope(envelope);

        if (preservePayload) {

            Object attachElem = attachPath.evaluate(newEnvelope);
            if (attachElem != null &&
                attachElem instanceof List && !((List) attachElem).isEmpty()) {
                attachElem = ((List) attachElem).get(0);
            }

            if (attachElem != null && attachElem instanceof OMElement) {
                ((OMElement) attachElem).addChild(o);
            } else {
                handleException("Error in attaching the splitted elements :: " +
                    "Unable to get the attach path specified by the expression " +
                    attachPath, synCtx);
            }

        } else if (newEnvelope.getBody() != null) {
            newEnvelope.getBody().addChild(o);
        }

        newCtx.setEnvelope(newEnvelope);

        return newCtx;
    }


    public boolean isContinueParent() {
        return continueParent;
    }

    public void setContinueParent(boolean continueParent) {
        this.continueParent = continueParent;
    }

    public boolean isPreservePayload() {
        return preservePayload;
    }

    public void setPreservePayload(boolean preservePayload) {
        this.preservePayload = preservePayload;
    }

    public SynapseXPath getExpression() {
        return expression;
    }

    public void setExpression(SynapseXPath expression) {
        this.expression = expression;
    }

    public SynapseXPath getAttachPath() {
        return attachPath;
    }

    public void setAttachPath(SynapseXPath attachPath) {
        this.attachPath = attachPath;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public void init(SynapseEnvironment se) {
        if (target.getSequence() != null) {
            target.getSequence().init(se);
        }
    }

    public void destroy() {
        if (target.getSequence() != null) {
            target.getSequence().destroy();
        }
    }
}
