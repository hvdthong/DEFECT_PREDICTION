package org.apache.synapse.config.xml.endpoints;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.endpoints.LoadbalanceEndpoint;
import org.apache.synapse.endpoints.algorithms.LoadbalanceAlgorithm;
import org.apache.synapse.endpoints.algorithms.RoundRobin;
import org.apache.synapse.SynapseException;
import org.apache.synapse.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Serializes LoadbalanceEndpoint to an XML configuration.
 *
 * <endpoint [name="name"]>
 *    <loadbalance policy="load balance algorithm">
 *       <endpoint>+
 *    </loadbalance>
 * </endpoint>
 */
public class LoadbalanceEndpointSerializer implements EndpointSerializer {

    private OMFactory fac = null;

    public OMElement serializeEndpoint(Endpoint endpoint) {

        if (!(endpoint instanceof LoadbalanceEndpoint)) {
            throw new SynapseException("Invalid endpoint type.");
        }

        fac = OMAbstractFactory.getOMFactory();
        OMElement endpointElement = fac.createOMElement("endpoint", Constants.SYNAPSE_OMNAMESPACE);

        LoadbalanceEndpoint loadbalanceEndpoint = (LoadbalanceEndpoint) endpoint;

        String name = loadbalanceEndpoint.getName();
        if (name != null) {
            endpointElement.addAttribute("name", name, null);
        }

        OMElement loadbalanceElement = fac.createOMElement("loadbalance", Constants.SYNAPSE_OMNAMESPACE);
        endpointElement.addChild(loadbalanceElement);

        LoadbalanceAlgorithm algorithm = loadbalanceEndpoint.getAlgorithm();
        String algorithmName = "roundRobin";
        if (algorithm instanceof RoundRobin) {
             algorithmName = "roundRobin";
        }
        loadbalanceElement.addAttribute
                (org.apache.synapse.config.xml.Constants.ALGORITHM_NAME, algorithmName, null);

        if (!loadbalanceEndpoint.isFailover()) {
            loadbalanceElement.addAttribute("failover", "false", null);
        }

        List endpoints = loadbalanceEndpoint.getEndpoints();
        for (int i = 0; i < endpoints.size(); i++) {
            Endpoint childEndpoint = (Endpoint) endpoints.get(i);
            EndpointSerializer serializer = EndpointAbstractSerializer.
                    getEndpointSerializer(childEndpoint);
            OMElement aeElement = serializer.serializeEndpoint(childEndpoint);
            loadbalanceElement.addChild(aeElement);
        }

        return endpointElement;
    }
}
