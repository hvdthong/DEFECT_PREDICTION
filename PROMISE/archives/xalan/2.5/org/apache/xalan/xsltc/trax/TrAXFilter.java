package org.apache.xalan.xsltc.trax;

import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXResult;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * skeleton extension of XMLFilterImpl for now.  
 */
public class TrAXFilter extends XMLFilterImpl {
    private Templates              _templates;
    private TransformerHandlerImpl _transformer;

    public TrAXFilter(Templates templates)  throws 
	TransformerConfigurationException
    {
	_templates = templates;
        _transformer = new TransformerHandlerImpl( 
		(TransformerImpl) templates.newTransformer());
    }

    private void createParent() throws SAXException {
	XMLReader parent = null;
        try {
            SAXParserFactory pfactory = SAXParserFactory.newInstance();
            pfactory.setNamespaceAware(true);
            SAXParser saxparser = pfactory.newSAXParser();
            parent = saxparser.getXMLReader();
        }
        catch (ParserConfigurationException e) {
            throw new SAXException(e);
        }
        catch (FactoryConfigurationError e) {
            throw new SAXException(e.toString());
        }

        if (parent == null) {
            parent = XMLReaderFactory.createXMLReader();
        }

        setParent(parent);
    }

    public void parse (InputSource input) throws SAXException, IOException
    {
	if (getParent() == null) {
		try {
		    createParent();
		}
                catch (SAXException  e) {
                    throw new SAXException(e.toString());
                }
	}

	getParent().parse(input);
    }

    public void parse (String systemId) throws SAXException, IOException 
    {
        parse(new InputSource(systemId));
    }

    public void setContentHandler (ContentHandler handler) 
    {
	_transformer.setResult(new SAXResult(handler));
	if (getParent() == null) {
                try {
                    createParent();
                }
                catch (SAXException  e) {
                   return; 
                }
	}
	getParent().setContentHandler(_transformer);
    }

    public void setErrorListener (ErrorListener handler) { }
}
