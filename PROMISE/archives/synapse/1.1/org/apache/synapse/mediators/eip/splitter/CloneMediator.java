package org.apache.synapse.mediators.eip.splitter;

import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.util.MessageHelper;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.mediators.eip.Target;
import org.apache.synapse.mediators.eip.EIPConstants;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.context.OperationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * This mediator will clone the message in to different messages and mediated as specified in the
 * target elements.
 */
public class CloneMediator extends AbstractMediator {

    /**
     * This variable specifies whether to continue the parent message (i.e. message which is
     * sbjuected to cloning) or not
     */
    private boolean continueParent = false;

    /**
     * Holds the list of targets to which cloned copies of the message will be given for mediation
     */
    private List targets = new ArrayList();

    /**
     * This will implement the mediate method of the Mediator interface and will provide the
     * functionality of cloning message in to the specified targets and mediation
     *
     * @param synCtx - MessageContext which is subjected to the cloning
     * @return boolean true if this needs to be further mediated (continueParent=true) false
     *         otherwise
     */
    public boolean mediate(MessageContext synCtx) {

        boolean traceOn = isTraceOn(synCtx);
        boolean traceOrDebugOn = isTraceOrDebugOn(traceOn);

        if (traceOrDebugOn) {
            traceOrDebug(traceOn, "Start : Clone mediator");

            if (traceOn && trace.isTraceEnabled()) {
                trace.trace("Message : " + synCtx.getEnvelope());
            }
        }

        if (targets.size() != 0) {

            for (int i = 0; i < targets.size(); i++) {
                MessageContext newContext = getClonedMessageContext(synCtx, i, targets.size());
                Object o = targets.get(i);

                if (o instanceof Target) {
                    Target target = (Target) o;
                    target.mediate(newContext);
                }
            }
        }

        if (traceOrDebugOn) {
            traceOrDebug(traceOn, "End : Clone mediator");
        }

        OperationContext opCtx
            = ((Axis2MessageContext) synCtx).getAxis2MessageContext().getOperationContext();
        if (!continueParent && opCtx != null) {
            opCtx.setProperty(Constants.RESPONSE_WRITTEN,"SKIP");
        }

        return continueParent;
    }

    /**
     * This private method is used to clone the MC in to a new MC
     *
     * @param synCtx          - MessageContext which is subjected to the clonning
     * @param messageSequence - int clonning message number
     * @param messageCount    - int complete count of cloned messages
     * @return MessageContext which is cloned from the given parameters
     */
    private MessageContext getClonedMessageContext(MessageContext synCtx, int messageSequence,
        int messageCount) {

        MessageContext newCtx = null;
        try {
            newCtx = MessageHelper.cloneMessageContext(synCtx);
        } catch (AxisFault axisFault) {
            handleException("Error creating a new message context", axisFault, synCtx);
        }

        newCtx.setProperty(EIPConstants.MESSAGE_SEQUENCE,
            String.valueOf(messageSequence) + EIPConstants.MESSAGE_SEQUENCE_DELEMITER + messageCount);

        return newCtx;
    }


    public boolean isContinueParent() {
        return continueParent;
    }

    public void setContinueParent(boolean continueParent) {
        this.continueParent = continueParent;
    }

    public List getTargets() {
        return targets;
    }

    public void setTargets(List targets) {
        this.targets = targets;
    }

    public void addTarget(Target target) {
        this.targets.add(target);
    }

}
