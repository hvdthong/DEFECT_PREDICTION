package org.apache.xerces.jaxp;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import java.util.Hashtable;

/**
 * @author Rajiv Mordani
 * @author Edwin Goei
 * @version $Revision: 317014 $
 */

/**
 * This is the implementation specific class for the
 * <code>javax.xml.parsers.SAXParserFactory</code>. This is the platform
 * default implementation for the platform.
 */
public class SAXParserFactoryImpl extends SAXParserFactory {
    private Hashtable features;

    /**
     * Creates a new instance of <code>SAXParser</code> using the currently
     * configured factory parameters.
     * @return javax.xml.parsers.SAXParser
     */
    public SAXParser newSAXParser()
        throws ParserConfigurationException
    {
        SAXParser saxParserImpl;
        try {
            saxParserImpl = new SAXParserImpl(this, features);
        } catch (SAXException se) {
            throw new ParserConfigurationException(se.getMessage());
        }
	return saxParserImpl;
    }

    /**
     * Common code for translating exceptions
     */
    private SAXParserImpl newSAXParserImpl()
        throws ParserConfigurationException, SAXNotRecognizedException, 
        SAXNotSupportedException
    {
        SAXParserImpl saxParserImpl;
        try {
            saxParserImpl = new SAXParserImpl(this, features);
        } catch (SAXNotSupportedException e) {
            throw e;
        } catch (SAXNotRecognizedException e) {
            throw e;
        } catch (SAXException se) {
            throw new ParserConfigurationException(se.getMessage());
        }
        return saxParserImpl;
    }

    /**
     * Sets the particular feature in the underlying implementation of 
     * org.xml.sax.XMLReader.
     */
    public void setFeature(String name, boolean value)
        throws ParserConfigurationException, SAXNotRecognizedException, 
		SAXNotSupportedException
    {
        if (features == null) {
            features = new Hashtable();
        }
        features.put(name, new Boolean(value));

        try {
            newSAXParserImpl();
        } catch (SAXNotSupportedException e) {
            features.remove(name);
            throw e;
        } catch (SAXNotRecognizedException e) {
            features.remove(name);
            throw e;
        }
    }

    /**
     * returns the particular property requested for in the underlying 
     * implementation of org.xml.sax.XMLReader.
     */
    public boolean getFeature(String name)
        throws ParserConfigurationException, SAXNotRecognizedException,
		SAXNotSupportedException
    {
        return newSAXParserImpl().getXMLReader().getFeature(name);
    }
}
