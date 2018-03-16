package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.filters.InMediator;

/**
 * Factory for {@link InMediator} instances.
 *
 * @see InMediatorFactory
 */
public class InMediatorSerializer extends AbstractListMediatorSerializer {

    public OMElement serializeMediator(OMElement parent, Mediator m) {

        if (!(m instanceof InMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }

        InMediator mediator = (InMediator) m;
        OMElement in = fac.createOMElement("in", synNS);
        saveTracingState(in,mediator);

        serializeChildren(in, mediator.getList());

        if (parent != null) {
            parent.addChild(in);
        }
        return in;
    }

    public String getMediatorClassName() {
        return InMediator.class.getName();
    }
}
