package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.filters.FilterMediator;

/**
 * <pre>
 * &lt;filter (source="xpath" regex="string") | xpath="xpath"&gt;
 *   mediator+
 * &lt;/filter&gt;
 * </pre>
 */
public class FilterMediatorSerializer extends AbstractListMediatorSerializer {

    public OMElement serializeMediator(OMElement parent, Mediator m) {

        if (!(m instanceof FilterMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }

        FilterMediator mediator = (FilterMediator) m;
        OMElement filter = fac.createOMElement("filter", synNS);

        if (mediator.getSource() != null && mediator.getRegex() != null) {
            filter.addAttribute(fac.createOMAttribute(
                "source", nullNS, mediator.getSource().toString()));
            super.serializeNamespaces(filter, mediator.getSource());

            filter.addAttribute(fac.createOMAttribute(
                "regex", nullNS, mediator.getRegex().pattern()));

        } else if (mediator.getXpath() != null) {
            filter.addAttribute(fac.createOMAttribute(
                "xpath", nullNS, mediator.getXpath().toString()));
            super.serializeNamespaces(filter, mediator.getXpath());

        } else {
            handleException("Invalid filter mediator. " +
                "Should have either a 'source' and a 'regex' OR an 'xpath' ");
        }

        saveTracingState(filter, mediator);
        serializeChildren(filter, mediator.getList());

        if (parent != null) {
            parent.addChild(filter);
        }
        return filter;
    }

    public String getMediatorClassName() {
        return FilterMediator.class.getName();
    }
}
