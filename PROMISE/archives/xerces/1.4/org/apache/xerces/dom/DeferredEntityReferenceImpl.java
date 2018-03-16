package org.apache.xerces.dom;

import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * EntityReference models the XML &entityname; syntax, when used for
 * entities defined by the DOM. Entities hardcoded into XML, such as
 * character entities, should instead have been translated into text
 * by the code which generated the DOM tree.
 * <P>
 * An XML processor has the alternative of fully expanding Entities
 * into the normal document tree. If it does so, no EntityReference nodes
 * will appear.
 * <P>
 * Similarly, non-validating XML processors are not required to read
 * or process entity declarations made in the external subset or
 * declared in external parameter entities. Hence, some applications
 * may not make the replacement value available for Parsed Entities 
 * of these types.
 * <P>
 * EntityReference behaves as a read-only node, and the children of 
 * the EntityReference (which reflect those of the Entity, and should
 * also be read-only) give its replacement value, if any. They are 
 * supposed to automagically stay in synch if the DocumentType is 
 * updated with new values for the Entity.
 * <P>
 * The defined behavior makes efficient storage difficult for the DOM
 * implementor. We can't just look aside to the Entity's definition
 * in the DocumentType since those nodes have the wrong parent (unless
 * we can come up with a clever "imaginary parent" mechanism). We
 * must at least appear to clone those children... which raises the
 * issue of keeping the reference synchronized with its parent.
 * This leads me back to the "cached image of centrally defined data"
 * solution, much as I dislike it.
 * <P>
 * For now I have decided, since REC-DOM-Level-1-19980818 doesn't
 * cover this in much detail, that synchronization doesn't have to be
 * considered while the user is deep in the tree. That is, if you're
 * looking within one of the EntityReferennce's children and the Entity
 * changes, you won't be informed; instead, you will continue to access
 * the same object -- which may or may not still be part of the tree.
 * This is the same behavior that obtains elsewhere in the DOM if the
 * subtree you're looking at is deleted from its parent, so it's
 * acceptable here. (If it really bothers folks, we could set things
 * up so deleted subtrees are walked and marked invalid, but that's
 * not part of the DOM's defined behavior.)
 * <P>
 * As a result, only the EntityReference itself has to be aware of
 * changes in the Entity. And it can take advantage of the same
 * structure-change-monitoring code I implemented to support
 * DeepNodeList.
 * 
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public class DeferredEntityReferenceImpl 
    extends EntityReferenceImpl 
    implements DeferredNode {


    /** Serialization version. */
    static final long serialVersionUID = 390319091370032223L;
    

    /** Node index. */
    protected transient int fNodeIndex;


    /**
     * This is the deferred constructor. Only the fNodeIndex is given here. 
     * All other data, can be requested from the ownerDocument via the index.
     */
    DeferredEntityReferenceImpl(DeferredDocumentImpl ownerDocument, int nodeIndex) {
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
        

    /** Synchronize the children. */
    protected void synchronizeChildren() {

        needsSyncChildren(false);

        DocumentType doctype = ownerDocument.getDoctype();
        boolean found = false;
        if (doctype != null) {

            boolean orig = ownerDocument.getMutationEvents();
            ownerDocument.setMutationEvents(false);

            NamedNodeMap entities = doctype.getEntities();
            if (entities != null) {
                Entity entity = (Entity)entities.getNamedItem(getNodeName());
                if (entity != null) {

                    found = true;

                    boolean ro = isReadOnly();
                    isReadOnly(false);
                    Node child = entity.getFirstChild();
                    while (child != null) {
                        appendChild(child.cloneNode(true));
                        child = child.getNextSibling();
                    }
                    if (ro) {
                        setReadOnly(true, true);
                    }
                }
            }
            ownerDocument.setMutationEvents(orig);
        }

        if (!found) {
            isReadOnly(false);
            DeferredDocumentImpl ownerDocument =
                (DeferredDocumentImpl) ownerDocument();
            ownerDocument.synchronizeChildren(this, fNodeIndex);
            setReadOnly(true, true);
        }


    protected void synchronize() {
    }

