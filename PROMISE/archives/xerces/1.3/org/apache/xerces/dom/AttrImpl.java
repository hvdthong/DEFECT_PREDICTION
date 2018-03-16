package org.apache.xerces.dom;

import java.io.*;

import org.w3c.dom.*;
import org.w3c.dom.events.MutationEvent;
import org.apache.xerces.dom.events.MutationEventImpl;

/**
 * Attribute represents an XML-style attribute of an
 * Element. Typically, the allowable values are controlled by its
 * declaration in the Document Type Definition (DTD) governing this
 * kind of document.
 * <P>
 * If the attribute has not been explicitly assigned a value, but has
 * been declared in the DTD, it will exist and have that default. Only
 * if neither the document nor the DTD specifies a value will the
 * Attribute really be considered absent and have no value; in that
 * case, querying the attribute will return null.
 * <P>
 * Attributes may have multiple children that contain their data. (XML
 * allows attributes to contain entity references, and tokenized
 * attribute types such as NMTOKENS may have a child for each token.)
 * For convenience, the Attribute object's getValue() method returns
 * the string version of the attribute's value.
 * <P>
 * Attributes are not children of the Elements they belong to, in the
 * usual sense, and have no valid Parent reference. However, the spec
 * says they _do_ belong to a specific Element, and an INUSE exception
 * is to be thrown if the user attempts to explicitly share them
 * between elements.
 * <P>
 * Note that Elements do not permit attributes to appear to be shared
 * (see the INUSE exception), so this object's mutability is
 * officially not an issue.
 * <p>
 * Note: The ownerNode attribute is used to store the Element the Attr
 * node is associated with. Attr nodes do not have parent nodes.
 * Besides, the getOwnerElement() method can be used to get the element node
 * this attribute is associated with.
 * <P>
 * AttrImpl does not support Namespaces. AttrNSImpl, which inherits from
 * it, does.
 *
 * <p>AttrImpl used to inherit from ParentNode. It now directly inherits from
 * NodeImpl and provide its own implementation of the ParentNode's behavior.
 * The reason is that we now try and avoid to always creating a Text node to
 * hold the value of an attribute. The DOM spec requires it, so we still have
 * to do it in case getFirstChild() is called for instance. The reason
 * attribute values are stored as a list of nodes is so that they can carry
 * more than a simple string. They can also contain EntityReference nodes.
 * However, most of the times people only have a single string that they only
 * set and get through Element.set/getAttribute or Attr.set/getValue. In this
 * new version, the Attr node has a value pointer which can either be the
 * String directly or a pointer to the first ChildNode. A flag tells which one
 * it currently is. Note that while we try to stick with the direct String as
 * much as possible once we've switched to a node there is no going back. This
 * is because we have no way to know whether the application keeps referring to
 * the node we once returned.
 * <p> The gain in memory varies on the density of attributes in the document.
 * But in the tests I've run I've seen up to 12% of memory gain. And the good
 * thing is that it also leads to a slight gain in speed because we allocate
 * fewer objects! I mean, that's until we have to actually create the node...
 * <p>
 * To avoid too much duplicated code, I got rid of ParentNode and renamed
 * ChildAndParentNode, which I never really liked, to ParentNode for
 * simplicity, this doesn't make much of a difference in memory usage because
 * there are only very objects that are only a Parent. This is only true now
 * because AttrImpl now inherits directly from NodeImpl and has its own
 * implementation of the ParentNode's node behavior. So there is still some
 * duplicated code there.
 *
 * <p><b>WARNING</b>: Some of the code here is partially duplicated in
 * ParentNode, be careful to keep these two classes in sync!
 *
 * @see AttrNSImpl
 *
 * @author Arnaud  Le Hors, IBM
 * @author Joe Kesselman, IBM
 * @author Andy Clark, IBM
 * @version
 * @since PR-DOM-Level-1-19980818.
 *
 */
