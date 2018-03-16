package org.apache.synapse.core.axis2;

import org.apache.axis2.client.async.Callback;
import org.apache.axis2.client.async.AsyncResult;
import org.apache.axis2.client.async.AxisCallback;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.MessageContext;

/**
 * This class only "holds" the Synapse out message context for the Synapse callback message
 * receiver when a response is received or error is encountered
 */
public class AsyncCallback implements AxisCallback {

    /** The corresponding Synapse outgoing message context this instance is holding onto */
    MessageContext synapseOutMsgCtx = null;

    /** Time to timeout this callback */
    private long timeOutOn;

    /** Action to perform when timeout occurs */
    private int timeOutAction = SynapseConstants.NONE;

    public AsyncCallback(MessageContext synapseOutMsgCtx) {
        this.synapseOutMsgCtx = synapseOutMsgCtx;
    }

    public void onMessage(org.apache.axis2.context.MessageContext messageContext) {}

    public void onFault(org.apache.axis2.context.MessageContext messageContext) {}

    public void onError(Exception e) {}

    public void onComplete() {}

    public org.apache.synapse.MessageContext getSynapseOutMsgCtx() {
        return synapseOutMsgCtx;
    }

    public long getTimeOutOn() {
        return timeOutOn;
    }

    public void setTimeOutOn(long timeOutOn) {
        this.timeOutOn = timeOutOn;
    }

    public int getTimeOutAction() {
        return timeOutAction;
    }

    public void setTimeOutAction(int timeOutAction) {
        this.timeOutAction = timeOutAction;
    }
}
