package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.transform.HeaderMediator;

import javax.xml.namespace.QName;

/**
 * Set header
 *   <pre>
 *      &lt;header name="qname" (value="literal" | expression="xpath")/&gt;
 *   </pre>
 *
 * Remove header
 *   <pre>
 *      &lt;header name="qname" action="remove"/&gt;
 *   </pre>
 */
public class HeaderMediatorSerializer extends AbstractMediatorSerializer {

    public OMElement serializeMediator(OMElement parent, Mediator m) {

        if (!(m instanceof HeaderMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }

        HeaderMediator mediator = (HeaderMediator) m;
        OMElement header = fac.createOMElement("header", synNS);
        saveTracingState(header,mediator);

        QName qName = mediator.getQName();
        if (qName != null) {
            if (qName.getNamespaceURI() != null) {
                header.addAttribute(fac.createOMAttribute(
                    "name", nullNS,
                    (qName.getPrefix() != null && !"".equals(qName.getPrefix())
                        ? qName.getPrefix() + ":" : "") + 
                    qName.getLocalPart()));
                header.declareNamespace(qName.getNamespaceURI(), qName.getPrefix());
            } else {
                header.addAttribute(fac.createOMAttribute(
                    "name", nullNS, qName.getLocalPart()));
            }
        }

        if (mediator.getAction() == HeaderMediator.ACTION_REMOVE) {
            header.addAttribute(fac.createOMAttribute(
                "action", nullNS, "remove"));
        } else {
            if (mediator.getValue() != null) {
                header.addAttribute(fac.createOMAttribute(
                    "value", nullNS, mediator.getValue()));

            } else if (mediator.getExpression() != null) {
                header.addAttribute(fac.createOMAttribute(
                    "expression", nullNS, mediator.getExpression().toString()));
                super.serializeNamespaces(header, mediator.getExpression());

            } else {
                handleException("Value or expression required for a set header mediator");
            }
        }

        if (parent != null) {
            parent.addChild(header);
        }
        return header;
    }

    public String getMediatorClassName() {
        return HeaderMediator.class.getName();
    }
}
