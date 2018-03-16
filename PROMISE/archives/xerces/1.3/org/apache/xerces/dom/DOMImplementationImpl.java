package org.apache.xerces.dom;

import org.w3c.dom.*;

/**
 * The DOMImplementation class is description of a particular
 * implementation of the Document Object Model. As such its data is
 * static, shared by all instances of this implementation.
 * <P>
 * The DOM API requires that it be a real object rather than static
 * methods. However, there's nothing that says it can't be a singleton,
 * so that's how I've implemented it.
 *
 * @version
 * @since  PR-DOM-Level-1-19980818.
 */
public class DOMImplementationImpl  
    implements DOMImplementation {



    /** Dom implementation singleton. */
    static DOMImplementationImpl singleton = new DOMImplementationImpl();


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
         || (feature.equalsIgnoreCase("Events") 
	     && (anyVersion
		 || version.equals("2.0")))
         || (feature.equalsIgnoreCase("MutationEvents") 
	     && (anyVersion
		 || version.equals("2.0")))
         || (feature.equalsIgnoreCase("Traversal") 
	     && (anyVersion
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
    	if (!DocumentImpl.isXMLName(qualifiedName)) {
    		throw new DOMException(DOMException.INVALID_CHARACTER_ERR, 
    		                           "DOM002 Illegal character");
        }
        int index = qualifiedName.indexOf(':');
        if (index == 0 || index == qualifiedName.length() - 1) {
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
     *                         element to create, or null. 
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
        DocumentImpl doc = new DocumentImpl(doctype);
        Element e = doc.createElementNS( namespaceURI, qualifiedName);
        doc.appendChild(e);
        return doc;
    }

