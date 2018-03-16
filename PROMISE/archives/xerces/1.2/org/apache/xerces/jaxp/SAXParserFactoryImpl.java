package org.apache.xerces.jaxp;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

/**
 * The <code>SAXParserFactory</code> implementation for the Apache Xerces
 * XML parser.
 *
 * @author <a href="mailto:fumagalli@exoffice.com">Pierpaolo Fumagalli</a>
 *         (Apache Software Foundation, Exoffice Technologies)
 * @version $Revision: 315776 $ $Date: 2000-06-10 01:03:45 +0800 (周六, 2000-06-10) $
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
}
