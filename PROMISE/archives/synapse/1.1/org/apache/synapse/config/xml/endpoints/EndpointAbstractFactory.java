package org.apache.synapse.config.xml.endpoints;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.SynapseException;

import javax.xml.namespace.QName;

/**
 * Abstract factory for endpoint factories. Use this class to obtain the EndpointFactory implementation
 * for the required endpoint configuration.
 */
public class EndpointAbstractFactory {

    /**
     * Returns the EndpointFactory implementation for given endpoint configuration. Throws a SynapseException,
     * if there is no EndpointFactory for given configuration.
     *
     * @param configElement Endpoint configuration.
     * @return EndpointFactory implementation.
     */
    public static EndpointFactory getEndpointFactroy(OMElement configElement) {

        if (configElement.getAttribute(new QName("key")) != null) {
            IndirectEndpointFactory endpointFactory = IndirectEndpointFactory.getInstance();
            return endpointFactory;
        }

        OMElement addressElement = configElement.getFirstChildWithName
                (new QName(SynapseConstants.SYNAPSE_NAMESPACE, "address"));
        if (addressElement != null) {
            EndpointFactory endpointFactory = AddressEndpointFactory.getInstance();
            return endpointFactory;
        }

        OMElement wsdlElement = configElement.getFirstChildWithName
                (new QName(SynapseConstants.SYNAPSE_NAMESPACE, "wsdl"));
        if (wsdlElement != null) {
            EndpointFactory endpointFactory = WSDLEndpointFactory.getInstance();
            return endpointFactory;
        }

        OMElement lbElement = configElement.getFirstChildWithName
                (new QName(SynapseConstants.SYNAPSE_NAMESPACE, "loadbalance"));
        if (lbElement != null) {
            OMElement sessionElement = configElement.
                    getFirstChildWithName(new QName(SynapseConstants.SYNAPSE_NAMESPACE, "session"));
            if (sessionElement != null) {
                EndpointFactory endpointFactory = SALoadbalanceEndpointFactory.getInstance();
                return endpointFactory;
            } else {
                EndpointFactory endpointFactory = LoadbalanceEndpointFactory.getInstance();
                return endpointFactory;
            }            
        }

        OMElement foElement = configElement.getFirstChildWithName
                (new QName(SynapseConstants.SYNAPSE_NAMESPACE, "failover"));
        if (foElement != null) {
            EndpointFactory endpointFactory = FailoverEndpointFactory.getInstance();
            return endpointFactory;
        }

        throw new SynapseException("Invalid endpoint configuration.");
    }
}
