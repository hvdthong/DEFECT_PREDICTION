package org.apache.xalan.xsltc.trax;

import java.util.Stack;
import java.util.Vector;

import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.Attributes;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Attr;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.SAXException;

import org.apache.xalan.xsltc.runtime.Constants;

public class SAX2DOM implements ContentHandler, LexicalHandler, Constants {

    private Document _root = null;
    private Stack _nodeStk = new Stack();
    private Vector _namespaceDecls = null;

    public SAX2DOM() throws ParserConfigurationException {
	final DocumentBuilderFactory factory = 
		DocumentBuilderFactory.newInstance();
	_root = factory.newDocumentBuilder().newDocument();
    }

    public SAX2DOM(Node root) throws ParserConfigurationException {
	if (root != null) {
	}
	else {
	    final DocumentBuilderFactory factory = 
		DocumentBuilderFactory.newInstance();
	    _root = factory.newDocumentBuilder().newDocument();
	}
    }

    public Node getDOM() {
	return _root;
    }

    public void characters(char[] ch, int start, int length) {
	final Node last = (Node)_nodeStk.peek();

	if (last != _root) {
	    final String text = new String(ch, start, length);
	    last.appendChild(_root.createTextNode(text));
	}
    }

    public void startDocument() {
	_nodeStk.push(_root);
    }

    public void endDocument() {
    }

    public void startElement(String namespace, String localName, String qName,
	Attributes attrs) 
    {
	final Element tmp = (Element)_root.createElementNS(namespace, qName);

	if (_namespaceDecls != null) {
	    final int nDecls = _namespaceDecls.size();
	    for (int i = 0; i < nDecls; i++) {
		final String prefix = (String) _namespaceDecls.elementAt(i++);

		if (prefix == null || prefix.equals(EMPTYSTRING)) {
		    tmp.setAttributeNS(XMLNS_URI, XMLNS_PREFIX,
			(String) _namespaceDecls.elementAt(i));
		}
		else {
		    tmp.setAttributeNS(XMLNS_URI, XMLNS_STRING + prefix, 
			(String) _namespaceDecls.elementAt(i));
		}
	    }
	    _namespaceDecls.clear();
	}

	final int nattrs = attrs.getLength();
	for (int i = 0; i < nattrs; i++) {
	    if (attrs.getLocalName(i) == null) {
		tmp.setAttribute(attrs.getQName(i), attrs.getValue(i));
	    }
	    else {
		tmp.setAttributeNS(attrs.getURI(i), attrs.getQName(i), 
		    attrs.getValue(i));
	    }
	}

	Node last = (Node)_nodeStk.peek();
	last.appendChild(tmp);

	_nodeStk.push(tmp);
    }

    public void endElement(String namespace, String localName, String qName) {
	_nodeStk.pop();  
    }

    public void startPrefixMapping(String prefix, String uri) {
	if (_namespaceDecls == null) {
	    _namespaceDecls = new Vector(2);
	}
	_namespaceDecls.addElement(prefix);
	_namespaceDecls.addElement(uri);
    }

    public void endPrefixMapping(String prefix) {
    }

    /**
     * This class is only used internally so this method should never 
     * be called.
     */
    public void ignorableWhitespace(char[] ch, int start, int length) {
    }

    /**
     * adds processing instruction node to DOM.
     */
    public void processingInstruction(String target, String data) {
	final Node last = (Node)_nodeStk.peek();
	ProcessingInstruction pi = _root.createProcessingInstruction(
		target, data);
	if (pi != null)  last.appendChild(pi);
    }

    /**
     * This class is only used internally so this method should never 
     * be called.
     */
    public void setDocumentLocator(Locator locator) {
    }

    /**
     * This class is only used internally so this method should never 
     * be called.
     */
    public void skippedEntity(String name) {
    }


    /**
     * Lexical Handler method to create comment node in DOM tree.
     */
    public void comment(char[] ch, int start, int length) {
	final Node last = (Node)_nodeStk.peek();
	Comment comment = _root.createComment(new String(ch,start,length));
	if (comment != null) last.appendChild(comment);
    }

    public void startCDATA() { }
    public void endCDATA() { }
    public void startEntity(java.lang.String name) { }
    public void endDTD() { }
    public void endEntity(String name) { }
    public void startDTD(String name, String publicId, String systemId)
        throws SAXException { }

}
