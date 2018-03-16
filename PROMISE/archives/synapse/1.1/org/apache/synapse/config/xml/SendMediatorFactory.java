package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.Mediator;
import org.apache.synapse.SynapseException;
import org.apache.synapse.config.xml.endpoints.EndpointFactory;
import org.apache.synapse.config.xml.endpoints.EndpointAbstractFactory;
import org.apache.synapse.mediators.builtin.SendMediator;
import org.apache.synapse.endpoints.Endpoint;

import javax.xml.namespace.QName;

/**
 * The Send mediator factory parses a Send element and creates an instance of the mediator
 *
 *
 * The &lt;send&gt; element is used to send messages out of Synapse to some endpoint. In the simplest case,
 * the place to send the message to is implicit in the message (via a property of the message itself)-
 * that is indicated by the following
 * <pre>
 *  &lt;send/&gt;
 * </pre>
 *
 * If the message is to be sent to one or more endpoints, then the following is used:
 * <pre>
 *  &lt;send&gt;
 *   (endpointref | endpoint)+
 *  &lt;/send&gt;
 * </pre>
 * where the endpointref token refers to the following:
 * <pre>
 * &lt;endpoint ref="name"/&gt;
 * </pre>
 * and the endpoint token refers to an anonymous endpoint defined inline:
 * <pre>
 *  &lt;endpoint address="url"/&gt;
 * </pre>
 * If the message is to be sent to an endpoint selected by load balancing across a set of endpoints,
 * then it is indicated by the following:
 * <pre>
 * &lt;send&gt;
 *   &lt;load-balance algorithm="uri"&gt;
 *     (endpointref | endpoint)+
 *   &lt;/load-balance&gt;
 * &lt;/send&gt;
 * </pre>
 * Similarly, if the message is to be sent to an endpoint with failover semantics, then it is indicated by the following:
 * <pre>
 * &lt;send&gt;
 *   &lt;failover&gt;
 *     (endpointref | endpoint)+
 *   &lt;/failover&gt;
 * &lt;/send&gt;
 * </pre>
 */
public class SendMediatorFactory extends AbstractMediatorFactory  {

    private static final QName SEND_Q = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "send");
    private static final QName ENDPOINT_Q = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "endpoint");

    public Mediator createMediator(OMElement elem) {

        SendMediator sm =  new SendMediator();

        processTraceState(sm,elem);

        OMElement epElement = elem.getFirstChildWithName(ENDPOINT_Q);
        if (epElement != null) {

            EndpointFactory fac = EndpointAbstractFactory.getEndpointFactroy(epElement);
            if (fac != null) {
                Endpoint endpoint = fac.createEndpoint(epElement, true);
                if (endpoint != null) {
                    sm.setEndpoint(endpoint);
                }
            } else {
                throw new SynapseException("Invalid endpoint fromat.");
            }
        }

        return sm;
    }

    public QName getTagQName() {
        return SEND_Q;
    }
}
