package org.apache.xalan.xsltc.trax;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.TransformerHandler;

import org.apache.xalan.xsltc.StripFilter;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.dom.DOMWSFilter;
import org.apache.xalan.xsltc.dom.SAXImpl;
import org.apache.xalan.xsltc.dom.XSLTCDTMManager;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xml.dtm.DTMWSFilter;
import org.apache.xml.serializer.SerializationHandler;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Implementation of a JAXP1.1 TransformerHandler
 */
public class TransformerHandlerImpl implements TransformerHandler, DeclHandler {

    private TransformerImpl  _transformer;
    private AbstractTranslet _translet = null;
    private String           _systemId;
    private SAXImpl          _dom = null;
    private ContentHandler   _handler = null;
    private LexicalHandler   _lexHandler = null;
    private DTDHandler       _dtdHandler = null;
    private DeclHandler      _declHandler = null;
    private Result           _result = null;
    private Locator          _locator = null;


    /**
     * A flag indicating whether this transformer handler implements the 
     * identity transform.
     */
    private boolean _isIdentity = false;

    /**
     * Cosntructor - pass in reference to a TransformerImpl object
     */
    public TransformerHandlerImpl(TransformerImpl transformer) {
	_transformer = transformer;

	if (transformer.isIdentity()) {
	    _handler = new DefaultHandler();
	    _isIdentity = true;
	}
	else {
	    _translet = _transformer.getTranslet();
	}
    }

    /**
     * Implements javax.xml.transform.sax.TransformerHandler.getSystemId()
     * Get the base ID (URI or system ID) from where relative URLs will be
     * resolved.
     * @return The systemID that was set with setSystemId(String id)
     */
    public String getSystemId() {
	return _systemId;
    }

    /**
     * Implements javax.xml.transform.sax.TransformerHandler.setSystemId()
     * Get the base ID (URI or system ID) from where relative URLs will be
     * resolved.
     * @param id Base URI for this stylesheet
     */
    public void setSystemId(String id) {
	_systemId = id;
    }

    /**
     * Implements javax.xml.transform.sax.TransformerHandler.getTransformer()
     * Get the Transformer associated with this handler, which is needed in
     * order to set parameters and output properties.
     * @return The Transformer object
     */
    public Transformer getTransformer() {
	return _transformer;
    }

    /**
     * Implements javax.xml.transform.sax.TransformerHandler.setResult()
     * Enables the user of the TransformerHandler to set the to set the Result
     * for the transformation.
     * @param result A Result instance, should not be null
     * @throws IllegalArgumentException if result is invalid for some reason
     */
    public void setResult(Result result) throws IllegalArgumentException {
	_result = result;

	if (_isIdentity) {
	    try {
		SerializationHandler outputHandler =
		    _transformer.getOutputHandler(result);
		_transformer.transferOutputProperties(outputHandler);

		_handler = outputHandler;
		_lexHandler = outputHandler;
	    }
	    catch (TransformerException e) {
		_result = null;
	    }
	}
	else if (_done) {
	    try {
		_transformer.setDOM(_dom);
		_transformer.transform(null, _result);
	    }
	    catch (TransformerException e) {
		throw new IllegalArgumentException(e.getMessage());
	    }
	}
    }

    /**
     * Implements org.xml.sax.ContentHandler.characters()
     * Receive notification of character data.
     */
    public void characters(char[] ch, int start, int length) 
	throws SAXException 
    {
	_handler.characters(ch, start, length);
    }

    /**
     * Implements org.xml.sax.ContentHandler.startDocument()
     * Receive notification of the beginning of a document.
     */
    public void startDocument() throws SAXException {
	if (_result == null) {
	    ErrorMsg err = new ErrorMsg(ErrorMsg.JAXP_SET_RESULT_ERR);
	    throw new SAXException(err.toString());
	}

        if (!_isIdentity) {
            boolean hasIdCall = (_translet != null) ? _translet.hasIdCall() : false;
            
            XSLTCDTMManager dtmManager = XSLTCDTMManager.newInstance();

            DTMWSFilter wsFilter;
            if (_translet != null && _translet instanceof StripFilter) {
                wsFilter = new DOMWSFilter(_translet);
            } else {
                wsFilter = null;
            }            
          
            _dom = (SAXImpl)dtmManager.getDTM(null, false, wsFilter, true, false, hasIdCall);         
            
            _handler = _dom.getBuilder();
            _lexHandler = (LexicalHandler) _handler;
            _dtdHandler = (DTDHandler) _handler;
            _declHandler = (DeclHandler) _handler;  
            
            
	        _dom.setDocumentURI(_systemId);
            
             if (_locator != null) {
                _handler.setDocumentLocator(_locator);
             }            
        }

	_handler.startDocument();
    }

    /**
     * Implements org.xml.sax.ContentHandler.endDocument()
     * Receive notification of the end of a document.
     */
    public void endDocument() throws SAXException {
	_handler.endDocument();

	if (!_isIdentity) {
	    if (_result != null) {
		try {
		    _transformer.setDOM(_dom);
		    _transformer.transform(null, _result);
		}
		catch (TransformerException e) {
		    throw new SAXException(e);
		}
	    }
	    _done = true;

	    _transformer.setDOM(_dom);
	}
    }
	
    /**
     * Implements org.xml.sax.ContentHandler.startElement()
     * Receive notification of the beginning of an element.
     */
    public void startElement(String uri, String localName,
			     String qname, Attributes attributes)
	throws SAXException 
    {
	_handler.startElement(uri, localName, qname, attributes);
    }
	
