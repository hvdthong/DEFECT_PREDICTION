package org.apache.xalan.xsltc.runtime;

import org.xml.sax.*;
import org.xml.sax.ext.*;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.TransletOutputHandler;
import org.apache.xalan.xsltc.dom.DOMBuilder;

public final class SAXAdapter implements TransletOutputHandler {

    private final DOMBuilder _domBuilder;
    private final AttributeList  _attributes = new AttributeList();

    private String _openElementName;
    
    public SAXAdapter(DOMBuilder domBuilder) {
	_domBuilder = domBuilder;
    }

    private void maybeEmitStartElement() throws SAXException {
	if (_openElementName != null) {
	    _domBuilder.startElement(null, null, _openElementName, _attributes);
	    _openElementName = null;
	}
    }

    public void startDocument() throws TransletException {
	try {
	    _domBuilder.startDocument();
	}
	catch (SAXException e) {
	    throw new TransletException(e);
	}
    }
    
    public void endDocument() throws TransletException {
	try {
	    _domBuilder.endDocument();
	}
	catch (SAXException e) {
	    throw new TransletException(e);
	}
    }
    
    public void characters(String characters) throws TransletException {
	characters(characters.toCharArray(), 0, characters.length());
    }

    public void characters(char[] characters, int offset, int length)
	throws TransletException {
	try {
	    maybeEmitStartElement();
	    _domBuilder.characters(characters, offset, length);
	}
	catch (SAXException e) {
	    throw new TransletException(e);
	}
    }
    
    public void startElement(String elementName) throws TransletException {
	try {
	    maybeEmitStartElement();
	    _openElementName = elementName;
	    _attributes.clear();
	}
	catch (SAXException e) {
	    throw new TransletException(e);
	}
    }
    
    public void endElement(String elementName) throws TransletException {
	try {
	    maybeEmitStartElement();
	    _domBuilder.endElement(null, null, elementName);
	}
	catch (SAXException e) {
	    throw new TransletException(e);
	}
    }
    
    public void attribute(String name, String value)
	throws TransletException {
	if (_openElementName != null) {
	    _attributes.add(name, value);
	}
	else {
	    BasisLibrary.runTimeError(BasisLibrary.STRAY_ATTRIBUTE_ERR, name);
	}
    }
    
    public void namespace(String prefix, String uri)
	throws TransletException {
    }

    public void comment(String comment) throws TransletException {
	try {
	    maybeEmitStartElement();
	    if (_domBuilder != null) {
		char[] chars = comment.toCharArray();
		_domBuilder.comment(chars, 0, chars.length);
	    }
	}
	catch (SAXException e) {
	    throw new TransletException(e);
	}
    }
    
    public void processingInstruction(String target, String data)
	throws TransletException {
	try {
	    maybeEmitStartElement();
	    _domBuilder.processingInstruction(target, data);
	}
	catch (SAXException e) {
	    throw new TransletException(e);
	}
    }

    public boolean setEscaping(boolean escape) throws TransletException {
	return _domBuilder.setEscaping(escape);
    }

    public void startCDATA() throws TransletException {}
    public void endCDATA() throws TransletException {}
    public void setType(int type) {}
    public void setHeader(String header) {}
    public void setIndent(boolean indent) {}
    public void omitHeader(boolean value) {}
    public void setCdataElements(Hashtable elements) {}
    public void setDoctype(String system, String pub) {}
    public void setMediaType(String mediaType) {}
    public void setStandalone(String standalone) {}
    public void setVersion(String version) {}
    public void close() {}
    public String getPrefix(String uri) { return(""); }
}
