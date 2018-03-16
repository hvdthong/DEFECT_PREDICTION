package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseException;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.transform.XSLTMediator;
import org.apache.synapse.config.xml.AbstractMediatorSerializer;

/**
 * <pre>
 * &lt;xslt key="property-key" [source="xpath"]&gt;
 *   &lt;property name="string" (value="literal" | expression="xpath")/&gt;*
 * &lt;/transform&gt;
 * </pre>
 */
public class XSLTMediatorSerializer extends AbstractMediatorSerializer {

    private static final Log log = LogFactory.getLog(XSLTMediatorSerializer.class);

    public OMElement serializeMediator(OMElement parent, Mediator m) {

        if (!(m instanceof XSLTMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }

        XSLTMediator mediator = (XSLTMediator) m;
        OMElement xslt = fac.createOMElement("xslt", synNS);

        if (mediator.getXsltKey() != null) {
            xslt.addAttribute(fac.createOMAttribute(
                "key", nullNS, mediator.getXsltKey()));
        } else {
            handleException("Invalid XSLT mediator. XSLT registry key is required");
        }
        finalizeSerialization(xslt,mediator);

        if (mediator.getSource() != null &&
            !XSLTMediator.DEFAULT_XPATH.toString().equals(mediator.getSource().toString())) {
            xslt.addAttribute(fac.createOMAttribute(
                "source", nullNS, mediator.getSource().toString()));
            serializeNamespaces(xslt, mediator.getSource());
        }

        serializeProperties(xslt, mediator.getProperties());

        if (parent != null) {
            parent.addChild(xslt);
        }
        return xslt;
    }

    public String getMediatorClassName() {
        return XSLTMediator.class.getName();
    }

    private void handleException(String msg) {
        log.error(msg);
        throw new SynapseException(msg);
    }
}
