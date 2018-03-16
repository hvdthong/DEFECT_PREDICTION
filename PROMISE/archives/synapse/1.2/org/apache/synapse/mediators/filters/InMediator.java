package org.apache.synapse.mediators.filters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractListMediator;

/**
 * The In Mediator acts only on "incoming" messages into synapse. This is
 * performed by looking at the result of MessageContext#isResponse()
 *
 * @see org.apache.synapse.MessageContext#isResponse()
 */
public class InMediator extends AbstractListMediator implements org.apache.synapse.mediators.FilterMediator {

    /**
     * Executes the list of sub/child mediators, if the filter condition is satisfied
     *
     * @param synCtx the current message
     * @return true if filter condition fails. else returns as per List mediator semantics
     */
    public boolean mediate(MessageContext synCtx) {

        boolean traceOn = isTraceOn(synCtx);
        boolean traceOrDebugOn = isTraceOrDebugOn(traceOn);

        if (traceOrDebugOn) {
            traceOrDebug(traceOn, "Start : In mediator");

            if (traceOn && trace.isTraceEnabled()) {
                trace.trace("Message : " + synCtx.getEnvelope());
            }
        }

        boolean result = true;
        if (test(synCtx)) {
            if (traceOrDebugOn) {
                traceOrDebug(traceOn, "Current message is incoming - executing child mediators");
            }
            result = super.mediate(synCtx);

        } else {
            if (traceOrDebugOn) {
                traceOrDebug(traceOn, "Current message is a response - skipping child mediators");
            }
        }

        if (traceOrDebugOn) {
            traceOrDebug(traceOn, "End : In mediator");
        }

        return result;
    }

    /**
     * Apply mediation only on request messages
     *
     * @param synCtx the message context
     * @return MessageContext#isResponse()
     */
    public boolean test(MessageContext synCtx) {
        return !synCtx.isResponse();
    }
}
