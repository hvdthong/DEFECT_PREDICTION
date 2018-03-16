package org.apache.xerces.dom;

import org.w3c.dom.*;

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
public class EntityImpl 
    extends ParentNode
    implements Entity {


    /** Serialization version. */
    static final long serialVersionUID = -3575760943444303423L;
    

    /** Entity name. */
    protected String name;

    /** Public identifier. */
    protected String publicId;

    /** System identifier. */
    protected String systemId;

    /** Notation name. */
    protected String notationName;


    /** Factory constructor. */
    public EntityImpl(DocumentImpl ownerDoc, String name) {
    	super(ownerDoc);
        this.name = name;
        isReadOnly(true);
    }
    

    /** 
     * A short integer indicating what type of node this is. The named
     * constants for this value are defined in the org.w3c.dom.Node interface.
     */
    public short getNodeType() {
        return Node.ENTITY_NODE;
    }

    /**
     * Returns the entity name
     */
    public String getNodeName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return name;
    }

    /** Clone node. */
    public Node cloneNode(boolean deep) {
        EntityImpl newentity = (EntityImpl)super.cloneNode(deep);
        return newentity;
    }


    /** 
     * The public identifier associated with the entity. If not specified,
     * this will be null. 
     */
    public String getPublicId() {
        
        if (needsSyncData()) {
            synchronizeData();
        }
        return publicId;


    /** 
     * The system identifier associated with the entity. If not specified,
     * this will be null. 
     */
    public String getSystemId() {

        if (needsSyncData()) {
            synchronizeData();
        }
        return systemId;


    /** 
     * Unparsed entities -- which contain non-XML data -- have a
     * "notation name" which tells applications how to deal with them.
     * Parsed entities, which <em>are</em> in XML format, don't need this and
     * set it to null.  
     */
    public String getNotationName() {

        if (needsSyncData()) {
            synchronizeData();
        }
        return notationName;



    /**
     * NON-DOM The public identifier associated with the entity. If not specified,
     * this will be null. */
    public void setPublicId(String id) {
        
        if (needsSyncData()) {
            synchronizeData();
        }
    	publicId = id;


    /**
     * NON-DOM The system identifier associated with the entity. If not
     * specified, this will be null. 
     */
    public void setSystemId(String id) {

        if (needsSyncData()) {
            synchronizeData();
        }
    	systemId = id;


    /** 
     * NON-DOM Unparsed entities -- which contain non-XML data -- have a
     * "notation name" which tells applications how to deal with them.
     * Parsed entities, which <em>are</em> in XML format, don't need this and
     * set it to null.  
     */
    public void setNotationName(String name) {
        
        if (needsSyncData()) {
            synchronizeData();
        }
    	notationName = name;


