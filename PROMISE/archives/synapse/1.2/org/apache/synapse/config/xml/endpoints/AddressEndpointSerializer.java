package org.apache.synapse.config.xml.endpoints;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.SynapseException;
import org.apache.synapse.endpoints.AddressEndpoint;
import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.endpoints.utils.EndpointDefinition;

/**
 * Serializes {@link AddressEndpoint} to XML.
 *
 * @see AddressEndpointFactory
 */
public class AddressEndpointSerializer extends DefaultEndpointSerializer {

    protected OMElement serializeEndpoint(Endpoint endpoint) {

        if (!(endpoint instanceof AddressEndpoint)) {
            throw new SynapseException("Invalid endpoint type.");
        }

        fac = OMAbstractFactory.getOMFactory();
        OMElement endpointElement
                = fac.createOMElement("endpoint", SynapseConstants.SYNAPSE_OMNAMESPACE);

        AddressEndpoint addressEndpoint = (AddressEndpoint) endpoint;
        String name = addressEndpoint.getName();
        if (name != null) {
            endpointElement.addAttribute("name", name, null);
        }

        EndpointDefinition epAddress = addressEndpoint.getEndpoint();
        OMElement addressElement = serializeEndpointDefinition(epAddress);
        endpointElement.addChild(addressElement);

        return endpointElement;
    }

    public OMElement serializeEndpointDefinition(EndpointDefinition endpointDefinition) {

        OMElement element = fac.createOMElement("address", SynapseConstants.SYNAPSE_OMNAMESPACE);

        if (endpointDefinition.getAddress() != null) {
            element.addAttribute(
                    fac.createOMAttribute("uri", null, endpointDefinition.getAddress()));
        } else {
            handleException("Invalid Endpoint. Address is required");
        }

        serializeCommonEndpointProperties(endpointDefinition, element);
        serializeSpecificEndpointProperties(endpointDefinition, element);

        return element;
    }
}
