package org.apache.xerces.dom;

import java.util.Vector;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;


/**
 * AttributeMap inherits from NamedNodeMapImpl and extends it to deal with the
 * specifics of storing attributes. These are:
 * <ul>
 *  <li>managing ownership of attribute nodes
 *  <li>managing default attributes
 *  <li>firing mutation events
 * </ul>
 * <p>
 * This class doesn't directly support mutation events, however, it notifies
 * the document when mutations are performed so that the document class do so.
 *
 */
public class AttributeMap extends NamedNodeMapImpl {


    /** Constructs a named node map. */
    protected AttributeMap(ElementImpl ownerNode, NamedNodeMapImpl defaults) {
        super(ownerNode);
        if (defaults != null) {
            cloneContent(defaults);
            if (nodes != null) {
                hasDefaults(true);
            }
        }
    }

    /**
     * Adds an attribute using its nodeName attribute.
     * @see org.w3c.dom.NamedNodeMap#setNamedItem
     * @return If the new Node replaces an existing node the replaced Node is
     *      returned, otherwise null is returned. 
     * @param arg 
     *      An Attr node to store in this map.
     * @exception org.w3c.dom.DOMException The exception description.
     */
    public Node setNamedItem(Node arg)
        throws DOMException {

    	if (isReadOnly()) {
            throw
                new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
                                     "DOM001 Modification not allowed");
        }
    	if(arg.getOwnerDocument() != ownerNode.ownerDocument()) {
            throw new DOMException(DOMException.WRONG_DOCUMENT_ERR,
                                       "DOM005 Wrong document");
        }
        if (arg.getNodeType() != arg.ATTRIBUTE_NODE) {
            throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, 
                                   "DOM006 Hierarchy request error");
        }

        AttrImpl argn = (AttrImpl)arg;

    	if (argn.isOwned()) {
            throw new DOMException(DOMException.INUSE_ATTRIBUTE_ERR,
                                       "DOM009 Attribute already in use");
        }

        argn.ownerNode = ownerNode;
        argn.isOwned(true);

   	int i = findNamePoint(arg.getNodeName(),0);
    	AttrImpl previous = null;
    	if (i >= 0) {
            previous = (AttrImpl) nodes.elementAt(i);
            nodes.setElementAt(arg,i);
            previous.ownerNode = ownerNode.ownerDocument();
            previous.isOwned(false);
            previous.isSpecified(true);
    	} else {
            if (null == nodes) {
                nodes = new Vector(5, 10);
            }
            nodes.insertElementAt(arg, i);
        }

        ownerNode.ownerDocument().setAttrNode(argn, previous);

        if (!argn.isNormalized()) {
            ownerNode.isNormalized(false);
        }
        return previous;


    /**
     * Adds an attribute using its namespaceURI and localName.
     * @see org.w3c.dom.NamedNodeMap#setNamedItem
     * @return If the new Node replaces an existing node the replaced Node is
     *      returned, otherwise null is returned. 
     * @param arg A node to store in a named node map.
     */
    public Node setNamedItemNS(Node arg)
        throws DOMException {

    	if (isReadOnly()) {
            throw
                new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
                                     "DOM001 Modification not allowed");
        }
    
    	if(arg.getOwnerDocument() != ownerNode.ownerDocument()) {
            throw new DOMException(DOMException.WRONG_DOCUMENT_ERR,
                                       "DOM005 Wrong document");
        }

        if (arg.getNodeType() != arg.ATTRIBUTE_NODE) {
            throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, 
                                   "DOM006 Hierarchy request error");
        }
        AttrImpl argn = (AttrImpl)arg;
    	if (argn.isOwned()) {
            throw new DOMException(DOMException.INUSE_ATTRIBUTE_ERR,
                                       "DOM009 Attribute already in use");
        }

        argn.ownerNode = ownerNode;
        argn.isOwned(true);

    	int i = findNamePoint(argn.getNamespaceURI(), argn.getLocalName());
    	AttrImpl previous = null;
    	if (i >= 0) {
            previous = (AttrImpl) nodes.elementAt(i);
            nodes.setElementAt(arg,i);
            previous.ownerNode = ownerNode.ownerDocument();
            previous.isOwned(false);
            previous.isSpecified(true);
    	} else {
    	    i = findNamePoint(arg.getNodeName(),0);
            if (i >=0) {
                previous = (AttrImpl) nodes.elementAt(i);
                nodes.insertElementAt(arg,i);
            } else {
                if (null == nodes) {
                    nodes = new Vector(5, 10);
                }
                nodes.insertElementAt(arg, i);
            }
        }

        ownerNode.ownerDocument().setAttrNode(argn, previous);

        if (!argn.isNormalized()) {
            ownerNode.isNormalized(false);
        }
        return previous;

   
    /**
     * Removes an attribute specified by name.
     * @param name
     *      The name of a node to remove. If the
     *      removed attribute is known to have a default value, an
     *      attribute immediately appears containing the default value
     *      as well as the corresponding namespace URI, local name,
     *      and prefix when applicable.
     * @return The node removed from the map if a node with such a name exists.
     * @throws              NOT_FOUND_ERR: Raised if there is no node named
     *                      name in the map.
     */
    /***/
    public Node removeNamedItem(String name)
        throws DOMException {
        return internalRemoveNamedItem(name, true);
    }

    /**
     * Same as removeNamedItem except that it simply returns null if the
     * specified name is not found.
     */
    Node safeRemoveNamedItem(String name) {
        return internalRemoveNamedItem(name, false);
    }

    /**
     * Internal removeNamedItem method allowing to specify whether an exception
     * must be thrown if the specified name is not found.
     */
    final protected Node internalRemoveNamedItem(String name, boolean raiseEx){
    	if (isReadOnly()) {
            throw
                new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
                                     "DOM001 Modification not allowed");
        }
    	int i = findNamePoint(name,0);
    	if (i < 0) {
            if (raiseEx) {
                throw new DOMException(DOMException.NOT_FOUND_ERR,
                                           "DOM008 Not found");
            } else {
                return null;
            }
        }

        AttrImpl n = (AttrImpl)nodes.elementAt(i);
        CoreDocumentImpl ownerDocument = ownerNode.ownerDocument();

        if (hasDefaults()) {
            NamedNodeMapImpl defaults =
                ((ElementImpl) ownerNode).getDefaultAttributes();
            Node d;
            if (defaults != null && (d = defaults.getNamedItem(name)) != null
                && findNamePoint(name, i+1) < 0) {

                NodeImpl clone = (NodeImpl)d.cloneNode(true);
                clone.ownerNode = ownerNode;
                clone.isOwned(true);
                clone.isSpecified(false);
                nodes.setElementAt(clone, i);
            } else {
                nodes.removeElementAt(i);
            }
        } else {
            nodes.removeElementAt(i);
        }


        n.ownerNode = ownerDocument;
        n.isOwned(false);
        n.isSpecified(true);

        ownerDocument.removedAttrNode(n, ownerNode, name);

        return n;

    
    /**
     * Introduced in DOM Level 2. <p>
     * Removes an attribute specified by local name and namespace URI.
     * @param namespaceURI
     *                      The namespace URI of the node to remove.
     *                      When it is null or an empty string, this
     *                      method behaves like removeNamedItem.
     * @param               The local name of the node to remove. If the
     *                      removed attribute is known to have a default
     *                      value, an attribute immediately appears
     *                      containing the default value.
     * @return Node         The node removed from the map if a node with such
     *                      a local name and namespace URI exists.
     * @throws              NOT_FOUND_ERR: Raised if there is no node named
     *                      name in the map.
     */
    public Node removeNamedItemNS(String namespaceURI, String name)
        throws DOMException {
        return internalRemoveNamedItemNS(namespaceURI, name, true);
    }

    /**
     * Same as removeNamedItem except that it simply returns null if the
     * specified local name and namespace URI is not found.
     */
    Node safeRemoveNamedItemNS(String namespaceURI, String name) {
        return internalRemoveNamedItemNS(namespaceURI, name, false);
    }

    /**
     * Internal removeNamedItemNS method allowing to specify whether an
     * exception must be thrown if the specified local name and namespace URI
     * is not found.
     */
    final protected Node internalRemoveNamedItemNS(String namespaceURI,
                                                   String name,
                                                   boolean raiseEx) {
    	if (isReadOnly()) {
            throw
                new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
                                     "DOM001 Modification not allowed");
        }
    	int i = findNamePoint(namespaceURI, name);
    	if (i < 0) {
            if (raiseEx) {
                throw new DOMException(DOMException.NOT_FOUND_ERR,
                                           "DOM008 Not found");
            } else {
                return null;
            }
        }

        AttrImpl n = (AttrImpl)nodes.elementAt(i);
        CoreDocumentImpl ownerDocument = ownerNode.ownerDocument();

        String nodeName = n.getNodeName();
        if (hasDefaults()) {
            NamedNodeMapImpl defaults =
                ((ElementImpl) ownerNode).getDefaultAttributes();
            Node d;
            if (defaults != null
                && (d = defaults.getNamedItem(nodeName)) != null)
                {
                    int j = findNamePoint(nodeName,0);
                    if (j>=0 && findNamePoint(nodeName, j+1) < 0) {
                        NodeImpl clone = (NodeImpl)d.cloneNode(true);
                        clone.ownerNode = ownerNode;
                        if (clone instanceof AttrNSImpl) {
                            ((AttrNSImpl)clone).namespaceURI = namespaceURI;
                        }
                        clone.isOwned(true);
                        clone.isSpecified(false);
                        nodes.setElementAt(clone, i);
                    } else {
                        nodes.removeElementAt(i);
                    }
                } else {
                    nodes.removeElementAt(i);
                }
        } else {
            nodes.removeElementAt(i);
        }


        n.ownerNode = ownerDocument;
        n.isOwned(false);
        n.isSpecified(true);

        ownerDocument.removedAttrNode(n, ownerNode, name);

        return n;



    /**
     * Cloning a NamedNodeMap is a DEEP OPERATION; it always clones
     * all the nodes contained in the map.
     */
     
    public NamedNodeMapImpl cloneMap(NodeImpl ownerNode) {
    	AttributeMap newmap =
            new AttributeMap((ElementImpl) ownerNode, null);
        newmap.hasDefaults(hasDefaults());
        newmap.cloneContent(this);
    	return newmap;

    /**
     * Override parent's method to set the ownerNode correctly
     */
    protected void cloneContent(NamedNodeMapImpl srcmap) {
    	if (srcmap.nodes != null) {
            if (nodes == null) {
                nodes = new Vector(srcmap.nodes.size());
            }
            else {
                nodes.setSize(srcmap.nodes.size());
            }
            for (int i = 0; i < srcmap.nodes.size(); ++i) {
                NodeImpl n = (NodeImpl) srcmap.nodes.elementAt(i);
                NodeImpl clone = (NodeImpl) n.cloneNode(true);
                clone.isSpecified(n.isSpecified());
                nodes.insertElementAt(clone, i);
                clone.ownerNode = ownerNode;
                clone.isOwned(true);
            }
        }


    /**
     * Get this AttributeMap in sync with the given "defaults" map.
     * @param defaults The default attributes map to sync with.
     */
    protected void reconcileDefaults(NamedNodeMapImpl defaults) {
        
        int nsize = (nodes != null) ? nodes.size() : 0;
        for (int i = nsize - 1; i >= 0; i--) {
            AttrImpl attr = (AttrImpl) nodes.elementAt(i);
            if (!attr.isSpecified()) {
                attr.ownerNode = ownerNode.ownerDocument();
                attr.isOwned(false);
                attr.isSpecified(true);
                nodes.removeElementAt(i);
            }
        }
    	if (defaults == null) {
            return;
        }
        if (nodes == null || nodes.size() == 0) {
            cloneContent(defaults);
        }
        else {
            int dsize = defaults.nodes.size();
            for (int n = 0; n < dsize; n++) {
                AttrImpl d = (AttrImpl) defaults.nodes.elementAt(n);
                int i = findNamePoint(d.getNodeName(), 0);
                if (i < 0) {
                    NodeImpl clone = (NodeImpl) d.cloneNode(true);
                    clone.ownerNode = ownerNode;
                    clone.isOwned(true);
                    clone.isSpecified(false);
                    nodes.setElementAt(clone, i);
                }
            }
        }


