package org.apache.xerces.dom;

/**
 * XML provides the CDATA markup to allow a region of text in which
 * most of the XML delimiter recognition does not take place. This is
 * intended to ease the task of quoting XML fragments and other
 * programmatic information in a document's text without needing to
 * escape these special characters. It's primarily a convenience feature
 * for those who are hand-editing XML.
 * <P>
 * CDATASection is an Extended DOM feature, and is not used in HTML 
 * contexts.
 * <P>
 * Within the DOM, CDATASections are treated essentially as Text
 * blocks. Their distinct type is retained in order to allow us to
 * properly recreate the XML syntax when we write them out.
 * <P>
 * Reminder: CDATA IS NOT A COMPLETELY GENERAL SOLUTION; it can't
 * quote its own end-of-block marking. If you need to write out a
 * CDATA that contains the ]]> sequence, it's your responsibility to
 * split that string over two successive CDATAs at that time.
 * <P>
 * CDATA does not participate in Element.normalize() processing.
 *
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public class DeferredCDATASectionImpl 
    extends CDATASectionImpl 
    implements DeferredNode {


    /** Serialization version. */
    static final long serialVersionUID = 1983580632355645726L;


    /** Node index. */
    protected transient int fNodeIndex;


    /**
     * This is the deferred constructor. Only the fNodeIndex is given here. All other data,
     * can be requested from the ownerDocument via the index.
     */
    DeferredCDATASectionImpl(DeferredDocumentImpl ownerDocument, int nodeIndex) {
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
        isIgnorableWhitespace(ownerDocument.getLastChild(fNodeIndex) == 1);


