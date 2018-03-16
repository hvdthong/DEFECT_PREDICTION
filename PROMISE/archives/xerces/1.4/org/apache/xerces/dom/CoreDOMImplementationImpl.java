package org.apache.xerces.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

/**
 * The DOMImplementation class is description of a particular
 * implementation of the Document Object Model. As such its data is
 * static, shared by all instances of this implementation.
 * <P>
 * The DOM API requires that it be a real object rather than static
 * methods. However, there's nothing that says it can't be a singleton,
 * so that's how I've implemented it.
 * <P>
 * This particular class, along with CoreDocumentImpl, only supports the DOM
 * Core. Optional modules are supported by the more complete DOMImplementation
 * class along with DocumentImpl.
 *
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public class CoreDOMImplementationImpl  
    implements DOMImplementation {



    /** Dom implementation singleton. */
    static CoreDOMImplementationImpl singleton =
        new CoreDOMImplementationImpl();


    /** 
     * Test if the DOM implementation supports a specific "feature" --
     * currently meaning language and level thereof.
     * 
     * @param feature      The package name of the feature to test.
     * In Level 1, supported values are "HTML" and "XML" (case-insensitive).
     * At this writing, org.apache.xerces.dom supports only XML.
     *
     * @param version      The version number of the feature being tested.
     * This is interpreted as "Version of the DOM API supported for the
     * specified Feature", and in Level 1 should be "1.0"
     *
     * @returns    true iff this implementation is compatable with the
     * specified feature and version.
     */
    public boolean hasFeature(String feature, String version) {

        boolean anyVersion = version == null || version.length() == 0;
        return 
            (feature.equalsIgnoreCase("Core") 
            && (anyVersion
		|| version.equals("1.0")
		|| version.equals("2.0")))
         || (feature.equalsIgnoreCase("XML") 
            && (anyVersion
		|| version.equals("1.0")
		|| version.equals("2.0")))
            ;



    /** NON-DOM: Obtain and return the single shared object */
    public static DOMImplementation getDOMImplementation() {
        return singleton;
    }  
    
    /**
     * Introduced in DOM Level 2. <p>
     * 
     * Creates an empty DocumentType node.
     *
     * @param qualifiedName The qualified name of the document type to be created. 
     * @param publicID The document type public identifier.
     * @param systemID The document type system identifier.
     * @since WD-DOM-Level-2-19990923
     */
    public DocumentType       createDocumentType(String qualifiedName, 
                                                 String publicID, 
                                                 String systemID)
    {
    	if (!CoreDocumentImpl.isXMLName(qualifiedName)) {
    		throw new DOMException(DOMException.INVALID_CHARACTER_ERR, 
    		                           "DOM002 Illegal character");
        }
        int index = qualifiedName.indexOf(':');
        int lastIndex = qualifiedName.lastIndexOf(':');
        if (index == 0 || index == qualifiedName.length() - 1  || lastIndex!=index) {
	    throw new DOMException(DOMException.NAMESPACE_ERR, 
				       "DOM003 Namespace error");
	}
    	return new DocumentTypeImpl(null, qualifiedName, publicID, systemID);
    }
    /**
     * Introduced in DOM Level 2. <p>
     * 
     * Creates an XML Document object of the specified type with its document
     * element.
     *
     * @param namespaceURI     The namespace URI of the document
     *                         element to create, or null. 
     * @param qualifiedName    The qualified name of the document
     *                         element to create. 
     * @param doctype          The type of document to be created or null.<p>
     *
     *                         When doctype is not null, its
     *                         Node.ownerDocument attribute is set to
     *                         the document being created.
     * @return Document        A new Document object.
     * @throws DOMException    WRONG_DOCUMENT_ERR: Raised if doctype has
     *                         already been used with a different document.
     * @since WD-DOM-Level-2-19990923
     */
    public Document           createDocument(String namespaceURI, 
                                             String qualifiedName, 
                                             DocumentType doctype)
                                             throws DOMException
    {
    	if (doctype != null && doctype.getOwnerDocument() != null) {
            throw new DOMException(DOMException.WRONG_DOCUMENT_ERR, 
                                   "DOM005 Wrong document");
        }
        CoreDocumentImpl doc = new CoreDocumentImpl(doctype);
        Element e = doc.createElementNS( namespaceURI, qualifiedName);
        doc.appendChild(e);
        return doc;
    }

