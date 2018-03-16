package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.synapse.config.xml.endpoints.EndpointAbstractSerializer;
import org.apache.synapse.mediators.eip.Target;

/**
 * This will serialize the Target util object for EIP mediators in to the
 * &lt;target&gt; element as specified below
 *
 * &lt;target (sequence="string reference")? (endpoint="string reference")?&gt;
 *  (&lt;sequence&gt; | &lt;endpoinit&gt;)?
 * &lt;/target&gt;
 */
public class TargetSerializer {

    /**
     * This holds the OMFactory to be used for the OMElement creations
     */
    private static final OMFactory fac = OMAbstractFactory.getOMFactory();

    /**
     * This holds the Synapse namesapce for all the elements (qualified from default)
     */
    private static final OMNamespace synNS
            = fac.createOMNamespace(XMLConfigConstants.SYNAPSE_NAMESPACE, "syn");

    /**
     * This holds the null namespace for all the attributes (unqualified from default)
     */
    private static final OMNamespace nullNS = fac.createOMNamespace(XMLConfigConstants.NULL_NAMESPACE, "");

    /**
     * This static method will serialize the Target object to the target elements
     *
     * @param target - Target which is subjected to the serialization
     * @return OMElement representing the serialized Target
     */
    public static OMElement serializeTarget(Target target) {

        OMElement targetElem = fac.createOMElement("target", synNS);
        if (target.getTo() != null) {
            targetElem.addAttribute("to", target.getTo(), nullNS);
        }

        if (target.getSoapAction() != null) {
            targetElem.addAttribute("soapAction", target.getSoapAction(), nullNS);
        }

        if (target.getSequenceRef() != null) {
            targetElem.addAttribute("sequence", target.getSequenceRef(), nullNS);
        }

        if (target.getEndpointRef() != null) {
            targetElem.addAttribute("endpoint", target.getEndpointRef(), nullNS);
        }

        if (target.getSequence() != null) {
            SequenceMediatorSerializer serializer = new SequenceMediatorSerializer();
            serializer.serializeAnonymousSequence(targetElem, target.getSequence());
        }

        if (target.getEndpoint() != null) {
            targetElem.addChild(EndpointAbstractSerializer.getEndpointSerializer(
                    target.getEndpoint()).serializeEndpoint(target.getEndpoint()));
        }

        return targetElem;
    }
}
