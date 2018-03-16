package org.apache.xerces.validators.schema;

import org.apache.xerces.dom.AttrImpl;
import org.apache.xerces.dom.DocumentImpl;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Some useful utility methods.
 */
public class XUtil {


    /** This class cannot be instantiated. */
    protected XUtil() {}


    /**
     * Copies the source tree into the specified place in a destination
     * tree. The source node and its children are appended as children
     * of the destination node.
     * <p>
     * <em>Note:</em> This is an iterative implementation.
     */
    public static void copyInto(Node src, Node dest) throws DOMException {

        Document factory = dest.getOwnerDocument();
        boolean domimpl = factory instanceof DocumentImpl;

        Node start  = src;
        Node parent = src;
        Node place  = src;

        while (place != null) {

            Node node = null;
            int  type = place.getNodeType();
            switch (type) {
                case Node.CDATA_SECTION_NODE: {
                    node = factory.createCDATASection(place.getNodeValue());
                    break;
                }
                case Node.COMMENT_NODE: {
                    node = factory.createComment(place.getNodeValue());
                    break;
                }
                case Node.ELEMENT_NODE: {
                    Element element = factory.createElement(place.getNodeName());
                    node = element;
                    NamedNodeMap attrs  = place.getAttributes();
                    int attrCount = attrs.getLength();
                    for (int i = 0; i < attrCount; i++) {
                        Attr attr = (Attr)attrs.item(i);
                        String attrName = attr.getNodeName();
                        String attrValue = attr.getNodeValue();
                        element.setAttribute(attrName, attrValue);
                        if (domimpl && !attr.getSpecified()) {
                            ((AttrImpl)element.getAttributeNode(attrName)).setSpecified(false);
                        }
                    }
                    break;
                }
                case Node.ENTITY_REFERENCE_NODE: {
                    node = factory.createEntityReference(place.getNodeName());
                    break;
                }
                case Node.PROCESSING_INSTRUCTION_NODE: {
                    node = factory.createProcessingInstruction(place.getNodeName(),
                                                               place.getNodeValue());
                    break;
                }
                case Node.TEXT_NODE: {
                    node = factory.createTextNode(place.getNodeValue());
                    break;
                }
                default: {
                    throw new IllegalArgumentException("can't copy node type, "+
                                                       type+" ("+
                                                       node.getNodeName()+')');
                }
            }
            dest.appendChild(node);

            if (place.hasChildNodes()) {
                parent = place;
                place  = place.getFirstChild();
                dest   = node;
            }

            else {
                place = place.getNextSibling();
                while (place == null && parent != start) {
                    place  = parent.getNextSibling();
                    parent = parent.getParentNode();
                    dest   = dest.getParentNode();
                }
            }

        }


    /** Finds and returns the first child element node. */
    public static Element getFirstChildElement(Node parent) {

        Node child = parent.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                return (Element)child;
            }
            child = child.getNextSibling();
        }

        return null;


    /** Finds and returns the last child element node. */
    public static Element getLastChildElement(Node parent) {

        Node child = parent.getLastChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                return (Element)child;
            }
            child = child.getPreviousSibling();
        }

        return null;


    /** Finds and returns the next sibling element node. */
    public static Element getNextSiblingElement(Node node) {

        Node sibling = node.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                return (Element)sibling;
            }
            sibling = sibling.getNextSibling();
        }

        return null;


    /** Finds and returns the first child node with the given name. */
    public static Element getFirstChildElement(Node parent, String elemName) {

        Node child = parent.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (child.getNodeName().equals(elemName)) {
                    return (Element)child;
                }
            }
            child = child.getNextSibling();
        }

        return null;


    /** Finds and returns the last child node with the given name. */
    public static Element getLastChildElement(Node parent, String elemName) {

        Node child = parent.getLastChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (child.getNodeName().equals(elemName)) {
                    return (Element)child;
                }
            }
            child = child.getPreviousSibling();
        }

        return null;


    /** Finds and returns the next sibling node with the given name. */
    public static Element getNextSiblingElement(Node node, String elemName) {

        Node sibling = node.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                if (sibling.getNodeName().equals(elemName)) {
                    return (Element)sibling;
                }
            }
            sibling = sibling.getNextSibling();
        }

        return null;


    /** Finds and returns the first child node with the given name. */
    public static Element getFirstChildElement(Node parent, String elemNames[]) {

        Node child = parent.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                for (int i = 0; i < elemNames.length; i++) {
                    if (child.getNodeName().equals(elemNames[i])) {
                        return (Element)child;
                    }
                }
            }
            child = child.getNextSibling();
        }

        return null;


    /** Finds and returns the last child node with the given name. */
    public static Element getLastChildElement(Node parent, String elemNames[]) {

        Node child = parent.getLastChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                for (int i = 0; i < elemNames.length; i++) {
                    if (child.getNodeName().equals(elemNames[i])) {
                        return (Element)child;
                    }
                }
            }
            child = child.getPreviousSibling();
        }

        return null;


    /** Finds and returns the next sibling node with the given name. */
    public static Element getNextSiblingElement(Node node, String elemNames[]) {

        Node sibling = node.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                for (int i = 0; i < elemNames.length; i++) {
                    if (sibling.getNodeName().equals(elemNames[i])) {
                        return (Element)sibling;
                    }
                }
            }
            sibling = sibling.getNextSibling();
        }

        return null;


    /**
     * Finds and returns the first child node with the given name and
     * attribute name, value pair.
     */
    public static Element getFirstChildElement(Node   parent,
                                               String elemName,
                                               String attrName,
                                               String attrValue) {

        Node child = parent.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)child;
                if (element.getNodeName().equals(elemName) &&
                    element.getAttribute(attrName).equals(attrValue)) {
                    return element;
                }
            }
            child = child.getNextSibling();
        }

        return null;


    /**
     * Finds and returns the last child node with the given name and
     * attribute name, value pair.
     */
    public static Element getLastChildElement(Node   parent,
                                               String elemName,
                                               String attrName,
                                               String attrValue) {

        Node child = parent.getLastChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)child;
                if (element.getNodeName().equals(elemName) &&
                    element.getAttribute(attrName).equals(attrValue)) {
                    return element;
                }
            }
            child = child.getPreviousSibling();
        }

        return null;


    /**
     * Finds and returns the next sibling node with the given name and
     * attribute name, value pair. Since only elements have attributes,
     * the node returned will be of type Node.ELEMENT_NODE.
     */
    public static Element getNextSiblingElement(Node   node,
                                                String elemName,
                                                String attrName,
                                                String attrValue) {

        Node sibling = node.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)sibling;
                if (element.getNodeName().equals(elemName) &&
                    element.getAttribute(attrName).equals(attrValue)) {
                    return element;
                }
            }
            sibling = sibling.getNextSibling();
        }

        return null;


