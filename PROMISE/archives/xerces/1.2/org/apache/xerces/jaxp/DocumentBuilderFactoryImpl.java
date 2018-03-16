package org.apache.xerces.jaxp;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.SAXException;

/**
 * The <code>DocumentBuilderFactory</code> implementation for the Apache
 * Xerces XML parser.
 *
 * @author <a href="mailto:fumagalli@exoffice.com">Pierpaolo Fumagalli</a>
 *         (Apache Software Foundation, Exoffice Technologies)
 * @version $Revision: 315776 $ $Date: 2000-06-10 01:03:45 +0800 (周六, 2000-06-10) $
 */
public class DocumentBuilderFactoryImpl extends DocumentBuilderFactory{

    /**
     * Create a new <code>DocumentBuilderFactoryImpl</code> instance.
     */
    public DocumentBuilderFactoryImpl() {
        super();
    }

    /**
     * Returns a new configured instance of type <code>DocumentBuilder</code>.
     */
    public DocumentBuilder newDocumentBuilder()
    throws ParserConfigurationException {
        return(new DocumentBuilderImpl(this.isNamespaceAware(),
                                       this.isValidating()));
    }
}
