package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.eip.aggregator.AggregateMediator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Serializer for {@link AggregateMediator} instances.
 * 
 * <pre>
 * &lt;aggregate&gt;
 *   &lt;correlateOn expression="xpath"/&gt;?
 *   &lt;completeCondition [timeout="time-in-seconds"]&gt;
 *     &lt;messageCount min="int-min" max="int-max"/&gt;?
 *   &lt;/completeCondition&gt;?
 *   &lt;onComplete expression="xpath" [sequence="sequence-ref"]&gt;
 *     (mediator +)?
 *   &lt;/onComplete&gt;
 * &lt;/aggregate&gt;
 * </pre>
 */
public class AggregateMediatorSerializer extends AbstractMediatorSerializer {

    private static final Log log = LogFactory.getLog(AggregateMediatorSerializer.class);

    public OMElement serializeMediator(OMElement parent, Mediator m) {

        if (!(m instanceof AggregateMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }
        AggregateMediator mediator = (AggregateMediator) m;
        OMElement aggregator = fac.createOMElement("aggregate", synNS);
        saveTracingState(aggregator, mediator);

        if (mediator.getCorrelateExpression() != null) {
            OMElement corelateOn = fac.createOMElement("correlateOn", synNS);
            SynapseXPathSerializer.serializeXPath(
                mediator.getCorrelateExpression(), corelateOn, "expression");
            aggregator.addChild(corelateOn);
        }

        OMElement completeCond = fac.createOMElement("completeCondition", synNS);
        if (mediator.getCompletionTimeoutMillis() != 0) {
            completeCond.addAttribute("timeout", Long.toString(mediator.getCompletionTimeoutMillis() / 1000), nullNS);
        }
        OMElement messageCount = fac.createOMElement("messageCount", synNS);
        if (mediator.getMinMessagesToComplete() != 0) {
            messageCount.addAttribute("min", Integer.toString(mediator.getMinMessagesToComplete()), nullNS);
        }
        if (mediator.getMaxMessagesToComplete() != 0) {
            messageCount.addAttribute("max", Integer.toString(mediator.getMaxMessagesToComplete()), nullNS);
        }
        completeCond.addChild(messageCount);
        aggregator.addChild(completeCond);

        OMElement onCompleteElem = fac.createOMElement("onComplete", synNS);
        if (mediator.getAggregationExpression() != null) {
            SynapseXPathSerializer.serializeXPath(
                mediator.getAggregationExpression(), onCompleteElem, "expression");
        }
        if (mediator.getOnCompleteSequenceRef() != null) {
            onCompleteElem.addAttribute("sequence", mediator.getOnCompleteSequenceRef(), nullNS);
        } else if (mediator.getOnCompleteSequence() != null) {
            new SequenceMediatorSerializer().serializeChildren(
                    onCompleteElem, mediator.getOnCompleteSequence().getList());
        }
        aggregator.addChild(onCompleteElem);

        if (parent != null) {
            parent.addChild(aggregator);
        }

        return aggregator;
    }

    public String getMediatorClassName() {
        return AggregateMediator.class.getName();
    }
}
