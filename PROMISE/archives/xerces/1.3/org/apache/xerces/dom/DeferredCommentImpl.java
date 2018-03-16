package org.apache.xerces.dom;

import org.w3c.dom.*;

/**
 * Represents an XML (or HTML) comment.
 *
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public class DeferredCommentImpl 
    extends CommentImpl 
    implements DeferredNode {


    /** Serialization version. */
    static final long serialVersionUID = 6498796371083589338L;


    /** Node index. */
    protected transient int fNodeIndex;


    /**
     * This is the deferred constructor. Only the fNodeIndex is given here. All other data,
     * can be requested from the ownerDocument via the index.
     */
    DeferredCommentImpl(DeferredDocumentImpl ownerDocument, int nodeIndex) {
        super(ownerDocument, null);

        fNodeIndex = nodeIndex;
        needsSyncData(true);

    

    /** Returns the node index. */
    public int getNodeIndex() {
        return fNodeIndex;
    }


    /** Synchronizes the data (name and value) for fast nodes. */
    protected void synchronizeData() {

        needsSyncData(false);

        DeferredDocumentImpl ownerDocument =
            (DeferredDocumentImpl) this.ownerDocument();
        data = ownerDocument.getNodeValueString(fNodeIndex);


