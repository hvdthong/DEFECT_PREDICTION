package org.apache.xerces.dom;

import java.io.*;
import java.util.Vector;
import java.util.Enumeration;

import org.w3c.dom.*;

/**
 * NamedNodeMaps represent collections of Nodes that can be accessed
 * by name. Entity and Notation nodes are stored in NamedNodeMaps
 * attached to the DocumentType. Attributes are placed in a NamedNodeMap
 * attached to the elem they're related too. However, because attributes
 * require more work, such as firing mutation events, they are stored in
 * a subclass of NamedNodeMapImpl.
 * <P>
 * Only one Node may be stored per name; attempting to
 * store another will replace the previous value.
 * <P>
 * NOTE: The "primary" storage key is taken from the NodeName attribute of the
 * node. The "secondary" storage key is the namespaceURI and localName, when
 * accessed by DOM level 2 nodes. All nodes, even DOM Level 2 nodes are stored
 * in a single Vector sorted by the primary "nodename" key.
 * <P>
 * NOTE: item()'s integer index does _not_ imply that the named nodes
 * must be stored in an array; that's only an access method. Note too
 * that these indices are "live"; if someone changes the map's
 * contents, the indices associated with nodes may change.
 * <P>
 *
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public class NamedNodeMapImpl
    implements NamedNodeMap, Serializable {


    /** Serialization version. */
    static final long serialVersionUID = -7039242451046758020L;


    protected short flags;

    protected final static short READONLY     = 0x1<<0;
    protected final static short CHANGED      = 0x1<<1;
    protected final static short HASDEFAULTS  = 0x1<<2;

    /** Nodes. */
    protected Vector nodes;



    /** Constructs a named node map. */
    protected NamedNodeMapImpl(NodeImpl ownerNode) {
        this.ownerNode = ownerNode;
    }


    /**
     * Report how many nodes are currently stored in this NamedNodeMap.
     * Caveat: This is a count rather than an index, so the
     * highest-numbered node at any time can be accessed via
     * item(getLength()-1).
     */
    public int getLength() {
    	return (nodes != null) ? nodes.size() : 0;
    }

    /**
     * Retrieve an item from the map by 0-based index.
     *
     * @param index Which item to retrieve. Note that indices are just an
     * enumeration of the current contents; they aren't guaranteed to be
     * stable, nor do they imply any promises about the order of the
     * NamedNodeMap's contents. In other words, DO NOT assume either that
     * index(i) will always refer to the same entry, or that there is any
     * stable ordering of entries... and be prepared for double-reporting
     * or skips as insertion and deletion occur.
     *
     * @returns the node which currenly has the specified index, or null
     * if index is greater than or equal to getLength().
     */
    public Node item(int index) {
    	return (nodes != null && index < nodes.size()) ?
                    (Node)(nodes.elementAt(index)) : null;
    }

    /**
     * Retrieve a node by name.
     *
     * @param name Name of a node to look up.
     * @returns the Node (of unspecified sub-class) stored with that name,
     * or null if no value has been assigned to that name.
     */
    public Node getNamedItem(String name) {

    	int i = findNamePoint(name,0);
        return (i < 0) ? null : (Node)(nodes.elementAt(i));


    /**
     * Introduced in DOM Level 2. <p>
     * Retrieves a node specified by local name and namespace URI.
     *
     * @param namespaceURI  The namespace URI of the node to retrieve.
     *                      When it is null or an empty string, this
     *                      method behaves like getNamedItem.
     * @param localName     The local name of the node to retrieve.
     * @return Node         A Node (of any type) with the specified name, or null if the specified
     *                      name did not identify any node in the map.
     */
    public Node getNamedItemNS(String namespaceURI, String localName) {

    	int i = findNamePoint(namespaceURI, localName);
        return (i < 0) ? null : (Node)(nodes.elementAt(i));


    /**
     * Adds a node using its nodeName attribute.
     * As the nodeName attribute is used to derive the name which the node must be
     * stored under, multiple nodes of certain types (those that have a "special" string
     * value) cannot be stored as the names would clash. This is seen as preferable to
     * allowing nodes to be aliased.
     * @see org.w3c.dom.NamedNodeMap#setNamedItem
     * @return If the new Node replaces an existing node the replaced Node is returned,
     *      otherwise null is returned. 
     * @param arg 
     *      A node to store in a named node map. The node will later be
     *      accessible using the value of the namespaceURI and localName
     *      attribute of the node. If a node with those namespace URI and
     *      local name is already present in the map, it is replaced by the new
     *      one.
     * @exception org.w3c.dom.DOMException The exception description.
     */
    public Node setNamedItem(Node arg)
        throws DOMException {

    	if (isReadOnly()) {
            throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
                                       "DOM001 Modification not allowed");
        }
    	if (arg.getOwnerDocument() != ownerNode.ownerDocument()) {
            throw new DOMException(DOMException.WRONG_DOCUMENT_ERR,
                                       "DOM005 Wrong document");
        }

   	int i = findNamePoint(arg.getNodeName(),0);
    	NodeImpl previous = null;
    	if (i >= 0) {
            previous = (NodeImpl) nodes.elementAt(i);
            nodes.setElementAt(arg,i);
    	} else {
            if (null == nodes) {
                nodes = new Vector(5, 10);
            }
            nodes.insertElementAt(arg, i);
        }
    	return previous;


    /**
     * Adds a node using its namespaceURI and localName.
     * @see org.w3c.dom.NamedNodeMap#setNamedItem
     * @return If the new Node replaces an existing node the replaced Node is returned,
     *      otherwise null is returned. 
     * @param arg A node to store in a named node map. The node will later be
     *      accessible using the value of the namespaceURI and localName
     *      attribute of the node. If a node with those namespace URI and
     *      local name is already present in the map, it is replaced by the new
     *      one.
     */
    public Node setNamedItemNS(Node arg)
        throws DOMException {

    	if (isReadOnly()) {
            throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
                                       "DOM001 Modification not allowed");
        }
    
    	if(arg.getOwnerDocument() != ownerNode.ownerDocument()) {
            throw new DOMException(DOMException.WRONG_DOCUMENT_ERR,
                                       "DOM005 Wrong document");
        }

    	int i = findNamePoint(arg.getNamespaceURI(), arg.getLocalName());
    	NodeImpl previous = null;
    	if (i >= 0) {
            previous = (NodeImpl) nodes.elementAt(i);
            nodes.setElementAt(arg,i);
    	} else {
    	    i = findNamePoint(arg.getNodeName(),0);
            if (i >=0) {
                previous = (NodeImpl) nodes.elementAt(i);
                nodes.insertElementAt(arg,i);
            } else {
                if (null == nodes) {
                    nodes = new Vector(5, 10);
                }
                nodes.insertElementAt(arg, i);
            }
        }
    	return previous;

   
    /**
     * Removes a node specified by name.
     * @param name The name of a node to remove.
     * @return The node removed from the map if a node with such a name exists.
     */
    /***/
    public Node removeNamedItem(String name)
        throws DOMException {

    	if (isReadOnly()) {
            throw
                new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
                                     "DOM001 Modification not allowed");
        }
    	int i = findNamePoint(name,0);
    	if (i < 0) {
            throw new DOMException(DOMException.NOT_FOUND_ERR,
                                       "DOM008 Not found");
        }

        NodeImpl n = (NodeImpl)nodes.elementAt(i);
        nodes.removeElementAt(i);

        return n;

    
    /**
     * Introduced in DOM Level 2. <p>
     * Removes a node specified by local name and namespace URI.
     * @param namespaceURI
     *                      The namespace URI of the node to remove.
     *                      When it is null or an empty string, this
     *                      method behaves like removeNamedItem.
     * @param               The local name of the node to remove.
     * @return Node         The node removed from the map if a node with such
     *                      a local name and namespace URI exists.
     * @throws              NOT_FOUND_ERR: Raised if there is no node named
     *                      name in the map.

     */
     public Node removeNamedItemNS(String namespaceURI, String name)
        throws DOMException {

    	if (isReadOnly()) {
            throw
                new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
                                     "DOM001 Modification not allowed");
        }
    	int i = findNamePoint(namespaceURI, name);
    	if (i < 0) {
            throw new DOMException(DOMException.NOT_FOUND_ERR,
                                       "DOM008 Not found");
        }

        NodeImpl n = (NodeImpl)nodes.elementAt(i);
        nodes.removeElementAt(i);

        return n;



    /**
     * Cloning a NamedNodeMap is a DEEP OPERATION; it always clones
     * all the nodes contained in the map.
     */
     
    public NamedNodeMapImpl cloneMap(NodeImpl ownerNode) {
    	NamedNodeMapImpl newmap = new NamedNodeMapImpl(ownerNode);
        newmap.cloneContent(this);
    	return newmap;
    }

    protected void cloneContent(NamedNodeMapImpl srcmap) {
    	if (srcmap.nodes != null) {
            nodes = new Vector(srcmap.nodes.size());
            for (int i = 0; i < srcmap.nodes.size(); ++i) {
                NodeImpl n = (NodeImpl) srcmap.nodes.elementAt(i);
                NodeImpl clone = (NodeImpl) n.cloneNode(true);
                clone.isSpecified(n.isSpecified());
                nodes.insertElementAt(clone, i);
            }
        }


    /**
     * Internal subroutine to allow read-only Nodes to make their contained
     * NamedNodeMaps readonly too. I expect that in fact the shallow
     * version of this operation will never be
     *
     * @param readOnly boolean true to make read-only, false to permit editing.
     * @param deep boolean true to pass this request along to the contained
     * nodes, false to only toggle the NamedNodeMap itself. I expect that
     * the shallow version of this operation will never be used, but I want
     * to design it in now, while I'm thinking about it.
     */
    void setReadOnly(boolean readOnly, boolean deep) {

        isReadOnly(readOnly);
    	if(deep && nodes != null) {
    		Enumeration e=nodes.elements();
    		while(e.hasMoreElements()) {
    			((NodeImpl)e.nextElement()).setReadOnly(readOnly,deep);
            }
    	}

    
    /**
     * Internal subroutine returns this NodeNameMap's (shallow) readOnly value.
     *
     */
    boolean getReadOnly() {
    	return isReadOnly();
    


    /**
     * NON-DOM
     * set the ownerDocument of this node, and the attributes it contains
     */
    void setOwnerDocument(DocumentImpl doc) {
        if (nodes != null) {
            for (int i = 0; i < nodes.size(); i++) {
                ((NodeImpl)item(i)).setOwnerDocument(doc);
            }
        }
    }

    final boolean isReadOnly() {
        return (flags & READONLY) != 0;
    }

    final void isReadOnly(boolean value) {
        flags = (short) (value ? flags | READONLY : flags & ~READONLY);
    }

    final boolean changed() {
        return (flags & CHANGED) != 0;
    }

    final void changed(boolean value) {
        flags = (short) (value ? flags | CHANGED : flags & ~CHANGED);
    }

    final boolean hasDefaults() {
        return (flags & HASDEFAULTS) != 0;
    }

    final void hasDefaults(boolean value) {
        flags = (short) (value ? flags | HASDEFAULTS : flags & ~HASDEFAULTS);
    }


    /**
     * Subroutine: Locate the named item, or the point at which said item
     * should be added. 
     *
     * @param name Name of a node to look up.
     *
     * @return If positive or zero, the index of the found item.
     * If negative, index of the appropriate point at which to insert
     * the item, encoded as -1-index and hence reconvertable by subtracting
     * it from -1. (Encoding because I don't want to recompare the strings
     * but don't want to burn bytes on a datatype to hold a flagged value.)
     */
    protected int findNamePoint(String name, int start) {

    	int i = 0;
    	if(nodes != null) {
    		int first = start;
            int last  = nodes.size() - 1;

    		while (first <= last) {
    			i = (first + last) / 2;
    			int test = name.compareTo(((Node)(nodes.elementAt(i))).getNodeName());
    			if(test == 0) {
                }
    			else if (test < 0) {
    				last = i - 1;
                }
    			else {
    				first = i + 1;
                }
    		}

    		if (first > i) {
                i = first;
            }
    	}



    
    /** This findNamePoint is for DOM Level 2 Namespaces.
     */
    protected int findNamePoint(String namespaceURI, String name) {
        
        if (nodes == null) return -1;
        if (name == null) return -1;
        
        for (int i = 0; i < nodes.size(); i++) {
            NodeImpl a = (NodeImpl)nodes.elementAt(i);
            String aNamespaceURI = a.getNamespaceURI();
            String aLocalName = a.getLocalName();
            if (namespaceURI == null) {
              if (aNamespaceURI == null
                  &&
                  (name.equals(aLocalName)
                   ||
                   (aLocalName == null && name.equals(a.getNodeName()))))
                return i;
            } else {
              if (namespaceURI.equals(aNamespaceURI)
                  &&
                  name.equals(aLocalName))
                return i;
            }
        }
        return -1;
    }
    

