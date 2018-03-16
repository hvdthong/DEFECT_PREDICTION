package org.apache.xerces.dom;

import org.w3c.dom.*;

/**
 * Notations are how the Document Type Description (DTD) records hints
 * about the format of an XML "unparsed entity" -- in other words,
 * non-XML data bound to this document type, which some applications
 * may wish to consult when manipulating the document. A Notation
 * represents a name-value pair, with its nodeName being set to the
 * declared name of the notation.
 * <P>
 * Notations are also used to formally declare the "targets" of
 * Processing Instructions.
 * <P>
 * Note that the Notation's data is non-DOM information; the DOM only
 * records what and where it is.
 * <P>
 * See the XML 1.0 spec, sections 4.7 and 2.6, for more info.
 * <P>
 * Level 1 of the DOM does not support editing Notation contents.
 *
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public class NotationImpl 
    extends NodeImpl 
    implements Notation {


    /** Serialization version. */
    static final long serialVersionUID = -764632195890658402L;
    

    /** Notation name. */
    protected String name;

    /** Public identifier. */
    protected String publicId;

    /** System identifier. */
    protected String systemId;


    /** Factory constructor. */
    public NotationImpl(DocumentImpl ownerDoc, String name) {
    	super(ownerDoc);
        this.name = name;
    }
    

    /** 
     * A short integer indicating what type of node this is. The named
     * constants for this value are defined in the org.w3c.dom.Node interface.
     */
    public short getNodeType() {
        return Node.NOTATION_NODE;
    }

    /**
     * Returns the notation name
     */
    public String getNodeName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return name;
    }


    /**
     * The Public Identifier for this Notation. If no public identifier
     * was specified, this will be null.  
     */
    public String getPublicId() {

        if (needsSyncData()) {
            synchronizeData();
        }
    	return publicId;


    /**
     * The System Identifier for this Notation. If no system identifier
     * was specified, this will be null.  
     */
    public String getSystemId() {

        if (needsSyncData()) {
            synchronizeData();
        }
    	return systemId;



    /** 
     * NON-DOM: The Public Identifier for this Notation. If no public
     * identifier was specified, this will be null.  
     */
    public void setPublicId(String id) {

    	if (isReadOnly()) {
    		throw new DOMExceptionImpl(
    			DOMException.NO_MODIFICATION_ALLOWED_ERR,
			"DOM001 Modification not allowed");
        }
        if (needsSyncData()) {
            synchronizeData();
        }
        publicId = id;


    /** 
     * NON-DOM: The System Identifier for this Notation. If no system
     * identifier was specified, this will be null.  
     */
    public void setSystemId(String id) {

    	if(isReadOnly()) {
    		throw new DOMExceptionImpl(
    			DOMException.NO_MODIFICATION_ALLOWED_ERR,
			"DOM001 Modification not allowed");
        }
        if (needsSyncData()) {
            synchronizeData();
        }
    	systemId = id;


