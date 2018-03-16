package org.apache.xerces.dom;

import org.w3c.dom.*;

/**
 * Represents an XML (or HTML) comment.
 *
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public class CommentImpl 
    extends CharacterDataImpl 
    implements CharacterData, Comment {


    /** Serialization version. */
    static final long serialVersionUID = -2685736833408134044L;


    /** Factory constructor. */
    public CommentImpl(DocumentImpl ownerDoc, String data) {
    	super(ownerDoc, data);
    }
    

    /** 
     * A short integer indicating what type of node this is. The named
     * constants for this value are defined in the org.w3c.dom.Node interface.
     */
    public short getNodeType() {
        return Node.COMMENT_NODE;
    }

    /** Returns the node name. */
    public String getNodeName() {
        return "#comment";
    }

