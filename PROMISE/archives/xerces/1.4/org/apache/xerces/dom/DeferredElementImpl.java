package org.apache.xerces.dom;

import java.util.Enumeration;
import java.util.Vector;

import org.w3c.dom.NamedNodeMap;

import org.apache.xerces.utils.StringPool;

/**
 * Elements represent most of the "markup" and structure of the
 * document.  They contain both the data for the element itself
 * (element name and attributes), and any contained nodes, including
 * document text (as children).
 * <P>
 * Elements may have Attributes associated with them; the API for this is
 * defined in Node, but the function is implemented here. In general, XML
 * applications should retrive Attributes as Nodes, since they may contain
 * entity references and hence be a fairly complex sub-tree. HTML users will
 * be dealing with simple string values, and convenience methods are provided
 * to work in terms of Strings.
 * <P>
 * DeferredElementImpl inherits from ElementImpl which does not support
 * Namespaces. DeferredElementNSImpl, which inherits from ElementNSImpl, does.
 * @see DeferredElementNSImpl
 *
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public class DeferredElementImpl
    extends ElementImpl
    implements DeferredNode {


    /** Serialization version. */
    static final long serialVersionUID = -7670981133940934842L;


    /** Node index. */
    protected transient int fNodeIndex;


    /**
     * This is the deferred constructor. Only the fNodeIndex is given here. All
     * other data, can be requested from the ownerDocument via the index.
     */
    DeferredElementImpl(DeferredDocumentImpl ownerDoc, int nodeIndex) {
        super(ownerDoc, null);

        fNodeIndex = nodeIndex;
        needsSyncChildren(true);



    /** Returns the node index. */
    public final int getNodeIndex() {
        return fNodeIndex;
    }


    /** Synchronizes the data (name and value) for fast nodes. */
    protected final void synchronizeData() {

        needsSyncData(false);

        DeferredDocumentImpl ownerDocument =
            (DeferredDocumentImpl)this.ownerDocument;

        boolean orig = ownerDocument.mutationEvents;
        ownerDocument.mutationEvents = false;

        int elementTypeName = ownerDocument.getNodeName(fNodeIndex);
        StringPool pool = ownerDocument.getStringPool();
        name = pool.toString(elementTypeName);

        setupDefaultAttributes();
        int index = ownerDocument.getNodeValue(fNodeIndex);
        if (index != -1) {
            NamedNodeMap attrs = getAttributes();
            do {
                NodeImpl attr = (NodeImpl)ownerDocument.getNodeObject(index);
                attrs.setNamedItem(attr);
                index = ownerDocument.getPrevSibling(index);
            } while (index != -1);
        }

        ownerDocument.mutationEvents = orig;


    protected final void synchronizeChildren() {
        DeferredDocumentImpl ownerDocument =
            (DeferredDocumentImpl) ownerDocument();
        ownerDocument.synchronizeChildren(this, fNodeIndex);

