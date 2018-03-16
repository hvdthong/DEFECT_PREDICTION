package org.apache.synapse.config.xml.endpoints;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.config.xml.XMLConfigConstants;
import org.apache.synapse.config.xml.endpoints.utils.LoadbalanceAlgorithmFactory;
import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.endpoints.LoadbalanceEndpoint;
import org.apache.synapse.endpoints.algorithms.LoadbalanceAlgorithm;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Creates {@link LoadbalanceEndpoint} using an XML configuration.
 *
 * &lt;endpoint [name="name"]&gt;
 *    &lt;loadbalance policy="load balance algorithm"&gt;
 *       &lt;endpoint&gt;+
 *    &lt;/loadbalance&gt;
 * &lt;/endpoint&gt;
 */
public class LoadbalanceEndpointFactory extends EndpointFactory {

    private static LoadbalanceEndpointFactory instance = new LoadbalanceEndpointFactory();

    private LoadbalanceEndpointFactory() {}

    public static LoadbalanceEndpointFactory getInstance() {
        return instance;
    }

    protected Endpoint createEndpoint(OMElement epConfig, boolean anonymousEndpoint) {


        OMElement loadbalanceElement = epConfig.getFirstChildWithName(
                new QName(SynapseConstants.SYNAPSE_NAMESPACE, "loadbalance"));

        if(loadbalanceElement != null) {

            LoadbalanceEndpoint loadbalanceEndpoint = new LoadbalanceEndpoint();

            OMAttribute name = epConfig.getAttribute(new QName(
                    org.apache.synapse.config.xml.XMLConfigConstants.NULL_NAMESPACE, "name"));

            if (name != null) {
                loadbalanceEndpoint.setName(name.getAttributeValue());
            }

            ArrayList<Endpoint> endpoints = getEndpoints(loadbalanceElement, loadbalanceEndpoint);
            loadbalanceEndpoint.setEndpoints(endpoints);

            LoadbalanceAlgorithm algorithm = LoadbalanceAlgorithmFactory.
                    createLoadbalanceAlgorithm(loadbalanceElement, endpoints);
            loadbalanceEndpoint.setAlgorithm(algorithm);

            String failover = loadbalanceElement.getAttributeValue(new QName("failover"));
            if (failover != null && failover.equalsIgnoreCase("false")) {
                loadbalanceEndpoint.setFailover(false);
            }

            return loadbalanceEndpoint;
        }

    }
}
