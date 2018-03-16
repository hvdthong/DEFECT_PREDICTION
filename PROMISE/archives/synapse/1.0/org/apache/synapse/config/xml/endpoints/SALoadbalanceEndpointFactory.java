package org.apache.synapse.config.xml.endpoints;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.endpoints.LoadbalanceEndpoint;
import org.apache.synapse.endpoints.SALoadbalanceEndpoint;
import org.apache.synapse.endpoints.dispatch.Dispatcher;
import org.apache.synapse.endpoints.dispatch.SoapSessionDispatcher;
import org.apache.synapse.endpoints.dispatch.SimpleClientSessionDispatcher;
import org.apache.synapse.endpoints.dispatch.HttpSessionDispatcher;
import org.apache.synapse.endpoints.algorithms.LoadbalanceAlgorithm;
import org.apache.synapse.Constants;
import org.apache.synapse.SynapseException;
import org.apache.synapse.config.xml.endpoints.utils.LoadbalanceAlgorithmFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMNode;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Creates SALoadbalanceEndpoint from a XML configuration.
 *
 * <endpoint [name="name"]>
 *    <session type="soap | ..other session types.." />
 *    <loadbalance policy="policy">
 *       <endpoint>+
 *    </loadbalance>
 * </endpoint>
 */
public class SALoadbalanceEndpointFactory implements EndpointFactory {

    private static Log log = LogFactory.getLog(LoadbalanceEndpointFactory.class);

    private static SALoadbalanceEndpointFactory instance = new SALoadbalanceEndpointFactory();

    private SALoadbalanceEndpointFactory() {}

    public static SALoadbalanceEndpointFactory getInstance() {
        return instance;
    }

    public Endpoint createEndpoint(OMElement epConfig, boolean anonymousEndpoint) {

        SALoadbalanceEndpoint loadbalanceEndpoint = new SALoadbalanceEndpoint();

        OMElement sessionElement = epConfig.
                getFirstChildWithName(new QName(Constants.SYNAPSE_NAMESPACE, "session"));
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
            handleException("Session affinity endpoints should have a session element in the configuration.");
        }

        OMAttribute name = epConfig.getAttribute(new QName(
                org.apache.synapse.config.xml.Constants.NULL_NAMESPACE, "name"));

        if (name != null) {
            loadbalanceEndpoint.setName(name.getAttributeValue());
        }

        OMElement loadbalanceElement =  null;
        loadbalanceElement = epConfig.getFirstChildWithName
                (new QName(Constants.SYNAPSE_NAMESPACE, "loadbalance"));

        if(loadbalanceElement != null) {

            ArrayList endpoints = getEndpoints(loadbalanceElement, loadbalanceEndpoint);
            loadbalanceEndpoint.setEndpoints(endpoints);

            LoadbalanceAlgorithm algorithm = LoadbalanceAlgorithmFactory.
                    createLoadbalanceAlgorithm(loadbalanceElement, endpoints);
            loadbalanceEndpoint.setAlgorithm(algorithm);




            return loadbalanceEndpoint;
        }

    }

    public Object getObjectFromOMNode(OMNode om) {
        if (om instanceof OMElement) {
            return createEndpoint((OMElement) om, false);
        } else {
            handleException("Invalid XML configuration for an Endpoint. OMElement expected");
        }
        return null;
    }

    private ArrayList getEndpoints(OMElement loadbalanceElement, Endpoint parent) {

        ArrayList endpoints = new ArrayList();
        Iterator iter = loadbalanceElement.getChildrenWithName
                (org.apache.synapse.config.xml.Constants.ENDPOINT_ELT);
        while (iter.hasNext()) {

            OMElement endptElem = (OMElement) iter.next();

            EndpointFactory epFac = EndpointAbstractFactory.getEndpointFactroy(endptElem);
            Endpoint endpoint = epFac.createEndpoint(endptElem, true);
            endpoint.setParentEndpoint(parent);
            endpoints.add(endpoint);
        }

        return endpoints;
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
