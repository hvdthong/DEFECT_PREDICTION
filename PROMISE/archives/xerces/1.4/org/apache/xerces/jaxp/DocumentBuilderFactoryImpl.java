package org.apache.xerces.jaxp;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.util.Hashtable;

import org.apache.xerces.parsers.DOMParser;

/**
 * @author Rajiv Mordani
 * @author Edwin Goei
 * @version $Revision: 317270 $
 */
public class DocumentBuilderFactoryImpl extends DocumentBuilderFactory {
    /** These are DocumentBuilderFactory attributes not DOM attributes */
    private Hashtable attributes;

    public DocumentBuilderFactoryImpl() {
   	 
    }

    /**
     * 
     */
    public DocumentBuilder newDocumentBuilder()
        throws ParserConfigurationException 
    {
        try {
            return new DocumentBuilderImpl(this, attributes);
        } catch (SAXException se) {
            throw new ParserConfigurationException(se.getMessage());
        }
    }

    /**
     * Allows the user to set specific attributes on the underlying 
     * implementation.
     */
    public void setAttribute(String name, Object value)
        throws IllegalArgumentException
    {
        if (attributes == null) {
            attributes = new Hashtable();
        }
        attributes.put(name, value);

        try {
            new DocumentBuilderImpl(this, attributes);
        } catch (Exception e) {
            attributes.remove(name);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Allows the user to retrieve specific attributes on the underlying 
     * implementation.
     */
    public Object getAttribute(String name)
        throws IllegalArgumentException
    {
        DOMParser domParser = null;

        try {
            domParser =
                new DocumentBuilderImpl(this, attributes).getDOMParser();
            return domParser.getProperty(name);
        } catch (SAXException se1) {
            try {
                boolean result = domParser.getFeature(name);
                return new Boolean(result);
            } catch (SAXException se2) {
                throw new IllegalArgumentException(se1.getMessage());
            }
        }
    }
}
