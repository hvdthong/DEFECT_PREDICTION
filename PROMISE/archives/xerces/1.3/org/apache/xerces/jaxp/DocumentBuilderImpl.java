package org.apache.xerces.jaxp;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xerces.parsers.DOMParser;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;

/**
 * The <code>DocumentBuilder</code> implementation for the Apache Xerces XML
 * parser.
 *
 * @author <a href="mailto:fumagalli@exoffice.com">Pierpaolo Fumagalli</a>
 *         (Apache Software Foundation, Exoffice Technologies)
 * @version $Revision: 315776 $ $Date: 2000-06-10 01:03:45 +0800 (周六, 2000-06-10) $
 */
public class DocumentBuilderImpl extends DocumentBuilder {

    /** Wether this <code>SAXParserImpl</code> supports namespaces. */
    private boolean namespaces=false;
    /** Wether this <code>SAXParserImpl</code> supports validataion. */
    private boolean validation=false;
    /** The current Xerces SAX <code>Parser</code>. */
    private DOMParser parser=null;

    /** Deny no-argument construction. */
    private DocumentBuilderImpl() {
        super();
    }

    /**
     * Create a new <code>SAXParserFactoryImpl</code> instance.
     */
    protected DocumentBuilderImpl(boolean namespaces, boolean validation)
    throws ParserConfigurationException {
        this();
        DOMParser p=new DOMParser();
        try {
        } catch (SAXException e) {
            throw new ParserConfigurationException("Cannot set namespace "+
                "awareness to "+namespaces);
        }
        try {
        } catch (SAXException e) {
            throw new ParserConfigurationException("Cannot set validation to "+
                validation);
        }
        this.namespaces=namespaces;
        this.validation=validation;
        this.parser=p;
    }

    /**
     * Parses the content of the given <code>InputSource</code> and returns
     * a <code>Document</code> object.
     */
    public Document parse(InputSource source)
    throws SAXException, IOException, IllegalArgumentException {
        if (source==null) throw new IllegalArgumentException();
        this.parser.parse(source);
        return(this.parser.getDocument());
    }

    /**
     * Creates an new <code>Document</code> instance from the underlying DOM
     * implementation.
     */
    public Document newDocument() {
        return(new org.apache.xerces.dom.DocumentImpl());
    }

    /**
     * Returns whether or not this parser supports XML namespaces.
     */
    public boolean isNamespaceAware() {
        return(this.namespaces);
    }

    /**
     * Returns whether or not this parser supports validating XML content.
     */
    public boolean isValidating() {
        return(this.validation);
    }

    /**
     * Specifies the <code>EntityResolver</code> to be used by this
     * <code>DocumentBuilderImpl</code>.
     */
    public void setEntityResolver(EntityResolver er) {
        this.parser.setEntityResolver(er);
    }

    /**
     * Specifies the <code>ErrorHandler</code> to be used by this
     * <code>DocumentBuilderImpl</code>.
     */
    public void setErrorHandler(ErrorHandler eh) {
        this.parser.setErrorHandler(eh);
    }
}

