package org.apache.synapse.config.xml.endpoints;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMNode;
import org.apache.axis2.description.WSDL2Constants;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.ServerManager;
import org.apache.synapse.config.SynapseConfigUtils;
import org.apache.synapse.config.xml.endpoints.utils.WSDL11EndpointBuilder;
import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.endpoints.WSDLEndpoint;
import org.apache.synapse.endpoints.utils.EndpointDefinition;

import javax.xml.namespace.QName;
import java.io.File;
import java.net.URL;

/**
 * Creates an {@link WSDLEndpoint} based endpoint from a XML configuration.
 * <p>
 * Configuration syntax:
 * <pre>
 * &lt;endpoint [name="<em>name</em>"]&gt;
 *   &lt;wsdl [uri="<em>WSDL location</em>"]
 *         service="<em>qualified name</em>" port="<em>qualified name</em>"
 *         [format="soap11|soap12|pox|get"] [optimize="mtom|swa"]
 *         [encoding="<em>charset encoding</em>"]
 *         [statistics="enable|disable"] [trace="enable|disable"]&gt;
 *     &lt;wsdl:definition&gt;...&lt;/wsdl:definition&gt;?
 *     &lt;wsdl20:description&gt;...&lt;/wsdl20:description&gt;?
 *     
 *     &lt;enableRM [policy="<em>key</em>"]/&gt;?
 *     &lt;enableSec [policy="<em>key</em>"]/&gt;?
 *     &lt;enableAddressing [version="final|submission"] [separateListener="true|false"]/&gt;?
 *     
 *     &lt;timeout&gt;
 *       &lt;duration&gt;<em>timeout duration in seconds</em>&lt;/duration&gt;
 *       &lt;action&gt;discard|fault&lt;/action&gt;
 *     &lt;/timeout&gt;?
 *     
 *     &lt;suspendDurationOnFailure&gt;
 *       <em>suspend duration in seconds</em>
 *     &lt;/suspendDurationOnFailure&gt;?
 *   &lt;/wsdl&gt;
 * &lt;/endpoint&gt;
 * </pre>
 */
public class WSDLEndpointFactory extends EndpointFactory {

    private static WSDLEndpointFactory instance = new WSDLEndpointFactory();

    private WSDLEndpointFactory() {
    }

    public static WSDLEndpointFactory getInstance() {
        return instance;
    }

    protected Endpoint createEndpoint(OMElement epConfig, boolean anonymousEndpoint) {

        WSDLEndpoint wsdlEndpoint = new WSDLEndpoint();
        OMAttribute name = epConfig.getAttribute(new QName(
                org.apache.synapse.config.xml.XMLConfigConstants.NULL_NAMESPACE, "name"));

        if (name != null) {
            wsdlEndpoint.setName(name.getAttributeValue());
        }

        OMElement wsdlElement = epConfig.getFirstChildWithName
                (new QName(SynapseConstants.SYNAPSE_NAMESPACE, "wsdl"));
        if (wsdlElement != null) {

            EndpointDefinition endpoint = null;

            String serviceName = wsdlElement.getAttributeValue(new QName("service"));
            String portName = wsdlElement.getAttributeValue(new QName("port"));
            String wsdlURI = wsdlElement.getAttributeValue(new QName("uri"));

            wsdlEndpoint.setServiceName(serviceName);
            wsdlEndpoint.setPortName(portName);

            if (wsdlURI != null) {

                wsdlEndpoint.setWsdlURI(wsdlURI.trim());
                try {
                    OMNode wsdlOM = SynapseConfigUtils.getOMElementFromURL(
                            new URL(wsdlURI).toString());
                    if (wsdlOM != null && wsdlOM instanceof OMElement) {
                        OMElement omElement = (OMElement) wsdlOM;
                        OMNamespace ns = omElement.getNamespace();
                        if (ns != null) {
                            String nsUri = omElement.getNamespace().getNamespaceURI();
                            if (org.apache.axis2.namespace.Constants.NS_URI_WSDL11.equals(nsUri)) {

                                endpoint = new WSDL11EndpointBuilder().
                                        createEndpointDefinitionFromWSDL(
                                                wsdlURI.trim(), omElement, serviceName, portName);

                            } else if (WSDL2Constants.WSDL_NAMESPACE.equals(nsUri)) {

                                handleException("WSDL 2.0 Endpoints are currently not supported");
                            }
                        }
                    }
                } catch (Exception e) {
                    handleException("Couldn't create endpoint from the given WSDL URI : "
                            + e.getMessage(), e);
                }
            }

            OMElement definitionElement = wsdlElement.getFirstChildWithName
                    (new QName(org.apache.axis2.namespace.Constants.NS_URI_WSDL11, "definitions"));
            if (endpoint == null && definitionElement != null) {
                wsdlEndpoint.setWsdlDoc(definitionElement);
                String resolveRoot = ServerManager.getInstance().getResolveRoot();
                String baseUri = "file:./";
                if (resolveRoot != null) {
                    baseUri = resolveRoot.trim();
                }
                if (!baseUri.endsWith(File.separator)) {
                    baseUri = baseUri + File.separator;
                }
                endpoint = new WSDL11EndpointBuilder().createEndpointDefinitionFromWSDL(
                        baseUri, definitionElement, serviceName, portName);
            }

            OMElement descriptionElement = wsdlElement.getFirstChildWithName
                    (new QName(org.apache.axis2.namespace.Constants.NS_URI_WSDL11, "description"));
            if (endpoint == null && descriptionElement != null) {
                wsdlEndpoint.setWsdlDoc(descriptionElement);
                handleException("WSDL 2.0 Endpoints are currently not supported.");
            }

            if (endpoint != null) {
                extractCommonEndpointProperties(endpoint, wsdlElement);
                extractSpecificEndpointProperties(endpoint, wsdlElement);
                wsdlEndpoint.setEndpoint(endpoint);
            } else {
                handleException("WSDL is not specified for WSDL endpoint.");
            }
        }

        return wsdlEndpoint;
    }

    protected void extractSpecificEndpointProperties(EndpointDefinition definition, OMElement elem) {

    }

}
