package org.apache.xerces.dom;

import org.w3c.dom.*;

/**
 * ElementNSImpl inherits from ElementImpl and adds namespace support. 
 * <P>
 * The qualified name is the node name, and we store localName which is also
 * used in all queries. On the other hand we recompute the prefix when
 * necessary.
 */
public class ElementNSImpl
    extends ElementImpl {


    /** Serialization version. */
    static final long serialVersionUID = -9142310625494392642L;


    /** DOM2: Namespace URI. */
    protected String namespaceURI;
  
    /** DOM2: localName. */
    protected String localName;

    
    /**
     * DOM2: Constructor for Namespace implementation.
     */
    protected ElementNSImpl(DocumentImpl ownerDocument, 
			    String namespaceURI,
			    String qualifiedName) 
        throws DOMException
    {
    	super(ownerDocument, qualifiedName);

        int index = qualifiedName.indexOf(':');
        String prefix;
        if (index < 0) {
            prefix = null;
            localName = qualifiedName;
        } 
        else {
            prefix = qualifiedName.substring(0, index); 
            localName = qualifiedName.substring(index+1);
        
            if (ownerDocument.errorChecking) {
                if (namespaceURI == null
                    || (localName.length() == 0)
                    || (localName.indexOf(':') >= 0)) {
                    throw new DOMException(DOMException.NAMESPACE_ERR, 
                                           "DOM003 Namespace error");
                }
                else if (prefix.equals("xml")) {
                    if (!namespaceURI.equals(xmlURI)) {
                        throw new DOMException(DOMException.NAMESPACE_ERR, 
                                               "DOM003 Namespace error");
                    }
                } else if (index == 0) {
                    throw new DOMException(DOMException.NAMESPACE_ERR, 
                                           "DOM003 Namespace error");
                }
            }
        }
	this.namespaceURI = namespaceURI;
    }

    protected ElementNSImpl(DocumentImpl ownerDocument, 
			    String value) {
	super(ownerDocument, value);
    }


    
    
    /** 
     * Introduced in DOM Level 2. <p>
     *
     * The namespace URI of this node, or null if it is unspecified.<p>
     *
     * This is not a computed value that is the result of a namespace lookup based on
     * an examination of the namespace declarations in scope. It is merely the
     * namespace URI given at creation time.<p>
     *
     * For nodes created with a DOM Level 1 method, such as createElement
     * from the Document interface, this is null.     
     * @since WD-DOM-Level-2-19990923
     */
    public String getNamespaceURI()
    {
        if (needsSyncData()) {
            synchronizeData();
        }
        return namespaceURI;
    }
    
    /** 
     * Introduced in DOM Level 2. <p>
     *
     * The namespace prefix of this node, or null if it is unspecified. <p>
     *
     * For nodes created with a DOM Level 1 method, such as createElement
     * from the Document interface, this is null. <p>
     *
     * @since WD-DOM-Level-2-19990923
     */
    public String getPrefix()
    {
        if (needsSyncData()) {
            synchronizeData();
        }
        int index = name.indexOf(':');
        return index < 0 ? null : name.substring(0, index); 
    }
    
    /** 
     * Introduced in DOM Level 2. <p>
     *
     * Note that setting this attribute changes the nodeName attribute, which holds the
     * qualified name, as well as the tagName and name attributes of the Element
     * and Attr interfaces, when applicable.<p>
     *
     * @throws INVALID_CHARACTER_ERR Raised if the specified
     * prefix contains an invalid character.     
     *
     * @since WD-DOM-Level-2-19990923
     */
    public void setPrefix(String prefix)
        throws DOMException
    {
        if (needsSyncData()) {
            synchronizeData();
        }
	if (ownerDocument().errorChecking) {
            if (isReadOnly()) {
                throw new DOMException(
                                     DOMException.NO_MODIFICATION_ALLOWED_ERR, 
                                     "DOM001 Modification not allowed");
            }
            if (!DocumentImpl.isXMLName(prefix)) {
                throw new DOMException(DOMException.INVALID_CHARACTER_ERR, 
    	                               "DOM002 Illegal character");
            }
            if (namespaceURI == null) {
                  throw new DOMException(DOMException.NAMESPACE_ERR, 
                                         "DOM003 Namespace error");
            } else if (prefix != null) {
                if (prefix.equals("xml")) {
                    if (!namespaceURI.equals(xmlURI)) {
                        throw new DOMException(DOMException.NAMESPACE_ERR, 
                                               "DOM003 Namespace error");
                    }
                }
            }
        }
        name = prefix + ":" + localName;
    }
                                        
    /** 
     * Introduced in DOM Level 2. <p>
     *
     * Returns the local part of the qualified name of this node.
     * @since WD-DOM-Level-2-19990923
     */
    public String             getLocalName()
    {
        if (needsSyncData()) {
            synchronizeData();
        }
        return localName;
    }
}
