package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseException;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.builtin.DropMediator;

public class DropMediatorSerializer extends AbstractMediatorSerializer
     {

    private static final Log log = LogFactory.getLog(DropMediatorSerializer.class);

    public OMElement serializeMediator(OMElement parent, Mediator m) {

        if (!(m instanceof DropMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }

        DropMediator mediator = (DropMediator) m;
        OMElement drop = fac.createOMElement("drop", synNS);
        finalizeSerialization(drop,mediator);

        if (parent != null) {
            parent.addChild(drop);
        }
        return drop;
    }

    public String getMediatorClassName() {
        return DropMediator.class.getName();
    }

    private void handleException(String msg) {
        log.error(msg);
        throw new SynapseException(msg);
    }
}
