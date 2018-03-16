package org.apache.xerces.dom;

/**
 * Processing Instructions (PIs) permit documents to carry
 * processor-specific information alongside their actual content. PIs
 * are most common in XML, but they are supported in HTML as well.
 *
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public class DeferredProcessingInstructionImpl
    extends ProcessingInstructionImpl 
    implements DeferredNode {


    /** Serialization version. */
    static final long serialVersionUID = -4643577954293565388L;
    

    /** Node index. */
    protected transient int fNodeIndex;


    /**
     * This is the deferred constructor. Only the fNodeIndex is given here. 
     * All other data, can be requested from the ownerDocument via the index.
     */
    DeferredProcessingInstructionImpl(DeferredDocumentImpl ownerDocument, int nodeIndex) {
        super(ownerDocument, null, null);

        fNodeIndex = nodeIndex;
        needsSyncData(true);



    /** Returns the node index. */
    public int getNodeIndex() {
        return fNodeIndex;
    }


    /** Synchronizes the data. */
    protected void synchronizeData() {

        needsSyncData(false);

        DeferredDocumentImpl ownerDocument =
            (DeferredDocumentImpl) this.ownerDocument();
        target  = ownerDocument.getNodeNameString(fNodeIndex);
        data = ownerDocument.getNodeValueString(fNodeIndex);


