package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseException;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.transform.HeaderMediator;
import org.jaxen.JaxenException;

import javax.xml.namespace.QName;
import java.util.Iterator;

/**
 * This builds a Header Mediator parsing the XML configuration supplied
 *
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
public class HeaderMediatorFactory extends AbstractMediatorFactory  {

    private static final QName HEADER_Q = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "header");
    private static final QName ATT_ACTION = new QName("action");

    public Mediator createMediator(OMElement elem) {

        HeaderMediator headerMediator = new HeaderMediator();
        OMAttribute name   = elem.getAttribute(ATT_NAME);
        OMAttribute value  = elem.getAttribute(ATT_VALUE);
        OMAttribute exprn  = elem.getAttribute(ATT_EXPRN);
        OMAttribute action = elem.getAttribute(ATT_ACTION);

        if (name == null || name.getAttributeValue() == null) {
            String msg = "A valid name attribute is required for the header mediator";
            log.error(msg);
            throw new SynapseException(msg);
        } else {
            String nameAtt = name.getAttributeValue();
            int colonPos = nameAtt.indexOf(":");
            if (colonPos != -1) {
                String prefix = nameAtt.substring(0, colonPos);
                Iterator it = elem.getAllDeclaredNamespaces();
                while (it.hasNext()) {
                    OMNamespace n = (OMNamespace) it.next();
                    if (prefix.equals(n.getPrefix())) {
                        headerMediator.setQName(
                            new QName(n.getNamespaceURI(), nameAtt.substring(colonPos+1), prefix));
                    }
                }
            } else {
                headerMediator.setQName(new QName(nameAtt));
            }
        }

        processTraceState(headerMediator,elem);

        if (action != null && "remove".equals(action.getAttributeValue())) {
            headerMediator.setAction(HeaderMediator.ACTION_REMOVE);
        }

        if (headerMediator.getAction() == HeaderMediator.ACTION_SET &&
            value == null && exprn == null) {
            String msg = "A 'value' or 'expression' attribute is required for a [set] header mediator";
            log.error(msg);
            throw new SynapseException(msg);
        }

        if (value != null && value.getAttributeValue() != null) {
            headerMediator.setValue(value.getAttributeValue());

        } else if (exprn != null && exprn.getAttributeValue() != null) {
            try {
                AXIOMXPath xp = new AXIOMXPath(exprn.getAttributeValue());
                OMElementUtils.addNameSpaces(xp, elem, log);
                headerMediator.setExpression(xp);
            } catch (JaxenException je) {
                String msg = "Invalid XPath expression : " + exprn.getAttributeValue();
                log.error(msg);
                throw new SynapseException(msg, je);
            }
        }

        return headerMediator;
    }

    public QName getTagQName() {
        return HEADER_Q;
    }
}
