package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseException;
import org.apache.synapse.util.xpath.SynapseXPath;
import org.jaxen.JaxenException;

import javax.xml.namespace.QName;

/**
 * 
 */
public class SynapseXPathFactory {

    private static final Log log = LogFactory.getLog(SynapseXPathFactory.class);

    public static SynapseXPath getSynapseXPath(OMElement elem, QName attribName)
        throws JaxenException {

        SynapseXPath xpath = null;
        OMAttribute xpathAttrib = elem.getAttribute(attribName);

        if (xpathAttrib != null && xpathAttrib.getAttributeValue() != null) {

            xpath = new SynapseXPath(xpathAttrib.getAttributeValue());
            OMElementUtils.addNameSpaces(xpath, elem, log);

        } else {
            handleException("Couldn't find the XPath attribute with the QName : "
                + attribName.toString() + " in the element : " + elem.toString());
        }       

        return xpath;
    }

    private static void handleException(String message) {
        log.error(message);
        throw new SynapseException(message);
    }
}
