package org.apache.xerces.dom;

import java.util.Enumeration;
import java.util.Vector;

import org.w3c.dom.*;
import org.w3c.dom.events.*;

/**
 * ChildNode inherits from NodeImpl and adds the capability of being a child by
 * having references to its previous and next siblings.
 */
public abstract class ChildNode
    extends NodeImpl {


    /** Serialization version. */
    static final long serialVersionUID = -6112455738802414002L;



    /** Previous sibling. */
    protected ChildNode previousSibling;

    /** Next sibling. */
    protected ChildNode nextSibling;


    /**
     * No public constructor; only subclasses of Node should be
     * instantiated, and those normally via a Document's factory methods
     * <p>
     * Every Node knows what Document it belongs to.
     */
    protected ChildNode(DocumentImpl ownerDocument) {
        super(ownerDocument);

    /** Constructor for serialization. */
    public ChildNode() {}


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

    	ChildNode newnode = (ChildNode) super.cloneNode(deep);
    	
    	newnode.previousSibling = null;
        newnode.nextSibling     = null;
        newnode.isFirstChild(false);

    	return newnode;


    /**
     * Returns the parent node of this node
     */
    public Node getParentNode() {
        return isOwned() ? ownerNode : null;
    }

    /*
     * same as above but returns internal type
     */
    final NodeImpl parentNode() {
        return isOwned() ? ownerNode : null;
    }

    /** The next child of this node's parent, or null if none */
    public Node getNextSibling() {
        return nextSibling;
    }

    /** The previous child of this node's parent, or null if none */
    public Node getPreviousSibling() {
        return isFirstChild() ? null : previousSibling;
    }

    /*
     * same as above but returns internal type
     */
    final ChildNode previousSibling() {
        return isFirstChild() ? null : previousSibling;
    }

