package org.apache.xerces.dom;

import org.w3c.dom.*;

import org.apache.xerces.utils.StringPool;

/**
 * Attribute represents an XML-style attribute of an
 * Element. Typically, the allowable values are controlled by its
 * declaration in the Document Type Definition (DTD) governing this
 * kind of document.
 * <P>
 * If the attribute has not been explicitly assigned a value, but has
 * been declared in the DTD, it will exist and have that default. Only
 * if neither the document nor the DTD specifies a value will the
 * Attribute really be considered absent and have no value; in that
 * case, querying the attribute will return null.
 * <P>
 * Attributes may have multiple children that contain their data. (XML
 * allows attributes to contain entity references, and tokenized
 * attribute types such as NMTOKENS may have a child for each token.)
 * For convenience, the Attribute object's getValue() method returns
 * the string version of the attribute's value.
 * <P>
 * Attributes are not children of the Elements they belong to, in the
 * usual sense, and have no valid Parent reference. However, the spec
 * says they _do_ belong to a specific Element, and an INUSE exception
 * is to be thrown if the user attempts to explicitly share them
 * between elements.
 * <P>
 * Note that Elements do not permit attributes to appear to be shared
 * (see the INUSE exception), so this object's mutability is
 * officially not an issue.
 * <P>
 * DeferredAttrImpl inherits from AttrImpl which does not support
 * Namespaces. DeferredAttrNSImpl, which inherits from AttrNSImpl, does.
 * @see DeferredAttrNSImpl
 *
 *
 * @author Andy Clark, IBM
 * @author Arnaud  Le Hors, IBM
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public final class DeferredAttrImpl
    extends AttrImpl
    implements DeferredNode {


    /** Serialization version. */
    static final long serialVersionUID = 6903232312469148636L;


    /** Node index. */
    protected transient int fNodeIndex;


    /**
     * This is the deferred constructor. Only the fNodeIndex is given here.
     * All other data, can be requested from the ownerDocument via the index.
     */
    DeferredAttrImpl(DeferredDocumentImpl ownerDocument, int nodeIndex) {
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
        int elementTypeName = ownerDocument.getNodeName(fNodeIndex);
        StringPool pool = ownerDocument.getStringPool();
        name = pool.toString(elementTypeName);
        isSpecified(ownerDocument.getNodeValue(fNodeIndex) == 1);


    /**
     * Synchronizes the node's children with the internal structure.
     * Fluffing the children at once solves a lot of work to keep
     * the two structures in sync. The problem gets worse when
     * editing the tree -- this makes it a lot easier.
     */
    protected void synchronizeChildren() {
        synchronizeChildren(fNodeIndex);

