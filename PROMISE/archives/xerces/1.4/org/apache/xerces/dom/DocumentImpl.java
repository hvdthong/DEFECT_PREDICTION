package org.apache.xerces.dom;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Notation;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventException;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MutationEvent;
import org.w3c.dom.ranges.DocumentRange;
import org.w3c.dom.ranges.Range;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeIterator;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;

import org.apache.xerces.dom.events.EventImpl;
import org.apache.xerces.dom.events.MutationEventImpl;
import org.apache.xerces.utils.XMLCharacterProperties;


/**
 * The Document interface represents the entire HTML or XML document.
 * Conceptually, it is the root of the document tree, and provides the
 * primary access to the document's data.
 * <P>
 * Since elements, text nodes, comments, processing instructions,
 * etc. cannot exist outside the context of a Document, the Document
 * interface also contains the factory methods needed to create these
 * objects. The Node objects created have a ownerDocument attribute
 * which associates them with the Document within whose context they
 * were created.
 * <p>
 * The DocumentImpl class also implements the DOM Level 2 DocumentTraversal
 * interface. This interface is comprised of factory methods needed to
 * create NodeIterators and TreeWalkers. The process of creating NodeIterator
 * objects also adds these references to this document.
 * After finishing with an iterator it is important to remove the object
 * using the remove methods in this implementation. This allows the release of
 * the references from the iterator objects to the DOM Nodes.
 * <p>
 * <b>Note:</b> When any node in the document is serialized, the
 * entire document is serialized along with it.
 *
 * @author Arnaud  Le Hors, IBM
 * @author Joe Kesselman, IBM
 * @author Andy Clark, IBM
 * @author Ralf Pfeiffer, IBM
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public class DocumentImpl
    extends CoreDocumentImpl
    implements DocumentTraversal, DocumentEvent, DocumentRange {


    /** Serialization version. */
    static final long serialVersionUID = 515687835542616694L;


    /** Iterators */
    protected Vector iterators;

     /** Ranges */
    protected Vector ranges;

    /** Table for user data attached to this document nodes. */
    protected Hashtable userData;

    /** Table for event listeners registered to this document nodes. */
    protected Hashtable eventListeners;

    /** Bypass mutation events firing. */
    protected boolean mutationEvents = false;


    /**
     * NON-DOM: Actually creating a Document is outside the DOM's spec,
     * since it has to operate in terms of a particular implementation.
     */
    public DocumentImpl() {
        super();
    }

    /** Constructor. */
    public DocumentImpl(boolean grammarAccess) {
        super(grammarAccess);
    }

    /**
     * For DOM2 support.
     * The createDocument factory method is in DOMImplementation.
     */
    public DocumentImpl(DocumentType doctype)
    {
        super(doctype);
    }

    /** For DOM2 support. */
    public DocumentImpl(DocumentType doctype, boolean grammarAccess) {
        super(doctype, grammarAccess);
    }


    /**
     * Deep-clone a document, including fixing ownerDoc for the cloned
     * children. Note that this requires bypassing the WRONG_DOCUMENT_ERR
     * protection. I've chosen to implement it by calling importNode
     * which is DOM Level 2.
     *
     * @return org.w3c.dom.Node
     * @param deep boolean, iff true replicate children
     */
    public Node cloneNode(boolean deep) {

        DocumentImpl newdoc = new DocumentImpl();
        cloneNode(newdoc, deep);

        newdoc.mutationEvents = mutationEvents;

    	return newdoc;


    /**
     * Retrieve information describing the abilities of this particular
     * DOM implementation. Intended to support applications that may be
     * using DOMs retrieved from several different sources, potentially
     * with different underlying representations.
     */
    public DOMImplementation getImplementation() {
        return DOMImplementationImpl.getDOMImplementation();
    }

    /**
     * Store user data related to a given node
     * This is a place where we could use weak references! Indeed, the node
     * here won't be GC'ed as long as some user data is attached to it, since
     * the userData table will have a reference to the node.
     */
    protected void setUserData(NodeImpl n, Object data) {
        if (userData == null) {
            userData = new Hashtable();
        }
        if (data == null) {
            userData.remove(n);
        } else {
            userData.put(n, data);
        }
    }

    /**
     * Retreive user data related to a given node
     */
    protected Object getUserData(NodeImpl n) {
        if (userData == null) {
            return null;
        }
        return userData.get(n);
    }


    /**
     * NON-DOM extension:
     * Create and return a NodeIterator. The NodeIterator is
     * added to a list of NodeIterators so that it can be
     * removed to free up the DOM Nodes it references.
     * @see #removeNodeIterator
     * @see #removeNodeIterators
     *
     * @param root The root of the iterator.
     * @param whatToShow The whatToShow mask.
     * @param filter The NodeFilter installed. Null means no filter.
     */
    public NodeIterator createNodeIterator(Node root,
                                           short whatToShow,
                                           NodeFilter filter)
    {
        return createNodeIterator(root, whatToShow, filter, true);
    }

    /**
     * Create and return a NodeIterator. The NodeIterator is
     * added to a list of NodeIterators so that it can be
     * removed to free up the DOM Nodes it references.
     * @see #removeNodeIterator
     * @see #removeNodeIterators
     *
     * @param root The root of the iterator.
     * @param whatToShow The whatToShow mask.
     * @param filter The NodeFilter installed. Null means no filter.
     * @param entityReferenceExpansion true to expand the contents of
     *                                 EntityReference nodes
     * @since WD-DOM-Level-2-19990923
     */
    public NodeIterator createNodeIterator(Node root,
                                           int whatToShow,
                                           NodeFilter filter,
                                           boolean entityReferenceExpansion)
    {
        NodeIterator iterator = new NodeIteratorImpl(this,
                                                     root,
                                                     whatToShow,
                                                     filter,
                                                     entityReferenceExpansion);
        if (iterators == null) {
            iterators = new Vector();
        }

        iterators.addElement(iterator);

        return iterator;
    }

    /**
     * NON-DOM extension:
     * Create and return a TreeWalker.
     *
     * @param root The root of the iterator.
     * @param whatToShow The whatToShow mask.
     * @param filter The NodeFilter installed. Null means no filter.
     */
    public TreeWalker createTreeWalker(Node root,
                                       short whatToShow,
                                       NodeFilter filter)
    {
        return createTreeWalker(root, whatToShow, filter, true);
    }
    /**
     * Create and return a TreeWalker.
     *
     * @param root The root of the iterator.
     * @param whatToShow The whatToShow mask.
     * @param filter The NodeFilter installed. Null means no filter.
     * @param entityReferenceExpansion true to expand the contents of
     *                                 EntityReference nodes
     * @since WD-DOM-Level-2-19990923
     */
    public TreeWalker createTreeWalker(Node root,
                                       int whatToShow,
                                       NodeFilter filter,
                                       boolean entityReferenceExpansion)
    {
    	if (root == null) {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR,
                                   "DOM007 Not supported");
        }
        return new TreeWalkerImpl(root, whatToShow, filter,
                                  entityReferenceExpansion);
    }


    /** This is not called by the developer client. The
     *  developer client uses the detach() function on the
     *  NodeIterator itself. <p>
     *
     *  This function is called from the NodeIterator#detach().
     */
     void removeNodeIterator(NodeIterator nodeIterator) {

        if (nodeIterator == null) return;
        if (iterators == null) return;

        iterators.removeElement(nodeIterator);
    }

    /**
     */
    public Range createRange() {

        if (ranges == null) {
            ranges = new Vector();
        }

        Range range = new RangeImpl(this);

        ranges.addElement(range);

        return range;

    }

    /** Not a client function. Called by Range.detach(),
     *  so a Range can remove itself from the list of
     *  Ranges.
     */
    void removeRange(Range range) {

        if (range == null) return;
        if (ranges == null) return;

        ranges.removeElement(range);
    }

    /**
     * A method to be called when some text was changed in a text node,
     * so that live objects can be notified.
     */
    void replacedText(NodeImpl node) {
        if (ranges != null) {
            Enumeration enum = ranges.elements();
            while (enum.hasMoreElements()) {
                ((RangeImpl)enum.nextElement()).receiveReplacedText(node);
            }
        }
    }

    /**
     * A method to be called when some text was deleted from a text node,
     * so that live objects can be notified.
     */
    void deletedText(NodeImpl node, int offset, int count) {
        if (ranges != null) {
            Enumeration enum = ranges.elements();
            while (enum.hasMoreElements()) {
                ((RangeImpl)enum.nextElement()).receiveDeletedText(node,
                                                                   offset,
                                                                   count);
            }
        }
    }

    /**
     * A method to be called when some text was inserted into a text node,
     * so that live objects can be notified.
     */
    void insertedText(NodeImpl node, int offset, int count) {
        if (ranges != null) {
            Enumeration enum = ranges.elements();
            while (enum.hasMoreElements()) {
                ((RangeImpl)enum.nextElement()).receiveInsertedText(node,
                                                                    offset,
                                                                    count);
            }
        }
    }

    /**
     * A method to be called when a text node has been split,
     * so that live objects can be notified.
     */
    void splitData(Node node, Node newNode, int offset) {
        if (ranges != null) {
            Enumeration enum = ranges.elements();
            while (enum.hasMoreElements()) {
                ((RangeImpl)enum.nextElement()).receiveSplitData(node,
                                                                 newNode,
                                                                 offset);
            }
        }
    }


    /**
     * Introduced in DOM Level 2. Optional. <p>
     * Create and return Event objects.
     *
     * @param type The eventType parameter specifies the type of Event
     * interface to be created.  If the Event interface specified is supported
     * by the implementation this method will return a new Event of the
     * interface type requested. If the Event is to be dispatched via the
     * dispatchEvent method the appropriate event init method must be called
     * after creation in order to initialize the Event's values.  As an
     * example, a user wishing to synthesize some kind of Event would call
     * createEvent with the parameter "Events". The initEvent method could then
     * be called on the newly created Event to set the specific type of Event
     * to be dispatched and set its context information.
     * @return Newly created Event
     * @exception DOMException NOT_SUPPORTED_ERR: Raised if the implementation
     * does not support the type of Event interface requested
     * @since WD-DOM-Level-2-19990923
     */
    public Event createEvent(String type)
	throws DOMException {
	    if (type.equalsIgnoreCase("Events") || "Event".equals(type))
	        return new EventImpl();
	    if (type.equalsIgnoreCase("MutationEvents") ||
                "MutationEvent".equals(type))
	        return new MutationEventImpl();
	    else
	        throw new DOMException(DOMException.NOT_SUPPORTED_ERR,
					   "DOM007 Not supported");
	}

    /**
     * Sets whether the DOM implementation generates mutation events
     * upon operations.
     */
    void setMutationEvents(boolean set) {
        mutationEvents = set;
    }

    /**
     * Returns true if the DOM implementation generates mutation events.
     */
    boolean getMutationEvents() {
        return mutationEvents;
    }

    /**
     * Store event listener registered on a given node
     * This is another place where we could use weak references! Indeed, the
     * node here won't be GC'ed as long as some listener is registered on it,
     * since the eventsListeners table will have a reference to the node.
     */
    protected void setEventListeners(NodeImpl n, Vector listeners) {
        if (eventListeners == null) {
            eventListeners = new Hashtable();
        }
        if (listeners == null) {
            eventListeners.remove(n);
            if (eventListeners.isEmpty()) {
                mutationEvents = false;
            }
        } else {
            eventListeners.put(n, listeners);
            mutationEvents = true;
        }
    }

    /**
     * Retreive event listener registered on a given node
     */
    protected Vector getEventListeners(NodeImpl n) {
        if (eventListeners == null) {
            return null;
        }
        return (Vector) eventListeners.get(n);
    }



    /*
     * NON-DOM INTERNAL: Class LEntry is just a struct used to represent
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
        LEntry(String type, EventListener listener, boolean useCapture)
        {
            this.type = type;
            this.listener = listener;
            this.useCapture = useCapture;
        }
	
    /**
     * Introduced in DOM Level 2. <p> Register an event listener with this
     * Node. A listener may be independently registered as both Capturing and
     * Bubbling, but may only be registered once per role; redundant
     * registrations are ignored.
     * @param node node to add listener to
     * @param type Event name (NOT event group!) to listen for.
     * @param listener Who gets called when event is dispatched
     * @param useCapture True iff listener is registered on
     *  capturing phase rather than at-target or bubbling
     */
    protected void addEventListener(NodeImpl node, String type,
                                    EventListener listener, boolean useCapture)
    {
        if (type == null || type.equals("") || listener == null)
            return;

        removeEventListener(node, type, listener, useCapture);
	    
        Vector nodeListeners = getEventListeners(node);
        if(nodeListeners == null) {
            nodeListeners = new Vector();
            setEventListeners(node, nodeListeners);
        }
        nodeListeners.addElement(new LEntry(type, listener, useCapture));
	    
        LCount lc = LCount.lookup(type);
        if (useCapture)
            ++lc.captures;
        else
            ++lc.bubbles;

	
    /**
     * Introduced in DOM Level 2. <p> Deregister an event listener previously
     * registered with this Node.  A listener must be independently removed
     * from the Capturing and Bubbling roles. Redundant removals (of listeners
     * not currently registered for this role) are ignored.
     * @param node node to remove listener from
     * @param type Event name (NOT event group!) to listen for.
     * @param listener Who gets called when event is dispatched
     * @param useCapture True iff listener is registered on
     *  capturing phase rather than at-target or bubbling
     */
    protected void removeEventListener(NodeImpl node, String type,
                                       EventListener listener,
                                       boolean useCapture)
    {
        if (type == null || type.equals("") || listener == null)
            return;
        Vector nodeListeners = getEventListeners(node);
        if (nodeListeners == null)
            return;

        for (int i = nodeListeners.size() - 1; i >= 0; --i) {
            LEntry le = (LEntry) nodeListeners.elementAt(i);
            if (le.useCapture == useCapture && le.listener == listener && 
                le.type.equals(type)) {
                nodeListeners.removeElementAt(i);
                if (nodeListeners.size() == 0)
                    setEventListeners(node, null);

                LCount lc = LCount.lookup(type);
                if (useCapture)
                    --lc.captures;
                else
                    --lc.bubbles;

            }
        }
	
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
     * <li>Default processing: Some DOMs have default behaviors bound to
     *   specific nodes. If this DOM does, and if the event's preventDefault
     *   flag has not been set, we now return to the target node and process
     *   its default handler for this event, if any.
     * </ol>
     * <p>
     * Note that registration of handlers during processing of an event does
     * not take effect during this phase of this event; they will not be called
     * until the next time this node is visited by dispatchEvent. On the other
     * hand, removals take effect immediately.
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
     * @param node node to dispatch to
     * @param event the event object to be dispatched to 
     *              registered EventListeners
     * @return true if the event's <code>preventDefault()</code>
     *              method was invoked by an EventListener; otherwise false.
    */
    protected boolean dispatchEvent(NodeImpl node, Event event) {
        if (event == null) return false;
        
        EventImpl evt = (EventImpl)event;

        if(!evt.initialized || evt.type == null || evt.type.equals(""))
            throw new EventException(EventException.UNSPECIFIED_EVENT_TYPE_ERR,
                                     "DOM010 Unspecified event type");
        
        LCount lc = LCount.lookup(evt.getType());
        if (lc.captures + lc.bubbles + lc.defaults == 0)
            return evt.preventDefault;

        evt.target = node;
        evt.stopPropagation = false;
        evt.preventDefault = false;
        
        Vector pv = new Vector(10,10);
        Node p = node;
        Node n = p.getParentNode();
        while (n != null) {
            pv.addElement(n);
            p = n;
            n = n.getParentNode();
        }
        
        if (lc.captures > 0) {
            evt.eventPhase = Event.CAPTURING_PHASE;
            for (int j = pv.size() - 1; j >= 0; --j) {
                if (evt.stopPropagation)

                NodeImpl nn = (NodeImpl) pv.elementAt(j);
                evt.currentTarget = nn;
                Vector nodeListeners = getEventListeners(nn);
                if (nodeListeners != null) {
                    Vector nl = (Vector) nodeListeners.clone();
                    for (int i = nl.size() - 1; i >= 0; --i) {
                        LEntry le = (LEntry) nl.elementAt(i);
                        if (le.useCapture && le.type.equals(evt.type) &&
                            nodeListeners.contains(le)) {
                            try {
                                le.listener.handleEvent(evt);
                            }
                            catch (Exception e) {
                            }
                        }
                    }
                }
            }
        }
        
        if (lc.bubbles > 0) {
            evt.eventPhase = Event.AT_TARGET;
            evt.currentTarget = node;
            Vector nodeListeners = getEventListeners(node);
            if (!evt.stopPropagation && nodeListeners != null) {
                Vector nl = (Vector) nodeListeners.clone();
                for (int i = nl.size() - 1; i >= 0; --i) {
                    LEntry le = (LEntry) nl.elementAt(i);
                    if (!le.useCapture && le.type.equals(evt.type) &&
                        nodeListeners.contains(le)) {
                        try {
                            le.listener.handleEvent(evt);
                        }
                        catch (Exception e) {
                        }
                    }
                }
            }
            if (evt.bubbles) {
                evt.eventPhase = Event.BUBBLING_PHASE;
                for (int j = 0; j < pv.size(); ++j) {
                    if (evt.stopPropagation)

                    NodeImpl nn = (NodeImpl) pv.elementAt(j);
                    evt.currentTarget = nn;
                    nodeListeners = getEventListeners(nn);
                    if (nodeListeners != null) {
                        Vector nl = (Vector) nodeListeners.clone();
                        for (int i = nl.size() - 1; i >= 0; --i) {
                            LEntry le = (LEntry) nl.elementAt(i);
                            if (!le.useCapture && le.type.equals(evt.type) &&
                                nodeListeners.contains(le)) {
                                try {
                                    le.listener.handleEvent(evt);
                                }
                                catch (Exception e) {
                                }
                            }
                        }
                    }
                }
            }
        }
        
        if (lc.defaults > 0 && (!evt.cancelable || !evt.preventDefault)) {
        }

        return evt.preventDefault;        


    /**
     * NON-DOM INTERNAL: DOMNodeInsertedIntoDocument and ...RemovedFrom...
     * are dispatched to an entire subtree. This is the distribution code
     * therefor. They DO NOT bubble, thanks be, but may be captured.
     * <p>
     * ***** At the moment I'm being sloppy and using the normal
     * capture dispatcher on every node. This could be optimized hugely
     * by writing a capture engine that tracks our position in the tree to
     * update the capture chain without repeated chases up to root.
     * @param node node to dispatch to
     * @param n node which was directly inserted or removed
     * @param e event to be sent to that node and its subtree
     */
    protected void dispatchEventToSubtree(NodeImpl node, Node n, Event e) {
        Vector nodeListeners = getEventListeners(node);
        if (nodeListeners == null || n == null)
            return;

        ((NodeImpl) n).dispatchEvent(e);
        if (n.getNodeType() == Node.ELEMENT_NODE) {
            NamedNodeMap a = n.getAttributes();
            for (int i = a.getLength() - 1; i >= 0; --i)
                dispatchEventToSubtree(node, a.item(i), e);
        }
        dispatchEventToSubtree(node, n.getFirstChild(), e);
        dispatchEventToSubtree(node, n.getNextSibling(), e);

    /**
     * NON-DOM INTERNAL: Return object for getEnclosingAttr. Carries
     * (two values, the Attr node affected (if any) and its previous 
     * string value. Simple struct, no methods.
     */
    class EnclosingAttr
    {
        AttrImpl node;
        String oldvalue;
    }

    EnclosingAttr savedEnclosingAttr;

    /**
     * NON-DOM INTERNAL: Convenience wrapper for calling
     * dispatchAggregateEvents when the context was established
     * by <code>savedEnclosingAttr</code>.
     * @param node node to dispatch to
     * @param ea description of Attr affected by current operation
     */
    protected void dispatchAggregateEvents(NodeImpl node, EnclosingAttr ea) {
        if (ea != null)
            dispatchAggregateEvents(node, ea.node, ea.oldvalue,
                                    MutationEvent.MODIFICATION);
        else
            dispatchAggregateEvents(node, null, null, (short) 0);
	        

    /**
     * NON-DOM INTERNAL: Generate the "aggregated" post-mutation events
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
     * @param node The node to dispatch to
     * @param enclosingAttr The Attr node (if any) whose value has been changed
     * as a result of the DOM operation. Null if none such.
     * @param oldValue The String value previously held by the
     * enclosingAttr. Ignored if none such.
     * @param change Type of modification to the attr. See
     * MutationEvent.attrChange
     */
    protected void dispatchAggregateEvents(NodeImpl node,
                                           AttrImpl enclosingAttr,
                                           String oldvalue, short change) {
        NodeImpl owner = null;
        if (enclosingAttr != null) {
            LCount lc = LCount.lookup(MutationEventImpl.DOM_ATTR_MODIFIED);
            owner = (NodeImpl) enclosingAttr.getOwnerElement();
            if (lc.captures + lc.bubbles + lc.defaults > 0) {
                if (owner != null) {
                    MutationEventImpl me =  new MutationEventImpl();
                    me.initMutationEvent(MutationEventImpl.DOM_ATTR_MODIFIED,
                                         true, false, enclosingAttr,
                                         oldvalue,
                                         enclosingAttr.getNodeValue(),
                                         enclosingAttr.getNodeName(),
                                         change);
                    owner.dispatchEvent(me);
                }
            }
        }
        LCount lc = LCount.lookup(MutationEventImpl.DOM_SUBTREE_MODIFIED);
        if (lc.captures + lc.bubbles + lc.defaults > 0) {
            MutationEvent me =  new MutationEventImpl();
            me.initMutationEvent(MutationEventImpl.DOM_SUBTREE_MODIFIED,
                                 true, false, null, null,
                                 null, null, (short) 0);

            if (enclosingAttr != null) {
                dispatchEvent(enclosingAttr, me);
                if (owner != null)
                    dispatchEvent(owner, me);
            }
            else
                dispatchEvent(node, me);
        }

    /**
     * NON-DOM INTERNAL: Pre-mutation context check, in
     * preparation for later generating DOMAttrModified events.
     * Determines whether this node is within an Attr
     * @param node node to get enclosing attribute for
     * @return either a description of that Attr, or null if none such. 
     */
    protected void saveEnclosingAttr(NodeImpl node) {
        savedEnclosingAttr = null;
        LCount lc = LCount.lookup(MutationEventImpl.DOM_ATTR_MODIFIED);
        if (lc.captures + lc.bubbles + lc.defaults > 0) {
            NodeImpl eventAncestor = node;
            while (true) {
                if (eventAncestor == null)
                    return;
                int type = eventAncestor.getNodeType();
                if (type == Node.ATTRIBUTE_NODE) {
                    EnclosingAttr retval = new EnclosingAttr();
                    retval.node = (AttrImpl) eventAncestor;
                    retval.oldvalue = retval.node.getNodeValue();
                    savedEnclosingAttr = retval;
                    return;
                }
                else if (type == Node.ENTITY_REFERENCE_NODE)
                    eventAncestor = eventAncestor.parentNode();
                else
                    return;
            }
        }

    /**
     * A method to be called when a character data node has been modified
     */
    void modifyingCharacterData(NodeImpl node) {
        if (mutationEvents) {
            saveEnclosingAttr(node);
        }
    }

    /**
     * A method to be called when a character data node has been modified
     */
    void modifiedCharacterData(NodeImpl node, String oldvalue, String value) {
        if (mutationEvents) {
            LCount lc =
                LCount.lookup(MutationEventImpl.DOM_CHARACTER_DATA_MODIFIED);
            if (lc.captures + lc.bubbles + lc.defaults > 0) {
                MutationEvent me = new MutationEventImpl();
                me.initMutationEvent(
                                 MutationEventImpl.DOM_CHARACTER_DATA_MODIFIED,
                                     true, false, null,
                                     oldvalue, value, null, (short) 0);
                dispatchEvent(me);
            }
            
            dispatchAggregateEvents(node, savedEnclosingAttr);
    }

    /**
     * A method to be called when a node is about to be inserted in the tree.
     */
    void insertingNode(NodeImpl node, boolean replace) {
        if (mutationEvents) {
            if (!replace) {
                saveEnclosingAttr(node);
            }
        }
    }

    /**
     * A method to be called when a node has been inserted in the tree.
     */
    void insertedNode(NodeImpl node, NodeImpl newInternal, boolean replace) {
        if (mutationEvents) {
            LCount lc = LCount.lookup(MutationEventImpl.DOM_NODE_INSERTED);
            if (lc.captures + lc.bubbles + lc.defaults > 0) {
                MutationEventImpl me = new MutationEventImpl();
                me.initMutationEvent(MutationEventImpl.DOM_NODE_INSERTED,
                                     true, false, node,
                                     null, null, null, (short) 0);
                dispatchEvent(newInternal, me);
            }

            lc = LCount.lookup(
                            MutationEventImpl.DOM_NODE_INSERTED_INTO_DOCUMENT);
            if (lc.captures + lc.bubbles + lc.defaults > 0) {
                NodeImpl eventAncestor = node;
                if (savedEnclosingAttr != null)
                    eventAncestor = (NodeImpl)
                        savedEnclosingAttr.node.getOwnerElement();
                    NodeImpl p = eventAncestor;
                    while (p != null) {
                        if (p.getNodeType() == ATTRIBUTE_NODE) {
                            p = (NodeImpl) ((AttrImpl)p).getOwnerElement();
                        }
                        else {
                            p = p.parentNode();
                        }
                    }
                    if (eventAncestor.getNodeType() == Node.DOCUMENT_NODE){
                        MutationEventImpl me = new MutationEventImpl();
                        me.initMutationEvent(MutationEventImpl
                                             .DOM_NODE_INSERTED_INTO_DOCUMENT,
                                             false,false,null,null,
                                             null,null,(short)0);
                        dispatchEventToSubtree(node, newInternal, me);
                    }
                }
            }
            if (!replace) {
                dispatchAggregateEvents(node, savedEnclosingAttr);
            }
        }
    }

    /**
     * A method to be called when a node is about to be removed from the tree.
     */
    void removingNode(NodeImpl node, NodeImpl oldChild, boolean replace) {

        if (iterators != null) {
            Enumeration enum = iterators.elements();
            while (enum.hasMoreElements()) {
                ((NodeIteratorImpl)enum.nextElement()).removeNode(oldChild);
            }
        }

        if (ranges != null) {
            Enumeration enum = ranges.elements();
            while (enum.hasMoreElements()) {
                ((RangeImpl)enum.nextElement()).removeNode(oldChild);
            }
        }

        if (mutationEvents) {
            if (!replace) {
                saveEnclosingAttr(node);
            }
            LCount lc = LCount.lookup(MutationEventImpl.DOM_NODE_REMOVED);
            if (lc.captures + lc.bubbles + lc.defaults > 0) {
                MutationEventImpl me= new MutationEventImpl();
                me.initMutationEvent(MutationEventImpl.DOM_NODE_REMOVED,
                                     true, false, node, null,
                                     null, null, (short) 0);
                dispatchEvent(oldChild, me);
            }

            lc = LCount.lookup(
                             MutationEventImpl.DOM_NODE_REMOVED_FROM_DOCUMENT);
            if (lc.captures + lc.bubbles + lc.defaults > 0) {
                NodeImpl eventAncestor = this;
                if(savedEnclosingAttr != null)
                    eventAncestor = (NodeImpl)
                        savedEnclosingAttr.node.getOwnerElement();
                    for (NodeImpl p = eventAncestor.parentNode();
                         p != null; p = p.parentNode()) {
                    }
                    if (eventAncestor.getNodeType() == Node.DOCUMENT_NODE){
                        MutationEventImpl me = new MutationEventImpl();
                        me.initMutationEvent(
                              MutationEventImpl.DOM_NODE_REMOVED_FROM_DOCUMENT,
                                             false, false, null,
                                             null, null, null, (short) 0);
                        dispatchEventToSubtree(node, oldChild, me);
                    }
                }
            }
    }

    /**
     * A method to be called when a node has been removed from the tree.
     */
    void removedNode(NodeImpl node, boolean replace) {
        if (mutationEvents) {
            if (!replace) {
                dispatchAggregateEvents(node, savedEnclosingAttr);
            }
    }

    /**
     * A method to be called when a node is about to be replaced in the tree.
     */
    void replacingNode(NodeImpl node) {
        if (mutationEvents) {
            saveEnclosingAttr(node);
        }
    }

    /**
     * A method to be called when a node has been replaced in the tree.
     */
    void replacedNode(NodeImpl node) {
        if (mutationEvents) {
            dispatchAggregateEvents(node, savedEnclosingAttr);
        }
    }

    /**
     * A method to be called when an attribute value has been modified
     */
    void modifiedAttrValue(AttrImpl attr, String oldvalue) {
        if (mutationEvents) {
            dispatchAggregateEvents(attr, attr, oldvalue,
                                    MutationEvent.MODIFICATION);
        }
    }

    /**
     * A method to be called when an attribute node has been set
     */
    void setAttrNode(AttrImpl attr, AttrImpl previous) {
        if (mutationEvents) {
            if (previous == null) {
                dispatchAggregateEvents(attr.ownerNode, attr, null,
                                        MutationEvent.ADDITION);
            }
            else {
                dispatchAggregateEvents(attr.ownerNode, attr,
                                        previous.getNodeValue(),
                                        MutationEvent.MODIFICATION);
            }
        }
    }

    /**
     * A method to be called when an attribute node has been removed
     */
    void removedAttrNode(AttrImpl attr, NodeImpl oldOwner, String name) {
        if (mutationEvents) {
            LCount lc = LCount.lookup(MutationEventImpl.DOM_ATTR_MODIFIED);
            if (lc.captures + lc.bubbles + lc.defaults > 0) {
                MutationEventImpl me= new MutationEventImpl();
                me.initMutationEvent(MutationEventImpl.DOM_ATTR_MODIFIED,
                                     true, false, null,
                                     attr.getNodeValue(), null, name,
                                     MutationEvent.REMOVAL);
                dispatchEvent(oldOwner, me);
            }

            dispatchAggregateEvents(oldOwner, null, null, (short) 0);
        }
    }

