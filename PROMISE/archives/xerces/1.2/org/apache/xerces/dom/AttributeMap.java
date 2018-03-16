package org.apache.xerces.dom;

import java.io.*;
import java.util.Vector;
import java.util.Enumeration;

import org.w3c.dom.*;

import org.apache.xerces.dom.events.MutationEventImpl;
import org.w3c.dom.events.*;

/**
 * AttributeMap inherits from NamedNodeMapImpl and extends it to deal with the
 * specifics of storing attributes. These are:
 * <ul>
 *  <li>managing ownership of attribute nodes
 *  <li>managing default attributes
 *  <li>firing mutation events
 * </ul>
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
                new DOMExceptionImpl(DOMException.NO_MODIFICATION_ALLOWED_ERR,
                                     "DOM001 Modification not allowed");
        }
    	if(arg.getOwnerDocument() != ownerNode.ownerDocument()) {
            throw new DOMExceptionImpl(DOMException.WRONG_DOCUMENT_ERR,
                                       "DOM005 Wrong document");
        }

        NodeImpl argn = (NodeImpl)arg;

    	if (argn.isOwned()) {
            throw new DOMExceptionImpl(DOMException.INUSE_ATTRIBUTE_ERR,
                                       "DOM009 Attribute already in use");
        }

        argn.ownerNode = ownerNode;
        argn.isOwned(true);

   	int i = findNamePoint(arg.getNodeName(),0);
    	NodeImpl previous = null;
    	if (i >= 0) {
            previous = (NodeImpl) nodes.elementAt(i);
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

        if (NodeImpl.MUTATIONEVENTS &&
            ownerNode.ownerDocument().mutationEvents) {
            ownerNode.dispatchAggregateEvents(
                (AttrImpl)arg,
                previous==null ? null : previous.getNodeValue()
                );
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
                new DOMExceptionImpl(DOMException.NO_MODIFICATION_ALLOWED_ERR,
                                     "DOM001 Modification not allowed");
        }
    
    	if(arg.getOwnerDocument() != ownerNode.ownerDocument()) {
            throw new DOMExceptionImpl(DOMException.WRONG_DOCUMENT_ERR,
                                       "DOM005 Wrong document");
        }

        NodeImpl argn = (NodeImpl)arg;
    	if (argn.isOwned()) {
            throw new DOMExceptionImpl(DOMException.INUSE_ATTRIBUTE_ERR,
                                       "DOM009 Attribute already in use");
        }

        argn.ownerNode = ownerNode;
        argn.isOwned(true);

    	int i = findNamePoint(argn.getNamespaceURI(), argn.getLocalName());
    	NodeImpl previous = null;
    	if (i >= 0) {
            previous = (NodeImpl) nodes.elementAt(i);
            nodes.setElementAt(arg,i);
            previous.ownerNode = ownerNode.ownerDocument();
            previous.isOwned(false);
            previous.isSpecified(true);
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

        if (NodeImpl.MUTATIONEVENTS
            && ownerNode.ownerDocument().mutationEvents)
        {
            ownerNode.dispatchAggregateEvents(
                (AttrImpl)arg,
                previous==null ? null : previous.getNodeValue()
                );
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
                new DOMExceptionImpl(DOMException.NO_MODIFICATION_ALLOWED_ERR,
                                     "DOM001 Modification not allowed");
        }
    	int i = findNamePoint(name,0);
    	if (i < 0) {
            if (raiseEx) {
                throw new DOMExceptionImpl(DOMException.NOT_FOUND_ERR,
                                           "DOM008 Not found");
            } else {
                return null;
            }
        }

        NodeImpl n = (NodeImpl)nodes.elementAt(i);
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


        n.ownerNode = ownerNode.ownerDocument();
        n.isOwned(false);
        n.isSpecified(true);
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
                new DOMExceptionImpl(DOMException.NO_MODIFICATION_ALLOWED_ERR,
                                     "DOM001 Modification not allowed");
        }
    	int i = findNamePoint(namespaceURI, name);
    	if (i < 0) {
            if (raiseEx) {
                throw new DOMExceptionImpl(DOMException.NOT_FOUND_ERR,
                                           "DOM008 Not found");
            } else {
                return null;
            }
        }

        LCount lc=null;
        String oldvalue="";
        AttrImpl enclosingAttribute=null;
        if (NodeImpl.MUTATIONEVENTS
            && ownerNode.ownerDocument().mutationEvents)
        {
            lc=LCount.lookup(MutationEventImpl.DOM_ATTR_MODIFIED);
            if(lc.captures+lc.bubbles+lc.defaults>0)
            {
               enclosingAttribute=(AttrImpl)(nodes.elementAt(i));
               oldvalue=enclosingAttribute.getNodeValue();
            }

        NodeImpl n = (NodeImpl)nodes.elementAt(i);
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


        n.ownerNode = ownerNode.ownerDocument();
        n.isOwned(false);
        n.isSpecified(true);

        if(NodeImpl.MUTATIONEVENTS && ownerNode.ownerDocument().mutationEvents)
        {
            if(lc.captures+lc.bubbles+lc.defaults>0) {
                MutationEvent me= new MutationEventImpl();
                me.initMutationEvent(MutationEventImpl.DOM_ATTR_MODIFIED,
                                     true, false,
                                     null, n.getNodeValue(),
                             ((ElementImpl)ownerNode).getAttribute(name),name);
                ownerNode.dispatchEvent(me);
            }

            ownerNode.dispatchAggregateEvents(null,null);
        }
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
     * Subroutine: If this NamedNodeMap is backed by a "defaults" map (eg,
     * if it's being used for Attributes of an XML file validated against
     * a DTD), we need to deal with the risk that those defaults might
     * have changed. Entries may have been added, changed, or removed, and
     * if so we need to update our version of that information
     * <P>
     * Luckily, this currently applies _only_ to Attributes, which have a
     * "specified" flag that allows us to distinguish which we set manually
     * versus which were defaults... assuming that the defaults list is being
     * maintained properly, of course.
     * <P>
     * Also luckily, The NameNodeMaps are maintained as 
     * sorted lists. This should keep the cost of convolving the two lists
     * managable... not wonderful, but at least more like 2N than N**2..
     * <P>
     * Finally, to avoid doing the convolution except when there are actually
     * changes to be absorbed, I've made the Map aware of whether or not
     * its defaults Map has changed. This is not 110% reliable, but it should
     * work under normal circumstances, especially since the DTD is usually
     * relatively static information.
     * <P>
     * Note: This is NON-DOM implementation, though used to support behavior
     * that the DOM requires.
     */

