package org.apache.synapse.mediators.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.Constants;
import org.apache.synapse.MessageContext;
import org.apache.synapse.statistics.StatisticsStack;
import org.apache.synapse.statistics.StatisticsUtils;
import org.apache.synapse.statistics.impl.SequenceStatisticsStack;
import org.apache.synapse.mediators.AbstractListMediator;

/**
 * The SynapseMediator is the "mainmediator" of the synapse engine. It is
 * given each message on arrival at the synapse engine. The synapse configuration
 * holds a reference to this special mediator instance. The SynapseMediator
 * holds the list of mediators supplied within the <rules> element of an XML
 * based Synapse configuration
 *
 * @see org.apache.synapse.config.SynapseConfiguration#getMainSequence()
 */
public class SynapseMediator extends AbstractListMediator {

    private static final Log log = LogFactory.getLog(SynapseMediator.class);
    private static final Log trace = LogFactory.getLog(Constants.TRACE_LOGGER);

    /**
     * Perform the mediation specified by the rule set
     *
     * @param synCtx the message context
     * @return as per standard mediate() semantics
     */
    public boolean mediate(MessageContext synCtx) {
        log.debug("Synapse main mediator :: mediate()");
        if(synCtx.isResponse()) {
            StatisticsUtils.processAllSequenceStatistics(synCtx);
        }
        StatisticsStack sequenceStack = (StatisticsStack) synCtx.getProperty(
                Constants.SEQUENCE_STATISTICS_STACK);
        if (sequenceStack == null) {
            sequenceStack = new SequenceStatisticsStack();
            synCtx.setProperty(Constants.SEQUENCE_STATISTICS_STACK, sequenceStack);
        }
        String seqName = "MainSequence";
        boolean isFault = synCtx.getEnvelope().getBody().hasFault();
        sequenceStack.put(seqName, System.currentTimeMillis(), !synCtx.isResponse(), true, isFault);
        boolean shouldTrace = shouldTrace(synCtx.getTracingState());
         try {
            if (shouldTrace) {
                trace.trace("Start : Synapse main mediator");
            }
            return super.mediate(synCtx);
        } finally {
            if (shouldTrace) {
                trace.trace("End : Synapse main mediator");
            }
        }
    }
}
