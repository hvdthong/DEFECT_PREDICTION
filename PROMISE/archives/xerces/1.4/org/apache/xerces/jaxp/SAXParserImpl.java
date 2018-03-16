package org.apache.xerces.jaxp;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.HandlerBase;
import org.xml.sax.Parser;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderAdapter;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import java.util.*;

/**
 * @author Rajiv Mordani
 * @author Edwin Goei
 * @version $Revision: 317921 $
 */

/**
 * This is the implementation specific class for the
 * <code>javax.xml.parsers.SAXParser</code>. 
 */
public class SAXParserImpl extends javax.xml.parsers.SAXParser {
    private XMLReader xmlReader;
    private Parser parser = null;

    private boolean validating = false;
    private boolean namespaceAware = false;
    
    /**
     * Create a SAX parser with the associated features
     * @param features Hashtable of SAX features, may be null
     */
    SAXParserImpl(SAXParserFactory spf, Hashtable features)
        throws SAXException
    {
        xmlReader = new org.apache.xerces.parsers.SAXParser();

        validating = spf.isValidating();

        if (validating) {
            xmlReader.setErrorHandler(new DefaultValidationErrorHandler());
        }

        xmlReader.setFeature(validation, validating);

        namespaceAware = spf.isNamespaceAware();
        xmlReader.setFeature(namespaces, namespaceAware);

        if (namespaceAware == false) {
            xmlReader.setFeature(prefixes, true);
        }

        setFeatures(features);
    }

    /**
     * Set any features of our XMLReader based on any features set on the
     * SAXParserFactory.
     *
     * XXX Does not handle possible conflicts between SAX feature names and
     * JAXP specific feature names, eg. SAXParserFactory.isValidating()
     */
    private void setFeatures(Hashtable features)
        throws SAXNotSupportedException, SAXNotRecognizedException
    {
        if (features != null) {
            for (Enumeration e = features.keys(); e.hasMoreElements();) {
                String feature = (String)e.nextElement();
                boolean value = ((Boolean)features.get(feature)).booleanValue();
                xmlReader.setFeature(feature, value);
            }
        }
    }

    public Parser getParser() throws SAXException {
        if (parser == null) {
            parser = new XMLReaderAdapter(xmlReader);

            parser.setDocumentHandler(new HandlerBase());
        }
        return parser;
    }

    /**
     * Returns the XMLReader that is encapsulated by the implementation of
     * this class.
     */
    public XMLReader getXMLReader() {
        return xmlReader;
    }

    public boolean isNamespaceAware() {
        return namespaceAware;
    }

    public boolean isValidating() {
        return validating;
    }

    /**
     * Sets the particular property in the underlying implementation of 
     * org.xml.sax.XMLReader.
     */
    public void setProperty(String name, Object value)
        throws SAXNotRecognizedException, SAXNotSupportedException
    {
        xmlReader.setProperty(name, value);
    }

    /**
     * returns the particular property requested for in the underlying 
     * implementation of org.xml.sax.XMLReader.
     */
    public Object getProperty(String name)
        throws SAXNotRecognizedException, SAXNotSupportedException
    {
        return xmlReader.getProperty(name);
    }
}
