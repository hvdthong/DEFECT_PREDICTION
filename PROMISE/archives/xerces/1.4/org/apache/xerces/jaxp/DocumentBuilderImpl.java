package org.apache.xerces.jaxp;

import java.util.Hashtable;
import java.util.Enumeration;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;

import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.helpers.DefaultHandler;

import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.dom.DOMImplementationImpl;

/**
 * @author Rajiv Mordani
 * @author Edwin Goei
 * @version $Revision: 317270 $
 */
public class DocumentBuilderImpl extends DocumentBuilder {
    /** Xerces features */
    static final String XERCES_FEATURE_PREFIX =
    static final String CREATE_ENTITY_REF_NODES_FEATURE =
                                        "dom/create-entity-ref-nodes";
    static final String INCLUDE_IGNORABLE_WHITESPACE =
                                        "dom/include-ignorable-whitespace";

    private EntityResolver er = null;
    private ErrorHandler eh = null;
    private DOMParser domParser = null;

    private boolean namespaceAware = false;
    private boolean validating = false;

    DocumentBuilderImpl(DocumentBuilderFactory dbf, Hashtable dbfAttrs)
        throws SAXNotRecognizedException, SAXNotSupportedException
    {
        domParser = new DOMParser();

        validating = dbf.isValidating();
        domParser.setFeature(validation, validating);

        if (validating) {
            setErrorHandler(new DefaultValidationErrorHandler());
        }

        namespaceAware = dbf.isNamespaceAware();
                             namespaceAware);

        domParser.setFeature(XERCES_FEATURE_PREFIX +
                             INCLUDE_IGNORABLE_WHITESPACE,
                             !dbf.isIgnoringElementContentWhitespace());
        domParser.setFeature(XERCES_FEATURE_PREFIX +
                             CREATE_ENTITY_REF_NODES_FEATURE,
                             !dbf.isExpandEntityReferences());


        setDocumentBuilderFactoryAttributes(dbfAttrs);
    }

    /**
     * Set any DocumentBuilderFactory attributes of our underlying DOMParser
     *
     * Note: code does not handle possible conflicts between DOMParser
     * attribute names and JAXP specific attribute names,
     * eg. DocumentBuilderFactory.setValidating()
     */
    private void setDocumentBuilderFactoryAttributes(Hashtable dbfAttrs)
        throws SAXNotSupportedException, SAXNotRecognizedException
    {
        if (dbfAttrs != null) {
            for (Enumeration e = dbfAttrs.keys(); e.hasMoreElements();) {
                String name = (String)e.nextElement();
                Object val = dbfAttrs.get(name);
                if (val instanceof Boolean) {
                    domParser.setFeature(name, ((Boolean)val).booleanValue());
                } else {
                    domParser.setProperty(name, val);
                }
            }
        }
    }

    /**
     * Non-preferred: use the getDOMImplementation() method instead of this
     * one to get a DOM Level 2 DOMImplementation object and then use DOM
     * Level 2 methods to create a DOM Document object.
     */
    public Document newDocument() {
        return new org.apache.xerces.dom.DocumentImpl();
    }

    public DOMImplementation getDOMImplementation() {
        return DOMImplementationImpl.getDOMImplementation();
    }

    public Document parse(InputSource is) throws SAXException, IOException {
        if (is == null) {
            throw new IllegalArgumentException("InputSource cannot be null");
        }

        if (er != null) {
            domParser.setEntityResolver(er);
        }

        if (eh != null) {
            domParser.setErrorHandler(eh);      
        }

        domParser.parse(is);
        return domParser.getDocument();
    }

    public boolean isNamespaceAware() {
        return namespaceAware;
    }

    public boolean isValidating() {
        return validating;
    }

    public void setEntityResolver(org.xml.sax.EntityResolver er) {
        this.er = er;
    }

    public void setErrorHandler(org.xml.sax.ErrorHandler eh) {
        this.eh = (eh == null) ? new DefaultHandler() : eh;
    }

    DOMParser getDOMParser() {
        return domParser;
    }
}
