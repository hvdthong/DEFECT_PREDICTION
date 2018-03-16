package org.apache.synapse.config.xml.endpoints;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.config.xml.XMLConfigConstants;
import org.apache.synapse.config.xml.endpoints.utils.LoadbalanceAlgorithmFactory;
import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.endpoints.SALoadbalanceEndpoint;
import org.apache.synapse.endpoints.algorithms.LoadbalanceAlgorithm;
import org.apache.synapse.endpoints.dispatch.Dispatcher;
import org.apache.synapse.endpoints.dispatch.HttpSessionDispatcher;
import org.apache.synapse.endpoints.dispatch.SimpleClientSessionDispatcher;
import org.apache.synapse.endpoints.dispatch.SoapSessionDispatcher;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Creates {@link SALoadbalanceEndpoint} from a XML configuration.
 *
 * &lt;endpoint [name="name"]&gt;
 *    &lt;session type="soap | ..other session types.." /&gt;
 *    &lt;loadbalance policy="policy"&gt;
 *       &lt;endpoint&gt;+
 *    &lt;/loadbalance&gt;
 * &lt;/endpoint&gt;
 */
public class SALoadbalanceEndpointFactory extends EndpointFactory {

    private static SALoadbalanceEndpointFactory instance = new SALoadbalanceEndpointFactory();

    private SALoadbalanceEndpointFactory() {}

    public static SALoadbalanceEndpointFactory getInstance() {
        return instance;
    }

    protected Endpoint createEndpoint(OMElement epConfig, boolean anonymousEndpoint) {

        SALoadbalanceEndpoint loadbalanceEndpoint = new SALoadbalanceEndpoint();

        OMElement sessionElement = epConfig.
                getFirstChildWithName(new QName(SynapseConstants.SYNAPSE_NAMESPACE, "session"));
        if (sessionElement != null) {

            String type = sessionElement.getAttributeValue(new QName("type"));

            if (type.equalsIgnoreCase("soap")) {
                Dispatcher soapDispatcher = new SoapSessionDispatcher();
                loadbalanceEndpoint.setDispatcher(soapDispatcher);

            } else if (type.equalsIgnoreCase("http")) {
                Dispatcher httpDispatcher = new HttpSessionDispatcher();
                loadbalanceEndpoint.setDispatcher(httpDispatcher);

            } else if (type.equalsIgnoreCase("simpleClientSession")) {
                Dispatcher csDispatcher = new SimpleClientSessionDispatcher();
                loadbalanceEndpoint.setDispatcher(csDispatcher);
            }
        } else {
            handleException("Session affinity endpoints should " +
                    "have a session element in the configuration.");
        }

        OMAttribute name = epConfig.getAttribute(new QName(
                org.apache.synapse.config.xml.XMLConfigConstants.NULL_NAMESPACE, "name"));

        if (name != null) {
            loadbalanceEndpoint.setName(name.getAttributeValue());
        }

        OMElement loadbalanceElement;
        loadbalanceElement = epConfig.getFirstChildWithName
                (new QName(SynapseConstants.SYNAPSE_NAMESPACE, "loadbalance"));

        if(loadbalanceElement != null) {

            ArrayList<Endpoint> endpoints = getEndpoints(loadbalanceElement, loadbalanceEndpoint);
            loadbalanceEndpoint.setEndpoints(endpoints);

            LoadbalanceAlgorithm algorithm = LoadbalanceAlgorithmFactory.
                    createLoadbalanceAlgorithm(loadbalanceElement, endpoints);
            loadbalanceEndpoint.setAlgorithm(algorithm);




            return loadbalanceEndpoint;
        }

        return null;
    }
}