/** COMMENTED OUT!!!!!!! ********
   Doing this dynamically is a killer, since editing the DTD isn't even
   supported this is commented out at least for now. In the long run it seems
   better to update the document on user's demand after the DTD has been
   changed rather than doing this anyway.

    protected void reconcileDefaults() {
        
        if (DEBUG) System.out.println("reconcileDefaults:");
    	if (defaults != null && changed()) {

    		int n = 0;
            int d = 0;
            int nsize = (nodes != null) ? nodes.size() : 0;
            int dsize = defaults.nodes.size();

    		AttrImpl nnode = (nsize == 0) ? null : (AttrImpl) nodes.elementAt(0);
    		AttrImpl dnode = (dsize == 0) ? null : (AttrImpl) defaults.nodes.elementAt(0);

    		while (n < nsize && d < dsize) {
    			nnode = (AttrImpl) nodes.elementAt(n);
    			dnode = (AttrImpl) defaults.nodes.elementAt(d);
    			if (DEBUG) {
    			System.out.println("\n\nnnode="+nnode.getNodeName());
    			System.out.println("dnode="+dnode.getNodeName());
    			}
    			
    			
    			int test = nnode.getNodeName().compareTo(dnode.getNodeName());

    			if (test == 0) {
    			    if (!nnode.getSpecified()) {
    			        if (DEBUG) System.out.println("reconcile (test==0, specified = false): clone default");
                        NodeImpl clone = (NodeImpl)dnode.cloneNode(true);
                        clone.ownerNode = ownerNode;
                        clone.isOwned(true);
    				    nodes.setElementAt(clone, n);
    				    ++n;
    				    ++d;
    			    }
    			        if (DEBUG)
                                    System.out.println("reconcile (test==0, specified=true): just increment");
    				    ++n;
    				    ++d;
    			    }
    			}

    			else if (test > 0) {
    			    if (DEBUG) System.out.println("reconcile (test>0): insert new default");
                    NodeImpl clone = (NodeImpl)dnode.cloneNode(true);
                    clone.ownerNode = ownerNode;
                    clone.isOwned(true);
    				nodes.insertElementAt(clone, n);
    				++n;
    				++d;
    			}

    			else if (!nnode.getSpecified()) {
    			    if (DEBUG) System.out.println("reconcile(!specified): remove old default:"
    			    +nnode.getNodeName());
    				nodes.removeElementAt(n);
    			}

                else {
    			    if (DEBUG) System.out.println("reconcile: Different name else accept it.");
    				++n;
                }
        	}

            if (d < dsize) {
                if (nodes == null) nodes = new Vector();
                while (d < dsize) {
                    dnode = (AttrImpl)defaults.nodes.elementAt(d++);
                    NodeImpl clone = (NodeImpl)dnode.cloneNode(true);
                    clone.ownerNode = ownerNode;
                    clone.isOwned(true);
    			    if (DEBUG) System.out.println("reconcile: adding"+clone);
                    nodes.addElement(clone);
                }
            }
            changed(false);
    	}
**/

