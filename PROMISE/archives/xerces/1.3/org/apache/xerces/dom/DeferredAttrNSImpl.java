package org.apache.xerces.dom;

import org.w3c.dom.*;

import org.apache.xerces.utils.StringPool;

/**
 * DeferredAttrNSImpl is to AttrNSImpl, what DeferredAttrImpl is to
 * AttrImpl. 
 * @author Andy Clark, IBM
 * @author Arnaud  Le Hors, IBM
 * @see DeferredAttrImpl
 */
public final class DeferredAttrNSImpl
    extends AttrNSImpl
    implements DeferredNode {


    /** Serialization version. */
    static final long serialVersionUID = 6074924934945957154L;


    /** Node index. */
    protected transient int fNodeIndex;


    /**
     * This is the deferred constructor. Only the fNodeIndex is given here.
     * All other data, can be requested from the ownerDocument via the index.
     */
    DeferredAttrNSImpl(DeferredDocumentImpl ownerDocument, int nodeIndex) {
        super(ownerDocument, null);

        fNodeIndex = nodeIndex;
        needsSyncData(true);
        needsSyncChildren(true);



    /** Returns the node index. */
    public int getNodeIndex() {
        return fNodeIndex;
    }


    /** Synchronizes the data (name and value) for fast nodes. */
    protected void synchronizeData() {

        needsSyncData(false);

        DeferredDocumentImpl ownerDocument =
	    (DeferredDocumentImpl) ownerDocument();
        int attrQName = ownerDocument.getNodeName(fNodeIndex);
        StringPool pool = ownerDocument.getStringPool();
        name = pool.toString(attrQName);

        int index = name.indexOf(':');
        String prefix;
        if (index < 0) {
            prefix = null;
            localName = name;
        } 
        else {
            prefix = name.substring(0, index); 
            localName = name.substring(index + 1);
        }

        isSpecified(ownerDocument.getNodeValue(fNodeIndex) == 1);
        namespaceURI = pool.toString(ownerDocument.getNodeURI(fNodeIndex));
	if (namespaceURI == null) {
	    if (prefix != null)  {
		if (prefix.equals("xmlns")) {
		}
	    } else if (name.equals("xmlns")) {
	    }
	}


    /**
     * Synchronizes the node's children with the internal structure.
     * Fluffing the children at once solves a lot of work to keep
     * the two structures in sync. The problem gets worse when
     * editing the tree -- this makes it a lot easier.
     */
    protected void synchronizeChildren() {
        synchronizeChildren(fNodeIndex);

