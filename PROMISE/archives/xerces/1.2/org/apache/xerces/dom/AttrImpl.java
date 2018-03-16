package org.apache.xerces.dom;

import org.w3c.dom.*;
import org.apache.xerces.dom.events.MutationEventImpl;

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
 * <p>
 * Note: The ownerNode attribute is used to store the Element the Attr
 * node is associated with. Attr nodes do not have parent nodes.
 * Besides, the getOwnerElement() method can be used to get the element node
 * this attribute is associated with.
 * <P>
 * AttrImpl does not support Namespaces. AttrNSImpl, which inherits from
 * it, does.
 * @see AttrNSImpl
 *
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public class AttrImpl
    extends ParentNode
    implements Attr {


    /** Serialization version. */
    static final long serialVersionUID = 7277707688218972102L;


    /** Attribute name. */
    protected String name;


    /**
     * Attribute has no public constructor. Please use the factory
     * method in the Document class.
     */
    protected AttrImpl(DocumentImpl ownerDocument, String name) {
    	super(ownerDocument);
        this.name = name;
        /** False for default attributes. */
        isSpecified(true);
    }

    protected AttrImpl() {}

    
    public Node cloneNode(boolean deep) {
        AttrImpl clone = (AttrImpl) super.cloneNode(deep);
        clone.isSpecified(true);
        return clone;
    }

    /**
     * A short integer indicating what type of node this is. The named
     * constants for this value are defined in the org.w3c.dom.Node interface.
     */
    public short getNodeType() {
        return Node.ATTRIBUTE_NODE;
    }

    /**
     * Returns the attribute name
     */
    public String getNodeName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return name;
    }

    /**
     * Implicit in the rerouting of getNodeValue to getValue is the
     * need to redefine setNodeValue, for symmetry's sake.  Note that
     * since we're explicitly providing a value, Specified should be set
     * true.... even if that value equals the default.
     */
    public void setNodeValue(String value) throws DOMException {
    	setValue(value);
    }

    /**
     * In Attribute objects, NodeValue is considered a synonym for
     * Value.
     *
     * @see #getValue()
     */
    public String getNodeValue() {
    	return getValue();
    }


    /**
     * In Attributes, NodeName is considered a synonym for the
     * attribute's Name
     */
    public String getName() {

        if (needsSyncData()) {
            synchronizeData();
        }
    	return name;


    /**
     * The DOM doesn't clearly define what setValue(null) means. I've taken it
     * as "remove all children", which from outside should appear
     * similar to setting it to the empty string.
     */
    public void setValue(String value) {

    	if (isReadOnly()) {
    		throw new DOMExceptionImpl(
    			DOMException.NO_MODIFICATION_ALLOWED_ERR, 
    			"DOM001 Modification not allowed");
        }
    		
        LCount lc=null;
        String oldvalue="";
        if(MUTATIONEVENTS && ownerDocument.mutationEvents)
        {
            lc=LCount.lookup(MutationEventImpl.DOM_ATTR_MODIFIED);
            if(lc.captures+lc.bubbles+lc.defaults>0 && ownerNode!=null)
            {
               oldvalue=getValue();
            }
            

        if(MUTATIONEVENTS && ownerDocument.mutationEvents)
        {
            if (needsSyncChildren()) {
                synchronizeChildren();
            }
            while(firstChild!=null)
                internalRemoveChild(firstChild,MUTATION_LOCAL);
        }
        else
        {
            if (firstChild != null) {
                firstChild.previousSibling = null;
                firstChild.isFirstChild(false);
                firstChild   = null;
            }
            needsSyncChildren(false);
        }

    	isSpecified(true);
        if (value != null) {
            internalInsertBefore(ownerDocument.createTextNode(value),null,
                                 MUTATION_LOCAL);
        }
		

        if(MUTATIONEVENTS && ownerDocument.mutationEvents)
        {
            dispatchAggregateEvents(this,oldvalue);            
        }
		

    /**
     * The "string value" of an Attribute is its text representation,
     * which in turn is a concatenation of the string values of its children.
     */
    public String getValue() {

        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        if (firstChild == null) {
            return "";
        }
        ChildNode node = firstChild.nextSibling;
        if (node == null) {
            return firstChild.getNodeValue();
        }
    	StringBuffer value = new StringBuffer(firstChild.getNodeValue());
    	while (node != null) {
            value.append(node.getNodeValue());
            node = node.nextSibling;
    	}
    	return value.toString();


    /**
     * The "specified" flag is true if and only if this attribute's
     * value was explicitly specified in the original document. Note that
     * the implementation, not the user, is in charge of this
     * property. If the user asserts an Attribute value (even if it ends
     * up having the same value as the default), it is considered a
     * specified attribute. If you really want to revert to the default,
     * delete the attribute from the Element, and the Implementation will
     * re-assert the default (if any) in its place, with the appropriate
     * specified=false setting.
     */
    public boolean getSpecified() {

        if (needsSyncData()) {
            synchronizeData();
        }
    	return isSpecified();



    /**
     * Returns the element node that this attribute is associated with,
     * or null if the attribute has not been added to an element.
     *
     * @see #getOwnerElement
     *
     * @deprecated Previous working draft of DOM Level 2. New method
     *             is <tt>getOwnerElement()</tt>.
     */
    public Element getElement() {
        return (Element) (isOwned() ? ownerNode : null);
    }

    /**
     * Returns the element node that this attribute is associated with,
     * or null if the attribute has not been added to an element.
     *
     * @since WD-DOM-Level-2-19990719
     */
    public Element getOwnerElement() {
        return (Element) (isOwned() ? ownerNode : null);
    }
    
    public void normalize() {

    	Node kid, next;
    	for (kid = firstChild; kid != null; kid = next) {
    		next = kid.getNextSibling();

    		if (next != null
			 && kid.getNodeType() == Node.TEXT_NODE
			 && next.getNodeType() == Node.TEXT_NODE)
    	    {
    			((Text)kid).appendData(next.getNodeValue());
    			removeChild(next);
    		}

        }



    /** NON-DOM, for use by parser */
    public void setSpecified(boolean arg) {

        if (needsSyncData()) {
            synchronizeData();
        }
    	isSpecified(arg);



    /** NON-DOM method for debugging convenience */
    public String toString() {
    	return getName() + "=" + "\"" + getValue() + "\"";
    }

