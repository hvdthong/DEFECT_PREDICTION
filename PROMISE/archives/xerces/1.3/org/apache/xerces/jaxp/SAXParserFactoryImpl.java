package org.apache.xerces.jaxp;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * The <code>SAXParserFactory</code> implementation for the Apache Xerces
 * XML parser.
 *
 * @author <a href="mailto:fumagalli@exoffice.com">Pierpaolo Fumagalli</a>
 *         (Apache Software Foundation, Exoffice Technologies)
 * @version $Revision: 316582 $ $Date: 2000-11-16 08:57:43 +0800 (周四, 2000-11-16) $
 */
public class SAXParserFactoryImpl extends SAXParserFactory {

    /**
     * Create a new <code>SAXParserFactoryImpl</code> instance.
     */
    public SAXParserFactoryImpl() {
        super();
    }

    /**
     * Returns a new configured instance of type <code>SAXParser</code>.
     */
    public SAXParser newSAXParser()
    throws ParserConfigurationException {
        return(new SAXParserImpl(this.isNamespaceAware(),this.isValidating()));
    }

    /**
     * Sets the particular feature in the underlying implementation of 
     * org.xml.sax.XMLReader.
     */
    public void setFeature(String name, boolean value)
        throws ParserConfigurationException, SAXNotRecognizedException, 
		SAXNotSupportedException
    {
        throw new SAXNotRecognizedException("Feature: " + name);
    }

    /**
     * returns the particular property requested for in the underlying 
     * implementation of org.xml.sax.XMLReader.
     */
    public boolean getFeature(String name)
        throws ParserConfigurationException, SAXNotRecognizedException,
		SAXNotSupportedException
    {
        throw new SAXNotRecognizedException("Feature: " + name);
    }

}
