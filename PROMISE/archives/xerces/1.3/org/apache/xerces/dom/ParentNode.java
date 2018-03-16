package org.apache.xerces.dom;

import java.io.*;

import org.w3c.dom.*;
import org.w3c.dom.events.*;
import org.apache.xerces.dom.*;
import org.apache.xerces.dom.events.*;

/**
 * ParentNode inherits from ChildImpl and adds the capability of having child
 * nodes. Not every node in the DOM can have children, so only nodes that can
 * should inherit from this class and pay the price for it.
 * <P>
 * ParentNode, just like NodeImpl, also implements NodeList, so it can
 * return itself in response to the getChildNodes() query. This eliminiates
 * the need for a separate ChildNodeList object. Note that this is an
 * IMPLEMENTATION DETAIL; applications should _never_ assume that
 * this identity exists. On the other hand, subclasses may need to override
 * this, in case of conflicting names. This is the case for the classes
 * HTMLSelectElementImpl and HTMLFormElementImpl of the HTML DOM.
 * <P>
 * While we have a direct reference to the first child, the last child is
 * stored as the previous sibling of the first child. First child nodes are
 * marked as being so, and getNextSibling hides this fact.
 * <P>Note: Not all parent nodes actually need to also be a child. At some
 * point we used to have ParentNode inheriting from NodeImpl and another class
 * called ChildAndParentNode that inherited from ChildNode. But due to the lack
 * of multiple inheritance a lot of code had to be duplicated which led to a
 * maintenance nightmare. At the same time only a few nodes (Document,
 * DocumentFragment, Entity, and Attribute) cannot be a child so the gain is
 * memory wasn't really worth it. The only type for which this would be the
 * case is Attribute, but we deal with there in another special way, so this is
 * not applicable.
 *
 * <p><b>WARNING</b>: Some of the code here is partially duplicated in
 * AttrImpl, be careful to keep these two classes in sync!
 *
 * @author Arnaud  Le Hors, IBM
 * @author Joe Kesselman, IBM
 * @author Andy Clark, IBM
 */
