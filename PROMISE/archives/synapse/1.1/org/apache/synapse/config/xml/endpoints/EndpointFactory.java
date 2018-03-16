package org.apache.synapse.config.xml.endpoints;

import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.config.XMLToObjectMapper;
import org.apache.axiom.om.OMElement;

/**
 * All endpoint factories should implement this interface. Use EndpointAbstractFactory to obtain the
 * correct endpoint fatory for particular endpoint configuration. As endpoints can be nested inside
 * each other, EndpointFactory implementations may call other EndpointFactory implementations recursively
 * to obtain the required endpoint hierachy.
 *
 * This also serves as the XMLToObjactMapper implementation for specific endpoint implementations.
 * If the endpoint type is not known use XMLToEndpointMapper as the generic XMLToObjectMapper for
 * all endpoints.
 */
public interface EndpointFactory extends XMLToObjectMapper {

    /**
     * Creates the Endpoint implementation for the given XML endpoint configuration. If the endpoint
     * configuration is an inline one, it should be anonymous endpoint. If it is defined as an immediate
     * child element of the <definitions> it should have a name, which is used as the key in local registry.
     *
     * @param epConfig OMElement conatining the endpoint configuration.
     * @param anonymousEndpoint false if the endpoint has a name. true otherwise.
     * @return Endpoint implementation for the given configuration.
     */
    public Endpoint createEndpoint(OMElement epConfig, boolean anonymousEndpoint);
}
