package org.apache.synapse.config.xml.endpoints;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.SynapseException;
import org.apache.synapse.config.xml.XMLConfigConstants;
import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.endpoints.LoadbalanceEndpoint;
import org.apache.synapse.endpoints.algorithms.LoadbalanceAlgorithm;
import org.apache.synapse.endpoints.algorithms.RoundRobin;

import java.util.List;

/**
 * Serializes {@link LoadbalanceEndpoint} to an XML configuration.
 *
 * &lt;endpoint [name="name"]&gt;
 *    &lt;loadbalance policy="load balance algorithm"&gt;
 *       &lt;endpoint&gt;+
 *    &lt;/loadbalance&gt;
 * &lt;/endpoint&gt;
 */
public class LoadbalanceEndpointSerializer extends EndpointSerializer {

    protected OMElement serializeEndpoint(Endpoint endpoint) {

        if (!(endpoint instanceof LoadbalanceEndpoint)) {
            throw new SynapseException("Invalid endpoint type.");
        }

        fac = OMAbstractFactory.getOMFactory();
        OMElement endpointElement
                = fac.createOMElement("endpoint", SynapseConstants.SYNAPSE_OMNAMESPACE);

        LoadbalanceEndpoint loadbalanceEndpoint = (LoadbalanceEndpoint) endpoint;

        String name = loadbalanceEndpoint.getName();
        if (name != null) {
            endpointElement.addAttribute("name", name, null);
        }

        OMElement loadbalanceElement
                = fac.createOMElement("loadbalance", SynapseConstants.SYNAPSE_OMNAMESPACE);
        endpointElement.addChild(loadbalanceElement);

        LoadbalanceAlgorithm algorithm = loadbalanceEndpoint.getAlgorithm();
        String algorithmName = "roundRobin";
        if (algorithm instanceof RoundRobin) {
             algorithmName = "roundRobin";
        }
        loadbalanceElement.addAttribute(XMLConfigConstants.ALGORITHM_NAME, algorithmName, null);

        if (!loadbalanceEndpoint.isFailover()) {
            loadbalanceElement.addAttribute("failover", "false", null);
        }

        for (Endpoint childEndpoint : loadbalanceEndpoint.getEndpoints()) {
            loadbalanceElement.addChild(EndpointSerializer.getElementFromEndpoint(childEndpoint));
        }

        return endpointElement;
    }
}
