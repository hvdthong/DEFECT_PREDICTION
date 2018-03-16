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
 * @version $Revision: 316582 $ $Date: 2000-11-16 08:57:43 +0800 (周四, 2000-11-16) $
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

    /**
     * Allows the user to set specific attributes on the underlying 
     * implementation.
     */
    public void setAttribute(String name, Object value)
        throws IllegalArgumentException
    {
        throw new IllegalArgumentException("No attributes are implemented");
    }

    /**
     * Allows the user to retrieve specific attributes on the underlying 
     * implementation.
     */
    public Object getAttribute(String name)
        throws IllegalArgumentException
    {
        throw new IllegalArgumentException("No attributes are implemented");
    }
}
