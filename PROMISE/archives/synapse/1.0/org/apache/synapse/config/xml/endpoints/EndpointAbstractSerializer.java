package org.apache.synapse.config.xml.endpoints;

import org.apache.synapse.endpoints.*;
import org.apache.synapse.SynapseException;

/**
 * Abstract serialier for endpoint serializers. Use this class to obtain the EndpointSerializer
 * implementation for particular endpoint type.
 */
public class EndpointAbstractSerializer {

    /**
     * Returns the EndpointSerializer implementation for the given endpoint. Throws a SynapseException,
     * if there is no serializer for the given endpoint type.
     *
     * @param endpoint Endpoint implementaion.
     * @return EndpointSerializer implementation.
     */
    public static EndpointSerializer getEndpointSerializer(Endpoint endpoint) {

        if (endpoint instanceof AddressEndpoint) {
            return new AddressEndpointSerializer();
        } else if (endpoint instanceof WSDLEndpoint) {
            return new WSDLEndpointSerializer();
        } else if (endpoint instanceof IndirectEndpoint) {
            return new IndirectEndpointSerializer();
        } else if (endpoint instanceof LoadbalanceEndpoint) {
            return new LoadbalanceEndpointSerializer();
        } else if (endpoint instanceof SALoadbalanceEndpoint) {
            return new SALoadbalanceEndpointSerializer();
        } else if (endpoint instanceof FailoverEndpoint) {
            return new FailoverEndpointSerializer();
        }

        throw new SynapseException("Serializer for endpoint " +
                endpoint.getClass().toString() + " is not defined.");
    }
}
