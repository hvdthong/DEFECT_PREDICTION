package org.apache.wml.dom;

import org.apache.wml.*;
import org.w3c.dom.*;
import org.apache.xerces.dom.*;

/**
 * @version $Id: WMLDOMImplementationImpl.java 315461 2000-04-23 18:07:48Z david $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */

public class WMLDOMImplementationImpl extends DOMImplementationImpl implements WMLDOMImplementation {

    static DOMImplementationImpl singleton = new WMLDOMImplementationImpl();

    /** NON-DOM: Obtain and return the single shared object */
    public static DOMImplementation getDOMImplementation() {
        return singleton;
    }  

    /**
     * @see org.w3c.dom.DOMImplementation
     */
    public Document createDocument(String namespaceURI, 
				   String qualifiedName, 
				   DocumentType doctype) throws DOMException {
        DocumentImpl doc = new WMLDocumentImpl(doctype);
        Element e = doc.createElementNS( namespaceURI, qualifiedName);
        doc.appendChild(e);
        return doc;
    }
}