    /**
     * Implements org.xml.sax.ContentHandler.endElement()
     * Receive notification of the end of an element.
     */
    public void endElement(String namespaceURI, String localName, String qname)
	throws SAXException 
    {
	_handler.endElement(namespaceURI, localName, qname);
    }

    /**
     * Implements org.xml.sax.ContentHandler.processingInstruction()
     * Receive notification of a processing instruction.
     */
    public void processingInstruction(String target, String data)
	throws SAXException 
    {
	_handler.processingInstruction(target, data);
    }

    /**
     * Implements org.xml.sax.ext.LexicalHandler.startCDATA()
     */
    public void startCDATA() throws SAXException { 
	if (_lexHandler != null) {
	    _lexHandler.startCDATA();
	}
    }

    /**
     * Implements org.xml.sax.ext.LexicalHandler.endCDATA()
     */
    public void endCDATA() throws SAXException { 
	if (_lexHandler != null) {
	    _lexHandler.endCDATA();
	}
    }

    /**
     * Implements org.xml.sax.ext.LexicalHandler.comment()
     * Receieve notification of a comment
     */
    public void comment(char[] ch, int start, int length) 
	throws SAXException 
    { 
	if (_lexHandler != null) {
	    _lexHandler.comment(ch, start, length);
	}
    }

    /**
     * Implements org.xml.sax.ContentHandler.ignorableWhitespace()
     * Receive notification of ignorable whitespace in element
     * content. Similar to characters(char[], int, int).
     */
    public void ignorableWhitespace(char[] ch, int start, int length)
	throws SAXException 
    {
	_handler.ignorableWhitespace(ch, start, length);
    }

    /**
     * Implements org.xml.sax.ContentHandler.setDocumentLocator()
     * Receive an object for locating the origin of SAX document events. 
     */
    public void setDocumentLocator(Locator locator) {
        _locator = locator;

        if (_handler != null) {
            _handler.setDocumentLocator(locator);
        }
    }

    /**
     * Implements org.xml.sax.ContentHandler.skippedEntity()
     * Receive notification of a skipped entity.
     */
    public void skippedEntity(String name) throws SAXException {
	_handler.skippedEntity(name);
    }

    /**
     * Implements org.xml.sax.ContentHandler.startPrefixMapping()
     * Begin the scope of a prefix-URI Namespace mapping.
     */
    public void startPrefixMapping(String prefix, String uri) 
	throws SAXException {
	_handler.startPrefixMapping(prefix, uri);
    }

    /**
     * Implements org.xml.sax.ContentHandler.endPrefixMapping()
     * End the scope of a prefix-URI Namespace mapping.
     */
    public void endPrefixMapping(String prefix) throws SAXException {
	_handler.endPrefixMapping(prefix);
    }

    /**
     * Implements org.xml.sax.ext.LexicalHandler.startDTD()
     */
    public void startDTD(String name, String publicId, String systemId) 
	throws SAXException
    { 
	if (_lexHandler != null) {
	    _lexHandler.startDTD(name, publicId, systemId);
	}
    }

    /**
     * Implements org.xml.sax.ext.LexicalHandler.endDTD()
     */
    public void endDTD() throws SAXException {
	if (_lexHandler != null) {
	    _lexHandler.endDTD();
	}
    }

    /**
     * Implements org.xml.sax.ext.LexicalHandler.startEntity()
     */
    public void startEntity(String name) throws SAXException { 
	if (_lexHandler != null) {
	    _lexHandler.startEntity(name);
	}
    }

    /**
     * Implements org.xml.sax.ext.LexicalHandler.endEntity()
     */
    public void endEntity(String name) throws SAXException { 
	if (_lexHandler != null) {
	    _lexHandler.endEntity(name);
	}
    }

    /**
     * Implements org.xml.sax.DTDHandler.unparsedEntityDecl()
     */
    public void unparsedEntityDecl(String name, String publicId, 
	String systemId, String notationName) throws SAXException 
    {
        if (_dtdHandler != null) {
	    _dtdHandler.unparsedEntityDecl(name, publicId, systemId,
                                           notationName);
        }
    }

    /**
     * Implements org.xml.sax.DTDHandler.notationDecl()
     */
    public void notationDecl(String name, String publicId, String systemId) 
	throws SAXException
    {
        if (_dtdHandler != null) {
	    _dtdHandler.notationDecl(name, publicId, systemId);
        }
    }

    /**
     * Implements org.xml.sax.ext.DeclHandler.attributeDecl()
     */
    public void attributeDecl(String eName, String aName, String type, 
	String valueDefault, String value) throws SAXException 
    {
        if (_declHandler != null) {
	    _declHandler.attributeDecl(eName, aName, type, valueDefault, value);
        }
    }

    /**
     * Implements org.xml.sax.ext.DeclHandler.elementDecl()
     */
    public void elementDecl(String name, String model) 
	throws SAXException
    {
        if (_declHandler != null) {
	    _declHandler.elementDecl(name, model);
        }
    }

    /**
     * Implements org.xml.sax.ext.DeclHandler.externalEntityDecl()
     */
    public void externalEntityDecl(String name, String publicId, String systemId) 
	throws SAXException
    {
        if (_declHandler != null) {
	    _declHandler.externalEntityDecl(name, publicId, systemId);
        }
    }

    /**
     * Implements org.xml.sax.ext.DeclHandler.externalEntityDecl()
     */
    public void internalEntityDecl(String name, String value) 
	throws SAXException
    {
        if (_declHandler != null) {
	    _declHandler.internalEntityDecl(name, value);
        }
    }
}
