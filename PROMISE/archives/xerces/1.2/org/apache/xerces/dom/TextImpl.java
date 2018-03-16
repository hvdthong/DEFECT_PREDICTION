package org.apache.xerces.dom;

import org.w3c.dom.*;

/**
 * Text nodes hold the non-markup, non-Entity content of
 * an Element or Attribute.
 * <P>
 * When a document is first made available to the DOM, there is only
 * one Text object for each block of adjacent plain-text. Users (ie,
 * applications) may create multiple adjacent Texts during editing --
 * see {@link Element#normalize} for discussion.
 * <P>
 * Note that CDATASection is a subclass of Text. This is conceptually
 * valid, since they're really just two different ways of quoting
 * characters when they're written out as part of an XML stream.
 *
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public class TextImpl 
    extends CharacterDataImpl 
    implements CharacterData, Text {


    /** Serialization version. */
    static final long serialVersionUID = -5294980852957403469L;
    

    /** Factory constructor. */
    public TextImpl(DocumentImpl ownerDoc, String data) {
        super(ownerDoc, data);
    }  
    

    /** 
     * A short integer indicating what type of node this is. The named
     * constants for this value are defined in the org.w3c.dom.Node interface.
     */
    public short getNodeType() {
        return Node.TEXT_NODE;
    }

    /** Returns the node name. */
    public String getNodeName() {
        return "#text";
    }

    /**
     * NON-DOM: Set whether this Text is ignorable whitespace.
     */
    public void setIgnorableWhitespace(boolean ignore) {

        if (needsSyncData()) {
            synchronizeData();
        }
        isIgnorableWhitespace(ignore);

    

    /**
     * NON-DOM: Returns whether this Text is ignorable whitespace.
     */
    public boolean isIgnorableWhitespace() {

        if (needsSyncData()) {
            synchronizeData();
        }
        return internalIsIgnorableWhitespace();

    

    /** 
     * Break a text node into two sibling nodes.  (Note that if the
     * current node has no parent, they won't wind up as "siblings" --
     * they'll both be orphans.)
     *
     * @param offset The offset at which to split. If offset is at the
     * end of the available data, the second node will be empty.
     *
     * @returns A reference to the new node (containing data after the
     * offset point). The original node will contain data up to that
     * point.
     *
     * @throws DOMException(INDEX_SIZE_ERR) if offset is <0 or >length.
     *
     * @throws DOMException(NO_MODIFICATION_ALLOWED_ERR) if node is read-only.
     */
    public Text splitText(int offset) 
        throws DOMException {

    	if (isReadOnly()) {
            throw new DOMExceptionImpl(
    			DOMException.NO_MODIFICATION_ALLOWED_ERR, 
    			"DOM001 Modification not allowed");
        }

        if (needsSyncData()) {
            synchronizeData();
        }
    	if (offset < 0 || offset > data.length() - 1) {
            throw new DOMExceptionImpl(DOMException.INDEX_SIZE_ERR, 
                                       "DOM004 Index out of bounds");
        }
    		
    	Text newText =
            getOwnerDocument().createTextNode(data.substring(offset));
    	setNodeValue(data.substring(0, offset));

        Node parentNode = getParentNode();
    	if (parentNode != null) {
    		parentNode.insertBefore(newText, nextSibling);
        }

    	return newText;


