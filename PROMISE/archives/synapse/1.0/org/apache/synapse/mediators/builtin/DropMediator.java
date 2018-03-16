package org.apache.synapse.mediators.builtin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.Constants;
import org.apache.synapse.MessageContext;
import org.apache.synapse.statistics.StatisticsUtils;
import org.apache.synapse.mediators.AbstractMediator;

/**
 * Halts further processing/mediation of the current message. i.e. returns false
 */
public class DropMediator extends AbstractMediator {

    private static final Log log = LogFactory.getLog(DropMediator.class);
    private static final Log trace = LogFactory.getLog(Constants.TRACE_LOGGER);

    /**
     * Halts further mediation of the current message by returning false.
     *
     * @param synCtx the current message
     * @return false always
     */
    public boolean mediate(MessageContext synCtx) {
        log.debug("Drop mediator :: mediate()");
        StatisticsUtils.processProxyServiceStatistics(synCtx);
        StatisticsUtils.processAllSequenceStatistics(synCtx);
        boolean shouldTrace = shouldTrace(synCtx.getTracingState());
        if (shouldTrace) {
            trace.trace("Start : Drop mediator");
        }
        if (synCtx.getTo() == null) {
            if (shouldTrace) {
                trace.trace("End : Drop mediator");
            }
            return false;
        } else {
            synCtx.setTo(null);
            if (shouldTrace) {
                trace.trace("End : Drop mediator");
            }
            return false;
        }
    }
}
