package org.apache.synapse.config.xml.endpoints;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMFactory;
import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.endpoints.FailoverEndpoint;
import org.apache.synapse.SynapseException;
import org.apache.synapse.SynapseConstants;

import java.util.List;

/**
 * Serializes FailoverEndpoint to XML configuration.
 *
 * <endpoint [name="name"]>
 *    <failover>
 *       <endpoint>+
 *    </failover>
 * </endpoint>
 */
public class FailoverEndpointSerializer implements EndpointSerializer {

    private OMFactory fac = null;

    public OMElement serializeEndpoint(Endpoint endpoint) {

        if (!(endpoint instanceof FailoverEndpoint)) {
            throw new SynapseException("Invalid endpoint type.");
        }

        FailoverEndpoint failoverEndpoint = (FailoverEndpoint) endpoint;

        fac = OMAbstractFactory.getOMFactory();
        OMElement endpointElement = fac.createOMElement("endpoint", SynapseConstants.SYNAPSE_OMNAMESPACE);

        OMElement failoverElement = fac.createOMElement("failover", SynapseConstants.SYNAPSE_OMNAMESPACE);
        endpointElement.addChild(failoverElement);

        String name = failoverEndpoint.getName();
        if (name != null) {
            endpointElement.addAttribute("name", name, null);
        }

        List endpoints = failoverEndpoint.getEndpoints();
        for (int i = 0; i < endpoints.size(); i++) {
            Endpoint childEndpoint = (Endpoint) endpoints.get(i);
            EndpointSerializer serializer = EndpointAbstractSerializer.
                    getEndpointSerializer(childEndpoint);
            OMElement aeElement = serializer.serializeEndpoint(childEndpoint);
            failoverElement.addChild(aeElement);
        }

        return endpointElement;
    }
}
