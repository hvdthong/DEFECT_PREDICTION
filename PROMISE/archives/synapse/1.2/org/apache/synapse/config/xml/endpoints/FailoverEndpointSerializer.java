package org.apache.synapse.config.xml.endpoints;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.endpoints.FailoverEndpoint;
import org.apache.synapse.SynapseException;
import org.apache.synapse.SynapseConstants;

import java.util.List;

/**
 * Serializes {@link FailoverEndpoint} to XML configuration.
 *
 * &lt;endpoint [name="name"]&gt;
 *    &lt;failover&gt;
 *       &lt;endpoint&gt;+
 *    &lt;/failover&gt;
 * &lt;/endpoint&gt;
 */
public class FailoverEndpointSerializer extends EndpointSerializer {

    protected OMElement serializeEndpoint(Endpoint endpoint) {

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

        for (Endpoint childEndpoint : failoverEndpoint.getEndpoints()) {
            failoverElement.addChild(EndpointSerializer.getElementFromEndpoint(childEndpoint));
        }

        return endpointElement;
    }
}
