package org.apache.xerces.dom;

import org.w3c.dom.*;

/**
 * NON-DOM CLASS: Describe one of the Elements (and its associated
 * Attributes) defined in this Document Type.
 * <p>
 * I've included this in Level 1 purely as an anchor point for default
 * attributes. In Level 2 it should enable the ChildRule support.
 *
 * @version
 */
public class DeferredElementDefinitionImpl 
    extends ElementDefinitionImpl 
    implements DeferredNode {


    /** Serialization version. */
    static final long serialVersionUID = 6703238199538041591L;
    

    /** Node index. */
    protected transient int fNodeIndex;


    /**
     * This is the deferred constructor. Only the fNodeIndex is given here.
     * All other data, can be requested from the ownerDocument via the index.
     */
    DeferredElementDefinitionImpl(DeferredDocumentImpl ownerDocument, int nodeIndex) {
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
            (DeferredDocumentImpl)this.ownerDocument;
        name = ownerDocument.getNodeNameString(fNodeIndex);


    /** Synchronizes the default attribute values. */
    protected void synchronizeChildren() {

        boolean orig = ownerDocument.mutationEvents;
        ownerDocument.mutationEvents = false;

        needsSyncChildren(false);

        DeferredDocumentImpl ownerDocument =
            (DeferredDocumentImpl)this.ownerDocument;
        attributes = new NamedNodeMapImpl(ownerDocument);

        for (int nodeIndex = ownerDocument.getLastChild(fNodeIndex);
             nodeIndex != -1;
             nodeIndex = ownerDocument.getPrevSibling(nodeIndex)) {
            Node attr = ownerDocument.getNodeObject(nodeIndex);
            attributes.setNamedItem(attr);
        }

        ownerDocument.mutationEvents = orig;


