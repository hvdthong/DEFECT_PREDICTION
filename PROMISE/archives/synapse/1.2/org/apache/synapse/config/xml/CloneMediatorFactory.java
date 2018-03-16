package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.eip.splitter.CloneMediator;

import javax.xml.namespace.QName;
import java.util.Iterator;

/**
 * The &lt;clone&gt; element is used to copy messages in Synapse to simillar messages but with
 * different message contexts and mediated using the specified targets
 *
 * <pre>
 * &lt;clone [continueParent=(true | false)]&gt;
 *   &lt;target [to="uri"] [soapAction="qname"] [sequence="sequence_ref"]
 *          [endpoint="endpoint_ref"]&gt;
 *     &lt;sequence&gt;
 *       (mediator)+
 *     &lt;/sequence&gt;?
 *     &lt;endpoint&gt;
 *       endpoint
 *     &lt;/endpoint&gt;?
 *   &lt;/target&gt;+
 * &lt;/clone&gt;
 * </pre>
 */
public class CloneMediatorFactory extends AbstractMediatorFactory {

    /**
     * This will hold the QName of the clone mediator element in the xml configuration
     */
    private static final QName CLONE_Q = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "clone");
    private static final QName ATT_CONTPAR = new QName("continueParent");
    private static final QName TARGET_Q = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "target");

    /**
     * This method implements the createMediator method of the MediatorFactory interface
     * 
     * @param elem - OMElement describing the element which will be parsed
     *  to build the CloneMediator
     * @return Mediator of the type CloneMediator built from the config element
     */
    public Mediator createMediator(OMElement elem) {

        CloneMediator mediator = new CloneMediator();
        processTraceState(mediator, elem);
        
        OMAttribute continueParent = elem.getAttribute(ATT_CONTPAR);
        if (continueParent != null) {
            mediator.setContinueParent(
                    Boolean.valueOf(continueParent.getAttributeValue()).booleanValue());
        }

        Iterator targetElements = elem.getChildrenWithName(TARGET_Q);
        while (targetElements.hasNext()) {
            mediator.addTarget(TargetFactory.createTarget((OMElement) targetElements.next()));
        }

        return mediator;
    }

    /**
     * This method will implement the getTagQName method of the MediatorFactory interface
     *
     * @return QName of the clone element in xml configuraiton
     */
    public QName getTagQName() {
        return CLONE_Q;
    }
}
