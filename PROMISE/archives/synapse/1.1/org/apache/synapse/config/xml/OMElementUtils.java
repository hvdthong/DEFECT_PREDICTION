package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMContainer;
import org.apache.axiom.om.OMDocument;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseException;
import org.jaxen.JaxenException;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * Holds Axiom utility methods used by Synapse
 */
public class OMElementUtils {

    private static final Log log = LogFactory.getLog(OMElementUtils.class);

    /**
     * Return the namespace with the given prefix, using the given element
     * @param prefix the prefix looked up
     * @param elem the source element to use
     * @return the namespace which maps to the prefix or null
     */
    public static String getNameSpaceWithPrefix(String prefix, OMElement elem) {
        if (prefix == null || elem == null) {
            log.warn("Searching for null NS prefix and/or using null OMElement");
            return null;
        }

        Iterator iter = elem.getAllDeclaredNamespaces();
        while (iter.hasNext()) {
            OMNamespace ns = (OMNamespace) iter.next();
            if (prefix.equals(ns.getPrefix())) {
                return ns.getNamespaceURI();     
            }
        }
        return null;
    }

    /**
     * Add all applicable xmlns NS declarations of element 'elem' into XPath expression
     * @param elem
     * @param log
     */
    public static void addNameSpaces(AXIOMXPath xpath, OMElement elem, Log log) {

        OMElement currentElem = elem;

        while (currentElem != null) {              
            Iterator it = currentElem.getAllDeclaredNamespaces();
            while (it.hasNext()) {

                OMNamespace n = (OMNamespace) it.next();
                if (n != null) {

                    try {
                        xpath.addNamespace(n.getPrefix(), n.getNamespaceURI());
                    } catch (JaxenException je) {
                        String msg = "Error adding declared name space with prefix : "
                            + n.getPrefix() + "and uri : " + n.getNamespaceURI()
                            + " to the XPath : " + xpath;
                        log.error(msg);
                        throw new SynapseException(msg, je);
                    }
                }
            }

            OMContainer parent = currentElem.getParent();
            if (parent == null || parent instanceof OMDocument) {
                return;
            }
            if (parent instanceof OMElement) {
                currentElem = (OMElement) parent;
            }
        }
    }
}
