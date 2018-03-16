package org.apache.xalan.xsltc.runtime.output;

import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.SAXException;

import org.apache.xalan.xsltc.TransletException;

public class SAXTextOutput extends SAXOutput {

    public SAXTextOutput(ContentHandler handler, String encoding) 
    {
    	super(handler, encoding);
    }

    public SAXTextOutput(ContentHandler handler, LexicalHandler lex, 
        String encoding)
    {
        super(handler, lex, encoding);
    }

    public void startDocument() throws TransletException { 
	try {
	    _saxHandler.startDocument();
	}
	catch (SAXException e) {
	    throw new TransletException(e);
	}
    }

    public void endDocument() throws TransletException { 
	try {
	    _saxHandler.endDocument();
	}
	catch (SAXException e) {
	    throw new TransletException(e);
	}
    }

    public void startElement(String elementName) 
	throws TransletException 
    {
    }

    public void endElement(String elementName) 
	throws TransletException 
    {
    }

    public void characters(String characters) 
	throws TransletException 
    { 
	try {
	    _saxHandler.characters(characters.toCharArray(), 0, 
		characters.length());
	}
	catch (SAXException e) {
	    throw new TransletException(e);
	}
    }

    public void characters(char[] characters, int offset, int length)
	throws TransletException 
    { 
	try {
	    _saxHandler.characters(characters, offset, length);
	}
	catch (SAXException e) {
	    throw new TransletException(e);
	}
    }

    public void comment(String comment) throws TransletException {
    }

    public void attribute(String name, String value) 
	throws TransletException 
    {
    }

    public void processingInstruction(String target, String data) 
	throws TransletException
    {
    }
}

