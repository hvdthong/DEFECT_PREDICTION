package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.eip.splitter.IterateMediator;

/**
 * This class will be acting as the serializer for the IterateMediator which will convert the
 * IterateMediator instance to the following xml configuration
 *
 * <pre>
 *  &lt;iterate continueParent=(true | false) expression="XPATH expression"&gt;
 *   &lt;target to="TO address" [soapAction="urn:Action"] sequence="sequence ref"
 *                                                         endpoint="endpoint ref"&gt;
 *    &lt;sequence&gt; (mediator +) &lt;/sequence&gt;
 *    &lt;endpoint&gt; endpoint &lt;/endpoint&gt;
 *   &lt;/target&gt;
 *  &lt;/iterate&gt;
 * </pre>
 */
public class IterateMediatorSerializer extends AbstractMediatorSerializer {

    /**
     * This method will implement the serialization logic of the IterateMediator class to the
     * relevant xml configuration
     *
     * @param parent
     *              OMElement specifying the parent element to which the created configurtaion
     *              element will be attached
     *
     * @param m
     *          IterateMediator to be serialized
     *
     * @return OMElement describing the serialized configuration of the IterateMediator
     */
    public OMElement serializeMediator(OMElement parent, Mediator m) {

        if (!(m instanceof IterateMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }
        
        OMElement itrElem = fac.createOMElement("iterate", synNS);
        saveTracingState(itrElem, m);

        IterateMediator itrMed = (IterateMediator) m;
        if (itrMed.isContinueParent()) {
            itrElem.addAttribute("continueParent", Boolean.toString(true), nullNS);
        }

        if (itrMed.isPreservePayload()) {
            itrElem.addAttribute("preservePayload", Boolean.toString(true), nullNS);
        }

        if (itrMed.getAttachPath() != null && !".".equals(itrMed.getAttachPath().toString())) {
            itrElem.addAttribute("attachPath", itrMed.getAttachPath().toString(), nullNS);
            serializeNamespaces(itrElem, itrMed.getAttachPath());
        }
        
        if (itrMed.getExpression() != null) {
            itrElem.addAttribute("expression", itrMed.getExpression().toString(), nullNS);
            serializeNamespaces(itrElem, itrMed.getExpression());
        } else {
            handleException("Missing expression of the IterateMediator which is required.");
        }

        itrElem.addChild(TargetSerializer.serializeTarget(itrMed.getTarget()));

        if (parent != null) {
            parent.addChild(itrElem);
        }

        return itrElem;
    }

    /**
     * This method implements the getMediatorClassName of the interface MediatorSerializer and
     * will be used in getting the mediator class name which will be serialized by this serializer
     *
     * @return String representing the full class name of the mediator
     */
    public String getMediatorClassName() {
        return IterateMediator.class.getName();
    }
}
