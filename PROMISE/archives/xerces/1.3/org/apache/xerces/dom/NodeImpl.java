package org.apache.xerces.dom;

import java.io.*;
import java.util.Vector;

import org.w3c.dom.*;

import org.apache.xerces.dom.events.EventImpl;
import org.apache.xerces.dom.events.MutationEventImpl;
import org.w3c.dom.events.*;

/**
 * Node provides the basic structure of a DOM tree. It is never used
 * directly, but instead is subclassed to add type and data
 * information, and additional methods, appropriate to each node of
 * the tree. Only its subclasses should be instantiated -- and those,
 * with the exception of Document itself, only through a specific
 * Document's factory methods.
 * <P>
 * The Node interface provides shared behaviors such as siblings and
 * children, both for consistancy and so that the most common tree
 * operations may be performed without constantly having to downcast
 * to specific node types. When there is no obvious mapping for one of
 * these queries, it will respond with null.
 * Note that the default behavior is that children are forbidden. To
 * permit them, the subclass ParentNode overrides several methods.
 * <P>
 * NodeImpl also implements NodeList, so it can return itself in
 * response to the getChildNodes() query. This eliminiates the need
 * for a separate ChildNodeList object. Note that this is an
 * IMPLEMENTATION DETAIL; applications should _never_ assume that
 * this identity exists.
 * <P>
 * All nodes in a single document must originate
 * in that document. (Note that this is much tighter than "must be
 * same implementation") Nodes are all aware of their ownerDocument,
 * and attempts to mismatch will throw WRONG_DOCUMENT_ERR.
 * <P>
 * However, to save memory not all nodes always have a direct reference
 * to their ownerDocument. When a node is owned by another node it relies
 * on its owner to store its ownerDocument. Parent nodes always store it
 * though, so there is never more than one level of indirection.
 * And when a node doesn't have an owner, ownerNode refers to its
 * ownerDocument.
 *
 * @author Arnaud  Le Hors, IBM
 * @author Joe Kesselman, IBM
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public abstract class NodeImpl
    implements Node, NodeList, EventTarget, Cloneable, Serializable {


    /** Serialization version. */
    static final long serialVersionUID = -6316591992167219696L;


    /** Element definition node type. */
    public static final short ELEMENT_DEFINITION_NODE = -1;





    protected short flags;

    protected final static short READONLY     = 0x1<<0;
    protected final static short SYNCDATA     = 0x1<<1;
    protected final static short SYNCCHILDREN = 0x1<<2;
    protected final static short OWNED        = 0x1<<3;
    protected final static short FIRSTCHILD   = 0x1<<4;
    protected final static short SPECIFIED    = 0x1<<5;
    protected final static short IGNORABLEWS  = 0x1<<6;
    protected final static short SETVALUE     = 0x1<<7;
    protected final static short HASSTRING    = 0x1<<8;
    protected final static short UNNORMALIZED = 0x1<<9;


    /**
     * No public constructor; only subclasses of Node should be
     * instantiated, and those normally via a Document's factory methods
     * <p>
     * Every Node knows what Document it belongs to.
     */
    protected NodeImpl(DocumentImpl ownerDocument) {
        ownerNode = ownerDocument;

    /** Constructor for serialization. */
    public NodeImpl() {}


    /**
     * A short integer indicating what type of node this is. The named
     * constants for this value are defined in the org.w3c.dom.Node interface.
     */
    public abstract short getNodeType();

    /**
     * the name of this node.
     */
    public abstract String getNodeName();
    
    /**
     * Returns the node value.
     */
    public String getNodeValue() {
    }

    /**
     * Sets the node value.
     * @throws DOMException(NO_MODIFICATION_ALLOWED_ERR)
     */
    public void setNodeValue(String x) 
        throws DOMException {
    }

    /**
     * Adds a child node to the end of the list of children for this node.
     * Convenience shorthand for insertBefore(newChild,null).
     * @see #insertBefore(Node, Node)
     * <P>
     * By default we do not accept any children, ParentNode overrides this.
     * @see ParentNode
     *
     * @returns newChild, in its new state (relocated, or emptied in the
     * case of DocumentNode.)
     *
     * @throws DOMException(HIERARCHY_REQUEST_ERR) if newChild is of a
     * type that shouldn't be a child of this node.
     *
     * @throws DOMException(WRONG_DOCUMENT_ERR) if newChild has a
     * different owner document than we do.
     *
     * @throws DOMException(NO_MODIFICATION_ALLOWED_ERR) if this node is
     * read-only.
     */
    public Node appendChild(Node newChild) throws DOMException {
    	return insertBefore(newChild, null);
    }

    /**
     * Returns a duplicate of a given node. You can consider this a
     * generic "copy constructor" for nodes. The newly returned object should
     * be completely independent of the source object's subtree, so changes
     * in one after the clone has been made will not affect the other.
     * <P>
     * Note: since we never have any children deep is meaningless here,
     * ParentNode overrides this behavior.
     * @see ParentNode
     *
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

        if (needsSyncData()) {
            synchronizeData();
	}
    	
    	NodeImpl newnode;
    	try {
            newnode = (NodeImpl)clone();
    	}
    	catch (CloneNotSupportedException e) {
            return null;
    	}
    	
    	newnode.ownerNode      = ownerDocument();
        newnode.isOwned(false);

        newnode.isReadOnly(false);

    	return newnode;


    /**
     * Find the Document that this Node belongs to (the document in
     * whose context the Node was created). The Node may or may not
     * currently be part of that Document's actual contents.
     */
    public Document getOwnerDocument() {
        if (isOwned()) {
            return ownerNode.ownerDocument();
        } else {
            return (Document) ownerNode;
        }
    }

    /**
     * same as above but returns internal type and this one is not overridden
     * by DocumentImpl to return null 
     */
    DocumentImpl ownerDocument() {
        if (isOwned()) {
            return ownerNode.ownerDocument();
        } else {
            return (DocumentImpl) ownerNode;
        }
    }

    /**
     * NON-DOM
     * set the ownerDocument of this node
     */
    void setOwnerDocument(DocumentImpl doc) {
        if (needsSyncData()) {
            synchronizeData();
        }
	if (!isOwned()) {
            ownerNode = doc;
        }
    }

    /**
     * Obtain the DOM-tree parent of this node, or null if it is not
     * currently active in the DOM tree (perhaps because it has just been
     * created or removed). Note that Document, DocumentFragment, and
     * Attribute will never have parents.
     */
    public Node getParentNode() {
    }

    /*
     * same as above but returns internal type
     */
    NodeImpl parentNode() {
        return null;
    }

    /** The next child of this node's parent, or null if none */
    public Node getNextSibling() {
    }

    /** The previous child of this node's parent, or null if none */
    public Node getPreviousSibling() {
    }

    ChildNode previousSibling() {
    }

    /**
     * Return the collection of attributes associated with this node,
     * or null if none. At this writing, Element is the only type of node
     * which will ever have attributes.
     *
     * @see ElementImpl
     */
    public NamedNodeMap getAttributes() {
    }

    /**
     *  Returns whether this node (if it is an element) has any attributes.
     * @return <code>true</code> if this node has any attributes, 
     *   <code>false</code> otherwise.
     * @since DOM Level 2
     * @see ElementImpl
     */
    public boolean hasAttributes() {
    }

    /**
     * Test whether this node has any children. Convenience shorthand
     * for (Node.getFirstChild()!=null)
     * <P>
     * By default we do not have any children, ParentNode overrides this.
     * @see ParentNode
     */
    public boolean hasChildNodes() {
        return false;
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
        return this;
    }

    /** The first child of this Node, or null if none.
     * <P>
     * By default we do not have any children, ParentNode overrides this.
     * @see ParentNode
     */
    public Node getFirstChild() {
    	return null;
    }

    /** The first child of this Node, or null if none.
     * <P>
     * By default we do not have any children, ParentNode overrides this.
     * @see ParentNode
     */
    public Node getLastChild() {
	return null;
    }

    /**
     * Move one or more node(s) to our list of children. Note that this
     * implicitly removes them from their previous parent.
     * <P>
     * By default we do not accept any children, ParentNode overrides this.
     * @see ParentNode
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
	throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, 
				   "DOM006 Hierarchy request error");
    }

    /**
     * Remove a child from this Node. The removed child's subtree
     * remains intact so it may be re-inserted elsewhere.
     * <P>
     * By default we do not have any children, ParentNode overrides this.
     * @see ParentNode
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
	throw new DOMException(DOMException.NOT_FOUND_ERR, 
				   "DOM008 Not found");
    }

    /**
     * Make newChild occupy the location that oldChild used to
     * have. Note that newChild will first be removed from its previous
     * parent, if any. Equivalent to inserting newChild before oldChild,
     * then removing oldChild.
     * <P>
     * By default we do not have any children, ParentNode overrides this.
     * @see ParentNode
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
	throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, 
				   "DOM006 Hierarchy request error");
    }


    /**
     * NodeList method: Count the immediate children of this node
     * <P>
     * By default we do not have any children, ParentNode overrides this.
     * @see ParentNode
     *
     * @return int
     */
    public int getLength() {
	return 0;
    }

    /**
     * NodeList method: Return the Nth immediate child of this node, or
     * null if the index is out of bounds.
     * <P>
     * By default we do not have any children, ParentNode overrides this.
     * @see ParentNode
     *
     * @return org.w3c.dom.Node
     * @param Index int
     */
    public Node item(int index) {
	return null;
    }


    /**
     * Puts all <code>Text</code> nodes in the full depth of the sub-tree 
     * underneath this <code>Node</code>, including attribute nodes, into a 
     * "normal" form where only markup (e.g., tags, comments, processing 
     * instructions, CDATA sections, and entity references) separates 
     * <code>Text</code> nodes, i.e., there are no adjacent <code>Text</code> 
     * nodes.  This can be used to ensure that the DOM view of a document is 
     * the same as if it were saved and re-loaded, and is useful when 
     * operations (such as XPointer lookups) that depend on a particular 
     * document tree structure are to be used.In cases where the document 
     * contains <code>CDATASections</code>, the normalize operation alone may 
     * not be sufficient, since XPointers do not differentiate between 
     * <code>Text</code> nodes and <code>CDATASection</code> nodes.
     * <p>
     * Note that this implementation simply calls normalize() on this Node's
     * children. It is up to implementors or Node to override normalize()
     * to take action.
     */
    public void normalize() {
	/* by default we do not have any children,
	   ParentNode overrides this behavior */
    }

    /**
     * Introduced in DOM Level 2. <p>
     * Tests whether the DOM implementation implements a specific feature and that
     * feature is supported by this node.
     * @param feature       The package name of the feature to test. This is the
     *                      same name as what can be passed to the method
     *                      hasFeature on DOMImplementation.
     * @param version       This is the version number of the package name to
     *                      test. In Level 2, version 1, this is the string "2.0". If
     *                      the version is not specified, supporting any version of
     *                      the feature will cause the method to return true.
     * @return boolean      Returns true if this node defines a subtree within which the
     *                      specified feature is supported, false otherwise.
     * @since WD-DOM-Level-2-19990923
     */
    public boolean isSupported(String feature, String version)
    {
        return ownerDocument().getImplementation().hasFeature(feature,
                                                              version);
    }

    /**
     * Introduced in DOM Level 2. <p>
     *
     * The namespace URI of this node, or null if it is unspecified. When this node
     * is of any type other than ELEMENT_NODE and ATTRIBUTE_NODE, this is always
     * null and setting it has no effect. <p>
     *
     * This is not a computed value that is the result of a namespace lookup based on
     * an examination of the namespace declarations in scope. It is merely the
     * namespace URI given at creation time.<p>
     *
     * For nodes created with a DOM Level 1 method, such as createElement
     * from the Document interface, this is null.
     * @since WD-DOM-Level-2-19990923
     * @see AttrNSImpl
     * @see ElementNSImpl
     */
    public String getNamespaceURI()
    {
        return null;
    }

    /**
     * Introduced in DOM Level 2. <p>
     *
     * The namespace prefix of this node, or null if it is unspecified. When this
     * node is of any type other than ELEMENT_NODE and ATTRIBUTE_NODE this is
     * always null and setting it has no effect.<p>
     *
     * For nodes created with a DOM Level 1 method, such as createElement
     * from the Document interface, this is null. <p>
     *
     * @since WD-DOM-Level-2-19990923
     * @see AttrNSImpl
     * @see ElementNSImpl
     */
    public String getPrefix()
    {
        return null;
    }

    /**
     *  Introduced in DOM Level 2. <p>
     *
     *  The namespace prefix of this node, or null if it is unspecified. When this
     *  node is of any type other than ELEMENT_NODE and ATTRIBUTE_NODE this is
     *  always null and setting it has no effect.<p>
     *
     *  For nodes created with a DOM Level 1 method, such as createElement
     *  from the Document interface, this is null.<p>
     *
     *  Note that setting this attribute changes the nodeName attribute, which holds the
     *  qualified name, as well as the tagName and name attributes of the Element
     *  and Attr interfaces, when applicable.<p>
     *
     * @throws INVALID_CHARACTER_ERR Raised if the specified
     *  prefix contains an invalid character.
     *
     * @since WD-DOM-Level-2-19990923
     * @see AttrNSImpl
     * @see ElementNSImpl
     */
    public void setPrefix(String prefix)
        throws DOMException
    {
	throw new DOMException(DOMException.NAMESPACE_ERR, 
				   "DOM003 Namespace error");
    }

    /**
     * Introduced in DOM Level 2. <p>
     *
     * Returns the local part of the qualified name of this node.
     * For nodes created with a DOM Level 1 method, such as createElement
     * from the Document interface, and for nodes of any type other than
     * ELEMENT_NODE and ATTRIBUTE_NODE this is the same as the nodeName
     * attribute.
     * @since WD-DOM-Level-2-19990923
     * @see AttrNSImpl
     * @see ElementNSImpl
     */
    public String             getLocalName()
    {
        return null;
    }
    
    
	/** Compile-time flag. If false, disables our code for
	    the DOM Level 2 Events module, perhaps allowing it
	    to be optimized out to save bytecodes.
	*/
	protected final static boolean MUTATIONEVENTS=true;
	
	/** The MUTATION_ values are parameters to the NON-DOM 
	    internalInsertBefore() and internalRemoveChild() operations,
	    allowing us to control which MutationEvents are generated.
	 */
	protected final static int MUTATION_NONE=0x00;
	protected final static int MUTATION_LOCAL=0x01;
	protected final static int MUTATION_AGGREGATE=0x02;
	protected final static int MUTATION_ALL=0xffff;
	
	/* NON-DOM INTERNAL: Class LEntry is just a struct used to represent
	 * event listeners registered with this node. Copies of this object
	 * are hung from the nodeListeners Vector.
	 * <p>
	 * I considered using two vectors -- one for capture,
	 * one for bubble -- but decided that since the list of listeners 
	 * is probably short in most cases, it might not be worth spending
	 * the space. ***** REVISIT WHEN WE HAVE MORE EXPERIENCE.
	 */
	class LEntry
	{
	    String type;
	    EventListener listener;
	    boolean useCapture;
	    
	    /** NON-DOM INTERNAL: Constructor for Listener list Entry 
	     * @param type Event name (NOT event group!) to listen for.
	     * @param listener Who gets called when event is dispatched
	     * @param useCaptue True iff listener is registered on
	     *  capturing phase rather than at-target or bubbling
	     */
	    LEntry(String type,EventListener listener,boolean useCapture)
	    {
	        this.type=type;this.listener=listener;this.useCapture=useCapture;
	    }
	
	/** Introduced in DOM Level 2. <p>
     * Register an event listener with this Node. A listener may be independently
     * registered as both Capturing and Bubbling, but may only be
     * registered once per role; redundant registrations are ignored.
     * @param type Event name (NOT event group!) to listen for.
	 * @param listener Who gets called when event is dispatched
	 * @param useCapture True iff listener is registered on
	 *  capturing phase rather than at-target or bubbling
	 */
	public void addEventListener(String type,EventListener listener,boolean useCapture)
	{
	    if(type==null || type.equals("") || listener==null)
	        return;

	    removeEventListener(type,listener,useCapture);
	    
            Vector nodeListeners = ownerDocument().getEventListeners(this);
	    if(nodeListeners==null) {
                nodeListeners=new Vector();
                ownerDocument().setEventListeners(this, nodeListeners);
            }
	    nodeListeners.addElement(new LEntry(type,listener,useCapture));
	    
	    LCount lc=LCount.lookup(type);
	    if(useCapture)
	        ++lc.captures;
	    else
	        ++lc.bubbles;

	
	/** Introduced in DOM Level 2. <p>
     * Deregister an event listener previously registered with this Node. 
     * A listener must be independently removed from the 
     * Capturing and Bubbling roles. Redundant removals (of
     * listeners not currently registered for this role) are ignored.
     * @param type Event name (NOT event group!) to listen for.
	 * @param listener Who gets called when event is dispatched
	 * @param useCapture True iff listener is registered on
	 *  capturing phase rather than at-target or bubbling
	 */
	public void removeEventListener(String type,EventListener listener,boolean useCapture)
	{
            Vector nodeListeners = ownerDocument().getEventListeners(this);
  	    if(nodeListeners==null || type==null || type.equals("") || listener==null)
	        return;

        {
            LEntry le=(LEntry)(nodeListeners.elementAt(i));
            if(le.useCapture==useCapture && le.listener==listener && 
                le.type.equals(type))
            {
                nodeListeners.removeElementAt(i);
                if(nodeListeners.size()==0)
                    ownerDocument().setEventListeners(this, null);

                LCount lc=LCount.lookup(type);
                if(useCapture)
                    --lc.captures;
                else
                    --lc.bubbles;

            }
        }
	
	/** COMMENTED OUT **
            Now that event listeners are stored on the document with the node
            as the key, nodes can't be finalized if they have any event
            listener. This finalize method becomes useless... This is a place
            where we could definitely use weak references!! If we did, then
            this finalize method could be put back in (which is why I don't
            remove if completely). - ALH
         ** NON-DOM INTERNAL:
	    A finalizer has added to NodeImpl in order to correct the event-usage
	    counts of any remaining listeners before discarding the Node.
	    This isn't absolutely required, and finalizers are of dubious
	    reliability and have odd effects on some implementations of GC.
	    But given the expense of event generation and distribution it 
	    seems a worthwhile safety net.
	    ***** RECONSIDER at some future point.

	protected void finalize() throws Throwable
	{
	    super.finalize();
	    if(nodeListeners!=null)
            {
                LEntry le=(LEntry)(nodeListeners.elementAt(i));
                LCount lc=LCount.lookup(le.type);
           	    if(le.useCapture)
	                --lc.captures;
                else
	                --lc.bubbles;
	        }
	}	
	   */

    /**
     * Introduced in DOM Level 2. <p>
     * Distribution engine for DOM Level 2 Events. 
     * <p>
     * Event propagation runs as follows:
     * <ol>
     * <li>Event is dispatched to a particular target node, which invokes
     *   this code. Note that the event's stopPropagation flag is
     *   cleared when dispatch begins; thereafter, if it has 
     *   been set before processing of a node commences, we instead
     *   immediately advance to the DEFAULT phase.
     * <li>The node's ancestors are established as destinations for events.
     *   For capture and bubble purposes, node ancestry is determined at 
     *   the time dispatch starts. If an event handler alters the document 
     *   tree, that does not change which nodes will be informed of the event. 
     * <li>CAPTURING_PHASE: Ancestors are scanned, root to target, for 
     *   Capturing listeners. If found, they are invoked (see below). 
     * <li>AT_TARGET: 
     *   Event is dispatched to NON-CAPTURING listeners on the
     *   target node. Note that capturing listeners on this node are _not_
     *   invoked.
     * <li>BUBBLING_PHASE: Ancestors are scanned, target to root, for
     *   non-capturing listeners. 
     * <li>Default processing: Some DOMs have default behaviors bound to specific
     *   nodes. If this DOM does, and if the event's preventDefault flag has
     *   not been set, we now return to the target node and process its
     *   default handler for this event, if any.
     * </ol>
     * <p>
     * Note that (de)registration of handlers during
     * processing of an event does not take effect during
     * this phase of this event; they will not be called until
     * the next time this node is visited by dispatchEvent.
     * <p>
     * If an event handler itself causes events to be dispatched, they are
     * processed synchronously, before processing resumes
     * on the event which triggered them. Please be aware that this may 
     * result in events arriving at listeners "out of order" relative
     * to the actual sequence of requests.
     * <p>
     * Note that our implementation resets the event's stop/prevent flags
     * when dispatch begins.
     * I believe the DOM's intent is that event objects be redispatchable,
     * though it isn't stated in those terms.
     * @param event the event object to be dispatched to 
     * registered EventListeners
     * @return true if the event's <code>preventDefault()</code>
     * method was invoked by an EventListener; otherwise false.
    */
	public boolean dispatchEvent(Event event)
    {
        if(event==null) return false;
        
        EventImpl evt=(EventImpl)event;

        if(!evt.initialized || evt.type==null || evt.type.equals(""))
            throw new EventException(EventException.UNSPECIFIED_EVENT_TYPE_ERR,
                                     "DOM010 Unspecified event type");
        
        LCount lc=LCount.lookup(evt.getType());
        if(lc.captures+lc.bubbles+lc.defaults==0)
            return evt.preventDefault;

        evt.target=this;
        evt.stopPropagation=false;
        evt.preventDefault=false;
        
        Vector pv=new Vector(10,10);
        Node p=this,n=p.getParentNode();
        while(n!=null)
        {
            pv.addElement(n);
            p=n;
            n=n.getParentNode();
        }
        
        if(lc.captures>0)
        {
            evt.eventPhase=Event.CAPTURING_PHASE;
            for(int j=pv.size()-1;j>=0;--j)
            {
                if(evt.stopPropagation)
                    
                NodeImpl nn=(NodeImpl)pv.elementAt(j);
                evt.currentTarget=nn;
                Vector nodeListeners = ownerDocument().getEventListeners(nn);
                if(nodeListeners!=null)
                {
                    Vector nl=(Vector)(nodeListeners.clone());
                    {
	                    LEntry le=(LEntry)(nl.elementAt(i));
                        if(le.useCapture && le.type.equals(evt.type))
                            try
                            {
    	                        le.listener.handleEvent(evt);
	                        }
	                        catch(Exception e)
	                        {
	                        }
	                }
	            }
            }
        }
        
        if(lc.bubbles>0)
        {
            evt.eventPhase=Event.AT_TARGET;
            evt.currentTarget=this;
            Vector nodeListeners = ownerDocument().getEventListeners(this);
            if(!evt.stopPropagation && nodeListeners!=null)
            {
                Vector nl=(Vector)nodeListeners.clone();
                {
                    LEntry le=(LEntry)nl.elementAt(i);
       	            if(le!=null && !le.useCapture && le.type.equals(evt.type))
   	                    try
   	                    {
                            le.listener.handleEvent(evt);
                        }
                        catch(Exception e)
                        {
                        }
	            }
            }
            if(evt.bubbles) 
            {
                evt.eventPhase=Event.BUBBLING_PHASE;
                for(int j=0;j<pv.size();++j)
                {
                    if(evt.stopPropagation)
                    
                    NodeImpl nn=(NodeImpl)pv.elementAt(j);
                    evt.currentTarget=nn;
                    nodeListeners = ownerDocument().getEventListeners(nn);
                    if(nodeListeners!=null)
                    {
                        Vector nl=(Vector)(nodeListeners.clone());
    	                {
	                        LEntry le=(LEntry)(nl.elementAt(i));
    	                    if(!le.useCapture && le.type.equals(evt.type))
            	                try
            	                {
	                                le.listener.handleEvent(evt);
	                            }
	                            catch(Exception e)
	                            {
	                            }
	                    }
	                }
                }
            }
        }
        
        if(lc.defaults>0 && (!evt.cancelable || !evt.preventDefault))
        {
        }

        return evt.preventDefault;        


    /** NON-DOM INTERNAL: DOMNodeInsertedIntoDocument and ...RemovedFrom...
     * are dispatched to an entire subtree. This is the distribution code
     * therefor. They DO NOT bubble, thanks be, but may be captured.
     * <p>
     * ***** At the moment I'm being sloppy and using the normal
     * capture dispatcher on every node. This could be optimized hugely
     * by writing a capture engine that tracks our position in the tree to
     * update the capture chain without repeated chases up to root.
     * @param n node which was directly inserted or removed
     * @param e event to be sent to that node and its subtree
     */
    void dispatchEventToSubtree(Node n,Event e)
    {
      if(MUTATIONEVENTS && ownerDocument().mutationEvents)
      {
          Vector nodeListeners = ownerDocument().getEventListeners(this);
	    if(nodeListeners==null || n==null)
            return;

	    ((NodeImpl)n).dispatchEvent(e);
	    if(n.getNodeType()==Node.ELEMENT_NODE)
	    {
	        NamedNodeMap a=n.getAttributes();
	        for(int i=a.getLength()-1;i>=0;--i)
	            dispatchEventToSubtree(a.item(i),e);
	    }
	    dispatchEventToSubtree(n.getFirstChild(),e);
	    dispatchEventToSubtree(n.getNextSibling(),e);
	  }

    /** NON-DOM INTERNAL: Return object for getEnclosingAttr. Carries
     * (two values, the Attr node affected (if any) and its previous 
     * string value. Simple struct, no methods.
     */
	class EnclosingAttr
	{
	    AttrImpl node;
	    String oldvalue;
	
	/** NON-DOM INTERNAL: Pre-mutation context check, in
	 * preparation for later generating DOMAttrModified events.
	 * Determines whether this node is within an Attr
	 * @return either a description of that Attr, or Null
	 * if none such. 
	 */
	EnclosingAttr getEnclosingAttr()
	{
      if(MUTATIONEVENTS && ownerDocument().mutationEvents)
      {
        NodeImpl eventAncestor=this;
        while(true)
        {
            if(eventAncestor==null)
                return null;
            int type=eventAncestor.getNodeType();
            if(type==Node.ATTRIBUTE_NODE)
            {
                EnclosingAttr retval=new EnclosingAttr();
                retval.node=(AttrImpl)eventAncestor;
                retval.oldvalue=retval.node.getNodeValue();
                return retval;
            }    
            else if(type==Node.ENTITY_REFERENCE_NODE)
                eventAncestor=eventAncestor.parentNode();
            else 
                return null;
        }
      }

	
	/** NON-DOM INTERNAL: Convenience wrapper for calling
	 * dispatchAggregateEvents when the context was established
	 * by <code>getEnclosingAttr</code>.
	 * @param ea description of Attr affected by current operation
	 */
	void dispatchAggregateEvents(EnclosingAttr ea)
	{
	    if(ea!=null)
	        dispatchAggregateEvents(ea.node, ea.oldvalue,
                                        MutationEvent.MODIFICATION);
            else
	        dispatchAggregateEvents(null,null,(short)0);
	        

	/** NON-DOM INTERNAL: Generate the "aggregated" post-mutation events
	 * DOMAttrModified and DOMSubtreeModified.
	 * Both of these should be issued only once for each user-requested
	 * mutation operation, even if that involves multiple changes to
	 * the DOM.
	 * For example, if a DOM operation makes multiple changes to a single
	 * Attr before returning, it would be nice to generate only one 
	 * DOMAttrModified, and multiple changes over larger scope but within
	 * a recognizable single subtree might want to generate only one 
	 * DOMSubtreeModified, sent to their lowest common ancestor. 
	 * <p>
	 * To manage this, use the "internal" versions of insert and remove
	 * with MUTATION_LOCAL, then make an explicit call to this routine
	 * at the higher level. Some examples now exist in our code.
	 *
	 * @param enclosingAttr The Attr node (if any) whose value has
	 * been changed as a result of the DOM operation. Null if none such.
	 * @param oldValue The String value previously held by the
	 * enclosingAttr. Ignored if none such.
         * @param change Type of modification to the attr. See
         * MutationEvent.attrChange
	 */
	void dispatchAggregateEvents(AttrImpl enclosingAttr,
                                     String oldvalue,
                                     short change)
	{
      if(MUTATIONEVENTS && ownerDocument().mutationEvents)
      {
	    NodeImpl owner=null;
	    if(enclosingAttr!=null)
	    {
            LCount lc=LCount.lookup(MutationEventImpl.DOM_ATTR_MODIFIED);
	        if(lc.captures+lc.bubbles+lc.defaults>0)
	        {
                owner=((NodeImpl)(enclosingAttr.getOwnerElement()));
                if(owner!=null)
                {
                    MutationEventImpl me= new MutationEventImpl();
                    me.initMutationEvent(MutationEventImpl.DOM_ATTR_MODIFIED,
                                         true,false, null,oldvalue,
                                         enclosingAttr.getNodeValue(),
                                         enclosingAttr.getNodeName(),(short)0);
                    me.attrChange = change;
                    owner.dispatchEvent(me);
                }
            }
        }
    
        LCount lc=LCount.lookup(MutationEventImpl.DOM_SUBTREE_MODIFIED);
        if(lc.captures+lc.bubbles+lc.defaults>0)
        {
            MutationEvent me= new MutationEventImpl();
            me.initMutationEvent(MutationEventImpl.DOM_SUBTREE_MODIFIED,
                                 true,false,null,null,null,null,(short)0);
            
    	    if(enclosingAttr!=null)
    	    {
    	        enclosingAttr.dispatchEvent(me);
    	        if(owner!=null)
    	            owner.dispatchEvent(me);
    	    }
            else
                dispatchEvent(me);
        }
      }



    /**
     * NON-DOM: PR-DOM-Level-1-19980818 mentions readonly nodes in conjunction
     * with Entities, but provides no API to support this.
     * <P>
     * Most DOM users should not touch this method. Its anticpated use
     * is during construction of EntityRefernces, where it will be used to
     * lock the contents replicated from Entity so they can't be casually
     * altered. It _could_ be published as a DOM extension, if desired.
     * <P>
     * Note: since we never have any children deep is meaningless here,
     * ParentNode overrides this behavior.
     * @see ParentNode
     *
     * @param readOnly True or false as desired.
     * @param deep If true, children are also toggled. Note that this will
     *	not change the state of an EntityReference or its children,
     *  which are always read-only.
     */
    public void setReadOnly(boolean readOnly, boolean deep) {

        if (needsSyncData()) {
            synchronizeData();
        }
    	isReadOnly(readOnly);


    /**
     * NON-DOM: Returns true if this node is read-only. This is a
     * shallow check.
     */
    public boolean getReadOnly() {

        if (needsSyncData()) {
            synchronizeData();
        }
        return isReadOnly();


    /**
     * NON-DOM: As an alternative to subclassing the DOM, this implementation
     * has been extended with the ability to attach an object to each node.
     * (If you need multiple objects, you can attach a collection such as a
     * vector or hashtable, then attach your application information to that.)
     * <p><b>Important Note:</b> You are responsible for removing references
     * to your data on nodes that are no longer used. Failure to do so will
     * prevent the nodes, your data is attached to, to be garbage collected
     * until the whole document is.
     *
     * @param data the object to store or null to remove any existing reference
     */
    public void setUserData(Object data) {
        ownerDocument().setUserData(this, data);
    }

    /**
     * NON-DOM:
     * Returns the user data associated to this node.
     */
    public Object getUserData() {
        return ownerDocument().getUserData(this);
    }


    /**
     * Denotes that this node has changed.
     */
    protected void changed() {
        ownerDocument().changed();
    }

    /**
     * Returns the number of changes to this node.
     */
    protected int changes() {
        return ownerDocument().changes();
    }

    /**
     * Override this method in subclass to hook in efficient
     * internal data structure.
     */
    protected void synchronizeData() {
        needsSyncData(false);
    }


    /*
     * Flags setters and getters
     */

    final boolean isReadOnly() {
        return (flags & READONLY) != 0;
    }

    final void isReadOnly(boolean value) {
        flags = (short) (value ? flags | READONLY : flags & ~READONLY);
    }

    final boolean needsSyncData() {
        return (flags & SYNCDATA) != 0;
    }

    final void needsSyncData(boolean value) {
        flags = (short) (value ? flags | SYNCDATA : flags & ~SYNCDATA);
    }

    final boolean needsSyncChildren() {
        return (flags & SYNCCHILDREN) != 0;
    }

    final void needsSyncChildren(boolean value) {
        flags = (short) (value ? flags | SYNCCHILDREN : flags & ~SYNCCHILDREN);
    }

    final boolean isOwned() {
        return (flags & OWNED) != 0;
    }

    final void isOwned(boolean value) {
        flags = (short) (value ? flags | OWNED : flags & ~OWNED);
    }

    final boolean isFirstChild() {
        return (flags & FIRSTCHILD) != 0;
    }

    final void isFirstChild(boolean value) {
        flags = (short) (value ? flags | FIRSTCHILD : flags & ~FIRSTCHILD);
    }

    final boolean isSpecified() {
        return (flags & SPECIFIED) != 0;
    }

    final void isSpecified(boolean value) {
        flags = (short) (value ? flags | SPECIFIED : flags & ~SPECIFIED);
    }

    final boolean internalIsIgnorableWhitespace() {
        return (flags & IGNORABLEWS) != 0;
    }

    final void isIgnorableWhitespace(boolean value) {
        flags = (short) (value ? flags | IGNORABLEWS : flags & ~IGNORABLEWS);
    }

    final boolean setValueCalled() {
        return (flags & SETVALUE) != 0;
    }

    final void setValueCalled(boolean value) {
        flags = (short) (value ? flags | SETVALUE : flags & ~SETVALUE);
    }

    final boolean hasStringValue() {
        return (flags & HASSTRING) != 0;
    }

    final void hasStringValue(boolean value) {
        flags = (short) (value ? flags | HASSTRING : flags & ~HASSTRING);
    }

    final boolean isNormalized() {
        return (flags & UNNORMALIZED) == 0;
    }

    final void isNormalized(boolean value) {
        if (!value && isNormalized() && ownerNode != null) {
            ownerNode.isNormalized(false);
        }
        flags = (short) (value ? flags & ~UNNORMALIZED : flags | UNNORMALIZED);
    }


    /** NON-DOM method for debugging convenience. */
    public String toString() {
        return "["+getNodeName()+": "+getNodeValue()+"]";
    }


    /** Serialize object. */
    private void writeObject(ObjectOutputStream out) throws IOException {

        if (needsSyncData()) {
            synchronizeData();
        }
        out.defaultWriteObject();


