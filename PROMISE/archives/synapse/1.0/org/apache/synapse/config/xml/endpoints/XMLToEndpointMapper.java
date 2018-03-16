package org.apache.synapse.config.xml.endpoints;

import org.apache.synapse.config.XMLToObjectMapper;
import org.apache.synapse.SynapseException;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMElement;

/**
 * This is a generic XMLToObjectMapper implementation for all endpoint types. Use this if the endpoint
 * type is not known at the time mapper is created. If the endpoint type is known use the EndpointFactory
 * implementation for that specific endpoint.
 */
public class XMLToEndpointMapper implements XMLToObjectMapper {

    private static XMLToEndpointMapper instance = new XMLToEndpointMapper();

    private XMLToEndpointMapper() {}

    public static XMLToEndpointMapper getInstance() {
        return instance;
    }

    /**
     * Constructs the Endpoint implementation for the given OMNode.
     *
     * @param om OMNode containig endpoint configuration. This should be an OMElement.
     * @return Endpoint implementaiotn for the given OMNode.
     */
    public Object getObjectFromOMNode(OMNode om) {
        if (om instanceof OMElement) {
            OMElement epElement = (OMElement) om;
            return EndpointAbstractFactory.
                    getEndpointFactroy(epElement).createEndpoint(epElement, false);
        } else {
            throw new SynapseException("Configuration is not in proper format.");
        }
    }
}
