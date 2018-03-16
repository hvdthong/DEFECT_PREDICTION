package org.apache.synapse.mediators.eip.splitter;

import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.mediators.eip.EIPUtils;
import org.apache.synapse.mediators.eip.Target;
import org.apache.synapse.mediators.eip.EIPConstants;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.util.MessageHelper;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.context.OperationContext;
import org.jaxen.JaxenException;

import java.util.List;
import java.util.Iterator;

/**
 * This mediator will split the message in the criterian specified to it and inject in to Synapse
 */
public class IterateMediator extends AbstractMediator {

    /**
     * This holds whether to continue mediation on the parent message or not
     */
    private boolean continueParent = false;

    /**
     * This holds whether to preserve the payload and attach the iteration child to specified node
     * or to attach the child to the body of the envelope
     */
    private boolean preservePayload = false;

    /**
     * This holds the expression which will be evaluated for the presence of elements in the
     * mediating message for iterations
     */
    private AXIOMXPath expression = null;

    /**
     * This holds the node to which the iteration childs will be attached. This does not have any
     * meaning when the preservePayload is set to false
     */
    private AXIOMXPath attachPath = null;

    /**
     * This holds the target object for the newly created messages by the iteration
     */
    private Target target = null;

    /**
     * This method implemenents the Mediator interface and this mediator implements the message
     * splitting logic
     *
     * @param synCtx - MessageContext to be mediated
     * @return boolean false if need to stop processing the parent message, boolean true if further
     *         processing of the parent message is required
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
            if (splitElements != null) {

                int msgCount = splitElements.size();
                int msgNumber = 0;

                if (!preservePayload && envelope.getBody() != null) {
                    for (Iterator itr = envelope.getBody().getChildren(); itr.hasNext();) {
                        ((OMNode) itr.next()).detach();
                    }
                }

                for (Object o : splitElements) {

                    MessageContext newCtx = MessageHelper.cloneMessageContext(synCtx);
                    newCtx.setProperty(EIPConstants.MESSAGE_SEQUENCE,
                        msgNumber + EIPConstants.MESSAGE_SEQUENCE_DELEMITER + msgCount);
                    SOAPEnvelope newEnvelope = MessageHelper.cloneSOAPEnvelope(envelope);

                    if (!(o instanceof OMNode)) {
                        handleException(
                            "Error in splitting the message with expression : " + expression,
                            synCtx);
                    }

                    if (preservePayload) {

                        Object attachElem = attachPath.evaluate(newEnvelope);
                        if (attachElem instanceof List) {
                            attachElem = ((List) attachElem).get(0);
                        }

                        if (attachElem instanceof OMElement) {
                            ((OMElement) attachElem).addChild((OMNode) o);
                        } else {
                            handleException("Error in attaching the splitted elements :: " +
                                "Unable to get the attach path specified by the expression " +
                                attachPath, synCtx);
                        }
                    } else if (o instanceof OMNode && newEnvelope.getBody() != null) {
                        newEnvelope.getBody().addChild((OMNode) o);
                    }

                    newCtx.setEnvelope(newEnvelope);
                    target.mediate(newCtx);
                    msgNumber++;

                }

            } else {
                handleException(
                    "Splitting by expression : " + expression + " did not yeild in an OMElement",
                    synCtx);
            }

        } catch (JaxenException e) {
            handleException("Error evaluating XPath expression : " + expression, e, synCtx);
        } catch (AxisFault axisFault) {
            handleException("Unable to split the message using the expression : " + expression,
                axisFault, synCtx);
        }

        if (traceOrDebugOn) {
            traceOrDebug(traceOn, "End : Iterate mediator");
        }

        OperationContext opCtx
            = ((Axis2MessageContext) synCtx).getAxis2MessageContext().getOperationContext();
        if (!continueParent && opCtx != null) {
            opCtx.setProperty(Constants.RESPONSE_WRITTEN,"SKIP");
        }

        return continueParent;
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

    public AXIOMXPath getExpression() {
        return expression;
    }

    public void setExpression(AXIOMXPath expression) {
        this.expression = expression;
    }

    public AXIOMXPath getAttachPath() {
        return attachPath;
    }

    public void setAttachPath(AXIOMXPath attachPath) {
        this.attachPath = attachPath;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

}
