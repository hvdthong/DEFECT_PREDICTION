package org.apache.synapse.mediators.builtin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.MessageContext;
import org.apache.synapse.statistics.StatisticsUtils;
import org.apache.synapse.mediators.AbstractMediator;

/**
 * Halts further processing/mediation of the current message. i.e. returns false
 */
public class DropMediator extends AbstractMediator {

    /**
     * Halts further mediation of the current message by returning false.
     *
     * @param synCtx the current message
     * @return false always
     */
    public boolean mediate(MessageContext synCtx) {

        boolean traceOn = isTraceOn(synCtx);
        boolean traceOrDebugOn = isTraceOrDebugOn(traceOn);

        if (traceOrDebugOn) {
            traceOrDebug(traceOn, "Start : Drop mediator");

            if (traceOn && trace.isTraceEnabled()) {
                trace.trace("Message : " + synCtx.getEnvelope());
            }
        }

        StatisticsUtils.processProxyServiceStatistics(synCtx);
        StatisticsUtils.processAllSequenceStatistics(synCtx);

        if (traceOrDebugOn) {
            traceOrDebug(traceOn, "End : Drop mediator");
        }
        synCtx.setTo(null);
        return false;
    }
}