public class AttrImpl
    extends NodeImpl
    implements Attr {


    /** Serialization version. */
    static final long serialVersionUID = 7277707688218972102L;


    /** This can either be a String or the first child node. */
    protected Object value = null;

    /** Attribute name. */
    protected String name;

    protected static TextImpl textNode = null;


    /**
     * Attribute has no public constructor. Please use the factory
     * method in the Document class.
     */
    protected AttrImpl(DocumentImpl ownerDocument, String name) {
    	super(ownerDocument);
        this.name = name;
        /** False for default attributes. */
        isSpecified(true);
        hasStringValue(true);
    }

    protected AttrImpl() {}

    protected void makeChildNode() {
        if (hasStringValue()) {
            if (value != null) {
                TextImpl text =
                    (TextImpl) ownerDocument().createTextNode((String) value);
                value = text;
                text.isFirstChild(true);
                text.previousSibling = text;
                text.ownerNode = this;
                text.isOwned(true);
            }
            hasStringValue(false);
        }
    }

    
    public Node cloneNode(boolean deep) {
        AttrImpl clone = (AttrImpl) super.cloneNode(deep);

    	if (!clone.hasStringValue()) {

            clone.value = null;

            if (deep) {
                for (Node child = (Node) value; child != null;
                     child = child.getNextSibling()) {
                    clone.appendChild(child.cloneNode(true));
                }
            }
        }
        clone.isSpecified(true);
        return clone;
    }

    /**
     * A short integer indicating what type of node this is. The named
     * constants for this value are defined in the org.w3c.dom.Node interface.
     */
    public short getNodeType() {
        return Node.ATTRIBUTE_NODE;
    }

    /**
     * Returns the attribute name
     */
    public String getNodeName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return name;
    }

    /**
     * Implicit in the rerouting of getNodeValue to getValue is the
     * need to redefine setNodeValue, for symmetry's sake.  Note that
     * since we're explicitly providing a value, Specified should be set
     * true.... even if that value equals the default.
     */
    public void setNodeValue(String value) throws DOMException {
    	setValue(value);
    }

    /**
     * In Attribute objects, NodeValue is considered a synonym for
     * Value.
     *
     * @see #getValue()
     */
    public String getNodeValue() {
    	return getValue();
    }


    /**
     * In Attributes, NodeName is considered a synonym for the
     * attribute's Name
     */
    public String getName() {

        if (needsSyncData()) {
            synchronizeData();
        }
    	return name;


    /**
     * The DOM doesn't clearly define what setValue(null) means. I've taken it
     * as "remove all children", which from outside should appear
     * similar to setting it to the empty string.
     */
    public void setValue(String newvalue) {

    	if (isReadOnly()) {
            throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, 
                                   "DOM001 Modification not allowed");
        }
    		
        LCount lc=null;
        String oldvalue="";
        DocumentImpl ownerDocument = ownerDocument();
        if(MUTATIONEVENTS && ownerDocument.mutationEvents)
        {
            lc=LCount.lookup(MutationEventImpl.DOM_ATTR_MODIFIED);
            if(lc.captures+lc.bubbles+lc.defaults>0 && ownerNode!=null)
            {
               oldvalue=getValue();
            }
            

        if(MUTATIONEVENTS && ownerDocument.mutationEvents)
        {
            if (needsSyncChildren()) {
                synchronizeChildren();
            }
            if (value != null) {
                if (hasStringValue()) {
                    if (textNode == null) {
                        textNode = (TextImpl)
                            ownerDocument.createTextNode((String) value);
                    }
                    else {
                        textNode.data = (String) value;
                    }
                    value = textNode;
                    textNode.isFirstChild(true);
                    textNode.previousSibling = textNode;
                    textNode.ownerNode = this;
                    textNode.isOwned(true);
                    hasStringValue(false);
                    internalRemoveChild(textNode, MUTATION_LOCAL);
                }
                else {
                    while (value != null) {
                        internalRemoveChild((Node) value, MUTATION_LOCAL);
                    }
                }
            }
        }
        else
        {
            if (!hasStringValue() && value != null) {
                ChildNode firstChild = (ChildNode) value;
                firstChild.previousSibling = null;
                firstChild.isFirstChild(false);
            }
            value = null;
            needsSyncChildren(false);
        }

    	isSpecified(true);
        if (newvalue != null) {
            if(MUTATIONEVENTS && ownerDocument.mutationEvents) {
                internalInsertBefore(ownerDocument.createTextNode(newvalue),
                                     null, MUTATION_LOCAL);
                hasStringValue(false);
            } else {
                value = newvalue;
                hasStringValue(true);
            }
        }
		

        if(MUTATIONEVENTS && ownerDocument.mutationEvents)
        {
            dispatchAggregateEvents(this,oldvalue,MutationEvent.MODIFICATION);
        }
		

    /**
     * The "string value" of an Attribute is its text representation,
     * which in turn is a concatenation of the string values of its children.
     */
    public String getValue() {

        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        if (value == null) {
            return "";
        }
        if (hasStringValue()) {
            return (String) value;
        }
        ChildNode firstChild = ((ChildNode) value);
        ChildNode node = firstChild.nextSibling;
        if (node == null) {
            return firstChild.getNodeValue();
        }
    	StringBuffer value = new StringBuffer(firstChild.getNodeValue());
    	while (node != null) {
            value.append(node.getNodeValue());
            node = node.nextSibling;
    	}
    	return value.toString();


    /**
     * The "specified" flag is true if and only if this attribute's
     * value was explicitly specified in the original document. Note that
     * the implementation, not the user, is in charge of this
     * property. If the user asserts an Attribute value (even if it ends
     * up having the same value as the default), it is considered a
     * specified attribute. If you really want to revert to the default,
     * delete the attribute from the Element, and the Implementation will
     * re-assert the default (if any) in its place, with the appropriate
     * specified=false setting.
     */
    public boolean getSpecified() {

        if (needsSyncData()) {
            synchronizeData();
        }
    	return isSpecified();



    /**
     * Returns the element node that this attribute is associated with,
     * or null if the attribute has not been added to an element.
     *
     * @see #getOwnerElement
     *
     * @deprecated Previous working draft of DOM Level 2. New method
     *             is <tt>getOwnerElement()</tt>.
     */
    public Element getElement() {
        return (Element) (isOwned() ? ownerNode : null);
    }

    /**
     * Returns the element node that this attribute is associated with,
     * or null if the attribute has not been added to an element.
     *
     * @since WD-DOM-Level-2-19990719
     */
    public Element getOwnerElement() {
        return (Element) (isOwned() ? ownerNode : null);
    }
    
    public void normalize() {

        if (isNormalized() || hasStringValue())
            return;

        Node kid, next;
        ChildNode firstChild = (ChildNode)value;
        for (kid = firstChild; kid != null; kid = next) {
            next = kid.getNextSibling();

            if ( kid.getNodeType() == Node.TEXT_NODE )
            {
                if ( next!=null && next.getNodeType() == Node.TEXT_NODE )
                {
                    ((Text)kid).appendData(next.getNodeValue());
                    removeChild( next );
                }
                else
                {
                    if ( kid.getNodeValue().length()==0 )
                        removeChild( kid );
                }
            }
        }

        isNormalized(true);


    /** NON-DOM, for use by parser */
    public void setSpecified(boolean arg) {

        if (needsSyncData()) {
            synchronizeData();
        }
    	isSpecified(arg);



    /** NON-DOM method for debugging convenience */
    public String toString() {
    	return getName() + "=" + "\"" + getValue() + "\"";
    }

    /**
     * Test whether this node has any children. Convenience shorthand
     * for (Node.getFirstChild()!=null)
     */
    public boolean hasChildNodes() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return value != null;
    }

    /**
     * Obtain a NodeList enumerating all children of this node. If there
     * are none, an (initially) empty NodeList is returned.
     * <p>
     * NodeLists are "live"; as children are added/removed the NodeList
     * will immediately reflect those changes. Also, the NodeList refers
     * to the actual nodes, so changes to those nodes made via the DOM tree
     * will be reflected in the NodeList and vice versa.
     * <p>
     * In this implementation, Nodes implement the NodeList interface and
     * provide their own getChildNodes() support. Other DOMs may solve this
     * differently.
     */
    public NodeList getChildNodes() {

        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return this;


    /** The first child of this Node, or null if none. */
    public Node getFirstChild() {

        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        makeChildNode();
    	return (Node) value;


    /** The last child of this Node, or null if none. */
    public Node getLastChild() {

        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return lastChild();


    final ChildNode lastChild() {
        makeChildNode();
        return value != null ? ((ChildNode) value).previousSibling : null;
    }

    final void lastChild(ChildNode node) {
        if (value != null) {
            ((ChildNode) value).previousSibling = node;
        }
    }

    /**
     * Move one or more node(s) to our list of children. Note that this
     * implicitly removes them from their previous parent.
     *
     * @param newChild The Node to be moved to our subtree. As a
     * convenience feature, inserting a DocumentNode will instead insert
     * all its children.
     *
     * @param refChild Current child which newChild should be placed
     * immediately before. If refChild is null, the insertion occurs
     * after all existing Nodes, like appendChild().
     *
     * @returns newChild, in its new state (relocated, or emptied in the
     * case of DocumentNode.)
     *
     * @throws DOMException(HIERARCHY_REQUEST_ERR) if newChild is of a
     * type that shouldn't be a child of this node, or if newChild is an
     * ancestor of this node.
     *
     * @throws DOMException(WRONG_DOCUMENT_ERR) if newChild has a
     * different owner document than we do.
     *
     * @throws DOMException(NOT_FOUND_ERR) if refChild is not a child of
     * this node.
     *
     * @throws DOMException(NO_MODIFICATION_ALLOWED_ERR) if this node is
     * read-only.
     */
    public Node insertBefore(Node newChild, Node refChild) 
        throws DOMException {
        return internalInsertBefore(newChild,refChild,MUTATION_ALL);
     
    /** NON-DOM INTERNAL: Within DOM actions,we sometimes need to be able
     * to control which mutation events are spawned. This version of the
     * insertBefore operation allows us to do so. It is not intended
     * for use by application programs.
     */
    Node internalInsertBefore(Node newChild, Node refChild,int mutationMask) 
        throws DOMException {

        DocumentImpl ownerDocument = ownerDocument();
        boolean errorChecking = ownerDocument.errorChecking;

        if (newChild.getNodeType() == Node.DOCUMENT_FRAGMENT_NODE) {


            if (errorChecking) {
                     kid != null; kid = kid.getNextSibling()) {

                    if (!ownerDocument.isKidOK(this, kid)) {
                        throw new DOMException(
                                           DOMException.HIERARCHY_REQUEST_ERR, 
                                           "DOM006 Hierarchy request error");
                    }
                }
            }

            while (newChild.hasChildNodes()) {
                insertBefore(newChild.getFirstChild(), refChild);
            }
            return newChild;
        }

        if (newChild == refChild) {
            refChild = refChild.getNextSibling();
            removeChild(newChild);
            insertBefore(newChild, refChild);
            return newChild;
        }

        if (needsSyncChildren()) {
            synchronizeChildren();
        }

        if (errorChecking) {
            if (isReadOnly()) {
                throw new DOMException(
                                     DOMException.NO_MODIFICATION_ALLOWED_ERR, 
                                       "DOM001 Modification not allowed");
            }
            if (newChild.getOwnerDocument() != ownerDocument) {
                throw new DOMException(DOMException.WRONG_DOCUMENT_ERR, 
                                       "DOM005 Wrong document");
            }
            if (!ownerDocument.isKidOK(this, newChild)) {
                throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, 
                                       "DOM006 Hierarchy request error");
            }
            if (refChild != null && refChild.getParentNode() != this) {
                throw new DOMException(DOMException.NOT_FOUND_ERR,
                                       "DOM008 Not found");
            }

            boolean treeSafe = true;
            for (NodeImpl a = this; treeSafe && a != null; a = a.parentNode())
            {
                treeSafe = newChild != a;
            }
            if (!treeSafe) {
                throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, 
                                       "DOM006 Hierarchy request error");
            }
        }


        EnclosingAttr enclosingAttr=null;
        if (MUTATIONEVENTS && ownerDocument.mutationEvents
            && (mutationMask&MUTATION_AGGREGATE)!=0) {
            LCount lc=LCount.lookup(MutationEventImpl.DOM_ATTR_MODIFIED);
            if (lc.captures+lc.bubbles+lc.defaults>0) {
                enclosingAttr=getEnclosingAttr();
            }
        }

        ChildNode newInternal = (ChildNode)newChild;

        Node oldparent = newInternal.parentNode();
        if (oldparent != null) {
            oldparent.removeChild(newInternal);
        }

        ChildNode refInternal = (ChildNode) refChild;

        newInternal.ownerNode = this;
        newInternal.isOwned(true);

        ChildNode firstChild = (ChildNode) value;
        if (firstChild == null) {
            newInternal.isFirstChild(true);
            newInternal.previousSibling = newInternal;
        }
        else {
            if (refInternal == null) {
                ChildNode lastChild = firstChild.previousSibling;
                lastChild.nextSibling = newInternal;
                newInternal.previousSibling = lastChild;
                firstChild.previousSibling = newInternal;
            }
            else {
                if (refChild == firstChild) {
                    firstChild.isFirstChild(false);
                    newInternal.nextSibling = firstChild;
                    newInternal.previousSibling = firstChild.previousSibling;
                    firstChild.previousSibling = newInternal;
                    newInternal.isFirstChild(true);
                }
                else {
                    ChildNode prev = refInternal.previousSibling;
                    newInternal.nextSibling = refInternal;
                    prev.nextSibling = newInternal;
                    refInternal.previousSibling = newInternal;
                    newInternal.previousSibling = prev;
                }
            }
        }

        changed();

        if (MUTATIONEVENTS && ownerDocument.mutationEvents) {
            if ((mutationMask&MUTATION_LOCAL) != 0) {
                LCount lc = LCount.lookup(MutationEventImpl.DOM_NODE_INSERTED);
                if (lc.captures+lc.bubbles+lc.defaults>0) {
                    MutationEvent me= new MutationEventImpl();
                    me.initMutationEvent(MutationEventImpl.DOM_NODE_INSERTED,
                                         true,false,this,null,
                                         null,null,(short)0);
                    newInternal.dispatchEvent(me);
                }

                lc=LCount.lookup(
                            MutationEventImpl.DOM_NODE_INSERTED_INTO_DOCUMENT);
                if (lc.captures+lc.bubbles+lc.defaults>0) {
                    NodeImpl eventAncestor=this;
                    if (enclosingAttr!=null) 
                        eventAncestor=
                            (NodeImpl)(enclosingAttr.node.getOwnerElement());
                        NodeImpl p=eventAncestor;
                        while (p!=null) {
                            if (p.getNodeType()==ATTRIBUTE_NODE) {
                                p=(ElementImpl)((AttrImpl)p).getOwnerElement();
                            }
                            else {
                                p=p.parentNode();
                            }
                        }
                        if (eventAncestor.getNodeType()==Node.DOCUMENT_NODE) {
                            MutationEvent me= new MutationEventImpl();
                            me.initMutationEvent(MutationEventImpl
                                              .DOM_NODE_INSERTED_INTO_DOCUMENT,
                                                 false,false,null,null,
                                                 null,null,(short)0);
                            dispatchEventToSubtree(newInternal,me);
                        }
                    }
                }
            }

            if ((mutationMask&MUTATION_AGGREGATE) != 0) {
                dispatchAggregateEvents(enclosingAttr);
            }
        }

        checkNormalizationAfterInsert(newInternal);

        return newChild;


    /**
     * Remove a child from this Node. The removed child's subtree
     * remains intact so it may be re-inserted elsewhere.
     *
     * @return oldChild, in its new state (removed).
     *
     * @throws DOMException(NOT_FOUND_ERR) if oldChild is not a child of
     * this node.
     *
     * @throws DOMException(NO_MODIFICATION_ALLOWED_ERR) if this node is
     * read-only.
     */
    public Node removeChild(Node oldChild) 
        throws DOMException {
        if (hasStringValue()) {
            throw new DOMException(DOMException.NOT_FOUND_ERR, 
                                   "DOM008 Not found");
        }
        return internalRemoveChild(oldChild,MUTATION_ALL);
     
    /** NON-DOM INTERNAL: Within DOM actions,we sometimes need to be able
     * to control which mutation events are spawned. This version of the
     * removeChild operation allows us to do so. It is not intended
     * for use by application programs.
     */
    Node internalRemoveChild(Node oldChild,int mutationMask)
        throws DOMException {

        DocumentImpl ownerDocument = ownerDocument();
        if (ownerDocument.errorChecking) {
            if (isReadOnly()) {
                throw new DOMException(
                                     DOMException.NO_MODIFICATION_ALLOWED_ERR, 
                                     "DOM001 Modification not allowed");
            }
            if (oldChild != null && oldChild.getParentNode() != this) {
                throw new DOMException(DOMException.NOT_FOUND_ERR, 
                                       "DOM008 Not found");
            }
        }

        ownerDocument.removedChildNode(oldChild);

        ChildNode oldInternal = (ChildNode) oldChild;

        EnclosingAttr enclosingAttr=null;
        if(MUTATIONEVENTS && ownerDocument.mutationEvents)
        {
            LCount lc=LCount.lookup(MutationEventImpl.DOM_ATTR_MODIFIED);
            if(lc.captures+lc.bubbles+lc.defaults>0)
            {
                enclosingAttr=getEnclosingAttr();
            }
            
            if( (mutationMask&MUTATION_LOCAL) != 0)
            {
                lc=LCount.lookup(MutationEventImpl.DOM_NODE_REMOVED);
                if(lc.captures+lc.bubbles+lc.defaults>0)
                {
                    MutationEvent me= new MutationEventImpl();
                    me.initMutationEvent(MutationEventImpl.DOM_NODE_REMOVED,
                                         true,false,this,null,
                                         null,null,(short)0);
                    oldInternal.dispatchEvent(me);
                }
            
                lc=LCount.lookup(
                             MutationEventImpl.DOM_NODE_REMOVED_FROM_DOCUMENT);
                if(lc.captures+lc.bubbles+lc.defaults>0)
                {
                    NodeImpl eventAncestor=this;
                    if(enclosingAttr!=null) 
                        eventAncestor=
                            (NodeImpl) enclosingAttr.node.getOwnerElement();
                    {
                        for(NodeImpl p=eventAncestor.parentNode();
                            p!=null;
                            p=p.parentNode())
                        {
                        }
                        if(eventAncestor.getNodeType()==Node.DOCUMENT_NODE)
                        {
                            MutationEvent me= new MutationEventImpl();
                            me.initMutationEvent(MutationEventImpl
                                               .DOM_NODE_REMOVED_FROM_DOCUMENT,
                                                 false,false,
                                                 null,null,null,null,(short)0);
                            dispatchEventToSubtree(oldInternal,me);
                        }
                    }
                }
            }

            oldInternal.isFirstChild(false);
            ChildNode firstChild = (ChildNode) value;
            if (firstChild != null) {
                firstChild.isFirstChild(true);
                firstChild.previousSibling = oldInternal.previousSibling;
            }
        } else {
            ChildNode prev = oldInternal.previousSibling;
            ChildNode next = oldInternal.nextSibling;
            prev.nextSibling = next;
            if (next == null) {
                ChildNode firstChild = (ChildNode) value;
                firstChild.previousSibling = prev;
            } else {
                next.previousSibling = prev;
            }
        }

        ChildNode oldPreviousSibling = oldInternal.previousSibling();

        oldInternal.ownerNode       = ownerDocument;
        oldInternal.isOwned(false);
        oldInternal.nextSibling     = null;
        oldInternal.previousSibling = null;

        changed();

        if(MUTATIONEVENTS && ownerDocument.mutationEvents)
        {
            if( (mutationMask&MUTATION_AGGREGATE) != 0)
                dispatchAggregateEvents(enclosingAttr);

        checkNormalizationAfterRemove(oldPreviousSibling);

        return oldInternal;


    /**
     * Make newChild occupy the location that oldChild used to
     * have. Note that newChild will first be removed from its previous
     * parent, if any. Equivalent to inserting newChild before oldChild,
     * then removing oldChild.
     *
     * @returns oldChild, in its new state (removed).
     *
     * @throws DOMException(HIERARCHY_REQUEST_ERR) if newChild is of a
     * type that shouldn't be a child of this node, or if newChild is
     * one of our ancestors.
     *
     * @throws DOMException(WRONG_DOCUMENT_ERR) if newChild has a
     * different owner document than we do.
     *
     * @throws DOMException(NOT_FOUND_ERR) if oldChild is not a child of
     * this node.
     *
     * @throws DOMException(NO_MODIFICATION_ALLOWED_ERR) if this node is
     * read-only.
     */
    public Node replaceChild(Node newChild, Node oldChild)
        throws DOMException {

        makeChildNode();


        EnclosingAttr enclosingAttr=null;
        DocumentImpl ownerDocument = ownerDocument();
        if(MUTATIONEVENTS && ownerDocument.mutationEvents)
        {
            LCount lc=LCount.lookup(MutationEventImpl.DOM_ATTR_MODIFIED);
            if(lc.captures+lc.bubbles+lc.defaults>0)
            {
                enclosingAttr=getEnclosingAttr();
            }

        internalInsertBefore(newChild, oldChild,MUTATION_LOCAL);
        if (newChild != oldChild) {
            internalRemoveChild(oldChild,MUTATION_LOCAL);
        }

        if(MUTATIONEVENTS && ownerDocument.mutationEvents)
        {
            dispatchAggregateEvents(enclosingAttr);
        }

        return oldChild;
    }


    /**
     * NodeList method: Count the immediate children of this node
     * @return int
     */
    public int getLength() {

        if (hasStringValue()) {
            return 1;
        }
        ChildNode node = (ChildNode) value;
        int length = 0;
        for (; node != null; node = node.nextSibling) {
            length++;
        }
        return length;


    /**
     * NodeList method: Return the Nth immediate child of this node, or
     * null if the index is out of bounds.
     * @return org.w3c.dom.Node
     * @param Index int
     */
    public Node item(int index) {

        if (hasStringValue()) {
            if (index != 0 || value == null) {
                return null;
            }
            else {
                makeChildNode();
                return (Node) value;
            }
        }
        ChildNode node = (ChildNode) value;
        for (int i = 0; i < index && node != null; i++) {
            node = node.nextSibling;
        }
        return node;




    /**
     * Override default behavior so that if deep is true, children are also
     * toggled.
     * @see Node
     * <P>
     * Note: this will not change the state of an EntityReference or its
     * children, which are always read-only.
     */
    public void setReadOnly(boolean readOnly, boolean deep) {

        super.setReadOnly(readOnly, deep);

        if (deep) {

            if (needsSyncChildren()) {
                synchronizeChildren();
            }

            if (hasStringValue()) {
                return;
            }
            for (ChildNode mykid = (ChildNode) value;
                 mykid != null;
                 mykid = mykid.nextSibling) {
                if (mykid.getNodeType() != Node.ENTITY_REFERENCE_NODE) {
                    mykid.setReadOnly(readOnly,true);
                }
            }
        }


    /**
     * Override this method in subclass to hook in efficient
     * internal data structure.
     */
    protected void synchronizeChildren() {
        needsSyncChildren(false);
    }

    /**
     * Synchronizes the node's children with the internal structure.
     * Fluffing the children at once solves a lot of work to keep
     * the two structures in sync. The problem gets worse when
     * editing the tree -- this makes it a lot easier.
     * Even though this is only used in deferred classes this method is
     * put here so that it can be shared by all deferred classes.
     */
    protected final void synchronizeChildren(int nodeIndex) {

        DeferredDocumentImpl ownerDocument =
            (DeferredDocumentImpl) ownerDocument();
        boolean orig = ownerDocument.mutationEvents;
        ownerDocument.mutationEvents = false;

        needsSyncChildren(false);

        int last = ownerDocument.getLastChild(nodeIndex);
        int prev = ownerDocument.getPrevSibling(last);
        if (prev == -1) {
            value = ownerDocument.getNodeValueString(last);
            hasStringValue(true);
        }
        else {
            ChildNode firstNode = null;
            ChildNode lastNode = null;
            for (int index = last; index != -1;
                 index = ownerDocument.getPrevSibling(index)) {

                ChildNode node = (ChildNode)ownerDocument.getNodeObject(index);
                if (lastNode == null) {
                    lastNode = node;
                }
                else {
                    firstNode.previousSibling = node;
                }
                node.ownerNode = this;
                node.isOwned(true);
                node.nextSibling = firstNode;
                firstNode = node;
            }
            if (lastNode != null) {
                firstNode.isFirstChild(true);
                lastChild(lastNode);
            }
            hasStringValue(false);
        }

        ownerDocument.mutationEvents = orig;


    /**
     * Checks the normalized state of this node after inserting a child.
     * If the inserted child causes this node to be unnormalized, then this
     * node is flagged accordingly.
     * The conditions for changing the normalized state are:
     * <ul>
     * <li>The inserted child is a text node and one of its adjacent siblings
     * is also a text node.
     * <li>The inserted child is is itself unnormalized.
     * </ul>
     *
     * @param insertedChild the child node that was inserted into this node
     *
     * @throws NullPointerException if the inserted child is <code>null</code>
     */
    void checkNormalizationAfterInsert(ChildNode insertedChild) {
        if (insertedChild.getNodeType() == Node.TEXT_NODE) {
            ChildNode prev = insertedChild.previousSibling();
            ChildNode next = insertedChild.nextSibling;
            if ((prev != null && prev.getNodeType() == Node.TEXT_NODE) ||
                (next != null && next.getNodeType() == Node.TEXT_NODE)) {
                isNormalized(false);
            }
        }
        else {
            if (!insertedChild.isNormalized()) {
                isNormalized(false);
            }
        }

    /**
     * Checks the normalized of this node after removing a child.
     * If the removed child causes this node to be unnormalized, then this
     * node is flagged accordingly.
     * The conditions for changing the normalized state are:
     * <ul>
     * <li>The removed child had two adjacent siblings that were text nodes.
     * </ul>
     *
     * @param previousSibling the previous sibling of the removed child, or
     * <code>null</code>
     */
    void checkNormalizationAfterRemove(ChildNode previousSibling) {
        if (previousSibling != null &&
            previousSibling.getNodeType() == Node.TEXT_NODE) {

            ChildNode next = previousSibling.nextSibling;
            if (next != null && next.getNodeType() == Node.TEXT_NODE) {
                isNormalized(false);
            }
        }


    /** Serialize object. */
    private void writeObject(ObjectOutputStream out) throws IOException {

        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        out.defaultWriteObject();


    /** Deserialize object. */
    private void readObject(ObjectInputStream ois)
        throws ClassNotFoundException, IOException {

        ois.defaultReadObject();


        needsSyncChildren(false);


