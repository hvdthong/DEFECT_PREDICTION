package org.apache.synapse.mediators;

import org.apache.synapse.*;
import org.apache.synapse.mediators.base.SequenceMediator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This implements the FaultHandler interface as a mediator fault handler. That is the fault handler is
 * specified by a sequence and this handler implements the logic of handling the fault through the set
 * of mediators present in the sequence.
 *
 * @see org.apache.synapse.FaultHandler
 */
public class MediatorFaultHandler extends FaultHandler {

    private static final Log log = LogFactory.getLog(MediatorFaultHandler.class);
    private static final Log trace = LogFactory.getLog(SynapseConstants.TRACE_LOGGER);

    /**
     * This holds the fault sequence for the mediator fault handler
     */
    private Mediator faultMediator = null;

    /**
     * Constructs the FaultHandler object for handling mediator faults
     *
     * @param faultMediator Mediator in which fault sequence is specified
     */
    public MediatorFaultHandler(Mediator faultMediator) {

        this.faultMediator = faultMediator;
    }

    /**
     * Implements the fault handling method for the mediators (basically sequences)
     *
     * @param synCtx Synapse Message Context of which mediation occurs
     * @throws SynapseException in case there is a failure in the fault execution
     * @see org.apache.synapse.FaultHandler#handleFault(org.apache.synapse.MessageContext)
     */
    public void onFault(MessageContext synCtx) throws SynapseException {

        boolean traceOn = synCtx.getTracingState() == SynapseConstants.TRACING_ON;
        boolean traceOrDebugOn = traceOn || log.isDebugEnabled();

        String name = null;
        if (faultMediator instanceof SequenceMediator) {
            name = ((SequenceMediator) faultMediator).getName();
        }
        if (name == null) {
            name = faultMediator.getClass().getName();
        }

        if (traceOrDebugOn) {
            traceOrDebugWarn(traceOn, "Executing fault handler mediator : " + name);
        }

        synCtx.getServiceLog().warn("Executing fault sequence mediator : " + name);
        this.faultMediator.mediate(synCtx);
    }

    /**
     * Getter for the mediator describing the fault sequence
     *
     * @return Mediator specifying the fault sequence for mediator fault handler
     */
    public Mediator getFaultMediator() {
        return faultMediator;
    }

    /**
     * Setter of the mediator describing the fault sequence
     *
     * @param faultMediator Mediator specifying the fault sequence to be used by the handler
     */
    public void setFaultMediator(Mediator faultMediator) {
        this.faultMediator = faultMediator;
    }

    private void traceOrDebugWarn(boolean traceOn, String msg) {
        if (traceOn) {
            trace.warn(msg);
        }
        log.warn(msg);
    }

}
