package org.apache.synapse.config.xml.endpoints;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.config.xml.XMLConfigConstants;
import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.endpoints.FailoverEndpoint;
import org.apache.synapse.endpoints.IndirectEndpoint;
import org.apache.synapse.endpoints.utils.EndpointDefinition;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Creates {@link FailoverEndpoint} using a XML configuration.
 *
 * &lt;endpoint [name="name"]&gt;
 *    &lt;failover&gt;
 *       &lt;endpoint&gt;+
 *    &lt;/failover&gt;
 * &lt;/endpoint&gt;
 */
public class FailoverEndpointFactory extends EndpointFactory {

    private static FailoverEndpointFactory instance = new FailoverEndpointFactory();

    private FailoverEndpointFactory() {}

    public static FailoverEndpointFactory getInstance() {
        return instance;
    }

    protected Endpoint createEndpoint(OMElement epConfig, boolean anonymousEndpoint) {

        OMElement failoverElement = epConfig.getFirstChildWithName
                (new QName(SynapseConstants.SYNAPSE_NAMESPACE, "failover"));
        if (failoverElement != null) {

            FailoverEndpoint failoverEndpoint = new FailoverEndpoint();
            String name = epConfig.getAttributeValue(new QName("name"));
            if (name != null) {
                failoverEndpoint.setName(name);
            }
            failoverEndpoint.setEndpoints(getEndpoints(failoverElement, failoverEndpoint));
            return failoverEndpoint;
        }
        return null;
    }

}
