package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseException;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.filters.OutMediator;

public class OutMediatorSerializer extends AbstractListMediatorSerializer
     {

    private static final Log log = LogFactory.getLog(OutMediatorSerializer.class);

    public OMElement serializeMediator(OMElement parent, Mediator m) {

        if (!(m instanceof OutMediator)) {
            handleException("Unsupported mediator passed out for serialization : " + m.getType());
        }

        OutMediator mediator = (OutMediator) m;
        OMElement out = fac.createOMElement("out", synNS);
        finalizeSerialization(out,mediator);
        serializeChildren(out, mediator.getList());

        if (parent != null) {
            parent.addChild(out);
        }
        return out;
    }

    public String getMediatorClassName() {
        return OutMediator.class.getName();
    }

    private void handleException(String msg) {
        log.error(msg);
        throw new SynapseException(msg);
    }
}
