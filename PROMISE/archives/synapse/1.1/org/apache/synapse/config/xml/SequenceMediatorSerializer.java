package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.base.SequenceMediator;

/**
 * <pre>
 * &lt;sequence name="string" [onError="string"]&gt;
 *   mediator+
 * &lt;/sequence&gt;
 * </pre>
 * <p/>
 * OR
 * <p/>
 * <pre>
 * &lt;sequence key="name"/&gt;
 * </pre>
 */
public class SequenceMediatorSerializer extends AbstractListMediatorSerializer {

    public OMElement serializeAnonymousSequence(OMElement parent, SequenceMediator mediator) {
        OMElement sequence = fac.createOMElement("sequence", synNS);
        int isEnableStatistics = mediator.getStatisticsState();
        String statisticsValue = null;
        if (isEnableStatistics == org.apache.synapse.SynapseConstants.STATISTICS_ON) {
            statisticsValue = XMLConfigConstants.STATISTICS_ENABLE;
        } else if (isEnableStatistics == org.apache.synapse.SynapseConstants.STATISTICS_OFF) {
            statisticsValue = XMLConfigConstants.STATISTICS_DISABLE;
        }
        if (statisticsValue != null) {
            sequence.addAttribute(fac.createOMAttribute(
                    XMLConfigConstants.STATISTICS_ATTRIB_NAME, nullNS, statisticsValue));
        }
        if (mediator.getErrorHandler() != null) {
            sequence.addAttribute(fac.createOMAttribute(
                    "onError", nullNS, mediator.getErrorHandler()));
        }
        saveTracingState(sequence, mediator);
        serializeChildren(sequence, mediator.getList());
        if (parent != null) {
            parent.addChild(sequence);
        }
        return sequence;
    }

    public OMElement serializeMediator(OMElement parent, Mediator m) {

        if (!(m instanceof SequenceMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }

        SequenceMediator mediator = (SequenceMediator) m;
        OMElement sequence = fac.createOMElement("sequence", synNS);

        if (mediator.isDynamic()) {
            sequence.addAttribute(fac.createOMAttribute(
                    "name", nullNS, mediator.getName()));
            sequence.addAttribute(fac.createOMAttribute(
                    "key", nullNS, mediator.getRegistryKey()));

        } else {

            int isEnableStatistics = mediator.getStatisticsState();
            String statisticsValue = null;
            if (isEnableStatistics == org.apache.synapse.SynapseConstants.STATISTICS_ON) {
                statisticsValue = XMLConfigConstants.STATISTICS_ENABLE;
            } else if (isEnableStatistics == org.apache.synapse.SynapseConstants.STATISTICS_OFF) {
                statisticsValue = XMLConfigConstants.STATISTICS_DISABLE;
            }
            if (statisticsValue != null) {
                sequence.addAttribute(fac.createOMAttribute(
                        XMLConfigConstants.STATISTICS_ATTRIB_NAME, nullNS, statisticsValue));
            }

            if (mediator.getKey() != null) {
                sequence.addAttribute(fac.createOMAttribute(
                        "key", nullNS, mediator.getKey()));
            } else if (mediator.getName() != null) {
                sequence.addAttribute(fac.createOMAttribute(
                        "name", nullNS, mediator.getName()));

                if (mediator.getErrorHandler() != null) {
                    sequence.addAttribute(fac.createOMAttribute(
                            "onError", nullNS, mediator.getErrorHandler()));
                }
                saveTracingState(sequence, mediator);
                serializeChildren(sequence, mediator.getList());
            }
        }

        if (parent != null) {
            parent.addChild(sequence);
        }
        return sequence;
    }

    public String getMediatorClassName() {
        return SequenceMediator.class.getName();
    }
}
