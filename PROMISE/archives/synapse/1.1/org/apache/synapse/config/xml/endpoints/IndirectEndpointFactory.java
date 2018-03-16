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
 * registry (e.g. <endpoint key="registry/endpoint1.xml" />).
 *
 * <endpoint key="key" />
 */
public class IndirectEndpointFactory implements EndpointFactory {

    private static Log log = LogFactory.getLog(IndirectEndpointFactory.class);

    private static IndirectEndpointFactory instance = new IndirectEndpointFactory();

    private IndirectEndpointFactory() {}

    public static IndirectEndpointFactory getInstance() {
        return instance;
    }

    public Endpoint createEndpoint(OMElement epConfig, boolean anonymousEndpoint) {

        IndirectEndpoint indirectEndpoint = new IndirectEndpoint();
        String ref = epConfig.getAttributeValue(new QName("key"));
        indirectEndpoint.setKey(ref);
        return indirectEndpoint;
    }

    public Object getObjectFromOMNode(OMNode om) {
        if (om instanceof OMElement) {
			return createEndpoint((OMElement) om, false);
		} else {
			handleException("Invalid XML configuration for an Endpoint. OMElement expected");
		}
		return null;
    }

     private static void handleException(String msg) {
        log.error(msg);
        throw new SynapseException(msg);
    }

    private static void handleException(String msg, Exception e) {
        log.error(msg, e);
        throw new SynapseException(msg, e);
    }
}
