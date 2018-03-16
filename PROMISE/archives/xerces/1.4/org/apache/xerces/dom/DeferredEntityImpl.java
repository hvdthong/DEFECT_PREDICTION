package org.apache.xerces.dom;

import org.apache.xerces.utils.StringPool;

/**
 * Entity nodes hold the reference data for an XML Entity -- either
 * parsed or unparsed. The nodeName (inherited from Node) will contain
 * the name (if any) of the Entity. Its data will be contained in the
 * Entity's children, in exactly the structure which an
 * EntityReference to this name will present within the document's
 * body.
 * <P>
 * Note that this object models the actual entity, _not_ the entity
 * declaration or the entity reference.
 * <P>
 * An XML processor may choose to completely expand entities before
 * the structure model is passed to the DOM; in this case, there will
 * be no EntityReferences in the DOM tree.
 * <P>
 * Quoting the 10/01 DOM Proposal,
 * <BLOCKQUOTE>
 * "The DOM Level 1 does not support editing Entity nodes; if a user
 * wants to make changes to the contents of an Entity, every related
 * EntityReference node has to be replaced in the structure model by
 * a clone of the Entity's contents, and then the desired changes
 * must be made to each of those clones instead. All the
 * descendants of an Entity node are readonly."
 * </BLOCKQUOTE>
 * I'm interpreting this as: It is the parser's responsibilty to call
 * the non-DOM operation setReadOnly(true,true) after it constructs
 * the Entity. Since the DOM explicitly decided not to deal with this,
 * _any_ answer will involve a non-DOM operation, and this is the
 * simplest solution.
 *
 *
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public class DeferredEntityImpl
    extends EntityImpl
    implements DeferredNode {


    /** Serialization version. */
    static final long serialVersionUID = 4760180431078941638L;


    /** Node index. */
    protected transient int fNodeIndex;


    /**
     * This is the deferred constructor. Only the fNodeIndex is given here.
     * All other data, can be requested from the ownerDocument via the index.
     */
    DeferredEntityImpl(DeferredDocumentImpl ownerDocument, int nodeIndex) {
        super(ownerDocument, null);

        fNodeIndex = nodeIndex;
        needsSyncData(true);
        needsSyncChildren(true);



    /** Returns the node index. */
    public int getNodeIndex() {
        return fNodeIndex;
    }


    /**
     * Synchronize the entity data. This is special because of the way
     * that the "fast" version stores the information.
     */
    protected void synchronizeData() {

        needsSyncData(false);

        DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl)this.ownerDocument;
        name = ownerDocument.getNodeNameString(fNodeIndex);

        StringPool pool = ownerDocument.getStringPool();
        int extraDataIndex = ownerDocument.getNodeValue(fNodeIndex);
        ownerDocument.getNodeType(extraDataIndex);
        publicId     = pool.toString(ownerDocument.getNodeName(extraDataIndex));
        notationName = pool.toString(ownerDocument.getLastChild(extraDataIndex));

        extraDataIndex = ownerDocument.getNodeValue(extraDataIndex);
        systemId    = pool.toString(ownerDocument.getNodeName(extraDataIndex));
        version     = pool.toString(ownerDocument.getNodeValue(extraDataIndex));
        encoding    = pool.toString(ownerDocument.getLastChild(extraDataIndex));
         

    /** Synchronize the children. */
    protected void synchronizeChildren() {

        needsSyncChildren(false);

        isReadOnly(false);
        DeferredDocumentImpl ownerDocument =
            (DeferredDocumentImpl) ownerDocument();
        ownerDocument.synchronizeChildren(this, fNodeIndex);
        setReadOnly(true, true);


