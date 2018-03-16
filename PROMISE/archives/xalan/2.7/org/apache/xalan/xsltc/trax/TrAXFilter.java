package org.apache.xalan.xsltc.trax;

import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXResult;

import org.apache.xml.utils.XMLReaderManager;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * skeleton extension of XMLFilterImpl for now.  
 * @author Santiago Pericas-Geertsen
 * @author G. Todd Miller 
 */
public class TrAXFilter extends XMLFilterImpl {
    private Templates              _templates;
    private TransformerImpl	   _transformer;
    private TransformerHandlerImpl _transformerHandler;

    public TrAXFilter(Templates templates)  throws 
	TransformerConfigurationException
    {
	_templates = templates;
	_transformer = (TransformerImpl) templates.newTransformer();
        _transformerHandler = new TransformerHandlerImpl(_transformer);
    }
    
    public Transformer getTransformer() {
        return _transformer;
    }

    private void createParent() throws SAXException {
	XMLReader parent = null;
        try {
            SAXParserFactory pfactory = SAXParserFactory.newInstance();
            pfactory.setNamespaceAware(true);
            
            if (_transformer.isSecureProcessing()) {
                try {
                    pfactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                }
                catch (SAXException e) {}
            }
            
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
        XMLReader managedReader = null;

        try {
            if (getParent() == null) {
                try {
                    managedReader = XMLReaderManager.getInstance()
                                                    .getXMLReader();
                    setParent(managedReader);
                } catch (SAXException  e) {
                    throw new SAXException(e.toString());
                }
            }

            getParent().parse(input);
        } finally {
            if (managedReader != null) {
                XMLReaderManager.getInstance().releaseXMLReader(managedReader);
            }
        }
    }

    public void parse (String systemId) throws SAXException, IOException 
    {
        parse(new InputSource(systemId));
    }

    public void setContentHandler (ContentHandler handler) 
    {
	_transformerHandler.setResult(new SAXResult(handler));
	if (getParent() == null) {
                try {
                    createParent();
                }
                catch (SAXException  e) {
                   return; 
                }
	}
	getParent().setContentHandler(_transformerHandler);
    }

    public void setErrorListener (ErrorListener handler) { }
}
