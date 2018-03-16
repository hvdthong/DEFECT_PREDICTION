package org.apache.xalan.xsltc.runtime.output;

import java.util.Stack;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.runtime.Constants;

abstract class SAXOutput extends OutputBase implements Constants { 

    protected ContentHandler _saxHandler;
    protected LexicalHandler _lexHandler = null;
    protected AttributesImpl _attributes = new AttributesImpl();
    protected String	     _elementName = null;
    protected String         _encoding = null; 

    public SAXOutput(ContentHandler handler, String encoding) {
	_saxHandler = handler;
	_encoding = encoding;	
    } 

    public SAXOutput(ContentHandler hdler, LexicalHandler lex, String encoding) {
	_saxHandler = hdler;
	_lexHandler = lex;
	_encoding = encoding;
    }

    public void startDocument() throws TransletException {
	try {
	    _saxHandler.startDocument();
	} 
	catch (SAXException e) {
	    throw new TransletException(e);
	}
    }

    public void characters(String  characters)
       throws TransletException
    {
	characters(characters.toCharArray(), 0, characters.length());	
    }

    public void comment(String comment) throws TransletException {
	try {
            if (_startTagOpen) {
		closeStartTag();
	    }
	    else if (_cdataTagOpen) {
		closeCDATA();
	    }

	    if (_lexHandler != null) {
		_lexHandler.comment(comment.toCharArray(), 0, comment.length());
	    }
	}
	catch (SAXException e) {
	    throw new TransletException(e);
	}
    }

    public void processingInstruction(String target, String data) 
	throws TransletException 
    {
    }

    protected void closeStartTag() throws TransletException {
    }

    protected void closeCDATA() throws SAXException {
    }
}
