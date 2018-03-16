package org.apache.synapse.config.xml.endpoints;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.endpoints.Endpoint;

/**
 * All endpoint serializers should implement this interface. Use EndpointAbstractSerializer to obtain
 * the correct EndpointSerializer implementation for a particular endpoint. EndpointSerializer implementation
 * may call other EndpointSerializer implementations to serialize nested endpoints.
 */
public interface EndpointSerializer {

    /**
     * Serializes the given endpoint implementation to an XML object.
     *
     * @param endpoint Endpoint implementation to be serialized.
     * @return OMElement containing XML configuration.
     */
    public OMElement serializeEndpoint(Endpoint endpoint);
}
