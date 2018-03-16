package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseException;
import org.apache.synapse.util.xpath.SynapseXPath;

/**
 * 
 */
public class SynapseXPathSerializer {

    private static final Log log = LogFactory.getLog(SynapseXPathSerializer.class);

    public static OMElement serializeXPath(SynapseXPath xpath, OMElement elem, String attribName) {

        OMNamespace nullNS = elem.getOMFactory()
            .createOMNamespace(XMLConfigConstants.NULL_NAMESPACE, "");

        if (xpath != null) {
            
            elem.addAttribute(elem.getOMFactory().createOMAttribute(
                attribName, nullNS, xpath.toString()));

            serializeNamespaces(elem, xpath);

        } else {
            handleException("Couldn't find the xpath in the SynapseXPath");
        }

        return elem;
    }

    private static void serializeNamespaces(OMElement elem, SynapseXPath xpath) {

        for (Object o : xpath.getNamespaces().keySet()) {
            String prefix = (String) o;
            String uri = xpath.getNamespaceContext().translateNamespacePrefixToUri(prefix);
            if (!XMLConfigConstants.SYNAPSE_NAMESPACE.equals(uri)) {
                elem.declareNamespace(uri, prefix);
            }
        }
    }

    private static void handleException(String message) {
        log.error(message);
        throw new SynapseException(message); 
    }
}
