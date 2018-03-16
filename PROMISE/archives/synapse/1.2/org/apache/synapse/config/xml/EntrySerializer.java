package org.apache.synapse.config.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMElement;
import org.apache.synapse.config.Entry;
import org.apache.synapse.SynapseException;
import org.apache.axiom.om.impl.llom.OMTextImpl;
import javax.xml.stream.XMLStreamConstants;
import java.net.URL;

/**
 * Serializer for {@link Entry} instances.
 */
public class EntrySerializer {

    private static Log log = LogFactory.getLog(EntrySerializer.class);

    protected static final OMFactory fac = OMAbstractFactory.getOMFactory();
    protected static final OMNamespace synNS = fac.createOMNamespace(
            XMLConfigConstants.SYNAPSE_NAMESPACE, "syn");
    protected static final OMNamespace nullNS = fac.createOMNamespace(XMLConfigConstants.NULL_NAMESPACE, "");

    /**
     * Serialize the Entry object to an OMElement representing the entry
     * @param entry
     * @param parent
     * @return OMElement representing the entry
     */
    public static OMElement serializeEntry(Entry entry, OMElement parent) {
        OMElement propertyElement = fac.createOMElement("localEntry", synNS);
        propertyElement.addAttribute(fac.createOMAttribute(
                "key", nullNS, entry.getKey().trim()));
        int type = entry.getType();
        if (type == Entry.URL_SRC) {
            URL srcUrl = entry.getSrc();
            if (srcUrl != null) {
                propertyElement.addAttribute(fac.createOMAttribute(
                        "src", nullNS, srcUrl.toString().trim()));
            }
        } else if (type == Entry.INLINE_XML) {
            Object value = entry.getValue();
            if (value != null && value instanceof OMElement) {
                propertyElement.addChild((OMElement) value);
            }
        } else if (type == Entry.INLINE_TEXT) {
            Object value = entry.getValue();
            if (value != null && value instanceof String) {
                OMTextImpl textData = (OMTextImpl) fac.createOMText(((String) value).trim());
                textData.setType(XMLStreamConstants.CDATA);
                propertyElement.addChild(textData);
            }
        } else if (type == Entry.REMOTE_ENTRY) {
            return null;
        } else {
            handleException("Entry type undefined");
        }
        if (parent != null) {
            parent.addChild(propertyElement);
        }
        return propertyElement;
    }

    private static void handleException(String msg) {
        log.error(msg);
        throw new SynapseException(msg);
    }
}
