package org.apache.synapse.mediators.filters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.Constants;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractListMediator;

/**
 * The In Mediator acts only on "incoming" messages into synapse. This is
 * performed by looking at the result of MessageContext#isResponse()
 *
 * @see org.apache.synapse.MessageContext#isResponse()
 */
public class InMediator extends AbstractListMediator implements org.apache.synapse.mediators.FilterMediator {

    private static final Log log = LogFactory.getLog(InMediator.class);
    private static final Log trace = LogFactory.getLog(Constants.TRACE_LOGGER);

    /**
     * Executes the list of sub/child mediators, if the filter condition is satisfied
     *
     * @param synCtx the current message
     * @return true if filter condition fails. else returns as per List mediator semantics
     */
    public boolean mediate(MessageContext synCtx) {
        log.debug("In mediator mediate()");
        boolean shouldTrace = shouldTrace(synCtx.getTracingState());
        try {
            if (shouldTrace) {
                trace.trace("Start : In mediator");
            }
            if (test(synCtx)) {
                log.debug("Current message is incoming.. executing child mediators");
                return super.mediate(synCtx);
            } else {
                log.debug("Current message is not incoming.. skipping child mediators");
                return true;
            }
        } finally {
            if (shouldTrace) {
                trace.trace("End : In mediator");
            }
        }
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
