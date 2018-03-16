package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMAttribute;
import org.apache.synapse.Mediator;
import org.apache.synapse.SynapseException;
import org.apache.synapse.mediators.eip.aggregator.AggregateMediator;
import org.apache.synapse.mediators.ext.ClassMediator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * &lt;aggregate&gt;
 *  &lt;corelateOn expression="XPATH-expression"/&gt;
 *  &lt;completeCondition timeout="time-in-seconds"&gt;
 *   &lt;messageCount min="int-min" max="int-max"/&gt;
 *  &lt;/completeCondition&gt;
 *  &lt;onComplete expression="XPATH-expression" sequence="sequence-ref"&gt;
 *   (mediator +)?
 *  &lt;/onComplete&gt;
 *  &lt;invalidate sequence="sequence-ref" timeout="time-in-seconds"&gt;
 *   (mediator +)?
 *  &lt;/invalidate&gt;
 * &lt;/aggregate&gt;
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

        if (mediator.getCorelateExpression() != null) {
            OMElement corelateOn = fac.createOMElement("corelateOn", synNS);
            corelateOn.addAttribute("expression", mediator.getCorelateExpression().toString(), nullNS);
            super.serializeNamespaces(corelateOn, mediator.getCorelateExpression());
            aggregator.addChild(corelateOn);
        }

        OMElement completeCond = fac.createOMElement("completeCondition", synNS);
        if (mediator.getCompleteTimeout() != 0) {
            completeCond.addAttribute("timeout", "" + mediator.getCompleteTimeout(), nullNS);
        }
        OMElement messageCount = fac.createOMElement("messageCount", synNS);
        if (mediator.getMinMessagesToComplete() != 0) {
            messageCount.addAttribute("min", "" + mediator.getMinMessagesToComplete(), nullNS);
        }
        if (mediator.getMaxMessagesToComplete() != 0) {
            messageCount.addAttribute("max", "" + mediator.getMaxMessagesToComplete(), nullNS);
        }
        completeCond.addChild(messageCount);
        aggregator.addChild(completeCond);

        OMElement aggregatorElem = fac.createOMElement("aggregator", synNS);

        return aggregator;
    }

    public String getMediatorClassName() {
        return AggregateMediator.class.getName();
    }
}
