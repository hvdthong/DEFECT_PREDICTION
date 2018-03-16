package org.apache.xalan.xsltc.trax;

import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import java.io.IOException;
import org.w3c.dom.Entity;
import org.w3c.dom.Notation;

import org.xml.sax.*;

import org.apache.xalan.xsltc.TransletOutputHandler;

public class DOM2TO implements XMLReader, Locator {

    private final static String EMPTYSTRING = "";
    private static final String XMLNS_PREFIX = "xmlns";

    /**
     * A reference to the DOM to be traversed.
     */
    private Node _dom;

    /**
     * A reference to the output handler receiving the events.
     */
    private TransletOutputHandler _handler;

    public DOM2TO(Node root, TransletOutputHandler handler) {
	_dom = root;
	_handler = handler;
    }

    public ContentHandler getContentHandler() { 
	return null;
    }

    public void setContentHandler(ContentHandler handler) {
    }

    public void parse(InputSource unused) throws IOException, SAXException {
        parse(_dom);
    }

    public void parse() throws IOException, SAXException {
	if (_dom != null) {
	    boolean isIncomplete = 
		(_dom.getNodeType() != org.w3c.dom.Node.DOCUMENT_NODE);

	    if (isIncomplete) {
		_handler.startDocument();
		parse(_dom);
		_handler.endDocument();
	    }
	    else {
		parse(_dom);
	    }
	}
    }

    /**
     * Traverse the DOM and generate TO events for a handler. Notice that 
     * we need to handle implicit namespace declarations too.
     */
    private void parse(Node node) 
	throws IOException, SAXException 
    {
 	if (node == null) return;

        switch (node.getNodeType()) {
	case Node.DOCUMENT_FRAGMENT_NODE:
	case Node.DOCUMENT_TYPE_NODE :
	case Node.ENTITY_NODE :
	case Node.ENTITY_REFERENCE_NODE:
	case Node.NOTATION_NODE :
	    break;
	case Node.CDATA_SECTION_NODE:
	    _handler.startCDATA();
	    _handler.characters(node.getNodeValue());
	    _handler.endCDATA();
	    break;

	    _handler.comment(node.getNodeValue());
	    break;

	case Node.DOCUMENT_NODE:
	    _handler.startDocument();
	    Node next = node.getFirstChild();
	    while (next != null) {
		parse(next);
		next = next.getNextSibling();
	    }
	    _handler.endDocument();
	    break;

	case Node.ELEMENT_NODE:
	    final String qname = node.getNodeName();
	    _handler.startElement(qname);
	    String prefix;
	    final NamedNodeMap map = node.getAttributes();
	    final int length = map.getLength();

	    for (int i = 0; i < length; i++) {
		int colon;
		final Node attr = map.item(i);
		final String qnameAttr = attr.getNodeName();

		if (qnameAttr.startsWith(XMLNS_PREFIX)) {
		    final String uriAttr = attr.getNodeValue();
		    colon = qnameAttr.lastIndexOf(':');
		    prefix = (colon > 0) ? qnameAttr.substring(colon + 1) 
			: EMPTYSTRING;
		    _handler.namespace(prefix, uriAttr);
		}
		else {
		    final String uriAttr = attr.getNamespaceURI();
		    if (uriAttr != null && !uriAttr.equals(EMPTYSTRING) ) {	
			colon = qnameAttr.lastIndexOf(':');
			prefix = (colon > 0) ? qnameAttr.substring(0, colon) 
			    : EMPTYSTRING;
			_handler.namespace(prefix, uriAttr);
		    }
		    _handler.attribute(qnameAttr, attr.getNodeValue());
		}
	    }

	    final String uri = node.getNamespaceURI();

	    if (uri != null) {	
		final int colon = qname.lastIndexOf(':');
		prefix = (colon > 0) ? qname.substring(0, colon) : EMPTYSTRING;
		_handler.namespace(prefix, uri);
	    }

	    next = node.getFirstChild();
	    while (next != null) {
		parse(next);
		next = next.getNextSibling();
	    }

	    _handler.endElement(qname);
	    break;

	case Node.PROCESSING_INSTRUCTION_NODE:
	    _handler.processingInstruction(node.getNodeName(),
					   node.getNodeValue());
	    break;

	case Node.TEXT_NODE:
	    _handler.characters(node.getNodeValue());
	    break;
	}
    }

