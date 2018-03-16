package org.apache.xerces.dom;

import org.w3c.dom.Node;
import org.apache.xerces.utils.StringPool;

/**
 * This class represents a Document Type <em>declaraction</em> in
 * the document itself, <em>not</em> a Document Type Definition (DTD).
 * An XML document may (or may not) have such a reference.
 * <P>
 * DocumentType is an Extended DOM feature, used in XML documents but
 * not in HTML.
 * <P>
 * Note that Entities and Notations are no longer children of the
 * DocumentType, but are parentless nodes hung only in their
 * appropriate NamedNodeMaps.
 * <P>
 * This area is UNDERSPECIFIED IN REC-DOM-Level-1-19981001
 * Most notably, absolutely no provision was made for storing
 * and using Element and Attribute information. Nor was the linkage
 * between Entities and Entity References nailed down solidly.
 *
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public class DeferredDocumentTypeImpl
    extends DocumentTypeImpl
    implements DeferredNode {


    /** Serialization version. */
    static final long serialVersionUID = -2172579663227313509L;


    /** Node index. */
    protected transient int fNodeIndex;


    /**
     * This is the deferred constructor. Only the fNodeIndex is given here.
     * All other data, can be requested from the ownerDocument via the index.
     */
    DeferredDocumentTypeImpl(DeferredDocumentImpl ownerDocument, int nodeIndex) {
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

        StringPool pool = ownerDocument.getStringPool();
        int extraDataIndex = ownerDocument.getNodeValue(fNodeIndex);
        publicID = pool.toString(ownerDocument.getNodeName(extraDataIndex));
        systemID = pool.toString(ownerDocument.getNodeValue(extraDataIndex));
        internalSubset =
            pool.toString(ownerDocument.getLastChild(extraDataIndex));

    /** Synchronizes the entities, notations, and elements. */
    protected void synchronizeChildren() {
        
        boolean orig = ownerDocument().getMutationEvents();
        ownerDocument().setMutationEvents(false);

        needsSyncChildren(false);

        DeferredDocumentImpl ownerDocument =
            (DeferredDocumentImpl)this.ownerDocument;

        entities  = new NamedNodeMapImpl(this);
        notations = new NamedNodeMapImpl(this);
        elements  = new NamedNodeMapImpl(this);

        DeferredNode last = null;
        for (int index = ownerDocument.getLastChild(fNodeIndex);
            index != -1;
            index = ownerDocument.getPrevSibling(index)) {

            DeferredNode node = ownerDocument.getNodeObject(index);
            int type = node.getNodeType();
            switch (type) {

                case Node.ENTITY_NODE: {
                    entities.setNamedItem(node);
                    break;
                }

                case Node.NOTATION_NODE: {
                    notations.setNamedItem(node);
                    break;
                }

                case NodeImpl.ELEMENT_DEFINITION_NODE: {
                    elements.setNamedItem(node);
                    break;
                }

                case Node.ELEMENT_NODE: {
                    if (((DocumentImpl)getOwnerDocument()).allowGrammarAccess) {
                        insertBefore(node, last);
                        last = node;
                        break;
                    }
                }

                default: {
                    System.out.println("DeferredDocumentTypeImpl#synchronizeInfo: node.getNodeType() = "+node.getNodeType()+", class = "+node.getClass().getName());
                }
             }
        }

        ownerDocument().setMutationEvents(orig);

        setReadOnly(true, false);


