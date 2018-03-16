package org.apache.synapse.config.xml.endpoints;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.SynapseException;
import org.apache.synapse.endpoints.DefaultEndpoint;
import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.endpoints.utils.EndpointDefinition;

/**
 * Serializes {@link DefaultEndpoint} to XML.
 *
 * @see DefaultEndpointFactory
 */
public class DefaultEndpointSerializer extends EndpointSerializer {

    protected OMElement serializeEndpoint(Endpoint endpoint) {

        if (!(endpoint instanceof DefaultEndpoint)) {
            throw new SynapseException("Invalid endpoint type.");
        }

        fac = OMAbstractFactory.getOMFactory();
        OMElement endpointElement
                = fac.createOMElement("endpoint", SynapseConstants.SYNAPSE_OMNAMESPACE);

        DefaultEndpoint defaultEndpoint = (DefaultEndpoint) endpoint;
        String name = defaultEndpoint.getName();
        if (name != null) {
            endpointElement.addAttribute("name", name, null);
        }

        EndpointDefinition epAddress = defaultEndpoint.getEndpoint();
        OMElement defaultElement = serializeEndpointDefinition(epAddress);
        endpointElement.addChild(defaultElement);

        return endpointElement;
    }

    protected void serializeSpecificEndpointProperties(EndpointDefinition endpointDefinition, OMElement element) {

        if (SynapseConstants.FORMAT_POX.equals(endpointDefinition.getFormat())) {
            element.addAttribute(fac.createOMAttribute("format", null, "pox"));
        } else if (SynapseConstants.FORMAT_GET.equals(endpointDefinition.getFormat())) {
            element.addAttribute(fac.createOMAttribute("format", null, "get"));
        } else if (SynapseConstants.FORMAT_SOAP11.equals(endpointDefinition.getFormat())) {
            element.addAttribute(fac.createOMAttribute("format", null, "soap11"));
        } else if (SynapseConstants.FORMAT_SOAP12.equals(endpointDefinition.getFormat())) {
            element.addAttribute(fac.createOMAttribute("format", null, "soap12"));

        } else if (endpointDefinition.isForcePOX()) {
            element.addAttribute(fac.createOMAttribute("format", null, "pox"));
        } else if (endpointDefinition.isForceGET()) {
            element.addAttribute(fac.createOMAttribute("format", null, "get"));
        } else if (endpointDefinition.isForceSOAP11()) {
            element.addAttribute(fac.createOMAttribute("format", null, "soap11"));
        } else if (endpointDefinition.isForceSOAP12()) {
            element.addAttribute(fac.createOMAttribute("format", null, "soap12"));
        }

    }

    public OMElement serializeEndpointDefinition(EndpointDefinition endpointDefinition) {
        OMElement element = fac.createOMElement("default", SynapseConstants.SYNAPSE_OMNAMESPACE);
        serializeCommonEndpointProperties(endpointDefinition, element);
        serializeSpecificEndpointProperties(endpointDefinition, element);
        return element;
    }
}
