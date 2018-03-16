package org.apache.synapse.util;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.impl.dom.NodeImpl;
import org.springframework.xml.transform.StaxSource;

/**
 * Utility class with AXIOM helper methods.
 */
public class AXIOMUtils {
    private AXIOMUtils() {}
    
    /**
     * Get a {@link Source} backed by a given AXIOM node.
     * 
     * @param node an AXIOM node
     * @return a {@link Source} object that can be used with XSL transformers,
     *         schema validators, etc.
     */
    public static Source asSource(OMNode node) {
        if (node instanceof NodeImpl) {
            return new DOMSource((NodeImpl)node);
        } else {
            return new StaxSource(((OMElement)node).getXMLStreamReader());
        }
    }
}
