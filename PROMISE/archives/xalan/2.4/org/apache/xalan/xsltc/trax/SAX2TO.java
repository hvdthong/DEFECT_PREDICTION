package org.apache.xalan.xsltc.trax;

import java.util.ArrayList;

import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.Attributes;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.SAXException;

import org.apache.xalan.xsltc.runtime.Constants;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.TransletOutputHandler;

public class SAX2TO implements ContentHandler, LexicalHandler, Constants {

    static private class Pair {
	String left;
	String right;

	public Pair(String ll, String rr) {
	    left = ll; right = rr;
	}
    }

    TransletOutputHandler _handler;
    ArrayList _nsDeclarations = new ArrayList();

    public SAX2TO(TransletOutputHandler handler) {
	_handler = handler;
    }

    public void startDocument() throws TransletException {
	_handler.startDocument();
    }

    public void endDocument() throws TransletException {
	_handler.endDocument();
	_handler.close();
    }

    public void startElement(String namespace, String localName, String qName,
	Attributes attrs) throws TransletException
    {
	_handler.startElement(qName);

	int n = _nsDeclarations.size();
	for (int i = 0; i < n; i++) {
	    final Pair pair = (Pair) _nsDeclarations.get(i);
	    _handler.namespace(pair.left, pair.right);
	}
	_nsDeclarations.clear();

	n = attrs.getLength();
	for (int i = 0; i < n; i++) {
	    _handler.attribute(attrs.getQName(i), attrs.getValue(i));
	}
    }

    public void endElement(String namespace, String localName, String qName) 
	throws TransletException
    {
	_handler.endElement(qName);
    }

    public void startPrefixMapping(String prefix, String uri)
	throws TransletException
    {
	_nsDeclarations.add(new Pair(prefix, uri));
    }

    public void endPrefixMapping(String prefix) {
    }

    public void characters(char[] ch, int start, int length)
	throws TransletException
    {
	_handler.characters(ch, start, length);
    }

    public void processingInstruction(String target, String data)
	throws TransletException
    {
	_handler.processingInstruction(target, data);
    }

    public void comment(char[] ch, int start, int length) 
	throws TransletException
    {
	_handler.comment(new String(ch, start, length));
    }

    public void ignorableWhitespace(char[] ch, int start, int length)
	throws TransletException
    {
	_handler.characters(ch, start, length);
    }

    public void startCDATA() throws TransletException { 
	_handler.startCDATA();
    }

    public void endCDATA() throws TransletException { 
	_handler.endCDATA();
    }

    public void setDocumentLocator(Locator locator) {
    }

    public void skippedEntity(String name) {
    }

    public void startEntity(java.lang.String name) { 
    }

    public void endDTD() { 
    }

    public void endEntity(String name) { 
    }

    public void startDTD(String name, String publicId, String systemId)
        throws SAXException 
    { 
    }
}
