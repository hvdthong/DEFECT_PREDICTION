package org.apache.synapse.config.xml.endpoints;

import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.endpoints.IndirectEndpoint;
import org.apache.synapse.SynapseException;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;

/**
 * Creates an IndirectEndpoint using a XML configuration. Key can be a name of an endpoint defined
 * in the same Synapse configuration or a registry key pointing to an endpoint configuration in the
 * registry (e.g. &lt;endpoint key="registry/endpoint1.xml"/&gt;).
 *
 * &lt;endpoint key="key"/&gt;
 */
public class IndirectEndpointFactory extends EndpointFactory {

    private static IndirectEndpointFactory instance = new IndirectEndpointFactory();

    private IndirectEndpointFactory() {}

    public static IndirectEndpointFactory getInstance() {
        return instance;
    }

    protected Endpoint createEndpoint(OMElement epConfig, boolean anonymousEndpoint) {

        IndirectEndpoint indirectEndpoint = new IndirectEndpoint();
        String ref = epConfig.getAttributeValue(new QName("key"));
        String name = epConfig.getAttributeValue(new QName("name"));
        if (name != null) {
            indirectEndpoint.setName(name);
        }
        indirectEndpoint.setKey(ref);
        return indirectEndpoint;
    }
}
