package org.apache.synapse.config.xml.endpoints;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.endpoints.SALoadbalanceEndpoint;
import org.apache.synapse.endpoints.algorithms.LoadbalanceAlgorithm;
import org.apache.synapse.endpoints.algorithms.RoundRobin;
import org.apache.synapse.endpoints.dispatch.Dispatcher;
import org.apache.synapse.endpoints.dispatch.HttpSessionDispatcher;
import org.apache.synapse.endpoints.dispatch.SimpleClientSessionDispatcher;
import org.apache.synapse.endpoints.dispatch.SoapSessionDispatcher;

public class SALoadbalanceEndpointSerializer extends EndpointSerializer {

    protected OMElement serializeEndpoint(Endpoint endpoint) {

        if (!(endpoint instanceof SALoadbalanceEndpoint)) {
            handleException("Invalid endpoint type for serializing. " +
                    "Expected: SALoadbalanceEndpoint Found: " + endpoint.getClass().getName());
        }

        SALoadbalanceEndpoint loadbalanceEndpoint = (SALoadbalanceEndpoint) endpoint;

        fac = OMAbstractFactory.getOMFactory();
        OMElement endpointElement = fac.createOMElement("endpoint", SynapseConstants.SYNAPSE_OMNAMESPACE);

        String name = loadbalanceEndpoint.getName();
        if (name != null) {
            endpointElement.addAttribute("name", name, null);
        }

        Dispatcher dispatcher = loadbalanceEndpoint.getDispatcher();

        if (dispatcher instanceof SoapSessionDispatcher) {
            OMElement sessionElement = fac.createOMElement("session", SynapseConstants.SYNAPSE_OMNAMESPACE);
            sessionElement.addAttribute("type", "soap", null);
            endpointElement.addChild(sessionElement);

        } else if (dispatcher instanceof HttpSessionDispatcher) {
            OMElement sessionElement = fac.createOMElement("session", SynapseConstants.SYNAPSE_OMNAMESPACE);
            sessionElement.addAttribute("type", "http", null);
            endpointElement.addChild(sessionElement);

        } else if (dispatcher instanceof SimpleClientSessionDispatcher) {
            OMElement sessionElement = fac.createOMElement("session", SynapseConstants.SYNAPSE_OMNAMESPACE);
            sessionElement.addAttribute("type", "simpleClientSession", null);
            endpointElement.addChild(sessionElement);
        }

        OMElement loadbalanceElement = fac.createOMElement("loadbalance", SynapseConstants.SYNAPSE_OMNAMESPACE);
        endpointElement.addChild(loadbalanceElement);

        LoadbalanceAlgorithm algorithm = loadbalanceEndpoint.getAlgorithm();
        String algorithmName = "roundRobin";
        if (algorithm instanceof RoundRobin) {
             algorithmName = "roundRobin";
        }
        loadbalanceElement.addAttribute("algorithm", algorithmName, null);

        for (Endpoint childEndpoint : loadbalanceEndpoint.getEndpoints()) {
            loadbalanceElement.addChild(EndpointSerializer.getElementFromEndpoint(childEndpoint));
        }

        return endpointElement;
    }
}
