package org.apache.synapse.mediators;

import org.apache.synapse.*;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class will be used as the executer for the injectAsync method for the
 * sequence mediation
 */
public class MediatorWorker implements Runnable {

    private static final Log log = LogFactory.getLog(MediatorWorker.class);
    private static final Log trace = LogFactory.getLog(SynapseConstants.TRACE_LOGGER);

    /** Mediator to be executed */
    private Mediator seq = null;

    /** MessageContext to be mediated using the mediator */
    private MessageContext synCtx = null;

    /**
     * Constructor of the MediatorWorker which sets the sequence and the message context
     *
     * @param seq    - Sequence Mediator to be set
     * @param synCtx - Synapse MessageContext to be set
     */
    public MediatorWorker(Mediator seq, MessageContext synCtx) {
        this.seq = seq;
        this.synCtx = synCtx;
    }

    /**
     * Constructor od the MediatorWorker which sets the provided message context and the
     * main sequence as the sequence for mediation
     *
     * @param synCtx - Synapse MessageContext to be set
     */
    public MediatorWorker(MessageContext synCtx) {
        this.synCtx = synCtx;
        seq = synCtx.getMainSequence();
    }

    /**
     * Execution method of the thread. This will just call the mediation of the specified
     * Synapse MessageContext using the specified Sequence Mediator
     */
    public void run() {
        try {
            seq.mediate(synCtx);

        } catch (SynapseException syne) {
            if (!synCtx.getFaultStack().isEmpty()) {
                warn(false, "Executing fault handler due to exception encountered", synCtx);
                ((FaultHandler) synCtx.getFaultStack().pop()).handleFault(synCtx, syne);

            } else {
                warn(false, "Exception encountered but no fault handler found - " +
                    "message dropped", synCtx);
            }

        } catch (Exception e) {
            String msg = "Unexpected error executing task/async inject";
            log.error(msg, e);
            if (synCtx.getServiceLog() != null) {
                synCtx.getServiceLog().error(msg, e);
            }
            if (!synCtx.getFaultStack().isEmpty()) {
                warn(false, "Executing fault handler due to exception encountered", synCtx);
                ((FaultHandler) synCtx.getFaultStack().pop()).handleFault(synCtx, e);

            } else {
                warn(false, "Exception encountered but no fault handler found - " +
                    "message dropped", synCtx);
            }
        }
        synCtx = null;
        seq = null;
    }

    private void warn(boolean traceOn, String msg, MessageContext msgContext) {
        if (traceOn) {
            trace.warn(msg);
        }
        if (log.isDebugEnabled()) {
            log.warn(msg);
        }
        if (msgContext.getServiceLog() != null) {
            msgContext.getServiceLog().warn(msg);
        }
    }

}
