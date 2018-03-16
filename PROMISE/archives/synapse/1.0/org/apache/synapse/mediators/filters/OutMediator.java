package org.apache.synapse.mediators.filters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.Constants;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractListMediator;

/**
 * The Out Mediator acts only on "outgoing" messages from synapse. This is
 * performed by looking at the result of MessageContext#isResponse()
 *
 * @see org.apache.synapse.MessageContext#isResponse()
 */
public class OutMediator extends AbstractListMediator implements org.apache.synapse.mediators.FilterMediator {

    private static final Log log = LogFactory.getLog(OutMediator.class);
    private static final Log trace = LogFactory.getLog(Constants.TRACE_LOGGER);

    /**
     * Executes the list of sub/child mediators, if the filter condition is satisfied
     *
     * @param synCtx the current message
     * @return true if filter condition fails. else returns as per List mediator semantics
     */
    public boolean mediate(MessageContext synCtx) {
        log.debug("Out mediator mediate()");
        boolean shouldTrace = shouldTrace(synCtx.getTracingState());
        try {
            if (shouldTrace) {
                trace.trace("Start : Out mediator");
            }
            
            if (test(synCtx)) {
                log.debug("Current message is outgoing.. executing child mediators");
                return super.mediate(synCtx);
            } else {
                log.debug("Current message is not outgoing.. skipping child mediators");
                return true;
            }
        } finally {
            if (shouldTrace) {
                trace.trace("End : Out mediator");
            }
        }
    }

    /**
     * Apply mediation only on response messages
     *
     * @param synCtx the message context
     * @return MessageContext#isResponse()
     */
    public boolean test(MessageContext synCtx) {
        return synCtx.isResponse();
    }
}
