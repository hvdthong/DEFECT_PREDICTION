package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseException;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.filters.InMediator;

public class InMediatorSerializer extends AbstractListMediatorSerializer
     {

    private static final Log log = LogFactory.getLog(InMediatorSerializer.class);

    public OMElement serializeMediator(OMElement parent, Mediator m) {

        if (!(m instanceof InMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }

        InMediator mediator = (InMediator) m;
        OMElement in = fac.createOMElement("in", synNS);
        finalizeSerialization(in,mediator);

        serializeChildren(in, mediator.getList());

        if (parent != null) {
            parent.addChild(in);
        }
        return in;
    }

    public String getMediatorClassName() {
        return InMediator.class.getName();
    }

    private void handleException(String msg) {
        log.error(msg);
        throw new SynapseException(msg);
    }
}
