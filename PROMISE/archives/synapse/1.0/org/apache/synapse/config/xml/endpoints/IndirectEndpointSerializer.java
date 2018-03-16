package org.apache.synapse.config.xml.endpoints;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMFactory;
import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.endpoints.IndirectEndpoint;
import org.apache.synapse.SynapseException;
import org.apache.synapse.Constants;

/**
 * Serializes an IndirectEndpoint to an XML configuration.
 *
 * <endpoint key="key" />
 */
public class IndirectEndpointSerializer implements EndpointSerializer {

    private OMFactory fac = null;

    public OMElement serializeEndpoint(Endpoint endpoint) {

        if (!(endpoint instanceof IndirectEndpoint)) {
            throw new SynapseException("Invalid endpoint type.");
        }

        fac = OMAbstractFactory.getOMFactory();
        OMElement endpointElement = fac.createOMElement("endpoint", Constants.SYNAPSE_OMNAMESPACE);

        IndirectEndpoint indirectEndpoint = (IndirectEndpoint) endpoint;
        String ref = indirectEndpoint.getKey();
        if (ref != null) {
            endpointElement.addAttribute("key", ref, null);
        }

        return endpointElement;
    }
}
