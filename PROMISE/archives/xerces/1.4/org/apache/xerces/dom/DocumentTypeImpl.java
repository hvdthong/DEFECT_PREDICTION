package org.apache.xerces.dom;

import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

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
 * @author Arnaud  Le Hors, IBM
 * @author Joe Kesselman, IBM
 * @author Andy Clark, IBM
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public class DocumentTypeImpl 
    extends ParentNode
    implements DocumentType {


    /** Serialization version. */
    static final long serialVersionUID = 7751299192316526485L;
    

    /** Document type name. */
    protected String name;

    /** Entities. */
    protected NamedNodeMapImpl entities;
    
    /** Notations. */
    protected NamedNodeMapImpl notations;


    /** Elements. */
    protected NamedNodeMapImpl elements;
    
    protected String publicID;
    
    protected String systemID;
    
    protected String internalSubset;


    /** Factory method for creating a document type node. */
    public DocumentTypeImpl(CoreDocumentImpl ownerDocument, String name) {
        super(ownerDocument);

        this.name = name;
        entities  = new NamedNodeMapImpl(this);
        notations = new NamedNodeMapImpl(this);

        elements = new NamedNodeMapImpl(this);

  
    /** Factory method for creating a document type node. */
    public DocumentTypeImpl(CoreDocumentImpl ownerDocument,
                            String qualifiedName,
                            String publicID, String systemID) {
        this(ownerDocument, qualifiedName);
        this.publicID = publicID;
        this.systemID = systemID;

    
    
    /**
     * Introduced in DOM Level 2. <p>
     * 
     * Return the public identifier of this Document type.
     * @since WD-DOM-Level-2-19990923
     */
    public String getPublicId() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return publicID;
    }
    /**
     * Introduced in DOM Level 2. <p>
     * 
     * Return the system identifier of this Document type.
     * @since WD-DOM-Level-2-19990923
     */
    public String getSystemId() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return systemID;
    }
    
    /**
     * NON-DOM. <p>
     *
     * Set the internalSubset given as a string.
     */
    public void setInternalSubset(String internalSubset) {
        if (needsSyncData()) {
            synchronizeData();
        }
        this.internalSubset = internalSubset;
    }

    /**
     * Introduced in DOM Level 2. <p>
     * 
     * Return the internalSubset given as a string.
     * @since WD-DOM-Level-2-19990923
     */
    public String getInternalSubset() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return internalSubset;
    }
    

    /** 
     * A short integer indicating what type of node this is. The named
     * constants for this value are defined in the org.w3c.dom.Node interface.
     */
    public short getNodeType() {
        return Node.DOCUMENT_TYPE_NODE;
    }
    
    /**
     * Returns the document type name
     */
    public String getNodeName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return name;
    }

    /** Clones the node. */
    public Node cloneNode(boolean deep) {

    	DocumentTypeImpl newnode = (DocumentTypeImpl)super.cloneNode(deep);
    	newnode.entities  = entities.cloneMap(newnode);
    	newnode.notations = notations.cloneMap(newnode);
    	newnode.elements  = elements.cloneMap(newnode);

    	return newnode;


    /**
     * NON-DOM
     * set the ownerDocument of this node and its children
     */
    void setOwnerDocument(CoreDocumentImpl doc) {
        super.setOwnerDocument(doc);
        entities.setOwnerDocument(doc);
        notations.setOwnerDocument(doc);
        elements.setOwnerDocument(doc);
    }


    /**
     * Name of this document type. If we loaded from a DTD, this should
     * be the name immediately following the DOCTYPE keyword.
     */
    public String getName() {

        if (needsSyncData()) {
            synchronizeData();
        }
    	return name;


    /**
     * Access the collection of general Entities, both external and
     * internal, defined in the DTD. For example, in:
     * <p>
     * <pre>
     *   &lt;!doctype example SYSTEM "ex.dtd" [
     *     &lt;!ENTITY foo "foo"&gt;
     *     &lt;!ENTITY bar "bar"&gt;
     *     &lt;!ENTITY % baz "baz"&gt;
     *     ]&gt;
     * </pre>
     * <p>
     * The Entities map includes foo and bar, but not baz. It is promised that
     * only Nodes which are Entities will exist in this NamedNodeMap.
     * <p>
     * For HTML, this will always be null.
     * <p>
     * Note that "built in" entities such as &amp; and &lt; should be
     * converted to their actual characters before being placed in the DOM's
     * contained text, and should be converted back when the DOM is rendered
     * as XML or HTML, and hence DO NOT appear here.
     */
    public NamedNodeMap getEntities() {
        if (needsSyncChildren()) {
            synchronizeChildren();
            }
    	return entities;
    }

    /**
     * Access the collection of Notations defined in the DTD.  A
     * notation declares, by name, the format of an XML unparsed entity
     * or is used to formally declare a Processing Instruction target.
     */
    public NamedNodeMap getNotations() {
        if (needsSyncChildren()) {
            synchronizeChildren();
            }
    	return notations;
    }


    /**
     * NON-DOM: Subclassed to flip the entities' and notations' readonly switch
     * as well.
     * @see NodeImpl#setReadOnly
     */
    public void setReadOnly(boolean readOnly, boolean deep) {
    	
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        super.setReadOnly(readOnly, deep);

        elements.setReadOnly(readOnly, true);
        entities.setReadOnly(readOnly, true);
    	notations.setReadOnly(readOnly, true);

    
    /**
     * NON-DOM: Access the collection of ElementDefinitions.
     * @see ElementDefinitionImpl
     */
    public NamedNodeMap getElements() {
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
    	return elements;
    }

