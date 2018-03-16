package org.apache.xerces.dom;

import org.apache.xerces.utils.StringPool;

import org.w3c.dom.*;

/**
 * Notations are how the Document Type Description (DTD) records hints
 * about the format of an XML "unparsed entity" -- in other words,
 * non-XML data bound to this document type, which some applications
 * may wish to consult when manipulating the document. A Notation
 * represents a name-value pair, with its nodeName being set to the
 * declared name of the notation.
 * <P>
 * Notations are also used to formally declare the "targets" of
 * Processing Instructions.
 * <P>
 * Note that the Notation's data is non-DOM information; the DOM only
 * records what and where it is.
 * <P>
 * See the XML 1.0 spec, sections 4.7 and 2.6, for more info.
 * <P>
 * Level 1 of the DOM does not support editing Notation contents.
 *
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public class DeferredNotationImpl
    extends NotationImpl
    implements DeferredNode {


    /** Serialization version. */
    static final long serialVersionUID = 5705337172887990848L;


    /** Node index. */
    protected transient int fNodeIndex;


    /**
     * This is the deferred constructor. Only the fNodeIndex is given here.
     * All other data, can be requested from the ownerDocument via the index.
     */
    DeferredNotationImpl(DeferredDocumentImpl ownerDocument, int nodeIndex) {
        super(ownerDocument, null);

        fNodeIndex = nodeIndex;
        needsSyncData(true);



    /** Returns the node index. */
    public int getNodeIndex() {
        return fNodeIndex;
    }


    /**
     * Synchronizes the data. This is special because of the way
     * that the "fast" notation stores its information internally.
     */
    protected void synchronizeData() {

        needsSyncData(false);

        DeferredDocumentImpl ownerDocument =
            (DeferredDocumentImpl)this.ownerDocument();
        name = ownerDocument.getNodeNameString(fNodeIndex);

        StringPool pool = ownerDocument.getStringPool();
        int extraDataIndex = ownerDocument.getNodeValue(fNodeIndex);
        ownerDocument.getNodeType(extraDataIndex);
        publicId = pool.toString(ownerDocument.getNodeName(extraDataIndex));
        systemId = pool.toString(ownerDocument.getNodeValue(extraDataIndex));


