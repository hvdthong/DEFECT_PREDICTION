package org.apache.xerces.dom;

import java.io.*;

import org.w3c.dom.*;
import org.w3c.dom.events.*;
import org.apache.xerces.dom.*;
import org.apache.xerces.dom.events.*;

/**
 * If we had multiple inheritance ChildAndParentNode would simply inherit both
 * from ChildNode and ParentNode. In this case it only inherits from
 * ChildNode and all the code of ParentNode is duplicated here (bummer :-(
 * <P>
 */
public abstract class ChildAndParentNode
    extends ChildNode {

    /** Serialization version. */
    static final long serialVersionUID = 0;

    /** Owner document. */
    protected DocumentImpl ownerDocument;

    /** First child. */
    protected ChildNode firstChild = null;


    /** Cached node list length. */
    protected transient int nodeListLength = -1;

    /** Last requested node. */
    protected transient ChildNode nodeListNode;

    /** Last requested node index. */
    protected transient int nodeListIndex = -1;


    /**
     * No public constructor; only subclasses of ParentNode should be
     * instantiated, and those normally via a Document's factory methods
     */
    protected ChildAndParentNode(DocumentImpl ownerDocument) {
        super(ownerDocument);
        this.ownerDocument = ownerDocument;
    }

    /** Constructor for serialization. */
    public ChildAndParentNode() {}


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
    	
    	ChildAndParentNode newnode = (ChildAndParentNode)super.cloneNode(deep);

        newnode.ownerDocument = ownerDocument;

        if (needsSyncChildren()) {
            synchronizeChildren();
        }

    	newnode.firstChild      = null;

        newnode.nodeListIndex = -1;
        newnode.nodeListLength = -1;

    	if (deep) {
            for (Node child = firstChild;
                 child != null;
                 child = child.getNextSibling()) {
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
	for (Node child = firstChild;
	     child != null; child = child.getNextSibling()) {
	    ((NodeImpl) child).setOwnerDocument(doc);
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

    	if (isReadOnly())
            throw new DOMExceptionImpl(
                        DOMException.NO_MODIFICATION_ALLOWED_ERR, 
                        "DOM001 Modification not allowed");

        boolean errorChecking = ownerDocument.errorChecking;
    	if (errorChecking && newChild.getOwnerDocument() != ownerDocument) {
            throw new DOMExceptionImpl(DOMException.WRONG_DOCUMENT_ERR, 
                                       "DOM005 Wrong document");
        }

        if (needsSyncChildren()) {
            synchronizeChildren();
        }

        if (errorChecking) {
            boolean treeSafe = true;
            for (NodeImpl a = parentNode();
                 treeSafe && a != null;
                 a = a.parentNode()) {
                treeSafe = newChild != a;
            }
            if(!treeSafe) {
                throw new DOMExceptionImpl(DOMException.HIERARCHY_REQUEST_ERR, 
                                           "DOM006 Hierarchy request error");
            }

            if(refChild != null && refChild.getParentNode() != this) {
                throw new DOMExceptionImpl(DOMException.NOT_FOUND_ERR,
                                           "DOM008 Not found");
            }
        }
        
        if (newChild.getNodeType() == Node.DOCUMENT_FRAGMENT_NODE) {


                 kid != null;
                 kid = kid.getNextSibling()) {

                if (errorChecking && !ownerDocument.isKidOK(this, kid)) {
                    throw new DOMExceptionImpl(
                                           DOMException.HIERARCHY_REQUEST_ERR, 
                                           "DOM006 Hierarchy request error");
                }
            }

            while (newChild.hasChildNodes()) {
                insertBefore(newChild.getFirstChild(), refChild);
            }
        }
        else if (errorChecking &&
                 (!(newChild instanceof ChildNode)
                  ||
                  !ownerDocument.isKidOK(this, newChild))) {
            throw new DOMExceptionImpl(DOMException.HIERARCHY_REQUEST_ERR, 
                                       "DOM006 Hierarchy request error");
        }
        else {
            ChildNode newInternal = (ChildNode)newChild;

            EnclosingAttr enclosingAttr=null;
            if(MUTATIONEVENTS && ownerDocument.mutationEvents
               && (mutationMask&MUTATION_AGGREGATE)!=0)
            {
                LCount lc=LCount.lookup(MutationEventImpl.DOM_ATTR_MODIFIED);
                if(lc.captures+lc.bubbles+lc.defaults>0)
                {
                    enclosingAttr=getEnclosingAttr();
                }
            }

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
            } else {
                if (refInternal == null) {
                    ChildNode lastChild = firstChild.previousSibling;
                    lastChild.nextSibling = newInternal;
                    newInternal.previousSibling = lastChild;
                    firstChild.previousSibling = newInternal;
                } else {
                    if (refChild == firstChild) {
                        firstChild.isFirstChild(false);
                        newInternal.nextSibling = firstChild;
                        newInternal.previousSibling =
                            firstChild.previousSibling;
                        firstChild.previousSibling = newInternal;
                        firstChild = newInternal;
                        newInternal.isFirstChild(true);
                    } else {
                        ChildNode prev = refInternal.previousSibling;
                        newInternal.nextSibling = refInternal;
                        prev.nextSibling = newInternal;
                        refInternal.previousSibling = newInternal;
                        newInternal.previousSibling = prev;
                    }
                }
            }

            changed();

            if (nodeListLength != -1) {
                nodeListLength++;
            }
            if (nodeListIndex != -1) {
                if (nodeListNode == refInternal) {
                    nodeListNode = newInternal;
                } else {
                    nodeListIndex = -1;
                }
            }

            if(MUTATIONEVENTS && ownerDocument.mutationEvents)
            {
                if( (mutationMask&MUTATION_LOCAL) != 0)
                {
                    LCount lc =
                        LCount.lookup(MutationEventImpl.DOM_NODE_INSERTED);
                    if(lc.captures+lc.bubbles+lc.defaults>0)
                    {
                        MutationEvent me= new MutationEventImpl();
                        me.initMutationEvent(
                                          MutationEventImpl.DOM_NODE_INSERTED,
                                          true,false,this,null,null,null);
                        newInternal.dispatchEvent(me);
                    }

                    lc=LCount.lookup(
                            MutationEventImpl.DOM_NODE_INSERTED_INTO_DOCUMENT);
                    if(lc.captures+lc.bubbles+lc.defaults>0)
                    {
                        NodeImpl eventAncestor=this;
                        if(enclosingAttr!=null) 
                            eventAncestor=
                              (NodeImpl)(enclosingAttr.node.getOwnerElement());
                        {
                            NodeImpl p=eventAncestor;
                            while(p!=null)
                            {
                                if(p.getNodeType()==ATTRIBUTE_NODE)
                                    p=(ElementImpl)
                                        ((AttrImpl)p).getOwnerElement();
                                else
                                    p=p.parentNode();
                            }
                            if(eventAncestor.getNodeType()==Node.DOCUMENT_NODE)
                            {
                                MutationEvent me= new MutationEventImpl();
                                me.initMutationEvent(MutationEventImpl
                                              .DOM_NODE_INSERTED_INTO_DOCUMENT,
                                                     false,false,
                                                     null,null,null,null);
                                dispatchEventToSubtree(newInternal,me);
                            }
                        }
                    }
                }

                if( (mutationMask&MUTATION_AGGREGATE) != 0)
                    dispatchAggregateEvents(enclosingAttr);
            }
        }
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

        if (isReadOnly()) {
            throw new DOMExceptionImpl(
                DOMException.NO_MODIFICATION_ALLOWED_ERR, 
                "DOM001 Modification not allowed");
        }
         
        if (ownerDocument.errorChecking && 
            oldChild != null && oldChild.getParentNode() != this) {
            throw new DOMExceptionImpl(DOMException.NOT_FOUND_ERR, 
                                       "DOM008 Not found");
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
                                         true,false,this,null,null,null);
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
                                                 null,null,null,null);
                            dispatchEventToSubtree(oldInternal,me);
                        }
                    }
                }
            }

        if (nodeListLength != -1) {
            nodeListLength--;
        }
        if (nodeListIndex != -1) {
            if (nodeListNode == oldInternal) {
                nodeListIndex--;
                nodeListNode = oldInternal.previousSibling();
            } else {
                nodeListIndex = -1;
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

        oldInternal.ownerNode       = ownerDocument;
        oldInternal.isOwned(false);
        oldInternal.nextSibling     = null;
        oldInternal.previousSibling = null;

        changed();

        if(MUTATIONEVENTS && ownerDocument.mutationEvents)
        {
            if( (mutationMask&MUTATION_AGGREGATE) != 0)
                dispatchAggregateEvents(enclosingAttr);

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
        internalRemoveChild(oldChild,MUTATION_LOCAL);

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

            ChildNode node;
            if (nodeListIndex != -1 && nodeListNode != null) {
                nodeListLength = nodeListIndex;
                node = nodeListNode;
            } else {
                node = firstChild;
                nodeListLength = 0;
            }
            for (; node != null; node = node.nextSibling) {
                nodeListLength++;
            }
        }

        return nodeListLength;


    /**
     * NodeList method: Return the Nth immediate child of this node, or
     * null if the index is out of bounds.
     * @return org.w3c.dom.Node
     * @param Index int
     */
    public Node item(int index) {
        if (nodeListIndex != -1 && nodeListNode != null) {
            if (nodeListIndex < index) {
                while (nodeListIndex < index && nodeListNode != null) {
                    nodeListIndex++;
                    nodeListNode = nodeListNode.nextSibling;
                }
            }
            else if (nodeListIndex > index) {
                while (nodeListIndex > index && nodeListNode != null) {
                    nodeListIndex--;
                    nodeListNode = nodeListNode.previousSibling();
                }
            }
            return nodeListNode;
        }

        nodeListNode = firstChild;
        for (nodeListIndex = 0; 
             nodeListIndex < index && nodeListNode != null; 
             nodeListIndex++) {
            nodeListNode = nodeListNode.nextSibling;
        }
        return nodeListNode;



    /**
     * Override default behavior to call normalize() on this Node's
     * children. It is up to implementors or Node to override normalize()
     * to take action.
     */
    public void normalize() {

        Node kid;
        for (kid = firstChild; kid != null; kid = kid.getNextSibling()) {
            kid.normalize();
        }
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
                if(!(mykid instanceof EntityReference)) {
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

        nodeListLength = -1;
        nodeListIndex = -1;


