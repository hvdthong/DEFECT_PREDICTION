package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.config.xml.endpoints.EndpointSerializer;
import org.apache.synapse.mediators.builtin.SendMediator;
import org.apache.synapse.endpoints.Endpoint;

/**
 * Serializer for {@link SendMediator} instances.
 * 
 * @see SendMediatorFactory
 */
public class SendMediatorSerializer extends AbstractMediatorSerializer {

    public OMElement serializeMediator(OMElement parent, Mediator m) {

        if (!(m instanceof SendMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }

        SendMediator mediator = (SendMediator) m;
        OMElement send = fac.createOMElement("send", synNS);
        saveTracingState(send, mediator);

        Endpoint activeEndpoint = mediator.getEndpoint();
        if (activeEndpoint != null) {
            send.addChild(EndpointSerializer.getElementFromEndpoint(activeEndpoint));
        }

        if (parent != null) {
            parent.addChild(send);
        }
        return send;
    }

    public String getMediatorClassName() {
        return SendMediator.class.getName();
    }
}