    /**
     * This class is only used internally so this method should never 
     * be called.
     */
    public DTDHandler getDTDHandler() { 
	return null;
    }

    /**
     * This class is only used internally so this method should never 
     * be called.
     */
    public ErrorHandler getErrorHandler() {
	return null;
    }

    /**
     * This class is only used internally so this method should never 
     * be called.
     */
    public boolean getFeature(String name) throws SAXNotRecognizedException,
	SAXNotSupportedException
    {
	return false;
    }

    /**
     * This class is only used internally so this method should never 
     * be called.
     */
    public void setFeature(String name, boolean value) throws 
	SAXNotRecognizedException, SAXNotSupportedException 
    {
    }

    /**
     * This class is only used internally so this method should never 
     * be called.
     */
    public void parse(String sysId) throws IOException, SAXException {
	throw new IOException("This method is not yet implemented.");
    }

    /**
     * This class is only used internally so this method should never 
     * be called.
     */
    public void setDTDHandler(DTDHandler handler) throws NullPointerException {
    }

    /**
     * This class is only used internally so this method should never 
     * be called.
     */
    public void setEntityResolver(EntityResolver resolver) throws 
	NullPointerException 
    {
    }

    /**
     * This class is only used internally so this method should never 
     * be called.
     */
    public EntityResolver getEntityResolver() {
	return null;
    }

    /**
     * This class is only used internally so this method should never 
     * be called.
     */
    public void setErrorHandler(ErrorHandler handler) throws 
	NullPointerException
    {
    }

    /**
     * This class is only used internally so this method should never 
     * be called.
     */
    public void setProperty(String name, Object value) throws
	SAXNotRecognizedException, SAXNotSupportedException {
    }

    /**
     * This class is only used internally so this method should never 
     * be called.
     */
    public Object getProperty(String name) throws SAXNotRecognizedException,
	SAXNotSupportedException
    {
	return null;
    }

    /**
     * This class is only used internally so this method should never 
     * be called.
     */
    public int getColumnNumber() { 
	return 0; 
    }
    
    /**
     * This class is only used internally so this method should never 
     * be called.
     */
    public int getLineNumber() { 
	return 0; 
    }

    /**
     * This class is only used internally so this method should never 
     * be called.
     */
    public String getPublicId() { 
	return null; 
    }

    /**
     * This class is only used internally so this method should never 
     * be called.
     */
    public String getSystemId() { 
	return null; 
    }

    private String getNodeTypeFromCode(short code) {
	String retval = null;
	switch (code) {
	case Node.ATTRIBUTE_NODE : 
	    retval = "ATTRIBUTE_NODE"; break; 
	case Node.CDATA_SECTION_NODE :
	    retval = "CDATA_SECTION_NODE"; break; 
	case Node.COMMENT_NODE :
	    retval = "COMMENT_NODE"; break; 
	case Node.DOCUMENT_FRAGMENT_NODE :
	    retval = "DOCUMENT_FRAGMENT_NODE"; break; 
	case Node.DOCUMENT_NODE :
	    retval = "DOCUMENT_NODE"; break; 
	case Node.DOCUMENT_TYPE_NODE :
	    retval = "DOCUMENT_TYPE_NODE"; break; 
	case Node.ELEMENT_NODE :
	    retval = "ELEMENT_NODE"; break; 
	case Node.ENTITY_NODE :
	    retval = "ENTITY_NODE"; break; 
	case Node.ENTITY_REFERENCE_NODE :
	    retval = "ENTITY_REFERENCE_NODE"; break; 
	case Node.NOTATION_NODE :
	    retval = "NOTATION_NODE"; break; 
	case Node.PROCESSING_INSTRUCTION_NODE :
	    retval = "PROCESSING_INSTRUCTION_NODE"; break; 
	case Node.TEXT_NODE:
	    retval = "TEXT_NODE"; break; 
        }
	return retval;
    }
}