public abstract class ParentNode
    extends ChildNode {

    /** Serialization version. */
    static final long serialVersionUID = 2815829867152120872L;

    /** Owner document. */
    protected DocumentImpl ownerDocument;

    /** First child. */
    protected ChildNode firstChild = null;


    /** Cached node list length. */
    protected transient int fCachedLength = -1;

    /** Last requested node. */
    protected transient ChildNode fCachedChild;

    /** Last requested node index. */
    protected transient int fCachedChildIndex = -1;


    /**
     * No public constructor; only subclasses of ParentNode should be
     * instantiated, and those normally via a Document's factory methods
     */
    protected ParentNode(DocumentImpl ownerDocument) {
        super(ownerDocument);
        this.ownerDocument = ownerDocument;
    }

    /** Constructor for serialization. */
    public ParentNode() {}


    /**
     * Returns a duplicate of a given node. You can consider this a
     * generic "copy constructor" for nodes. The newly returned object should
     * be completely independent of the source object's subtree, so changes
     * in one after the clone has been made will not affect the other.
     * <p>
     * Example: Cloning a Text node will copy both the node and the text it
     * contains.
     * <p>
     * Example: Cloning something that has children -- Element or Attr, for
     * example -- will _not_ clone those children unless a "deep clone"
     * has been requested. A shallow clone of an Attr node will yield an
     * empty Attr of the same name.
     * <p>
     * NOTE: Clones will always be read/write, even if the node being cloned
     * is read-only, to permit applications using only the DOM API to obtain
     * editable copies of locked portions of the tree.
     */
    public Node cloneNode(boolean deep) {
    	
    	ParentNode newnode = (ParentNode) super.cloneNode(deep);

        newnode.ownerDocument = ownerDocument;

        if (needsSyncChildren()) {
            synchronizeChildren();
        }

    	newnode.firstChild      = null;

        newnode.fCachedChildIndex = -1;
        newnode.fCachedLength = -1;

    	if (deep) {
            for (ChildNode child = firstChild;
                 child != null;
                 child = child.nextSibling) {
                newnode.appendChild(child.cloneNode(true));
            }
        }

    	return newnode;


    /**
     * Find the Document that this Node belongs to (the document in
     * whose context the Node was created). The Node may or may not
     * currently be part of that Document's actual contents.
     */
    public Document getOwnerDocument() {
        return ownerDocument;
    }

    /**
     * same as above but returns internal type and this one is not overridden
     * by DocumentImpl to return null
     */
    DocumentImpl ownerDocument() {
        return ownerDocument;
    }

    /**
     * NON-DOM
     * set the ownerDocument of this node and its children
     */
    void setOwnerDocument(DocumentImpl doc) {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
	for (ChildNode child = firstChild;
	     child != null; child = child.nextSibling) {
	    child.setOwnerDocument(doc);
	}
        ownerDocument = doc;
    }

    /**
     * Test whether this node has any children. Convenience shorthand
     * for (Node.getFirstChild()!=null)
     */
    public boolean hasChildNodes() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return firstChild != null;
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
    	return firstChild;


    /** The last child of this Node, or null if none. */
    public Node getLastChild() {

        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return lastChild();


    final ChildNode lastChild() {
        return firstChild != null ? firstChild.previousSibling : null;
    }

    final void lastChild(ChildNode node) {
        if (firstChild != null) {
            firstChild.previousSibling = node;
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
            if(!treeSafe) {
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

        ChildNode refInternal = (ChildNode)refChild;

        newInternal.ownerNode = this;
        newInternal.isOwned(true);

        if (firstChild == null) {
            firstChild = newInternal;
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
                    firstChild = newInternal;
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

        if (fCachedLength != -1) {
            fCachedLength++;
        }
        if (fCachedChildIndex != -1) {
            if (fCachedChild == refInternal) {
                fCachedChild = newInternal;
            } else {
                fCachedChildIndex = -1;
            }
        }

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
                            if(p.getNodeType()==ATTRIBUTE_NODE) {
                                p=(ElementImpl)((AttrImpl)p).getOwnerElement();
                            }
                            else {
                                p=p.parentNode();
                            }
                        }
                        if(eventAncestor.getNodeType()==Node.DOCUMENT_NODE) {
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

        if (fCachedLength != -1) {
            fCachedLength--;
        }
        if (fCachedChildIndex != -1) {
            if (fCachedChild == oldInternal) {
                fCachedChildIndex--;
                fCachedChild = oldInternal.previousSibling();
            } else {
                fCachedChildIndex = -1;
            }
        }

        if (oldInternal == firstChild) {
            oldInternal.isFirstChild(false);
            firstChild = oldInternal.nextSibling;
            if (firstChild != null) {
                firstChild.isFirstChild(true);
                firstChild.previousSibling = oldInternal.previousSibling;
            }
        } else {
            ChildNode prev = oldInternal.previousSibling;
            ChildNode next = oldInternal.nextSibling;
            prev.nextSibling = next;
            if (next == null) {
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

        EnclosingAttr enclosingAttr=null;
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
     * Count the immediate children of this node.  Use to implement
     * NodeList.getLength().
     * @return int
     */
    private int nodeListGetLength() {

            ChildNode node;
            if (fCachedChildIndex != -1 && fCachedChild != null) {
                fCachedLength = fCachedChildIndex;
                node = fCachedChild;
            } else {
                node = firstChild;
                fCachedLength = 0;
            }
            for (; node != null; node = node.nextSibling) {
                fCachedLength++;
            }
        }

        return fCachedLength;


    /**
     * NodeList method: Count the immediate children of this node
     * @return int
     */
    public int getLength() {
        return nodeListGetLength();
    }

    /**
     * Return the Nth immediate child of this node, or null if the index is
     * out of bounds.  Use to implement NodeList.item().
     * @param index int
     */
    private Node nodeListItem(int index) {
        if (fCachedChildIndex != -1 && fCachedChild != null) {
            if (fCachedChildIndex < index) {
                while (fCachedChildIndex < index && fCachedChild != null) {
                    fCachedChildIndex++;
                    fCachedChild = fCachedChild.nextSibling;
                }
            }
            else if (fCachedChildIndex > index) {
                while (fCachedChildIndex > index && fCachedChild != null) {
                    fCachedChildIndex--;
                    fCachedChild = fCachedChild.previousSibling();
                }
            }
            return fCachedChild;
        }

        fCachedChild = firstChild;
        for (fCachedChildIndex = 0; 
             fCachedChildIndex < index && fCachedChild != null; 
             fCachedChildIndex++) {
            fCachedChild = fCachedChild.nextSibling;
        }
        return fCachedChild;


    /**
     * NodeList method: Return the Nth immediate child of this node, or
     * null if the index is out of bounds.
     * @return org.w3c.dom.Node
     * @param index int
     */
    public Node item(int index) {
        return nodeListItem(index);

    /**
     * Create a NodeList to access children that is use by subclass elements
     * that have methods named getLength() or item(int).  ChildAndParentNode
     * optimizes getChildNodes() by implementing NodeList itself.  However if
     * a subclass Element implements methods with the same name as the NodeList
     * methods, they will override the actually methods in this class.
     * <p>
     * To use this method, the subclass should implement getChildNodes() and
     * have it call this method.  The resulting NodeList instance maybe
     * shared and cached in a transient field, but the cached value must be
     * cleared if the node is cloned.
     */
    protected final NodeList getChildNodesUnoptimized() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        return new NodeList() {
                /**
                 * @see NodeList.getLength()
                 */
                public int getLength() {
                    return nodeListGetLength();
                
                /**
                 * @see NodeList.item(int)
                 */
                public Node item(int index) {
                    return nodeListItem(index);
            };


    /**
     * Override default behavior to call normalize() on this Node's
     * children. It is up to implementors or Node to override normalize()
     * to take action.
     */
    public void normalize() {
        if (isNormalized()) {
            return;
        }
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        ChildNode kid;
        for (kid = firstChild; kid != null; kid = kid.nextSibling) {
            kid.normalize();
        }
        isNormalized(true);
    }


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

            for (ChildNode mykid = firstChild;
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

        boolean orig = ownerDocument.mutationEvents;
        ownerDocument.mutationEvents = false;

        needsSyncChildren(false);

        DeferredDocumentImpl ownerDocument =
            (DeferredDocumentImpl)this.ownerDocument;
        ChildNode first = null;
        ChildNode last = null;
        for (int index = ownerDocument.getLastChild(nodeIndex);
             index != -1;
             index = ownerDocument.getPrevSibling(index)) {

            ChildNode node = (ChildNode)ownerDocument.getNodeObject(index);
            if (last == null) {
                last = node;
            }
            else {
                first.previousSibling = node;
            }
            node.ownerNode = this;
            node.isOwned(true);
            node.nextSibling = first;
            first = node;
        }
        if (last != null) {
            firstChild = first;
            first.isFirstChild(true);
            lastChild(last);
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

        fCachedLength = -1;
        fCachedChildIndex = -1;


