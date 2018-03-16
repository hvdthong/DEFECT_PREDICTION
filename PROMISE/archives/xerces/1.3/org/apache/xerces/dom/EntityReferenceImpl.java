package org.apache.xerces.dom;

import org.w3c.dom.*;

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
 * @author Arnaud  Le Hors, IBM
 * @author Joe Kesselman, IBM
 * @author Andy Clark, IBM
 * @author Ralf Pfeiffer, IBM
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public class EntityReferenceImpl 
    extends ParentNode
    implements EntityReference {


    /** Serialization version. */
    static final long serialVersionUID = -7381452955687102062L;
    

    /** Name of Entity referenced */
    protected String name;

    /** Entity changes. */

    /** Enable synchronize. */


    /** Factory constructor. */
    public EntityReferenceImpl(DocumentImpl ownerDoc, String name) {
    	super(ownerDoc);
        this.name = name;
        isReadOnly(true);
    }
    

    /** 
     * A short integer indicating what type of node this is. The named
     * constants for this value are defined in the org.w3c.dom.Node interface.
     */
    public short getNodeType() {
        return Node.ENTITY_REFERENCE_NODE;
    }

    /**
     * Returns the name of the entity referenced
     */
    public String getNodeName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return name;
    }


    /**
     * Perform synchronize() before accessing children.
     * 
     * @return org.w3c.dom.NodeList
     */
    public NodeList getChildNodes() {
    	synchronize();
    	return super.getChildNodes();
    }

    /**
     * Perform synchronize() before accessing children.
     * 
     * @return org.w3c.dom.NodeList
     */
    public Node getFirstChild() {
    	synchronize();
    	return super.getFirstChild();
    }

    /**
     * Perform synchronize() before accessing children.
     * 
     * @return org.w3c.dom.NodeList
     */
    public Node getLastChild() {
    	synchronize();
    	return super.getLastChild();
    }

    /**
     * Query the number of children in the entity definition.
     * (A bit more work than asking locally, but may be able to avoid
     * or defer building the clone subtree.)
     *
     * @return org.w3c.dom.NodeList
     */
    public int getLength() {
        synchronize();
    	return super.getLength();
    }

    /**
     * Returns whether this node has any children.
     * @return boolean
     */
    public boolean hasChildNodes() {
    	synchronize();
    	return super.hasChildNodes();
    }

    /** Returns the node at the given index. */
    public Node item(int index) {
    	synchronize();
    	return super.item(index);
    }


    /**
     * EntityReference's children are a reflection of those defined in the
     * named Entity. This method creates them if they haven't been created yet.
     * This doesn't really support editing the Entity though.
     */
    protected void synchronize() {
        if (firstChild != null) {
            return;
        }
    	DocumentType doctype;
    	NamedNodeMap entities;
    	EntityImpl entDef;
    	if (null != (doctype = getOwnerDocument().getDoctype()) && 
            null != (entities = doctype.getEntities())) {
            
            entDef = (EntityImpl)entities.getNamedItem(getNodeName());

            if (entDef == null)
                return;

            isReadOnly(false);
            for (Node defkid = entDef.getFirstChild();
                 defkid != null;
                 defkid = defkid.getNextSibling()) {
                Node newkid = defkid.cloneNode(true);
                insertBefore(newkid,null);
            }
            setReadOnly(true, true);
    	}
    }


    /**
     * Enable the synchronize method which may do cloning. This method is enabled
     * when the parser is done with an EntityReference.
    /***
    public void enableSynchronize(boolean enableSynchronize) {
        fEnableSynchronize= enableSynchronize;
    }
    /***/

    /**
     * EntityReference's children are a reflection of those defined in the
     * named Entity. This method updates them if the Entity is changed.
     * <P>
     * It is unclear what the least-cost resynch mechanism is.
     * If we expect the kids to be shallow, and/or expect changes
     * to the Entity contents to be rare, wiping them all out
     * and recloning is simplest.
     * <P>
     * If we expect them to be deep,
     * it might be better to first decide which kids (if any)
     * persist, and keep the ones (if any) that are unchanged
     * rather than doing all the work of cloning them again.
     * But that latter gets into having to convolve the two child lists,
     * insert new information in the right order (and possibly reorder
     * the existing kids), and a few other complexities that I really
     * don't want to deal with in this implementation.
     * <P>
     * Note that if we decide that we need to update the EntityReference's
     * contents, we have to turn off the readOnly flag temporarily to do so.
     * When we get around to adding multitasking support, this whole method
     * should probably be an atomic operation.
     * 
     * @see DocumentTypeImpl
     * @see EntityImpl
     */
    /***
    private void synchronize() {
        if (!fEnableSynchronize) {
            return;
        }
    	DocumentType doctype;
    	NamedNodeMap entities;
    	EntityImpl entDef;
    	if (null != (doctype = getOwnerDocument().getDoctype()) && 
    		null != (entities = doctype.getEntities())) {
            
    		entDef = (EntityImpl)entities.getNamedItem(getNodeName());

    		if(null==entDef)
    			entityChanges=-1;

    		readOnly=false;
    		if(null==entDef || !entDef.hasChildNodes())
    			for(Node kid=super.getFirstChild();
    				kid!=null;
    				kid=super.getFirstChild())
    				removeChild(kid);

    		if(null!=entDef && entDef.changes!=entityChanges) {
    			for(Node defkid=entDef.getFirstChild();
    				defkid!=null;
    				defkid=defkid.getNextSibling()) {
                    
    				NodeImpl newkid=(NodeImpl) defkid.cloneNode(true);
    				newkid.setReadOnly(true,true);
    				insertBefore(newkid,null);
    			}
    			entityChanges=entDef.changes;
    		}
    		readOnly=true;
    	}
    }
     /***/
    
