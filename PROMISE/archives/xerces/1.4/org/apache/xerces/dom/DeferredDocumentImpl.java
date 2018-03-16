package org.apache.xerces.dom;

import org.apache.xerces.framework.XMLAttrList;
import org.apache.xerces.utils.StringPool;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.util.Vector;

/**
 * The Document interface represents the entire HTML or XML document.
 * Conceptually, it is the root of the document tree, and provides the
 * primary access to the document's data.
 * <P>
 * Since elements, text nodes, comments, processing instructions,
 * etc. cannot exist outside the context of a Document, the Document
 * interface also contains the factory methods needed to create these
 * objects. The Node objects created have a ownerDocument attribute
 * which associates them with the Document within whose context they
 * were created.
 *
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public class DeferredDocumentImpl
    extends DocumentImpl
    implements DeferredNode {


    /** Serialization version. */
    static final long serialVersionUID = 5186323580749626857L;


    /** To include code for printing the ref count tables. */
    private static final boolean DEBUG_PRINT_REF_COUNTS = false;

    /** To include code for printing the internal tables. */
    private static final boolean DEBUG_PRINT_TABLES = false;

    /** To debug identifiers set to true and recompile. */
    private static final boolean DEBUG_IDS = false;


    /** Chunk shift. */

    /** Chunk size. */
    protected static final int CHUNK_SIZE = (1 << CHUNK_SHIFT);

    /** Chunk mask. */
    protected static final int CHUNK_MASK = CHUNK_SIZE - 1;

    /** Initial chunk size. */



    /** Node count. */
    protected transient int fNodeCount = 0;

    /** Node types. */
    protected transient int fNodeType[][];

    /** Node names. */
    protected transient int fNodeName[][];

    /** Node values. */
    protected transient int fNodeValue[][];

    /** Node parents. */
    protected transient int fNodeParent[][];

    /** Node first children. */
    protected transient int fNodeLastChild[][];

    /** Node prev siblings. */
    protected transient int fNodePrevSib[][];

    /** Node namespace URI. */
    protected transient int fNodeURI[][];

    /** Identifier count. */
    protected transient int fIdCount;

    /** Identifier name indexes. */
    protected transient int fIdName[];

    /** Identifier element indexes. */
    protected transient int fIdElement[];

    /** String pool cache. */
    protected transient StringPool fStringPool;

	/** DOM2: For namespace support in the deferred case.
	 */
    protected boolean fNamespacesEnabled = false;

    private final StringBuffer fBufferStr = new StringBuffer();
    private final Vector fStrChunks = new Vector();

    /**
     * NON-DOM: Actually creating a Document is outside the DOM's spec,
     * since it has to operate in terms of a particular implementation.
     */
    public DeferredDocumentImpl(StringPool stringPool) {
        this(stringPool, false);

    /**
     * NON-DOM: Actually creating a Document is outside the DOM's spec,
     * since it has to operate in terms of a particular implementation.
     */
    public DeferredDocumentImpl(StringPool stringPool, boolean namespacesEnabled) {
        this(stringPool, namespacesEnabled, false);

    /** Experimental constructor. */
    public DeferredDocumentImpl(StringPool stringPool,
                                boolean namespaces, boolean grammarAccess) {
        super(grammarAccess);

        fStringPool = stringPool;

        needsSyncData(true);
        needsSyncChildren(true);

        fNamespacesEnabled = namespaces;



    /** Returns the cached parser.getNamespaces() value.*/
    boolean getNamespacesEnabled() {
        return fNamespacesEnabled;
    }


    /** Creates a document node in the table. */
    public int createDocument() {
        int nodeIndex = createNode(Node.DOCUMENT_NODE);
        return nodeIndex;
    }

    /** Creates a doctype. */
    public int createDocumentType(int rootElementNameIndex, int publicId, int systemId) {

        int nodeIndex = createNode(Node.DOCUMENT_TYPE_NODE);
        int chunk     = nodeIndex >> CHUNK_SHIFT;
        int index     = nodeIndex & CHUNK_MASK;


        int echunk = extraDataIndex >> CHUNK_SHIFT;
        int eindex = extraDataIndex & CHUNK_MASK;

        setChunkIndex(fNodeName, rootElementNameIndex, chunk, index);
        setChunkIndex(fNodeValue, extraDataIndex, chunk, index);
        setChunkIndex(fNodeName, publicId, echunk, eindex);
        setChunkIndex(fNodeValue, systemId, echunk, eindex);

        return nodeIndex;


    public void setInternalSubset(int doctypeIndex, int subsetIndex) {
        int chunk     = doctypeIndex >> CHUNK_SHIFT;
        int index     = doctypeIndex & CHUNK_MASK;
        int extraDataIndex = fNodeValue[chunk][index];
        int echunk = extraDataIndex >> CHUNK_SHIFT;
        int eindex = extraDataIndex & CHUNK_MASK;
        setChunkIndex(fNodeLastChild, subsetIndex, echunk, eindex);
    }

    /** Creates a notation in the table. */
    public int createNotation(int notationName, int publicId, int systemId) throws Exception {

        int nodeIndex = createNode(Node.NOTATION_NODE);
        int chunk     = nodeIndex >> CHUNK_SHIFT;
        int index     = nodeIndex & CHUNK_MASK;

        int echunk = extraDataIndex >> CHUNK_SHIFT;
        int eindex = extraDataIndex & CHUNK_MASK;

        setChunkIndex(fNodeName, notationName, chunk, index);
        setChunkIndex(fNodeValue, extraDataIndex, chunk, index);
        setChunkIndex(fNodeName, publicId, echunk, eindex);
        setChunkIndex(fNodeValue, systemId, echunk, eindex);

        return nodeIndex;


    /** Creates an entity in the table. */
    public int createEntity(int entityName, int publicId, int systemId, int notationName) throws Exception {
        int nodeIndex = createNode(Node.ENTITY_NODE);
        int chunk     = nodeIndex >> CHUNK_SHIFT;
        int index     = nodeIndex & CHUNK_MASK;

        int echunk = extraDataIndex >> CHUNK_SHIFT;
        int eindex = extraDataIndex & CHUNK_MASK;

        int echunk2 = extraDataIndex2 >> CHUNK_SHIFT;
        int eindex2 = extraDataIndex2 & CHUNK_MASK;

        setChunkIndex(fNodeName, entityName, chunk, index);
        setChunkIndex(fNodeValue, extraDataIndex, chunk, index);
        setChunkIndex(fNodeLastChild, notationName, echunk, eindex);
        setChunkIndex(fNodeName, publicId, echunk, eindex);
        setChunkIndex(fNodeValue, extraDataIndex2, echunk, eindex);
        setChunkIndex(fNodeName, systemId, echunk2, eindex2);

        setChunkIndex(fNodeValue, -1, echunk2, eindex2);
        setChunkIndex(fNodeLastChild, -1, echunk2, eindex2);

        return nodeIndex;


    public void setEntityInfo(int currentEntityDecl, int versionIndex, int encodingIndex){
        int eNodeIndex = getNodeValue(getNodeValue(currentEntityDecl, false), false);
        if (eNodeIndex !=-1) {
            int echunk = eNodeIndex >> CHUNK_SHIFT;
            int eindex = eNodeIndex & CHUNK_MASK;
            setChunkIndex(fNodeValue, versionIndex, echunk, eindex);
            setChunkIndex(fNodeLastChild, encodingIndex, echunk, eindex);
        }
    }

    /** Creates an entity reference node in the table. */
    public int createEntityReference(int nameIndex) throws Exception {

        int nodeIndex = createNode(Node.ENTITY_REFERENCE_NODE);
        int chunk     = nodeIndex >> CHUNK_SHIFT;
        int index     = nodeIndex & CHUNK_MASK;
        setChunkIndex(fNodeName, nameIndex, chunk, index);

        return nodeIndex;


    /** Creates an element node in the table. */
    public int createElement(int elementNameIndex,
                             XMLAttrList attrList, int attrListIndex) {
        return createElement(elementNameIndex, -1, attrList, attrListIndex);
    }

    /** Creates an element node with a URI in the table. */
    public int createElement(int elementNameIndex, int elementURIIndex,
                             XMLAttrList attrList, int attrListIndex) {

        int elementNodeIndex = createNode(Node.ELEMENT_NODE);
        int elementChunk     = elementNodeIndex >> CHUNK_SHIFT;
        int elementIndex     = elementNodeIndex & CHUNK_MASK;
        setChunkIndex(fNodeName, elementNameIndex, elementChunk, elementIndex);
        setChunkIndex(fNodeURI, elementURIIndex, elementChunk, elementIndex);

        if (attrListIndex != -1) {
            int first = attrList.getFirstAttr(attrListIndex);
            int lastAttrNodeIndex = -1;
            int lastAttrChunk = -1;
            int lastAttrIndex = -1;
            for (int index = first;
                 index != -1;
                 index = attrList.getNextAttr(index)) {

                int attrNodeIndex =
                    createAttribute(attrList.getAttrName(index),
                                    attrList.getAttrURI(index),
                                    attrList.getAttValue(index),
                                    attrList.isSpecified(index));
                int attrChunk = attrNodeIndex >> CHUNK_SHIFT;
                int attrIndex  = attrNodeIndex & CHUNK_MASK;
                setChunkIndex(fNodeParent, elementNodeIndex, attrChunk, attrIndex);

                if (index == first) {
                    setChunkIndex(fNodeValue, attrNodeIndex, elementChunk, elementIndex);
                }
                else {
                    setChunkIndex(fNodePrevSib, attrNodeIndex, lastAttrChunk, lastAttrIndex);
                }

                lastAttrNodeIndex = attrNodeIndex;
                lastAttrChunk     = attrChunk;
                lastAttrIndex     = attrIndex;
            }
        }

        return elementNodeIndex;

    /** Creates an attribute in the table. */
    public int createAttribute(int attrNameIndex,
                               int attrValueIndex, boolean specified) {
        return createAttribute(attrNameIndex, -1, attrValueIndex, specified);
    }

    /** Creates an attribute with a URI in the table. */
    public int createAttribute(int attrNameIndex, int attrURIIndex,
                               int attrValueIndex, boolean specified) {

        int nodeIndex = createNode(NodeImpl.ATTRIBUTE_NODE);
        int chunk = nodeIndex >> CHUNK_SHIFT;
        int index = nodeIndex & CHUNK_MASK;
        setChunkIndex(fNodeName, attrNameIndex, chunk, index);
        setChunkIndex(fNodeURI, attrURIIndex, chunk, index);
        setChunkIndex(fNodeValue, specified ? 1 : 0, chunk, index);

        int textNodeIndex = createTextNode(attrValueIndex, false);
        appendChild(nodeIndex, textNodeIndex);

        return nodeIndex;


    /** Creates an element definition in the table. */
    public int createElementDefinition(int elementNameIndex) {

        int nodeIndex = createNode(NodeImpl.ELEMENT_DEFINITION_NODE);
        int chunk = nodeIndex >> CHUNK_SHIFT;
        int index = nodeIndex & CHUNK_MASK;
        setChunkIndex(fNodeName, elementNameIndex, chunk, index);

        return nodeIndex;


    /** Creates a text node in the table. */
    public int createTextNode(int dataIndex, boolean ignorableWhitespace) {

        int nodeIndex = createNode(Node.TEXT_NODE);
        int chunk = nodeIndex >> CHUNK_SHIFT;
        int index = nodeIndex & CHUNK_MASK;
        setChunkIndex(fNodeValue, dataIndex, chunk, index);
        setChunkIndex(fNodeLastChild,
                      ignorableWhitespace ?  1 : 0, chunk, index);

        return nodeIndex;


    /** Creates a CDATA section node in the table. */
    public int createCDATASection(int dataIndex, boolean ignorableWhitespace) {

        int nodeIndex = createNode(Node.CDATA_SECTION_NODE);
        int chunk = nodeIndex >> CHUNK_SHIFT;
        int index = nodeIndex & CHUNK_MASK;
        setChunkIndex(fNodeValue, dataIndex, chunk, index);
        setChunkIndex(fNodeLastChild,
                      ignorableWhitespace ?  1 : 0, chunk, index);

        return nodeIndex;


    /** Creates a processing instruction node in the table. */
    public int createProcessingInstruction(int targetIndex, int dataIndex) {

        int nodeIndex = createNode(Node.PROCESSING_INSTRUCTION_NODE);
        int chunk = nodeIndex >> CHUNK_SHIFT;
        int index = nodeIndex & CHUNK_MASK;
        setChunkIndex(fNodeName, targetIndex, chunk, index);
        setChunkIndex(fNodeValue, dataIndex, chunk, index);

        return nodeIndex;


    /** Creates a comment node in the table. */
    public int createComment(int dataIndex) {

        int nodeIndex = createNode(Node.COMMENT_NODE);
        int chunk = nodeIndex >> CHUNK_SHIFT;
        int index = nodeIndex & CHUNK_MASK;
        setChunkIndex(fNodeValue, dataIndex, chunk, index);

        return nodeIndex;


    /** Appends a child to the specified parent in the table. */
    public void appendChild(int parentIndex, int childIndex) {

        int pchunk = parentIndex >> CHUNK_SHIFT;
        int pindex = parentIndex & CHUNK_MASK;
        int cchunk = childIndex >> CHUNK_SHIFT;
        int cindex = childIndex & CHUNK_MASK;
        setChunkIndex(fNodeParent, parentIndex, cchunk, cindex);

        int olast = getChunkIndex(fNodeLastChild, pchunk, pindex);
        setChunkIndex(fNodePrevSib, olast, cchunk, cindex);

        setChunkIndex(fNodeLastChild, childIndex, pchunk, pindex);



    /** Adds an attribute node to the specified element. */
    public int setAttributeNode(int elemIndex, int attrIndex) {

        int echunk = elemIndex >> CHUNK_SHIFT;
        int eindex = elemIndex & CHUNK_MASK;
        int achunk = attrIndex >> CHUNK_SHIFT;
        int aindex = attrIndex & CHUNK_MASK;

        String attrName =
            fStringPool.toString(getChunkIndex(fNodeName, achunk, aindex));
        int oldAttrIndex = getChunkIndex(fNodeValue, echunk, eindex);
        int nextIndex = -1;
        int oachunk = -1;
        int oaindex = -1;
        while (oldAttrIndex != -1) {
            oachunk = oldAttrIndex >> CHUNK_SHIFT;
            oaindex = oldAttrIndex & CHUNK_MASK;
            String oldAttrName =
              fStringPool.toString(getChunkIndex(fNodeName, oachunk, oaindex));
            if (oldAttrName.equals(attrName)) {
                break;
            }
            nextIndex = oldAttrIndex;
            oldAttrIndex = getChunkIndex(fNodePrevSib, oachunk, oaindex);
        }

        if (oldAttrIndex != -1) {

            int prevIndex = getChunkIndex(fNodePrevSib, oachunk, oaindex);
            if (nextIndex == -1) {
                setChunkIndex(fNodeValue, prevIndex, echunk, eindex);
            }
            else {
                int pchunk = nextIndex >> CHUNK_SHIFT;
                int pindex = nextIndex & CHUNK_MASK;
                setChunkIndex(fNodePrevSib, prevIndex, pchunk, pindex);
            }

            clearChunkIndex(fNodeType, oachunk, oaindex);
            clearChunkIndex(fNodeName, oachunk, oaindex);
            clearChunkIndex(fNodeValue, oachunk, oaindex);
            clearChunkIndex(fNodeParent, oachunk, oaindex);
            clearChunkIndex(fNodePrevSib, oachunk, oaindex);
            int attrTextIndex =
                clearChunkIndex(fNodeLastChild, oachunk, oaindex);
            int atchunk = attrTextIndex >> CHUNK_SHIFT;
            int atindex = attrTextIndex & CHUNK_MASK;
            clearChunkIndex(fNodeType, atchunk, atindex);
            clearChunkIndex(fNodeValue, atchunk, atindex);
            clearChunkIndex(fNodeParent, atchunk, atindex);
            clearChunkIndex(fNodeLastChild, atchunk, atindex);
        }

        int prevIndex = getChunkIndex(fNodeValue, echunk, eindex);
        setChunkIndex(fNodeValue, attrIndex, echunk, eindex);
        setChunkIndex(fNodePrevSib, prevIndex, achunk, aindex);

        return oldAttrIndex;


    /** Inserts a child before the specified node in the table. */
    public int insertBefore(int parentIndex, int newChildIndex, int refChildIndex) {

        if (refChildIndex == -1) {
            appendChild(parentIndex, newChildIndex);
            return newChildIndex;
        }

        int nchunk = newChildIndex >> CHUNK_SHIFT;
        int nindex = newChildIndex & CHUNK_MASK;
        int rchunk = refChildIndex >> CHUNK_SHIFT;
        int rindex = refChildIndex & CHUNK_MASK;
        int previousIndex = getChunkIndex(fNodePrevSib, rchunk, rindex);
        setChunkIndex(fNodePrevSib, newChildIndex, rchunk, rindex);
        setChunkIndex(fNodePrevSib, previousIndex, nchunk, nindex);

        return newChildIndex;


    /** Sets the last child of the parentIndex to childIndex. */
    public void setAsLastChild(int parentIndex, int childIndex) {

        int pchunk = parentIndex >> CHUNK_SHIFT;
        int pindex = parentIndex & CHUNK_MASK;
        int chunk = childIndex >> CHUNK_SHIFT;
        int index = childIndex & CHUNK_MASK;
        setChunkIndex(fNodeLastChild, childIndex, pchunk, pindex);

    /**
     * Returns the parent node of the given node.
     * <em>Calling this method does not free the parent index.</em>
     */
    public int getParentNode(int nodeIndex) {
        return getParentNode(nodeIndex, false);
    }

    /**
     * Returns the parent node of the given node.
     * @param free True to free parent node.
     */
    public int getParentNode(int nodeIndex, boolean free) {

        if (nodeIndex == -1) {
            return -1;
        }

        int chunk = nodeIndex >> CHUNK_SHIFT;
        int index = nodeIndex & CHUNK_MASK;
        return free ? clearChunkIndex(fNodeParent, chunk, index)
                    : getChunkIndex(fNodeParent, chunk, index);


    /** Returns the last child of the given node. */
    public int getLastChild(int nodeIndex) {
        return getLastChild(nodeIndex, true);
    }

    /**
     * Returns the last child of the given node.
     * @param free True to free child index.
     */
    public int getLastChild(int nodeIndex, boolean free) {

        if (nodeIndex == -1) {
            return -1;
        }

        int chunk = nodeIndex >> CHUNK_SHIFT;
        int index = nodeIndex & CHUNK_MASK;
        return free ? clearChunkIndex(fNodeLastChild, chunk, index)
                    : getChunkIndex(fNodeLastChild, chunk, index);


    /**
     * Returns the prev sibling of the given node.
     * This is post-normalization of Text Nodes.
     */
    public int getPrevSibling(int nodeIndex) {
        return getPrevSibling(nodeIndex, true);
    }

    /**
     * Returns the prev sibling of the given node.
     * @param free True to free sibling index.
     */
    public int getPrevSibling(int nodeIndex, boolean free) {

        if (nodeIndex == -1) {
            return -1;
        }

        int chunk = nodeIndex >> CHUNK_SHIFT;
        int index = nodeIndex & CHUNK_MASK;
        int type = getChunkIndex(fNodeType, chunk, index);
        if (type == Node.TEXT_NODE) {
            do {
                nodeIndex = getChunkIndex(fNodePrevSib, chunk, index);
                if (nodeIndex == -1) {
                    break;
                }
                chunk = nodeIndex >> CHUNK_SHIFT;
                index = nodeIndex & CHUNK_MASK;
                type = getChunkIndex(fNodeType, chunk, index);
            } while (type == Node.TEXT_NODE);
        }
        else {
            nodeIndex = getChunkIndex(fNodePrevSib, chunk, index);
        }

        return nodeIndex;


    /**
     * Returns the <i>real</i> prev sibling of the given node,
     * directly from the data structures. Used by TextImpl#getNodeValue()
     * to normalize values.
     */
    public int getRealPrevSibling(int nodeIndex) {
        return getRealPrevSibling(nodeIndex, true);
    }

    /**
     * Returns the <i>real</i> prev sibling of the given node.
     * @param free True to free sibling index.
     */
    public int getRealPrevSibling(int nodeIndex, boolean free) {

        if (nodeIndex == -1) {
            return -1;
        }

        int chunk = nodeIndex >> CHUNK_SHIFT;
        int index = nodeIndex & CHUNK_MASK;
        return free ? clearChunkIndex(fNodePrevSib, chunk, index)
                    : getChunkIndex(fNodePrevSib, chunk, index);


    /**
     * Returns the index of the element definition in the table
     * with the specified name index, or -1 if no such definition
     * exists.
     */
    public int lookupElementDefinition(int elementNameIndex) {

        if (fNodeCount > 1) {

            int docTypeIndex = -1;
            int nchunk = 0;
            int nindex = 0;
            for (int index = getChunkIndex(fNodeLastChild, nchunk, nindex);
                 index != -1;
                 index = getChunkIndex(fNodePrevSib, nchunk, nindex)) {

                nchunk = index >> CHUNK_SHIFT;
                nindex = index  & CHUNK_MASK;
                if (getChunkIndex(fNodeType, nchunk, nindex) == Node.DOCUMENT_TYPE_NODE) {
                    docTypeIndex = index;
                    break;
                }
            }

            if (docTypeIndex == -1) {
                return -1;
            }
            nchunk = docTypeIndex >> CHUNK_SHIFT;
            nindex = docTypeIndex & CHUNK_MASK;
            for (int index = getChunkIndex(fNodeLastChild, nchunk, nindex);
                 index != -1;
                 index = getChunkIndex(fNodePrevSib, nchunk, nindex)) {

                nchunk = index >> CHUNK_SHIFT;
                nindex = index & CHUNK_MASK;
                if (getChunkIndex(fNodeName, nchunk, nindex) == elementNameIndex) {
                    return index;
                }
            }
        }

        return -1;


    /** Instantiates the requested node object. */
    public DeferredNode getNodeObject(int nodeIndex) {

        if (nodeIndex == -1) {
            return null;
        }

        int chunk = nodeIndex >> CHUNK_SHIFT;
        int index = nodeIndex & CHUNK_MASK;
        int type = getChunkIndex(fNodeType, chunk, index);
        if (type != Node.TEXT_NODE) {
            clearChunkIndex(fNodeType, chunk, index);
        }

        DeferredNode node = null;
        switch (type) {


            case Node.ATTRIBUTE_NODE: {
		if (fNamespacesEnabled) {
		    node = new DeferredAttrNSImpl(this, nodeIndex);
		} else {
		    node = new DeferredAttrImpl(this, nodeIndex);
		}
                break;
            }

            case Node.CDATA_SECTION_NODE: {
                node = new DeferredCDATASectionImpl(this, nodeIndex);
                break;
            }

            case Node.COMMENT_NODE: {
                node = new DeferredCommentImpl(this, nodeIndex);
                break;
            }

            case Node.DOCUMENT_NODE: {
                node = this;
                break;
            }

            case Node.DOCUMENT_TYPE_NODE: {
                node = new DeferredDocumentTypeImpl(this, nodeIndex);
                docType = (DocumentTypeImpl)node;
                break;
            }

            case Node.ELEMENT_NODE: {

                if (DEBUG_IDS) {
                    System.out.println("getNodeObject(ELEMENT_NODE): "+nodeIndex);
                }

		if (fNamespacesEnabled) {
		    node = new DeferredElementNSImpl(this, nodeIndex);
		} else {
		    node = new DeferredElementImpl(this, nodeIndex);
		}

                if (docElement == null) {
                    docElement = (ElementImpl)node;
                }

                if (fIdElement != null) {
                    int idIndex = DeferredDocumentImpl.binarySearch(fIdElement, 0, fIdCount-1, nodeIndex);
                    while (idIndex != -1) {

                        if (DEBUG_IDS) {
                            System.out.println("  id index: "+idIndex);
                            System.out.println("  fIdName["+idIndex+
                                               "]: "+fIdName[idIndex]);
                        }

                        int nameIndex = fIdName[idIndex];
                        if (nameIndex != -1) {
                            String name = fStringPool.toString(nameIndex);
                            if (DEBUG_IDS) {
                                System.out.println("  name: "+name);
                                System.out.print("getNodeObject()#");
                            }
                            putIdentifier0(name, (Element)node);
                            fIdName[idIndex] = -1;
                        }

                        if (idIndex + 1 < fIdCount &&
                            fIdElement[idIndex + 1] == nodeIndex) {
                            idIndex++;
                        }
                        else {
                            idIndex = -1;
                        }
                    }
                }
                break;
            }

            case Node.ENTITY_NODE: {
                node = new DeferredEntityImpl(this, nodeIndex);
                break;
            }

            case Node.ENTITY_REFERENCE_NODE: {
                node = new DeferredEntityReferenceImpl(this, nodeIndex);
                break;
            }

            case Node.NOTATION_NODE: {
                node = new DeferredNotationImpl(this, nodeIndex);
                break;
            }

            case Node.PROCESSING_INSTRUCTION_NODE: {
                node = new DeferredProcessingInstructionImpl(this, nodeIndex);
                break;
            }

            case Node.TEXT_NODE: {
                node = new DeferredTextImpl(this, nodeIndex);
                break;
            }


            case NodeImpl.ELEMENT_DEFINITION_NODE: {
                node = new DeferredElementDefinitionImpl(this, nodeIndex);
                break;
            }

            default: {
                throw new IllegalArgumentException("type: "+type);
            }


        if (node != null) {
            return node;
        }

        throw new IllegalArgumentException();


    /** Returns the name of the given node. */
    public String getNodeNameString(int nodeIndex) {
        return getNodeNameString(nodeIndex, true);

    /**
     * Returns the name of the given node.
     * @param free True to free the string index.
     */
    public String getNodeNameString(int nodeIndex, boolean free) {

        if (nodeIndex == -1) {
            return null;
        }

        int chunk = nodeIndex >> CHUNK_SHIFT;
        int index = nodeIndex & CHUNK_MASK;
        int nameIndex = free
                      ? clearChunkIndex(fNodeName, chunk, index)
                      : getChunkIndex(fNodeName, chunk, index);
        if (nameIndex == -1) {
            return null;
        }

        return fStringPool.toString(nameIndex);


    /** Returns the value of the given node. */
    public String getNodeValueString(int nodeIndex) {
        return getNodeValueString(nodeIndex, true);

    /**
     * Returns the value of the given node.
     * @param free True to free the string index.
     */
    public String getNodeValueString(int nodeIndex, boolean free) {

        if (nodeIndex == -1) {
            return null;
        }

        int chunk = nodeIndex >> CHUNK_SHIFT;
        int index = nodeIndex & CHUNK_MASK;
        int valueIndex = free
                       ? clearChunkIndex(fNodeValue, chunk, index)
                       : getChunkIndex(fNodeValue, chunk, index);
        if (valueIndex == -1) {
            return null;
        }

        int type  = getChunkIndex(fNodeType, chunk, index);
        if (type == Node.TEXT_NODE) {
            int prevSib = getRealPrevSibling(nodeIndex);
            if (prevSib != -1 && getNodeType(prevSib, false) == Node.TEXT_NODE) {
                fStrChunks.addElement(fStringPool.toString(valueIndex));
                do {
                    chunk = prevSib >> CHUNK_SHIFT;
                    index = prevSib & CHUNK_MASK;
                    valueIndex = getChunkIndex(fNodeValue, chunk, index);
                    fStrChunks.addElement(fStringPool.toString(valueIndex));
                    prevSib = getChunkIndex(fNodePrevSib, chunk, index);
                    if (prevSib == -1) {
                        break;
                    }
                } while (getNodeType(prevSib, false) == Node.TEXT_NODE);
                for (int i=fStrChunks.size()-1; i>=0; i--) {                                                               
                    fBufferStr.append((String)fStrChunks.elementAt(i));
                }
                                                        
                String value = fBufferStr.toString();
                fStrChunks.setSize(0);
                fBufferStr.setLength(0);
                return value;
            }
        }

        return fStringPool.toString(valueIndex);


    /** Returns the real int name of the given node. */
    public int getNodeName(int nodeIndex) {
        return getNodeName(nodeIndex, true);
    }

    /**
     * Returns the real int name of the given node.
     * @param free True to free the name index.
     */
    public int getNodeName(int nodeIndex, boolean free) {

        if (nodeIndex == -1) {
            return -1;
        }

        int chunk = nodeIndex >> CHUNK_SHIFT;
        int index = nodeIndex & CHUNK_MASK;
        return free ? clearChunkIndex(fNodeName, chunk, index)
                    : getChunkIndex(fNodeName, chunk, index);


    /**
     * Returns the real int value of the given node.
     *  Used by AttrImpl to store specified value (1 == true).
     */
    public int getNodeValue(int nodeIndex) {
        return getNodeValue(nodeIndex, true);
    }

    /**
     * Returns the real int value of the given node.
     * @param free True to free the value index.
     */
    public int getNodeValue(int nodeIndex, boolean free) {

        if (nodeIndex == -1) {
            return -1;
        }

        int chunk = nodeIndex >> CHUNK_SHIFT;
        int index = nodeIndex & CHUNK_MASK;
        return free ? clearChunkIndex(fNodeValue, chunk, index)
                    : getChunkIndex(fNodeValue, chunk, index);


    /** Returns the type of the given node. */
    public short getNodeType(int nodeIndex) {
        return getNodeType(nodeIndex, true);
    }

    /**
     * Returns the type of the given node.
     * @param True to free type index.
     */
    public short getNodeType(int nodeIndex, boolean free) {

        if (nodeIndex == -1) {
            return -1;
        }

        int chunk = nodeIndex >> CHUNK_SHIFT;
        int index = nodeIndex & CHUNK_MASK;
        if (free) {
            return (short)clearChunkIndex(fNodeType, chunk, index);
        }
        return (short)getChunkIndex(fNodeType, chunk, index);


    /** Returns the attribute value of the given name. */
    public int getAttribute(int elemIndex, int nameIndex) {
        if (elemIndex == -1 || nameIndex == -1) {
            return -1;
        }
        int echunk = elemIndex >> CHUNK_SHIFT;
        int eindex = elemIndex & CHUNK_MASK;
        int attrIndex = getChunkIndex(fNodeValue, echunk, eindex);
        while (attrIndex != -1) {
            int achunk = attrIndex >> CHUNK_SHIFT;
            int aindex = attrIndex & CHUNK_MASK;
            if (getChunkIndex(fNodeName, achunk, aindex) == nameIndex) {
                return getChunkIndex(fNodeValue, achunk, aindex);
            }
            attrIndex = getChunkIndex(fNodePrevSib, achunk, aindex);
        }
        return -1;
    }

    /** Returns the URI of the given node. */
    public short getNodeURI(int nodeIndex) {
        return getNodeURI(nodeIndex, true);
    }

    /**
     * Returns the URI of the given node.
     * @param True to free URI index.
     */
    public short getNodeURI(int nodeIndex, boolean free) {

        if (nodeIndex == -1) {
            return -1;
        }

        int chunk = nodeIndex >> CHUNK_SHIFT;
        int index = nodeIndex & CHUNK_MASK;
        if (free) {
            return (short)clearChunkIndex(fNodeURI, chunk, index);
        }
        return (short)getChunkIndex(fNodeURI, chunk, index);



    /** Registers an identifier name with a specified element node. */
    public void putIdentifier(int nameIndex, int elementNodeIndex) {

        if (DEBUG_IDS) {
            System.out.println("putIdentifier("+nameIndex+", "+elementNodeIndex+')'+
                               fStringPool.toString(nameIndex)+
                               ", "+
                               fStringPool.toString(getChunkIndex(fNodeName, elementNodeIndex >> CHUNK_SHIFT, elementNodeIndex & CHUNK_MASK)));
        }

        if (fIdName == null) {
            fIdName    = new int[64];
            fIdElement = new int[64];
        }

        if (fIdCount == fIdName.length) {
            int idName[] = new int[fIdCount * 2];
            System.arraycopy(fIdName, 0, idName, 0, fIdCount);
            fIdName = idName;

            int idElement[] = new int[idName.length];
            System.arraycopy(fIdElement, 0, idElement, 0, fIdCount);
            fIdElement = idElement;
        }

        fIdName[fIdCount] = nameIndex;
        fIdElement[fIdCount] = elementNodeIndex;
        fIdCount++;



    /** Prints out the tables. */
    public void print() {

        if (DEBUG_PRINT_REF_COUNTS) {
            System.out.print("num\t");
            System.out.print("type\t");
            System.out.print("name\t");
            System.out.print("val\t");
            System.out.print("par\t");
            System.out.print("fch\t");
            System.out.print("nsib");
            System.out.println();
            for (int i = 0; i < fNodeType.length; i++) {
                if (fNodeType[i] != null) {
                    System.out.print("--------");
                    System.out.print("--------");
                    System.out.print("--------");
                    System.out.print("--------");
                    System.out.print("--------");
                    System.out.print("--------");
                    System.out.print("--------");
                    System.out.println();

                    System.out.print(i);
                    System.out.print('\t');
                    System.out.print(fNodeType[i][CHUNK_SIZE]);
                    System.out.print('\t');
                    System.out.print(fNodeName[i][CHUNK_SIZE]);
                    System.out.print('\t');
                    System.out.print(fNodeValue[i][CHUNK_SIZE]);
                    System.out.print('\t');
                    System.out.print(fNodeParent[i][CHUNK_SIZE]);
                    System.out.print('\t');
                    System.out.print(fNodeLastChild[i][CHUNK_SIZE]);
                    System.out.print('\t');
                    System.out.print(fNodePrevSib[i][CHUNK_SIZE]);
                    System.out.println();

                    System.out.print(i);
                    System.out.print('\t');
                    System.out.print(fNodeType[i][CHUNK_SIZE + 1]);
                    System.out.print('\t');
                    System.out.print(fNodeName[i][CHUNK_SIZE + 1]);
                    System.out.print('\t');
                    System.out.print(fNodeValue[i][CHUNK_SIZE + 1]);
                    System.out.print('\t');
                    System.out.print(fNodeParent[i][CHUNK_SIZE + 1]);
                    System.out.print('\t');
                    System.out.print(fNodeLastChild[i][CHUNK_SIZE + 1]);
                    System.out.print('\t');
                    System.out.print(fNodePrevSib[i][CHUNK_SIZE + 1]);
                    System.out.println();
                }
            }
        }

        if (DEBUG_PRINT_TABLES) {
            System.out.println("# start table");
            for (int i = 0; i < fNodeCount; i++) {
                int chunk = i >> CHUNK_SHIFT;
                int index = i & CHUNK_MASK;
                if (i % 10 == 0) {
                    System.out.print("num\t");
                    System.out.print("type\t");
                    System.out.print("name\t");
                    System.out.print("val\t");
                    System.out.print("par\t");
                    System.out.print("fch\t");
                    System.out.print("nsib");
                    System.out.println();
                }
                System.out.print(i);
                System.out.print('\t');
                System.out.print(getChunkIndex(fNodeType, chunk, index));
                System.out.print('\t');
                System.out.print(getChunkIndex(fNodeName, chunk, index));
                System.out.print('\t');
                System.out.print(getChunkIndex(fNodeValue, chunk, index));
                System.out.print('\t');
                System.out.print(getChunkIndex(fNodeParent, chunk, index));
                System.out.print('\t');
                System.out.print(getChunkIndex(fNodeLastChild, chunk, index));
                System.out.print('\t');
                System.out.print(getChunkIndex(fNodePrevSib, chunk, index));
                /***
                System.out.print(fNodeType[0][i]);
                System.out.print('\t');
                System.out.print(fNodeName[0][i]);
                System.out.print('\t');
                System.out.print(fNodeValue[0][i]);
                System.out.print('\t');
                System.out.print(fNodeParent[0][i]);
                System.out.print('\t');
                System.out.print(fNodeFirstChild[0][i]);
                System.out.print('\t');
                System.out.print(fNodeLastChild[0][i]);
                System.out.print('\t');
                System.out.print(fNodePrevSib[0][i]);
                System.out.print('\t');
                System.out.print(fNodeNextSib[0][i]);
                /***/
                System.out.println();
            }
            System.out.println("# end table");
        }



    /** Returns the node index. */
    public int getNodeIndex() {
        return 0;
    }


    /** access to string pool. */
    protected StringPool getStringPool() {
        return fStringPool;
    }

    /** Synchronizes the node's data. */
    protected void synchronizeData() {

        needsSyncData(false);

        if (fIdElement != null) {


            IntVector path = new IntVector();
            for (int i = 0; i < fIdCount; i++) {

                int elementNodeIndex = fIdElement[i];
                int idNameIndex      = fIdName[i];
                if (idNameIndex == -1) {
                    continue;
                }

                path.removeAllElements();
                int index = elementNodeIndex;
                do {
                    path.addElement(index);
                    int pchunk = index >> CHUNK_SHIFT;
                    int pindex = index & CHUNK_MASK;
                    index = getChunkIndex(fNodeParent, pchunk, pindex);
                } while (index != -1);

                Node place = this;
                for (int j = path.size() - 2; j >= 0; j--) {
                    index = path.elementAt(j);
                    Node child = place.getLastChild();
                    while (child != null) {
                        if (child instanceof DeferredNode) {
                            int nodeIndex = ((DeferredNode)child).getNodeIndex();
                            if (nodeIndex == index) {
                                place = child;
                                break;
                            }
                        }
                        child = child.getPreviousSibling();
                    }
                }

                Element element = (Element)place;
                String  name    = fStringPool.toString(idNameIndex);
                putIdentifier0(name, element);
                fIdName[i] = -1;

                while (i + 1 < fIdCount && fIdElement[i + 1] == elementNodeIndex) {
                    idNameIndex = fIdName[++i];
                    if (idNameIndex == -1) {
                        continue;
                    }
                    name = fStringPool.toString(idNameIndex);
                    putIdentifier0(name, element);
                }
            }



    /**
     * Synchronizes the node's children with the internal structure.
     * Fluffing the children at once solves a lot of work to keep
     * the two structures in sync. The problem gets worse when
     * editing the tree -- this makes it a lot easier.
     */
    protected void synchronizeChildren() {

        if (needsSyncData()) {
            synchronizeData();
            /*
             * when we have elements with IDs this method is being recursively
             * called from synchronizeData, in which case we've already gone
             * through the following and we can now simply stop here.
             */
            if (!needsSyncChildren()) {
                return;
            }
        }

        boolean orig = mutationEvents;
        mutationEvents = false;

        needsSyncChildren(false);

        getNodeType(0);

        ChildNode first = null;
        ChildNode last = null;
        for (int index = getLastChild(0);
             index != -1;
             index = getPrevSibling(index)) {

            ChildNode node = (ChildNode)getNodeObject(index);
            if (last == null) {
                last = node;
            }
            else {
                first.previousSibling = node;
            }
            node.ownerNode = this;
            node.isOwned(true);
            node.nextSibling = first;
            first = node;

            int type = node.getNodeType();
            if (type == Node.ELEMENT_NODE) {
                docElement = (ElementImpl)node;
            }
            else if (type == Node.DOCUMENT_TYPE_NODE) {
                docType = (DocumentTypeImpl)node;
            }
        }

        if (first != null) {
            firstChild = first;
            first.isFirstChild(true);
            lastChild(last);
        }

        mutationEvents = orig;


    /**
     * Synchronizes the node's children with the internal structure.
     * Fluffing the children at once solves a lot of work to keep
     * the two structures in sync. The problem gets worse when
     * editing the tree -- this makes it a lot easier.
     * This is not directly used in this class but this method is
     * here so that it can be shared by all deferred subclasses of AttrImpl.
     */
    protected final void synchronizeChildren(AttrImpl a, int nodeIndex) {

        boolean orig = getMutationEvents();
        setMutationEvents(false);

        a.needsSyncChildren(false);

        int last = getLastChild(nodeIndex);
        int prev = getPrevSibling(last);
        if (prev == -1) {
            a.value = getNodeValueString(last);
            a.hasStringValue(true);
        }
        else {
            ChildNode firstNode = null;
            ChildNode lastNode = null;
            for (int index = last; index != -1;
                 index = getPrevSibling(index)) {

                ChildNode node = (ChildNode) getNodeObject(index);
                if (lastNode == null) {
                    lastNode = node;
                }
                else {
                    firstNode.previousSibling = node;
                }
                node.ownerNode = a;
                node.isOwned(true);
                node.nextSibling = firstNode;
                firstNode = node;
            }
            if (lastNode != null) {
                firstNode.isFirstChild(true);
                a.lastChild(lastNode);
            }
            a.hasStringValue(false);
        }

        setMutationEvents(orig);



    /**
     * Synchronizes the node's children with the internal structure.
     * Fluffing the children at once solves a lot of work to keep
     * the two structures in sync. The problem gets worse when
     * editing the tree -- this makes it a lot easier.
     * This is not directly used in this class but this method is
     * here so that it can be shared by all deferred subclasses of ParentNode.
     */
    protected final void synchronizeChildren(ParentNode p, int nodeIndex) {

        boolean orig = getMutationEvents();
        setMutationEvents(false);

        p.needsSyncChildren(false);

        ChildNode firstNode = null;
        ChildNode lastNode = null;
        for (int index = getLastChild(nodeIndex);
             index != -1;
             index = getPrevSibling(index)) {

            ChildNode node = (ChildNode) getNodeObject(index);
            if (lastNode == null) {
                lastNode = node;
            }
            else {
                firstNode.previousSibling = node;
            }
            node.ownerNode = p;
            node.isOwned(true);
            node.nextSibling = firstNode;
            firstNode = node;
        }
        if (lastNode != null) {
            p.firstChild = firstNode;
            firstNode.isFirstChild(true);
            p.lastChild(lastNode);
        }

        setMutationEvents(orig);



    /** Ensures that the internal tables are large enough. */
    protected boolean ensureCapacity(int chunk, int index) {

        if (fNodeType == null) {
            fNodeType       = new int[INITIAL_CHUNK_COUNT][];
            fNodeName       = new int[INITIAL_CHUNK_COUNT][];
            fNodeValue      = new int[INITIAL_CHUNK_COUNT][];
            fNodeParent     = new int[INITIAL_CHUNK_COUNT][];
            fNodeLastChild  = new int[INITIAL_CHUNK_COUNT][];
            fNodePrevSib    = new int[INITIAL_CHUNK_COUNT][];
            fNodeURI        = new int[INITIAL_CHUNK_COUNT][];
        }

        try {
            return fNodeType[chunk][index] != 0;
        }

        catch (ArrayIndexOutOfBoundsException ex) {
            int newsize = chunk * 2;

            int[][] newArray = new int[newsize][];
            System.arraycopy(fNodeType, 0, newArray, 0, chunk);
            fNodeType = newArray;

            newArray = new int[newsize][];
            System.arraycopy(fNodeName, 0, newArray, 0, chunk);
            fNodeName = newArray;

            newArray = new int[newsize][];
            System.arraycopy(fNodeValue, 0, newArray, 0, chunk);
            fNodeValue = newArray;

            newArray = new int[newsize][];
            System.arraycopy(fNodeParent, 0, newArray, 0, chunk);
            fNodeParent = newArray;

            newArray = new int[newsize][];
            System.arraycopy(fNodeLastChild, 0, newArray, 0, chunk);
            fNodeLastChild = newArray;

            newArray = new int[newsize][];
            System.arraycopy(fNodePrevSib, 0, newArray, 0, chunk);
            fNodePrevSib = newArray;

            newArray = new int[newsize][];
            System.arraycopy(fNodeURI, 0, newArray, 0, chunk);
            fNodeURI = newArray;
        }

        catch (NullPointerException ex) {
        }

        createChunk(fNodeType, chunk);
        createChunk(fNodeName, chunk);
        createChunk(fNodeValue, chunk);
        createChunk(fNodeParent, chunk);
        createChunk(fNodeLastChild, chunk);
        createChunk(fNodePrevSib, chunk);
        createChunk(fNodeURI, chunk);

        return true;


    /** Creates a node of the specified type. */
    protected int createNode(short nodeType) {

        int chunk = fNodeCount >> CHUNK_SHIFT;
        int index = fNodeCount & CHUNK_MASK;
        ensureCapacity(chunk, index);

        setChunkIndex(fNodeType, nodeType, chunk, index);

        return fNodeCount++;


    /**
     * Performs a binary search for a target value in an array of
     * values. The array of values must be in ascending sorted order
     * before calling this method and all array values must be
     * non-negative.
     *
     * @param values  The array of values to search.
     * @param start   The starting offset of the search.
     * @param end     The ending offset of the search.
     * @param target  The target value.
     *
     * @return This function will return the <i>first</i> occurrence
     *         of the target value, or -1 if the target value cannot
     *         be found.
     */
    protected static int binarySearch(final int values[],
                                      int start, int end, int target) {

        if (DEBUG_IDS) {
            System.out.println("binarySearch(), target: "+target);
        }

        while (start <= end) {

            int middle = (start + end) / 2;
            int value  = values[middle];
            if (DEBUG_IDS) {
                print(values, start, end, middle, target);
            }
            if (value == target) {
                while (middle > 0 && values[middle - 1] == target) {
                    middle--;
                }
                if (DEBUG_IDS) {
                    System.out.println("FOUND AT "+middle);
                }
                return middle;
            }

            if (value > target) {
                end = middle - 1;
            }
            else {
                start = middle + 1;
            }


        if (DEBUG_IDS) {
            System.out.println("NOT FOUND!");
        }
        return -1;



    /** Creates the specified chunk in the given array of chunks. */
    private final void createChunk(int data[][], int chunk) {
        data[chunk] = new int[CHUNK_SIZE + 2];
        for (int i = 0; i < CHUNK_SIZE; i++) {
            data[chunk][i] = -1;
        }
    }

    /**
     * Sets the specified value in the given of data at the chunk and index.
     *
     * @return Returns the old value.
     */
    private final int setChunkIndex(int data[][], int value, int chunk, int index) {
        if (value == -1) {
            return clearChunkIndex(data, chunk, index);
        }
        int ovalue = data[chunk][index];
        if (ovalue == -1) {
            data[chunk][CHUNK_SIZE]++;
        }
        data[chunk][index] = value;
        return ovalue;
    }

    /**
     * Returns the specified value in the given data at the chunk and index.
     */
    private final int getChunkIndex(int data[][], int chunk, int index) {
        return data[chunk] != null ? data[chunk][index] : -1;
    }

    /**
     * Clears the specified value in the given data at the chunk and index.
     * Note that this method will clear the given chunk if the reference
     * count becomes zero.
     *
     * @return Returns the old value.
     */
    private final int clearChunkIndex(int data[][], int chunk, int index) {
        int value = data[chunk] != null ? data[chunk][index] : -1;
        if (value != -1) {
            data[chunk][CHUNK_SIZE + 1]++;
            data[chunk][index] = -1;
            if (data[chunk][CHUNK_SIZE] == data[chunk][CHUNK_SIZE + 1]) {
                data[chunk] = null;
            }
        }
        return value;
    }

    /**
     * This version of putIdentifier is needed to avoid fluffing
     * all of the paths to ID attributes when a node object is
     * created that contains an ID attribute.
     */
    private final void putIdentifier0(String idName, Element element) {

        if (DEBUG_IDS) {
            System.out.println("putIdentifier0("+
                               idName+", "+
                               element+')');
        }

        if (identifiers == null) {
            identifiers = new java.util.Hashtable();
        }

        identifiers.put(idName, element);


    /** Prints the ID array. */
    private static void print(int values[], int start, int end,
                              int middle, int target) {

        if (DEBUG_IDS) {
            System.out.print(start);
            System.out.print(" [");
            for (int i = start; i < end; i++) {
                if (middle == i) {
                    System.out.print("!");
                }
                System.out.print(values[i]);
                if (values[i] == target) {
                    System.out.print("*");
                }
                if (i < end - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println("] "+end);
        }



    /**
     * A simple integer vector.
     */
    static class IntVector {


        /** Data. */
        private int data[];

        /** Size. */
        private int size;


        /** Returns the length of this vector. */
        public int size() {
            return size;
        }

        /** Returns the element at the specified index. */
        public int elementAt(int index) {
            return data[index];
        }

        /** Appends an element to the end of the vector. */
        public void addElement(int element) {
            ensureCapacity(size + 1);
            data[size++] = element;
        }

        /** Clears the vector. */
        public void removeAllElements() {
            size = 0;
        }


        /** Makes sure that there is enough storage. */
        private void ensureCapacity(int newsize) {

            if (data == null) {
                data = new int[newsize + 15];
            }
            else if (newsize > data.length) {
                int newdata[] = new int[newsize + 15];
                System.arraycopy(data, 0, newdata, 0, data.length);
                data = newdata;
            }



