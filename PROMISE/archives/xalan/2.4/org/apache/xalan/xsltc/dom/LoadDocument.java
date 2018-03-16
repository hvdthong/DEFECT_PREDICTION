package org.apache.xalan.xsltc.dom;

import java.net.URL;
import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.DOMCache;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.NodeIterator;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;

public final class LoadDocument {

    private static final String NAMESPACE_FEATURE =

    /**
     * Returns an iterator containing a set of nodes from an XML document
     * loaded by the document() function.
     */
    public static NodeIterator document(String uri, String base,
					AbstractTranslet translet, DOM dom)
	throws Exception 
    {
	final String originalUri = uri;
	MultiDOM multiplexer = (MultiDOM)dom;

	if (uri == null || uri.equals("")) {
	    return new SingletonIterator(DOM.NULL,true);
	}

	if (base != null && !base.equals("")) {
		uri = base + uri;
	    }
	}

	final File file = new File(uri);
	if (file.exists()) {
	    uri = file.toURL().toExternalForm();
	}
	
	int mask = multiplexer.getDocumentMask(uri);
	if (mask != -1) {
	    return new SingletonIterator(DOM.ROOTNODE | mask, true);
	}

	DOMCache cache = translet.getDOMCache();
	DOMImpl newdom;


	if (cache != null) {
	    newdom = cache.retrieveDocument(originalUri, mask, translet);
	    if (newdom == null) {
		final Exception e = new FileNotFoundException(originalUri);
		throw new TransletException(e);
	    }
	}
	else {
	    final SAXParserFactory factory = SAXParserFactory.newInstance();
	    try {
		factory.setFeature(NAMESPACE_FEATURE,true);
	    }
	    catch (Exception e) {
		factory.setNamespaceAware(true);
	    }
	    final SAXParser parser = factory.newSAXParser();
	    final XMLReader reader = parser.getXMLReader();

	    newdom = new DOMImpl();
	    reader.setContentHandler(newdom.getBuilder());
	    DTDMonitor dtdMonitor = new DTDMonitor();
	    dtdMonitor.handleDTD(reader);

	    newdom.setDocumentURI(uri);
	    reader.parse(uri);

	    translet.setIndexSize(newdom.getSize());
	    dtdMonitor.buildIdIndex(newdom, mask, translet);
	    translet.setUnparsedEntityURIs(dtdMonitor.getUnparsedEntityURIs());
	}

	final DOMAdapter domAdapter = translet.makeDOMAdapter(newdom);
	mask = multiplexer.addDOMAdapter(domAdapter);

	translet.buildKeys((DOM)newdom, null, null, DOM.ROOTNODE | mask);

	return new SingletonIterator(DOM.ROOTNODE | mask, true);
    }

    /**
     * Interprets the arguments passed from the document() function (see
     * org/apache/xalan/xsltc/compiler/DocumentCall.java) and returns an
     * iterator containing the requested nodes. Builds a union-iterator if
     * several documents are requested.
     */
    public static NodeIterator document(Object arg,String xmlURI,String xslURI,
					AbstractTranslet translet, DOM dom)
	throws TransletException {
	try {

	    if (xmlURI != null) {
		final int sep = xmlURI.lastIndexOf('/') + 1;
	    }
	    else {
		xmlURI = "";
	    }

	    if (xslURI != null) {
		final int sep = xslURI.lastIndexOf('/') + 1;
	    }
	    else {
		xslURI = "";
	    }

	    if (arg instanceof String) {
		try {
		    return document((String)arg, xmlURI, translet, dom);
		}
		catch (java.io.FileNotFoundException e) {
		    return document((String)arg, xslURI, translet, dom);
		}
		catch (org.xml.sax.SAXParseException e) {
		    return document((String)arg, xslURI, translet, dom);
		}
	    }
	    else if (arg instanceof NodeIterator) {
		UnionIterator union = new UnionIterator(dom);
		NodeIterator iterator = (NodeIterator)arg;
		int node;

		while ((node = iterator.next()) != DOM.NULL) {
		    String uri = dom.getNodeValue(node);
		    if ((xmlURI == null) || xmlURI.equals("")) {
			xmlURI = dom.getDocumentURI(node);
			final int sep = xmlURI.lastIndexOf('/') + 1;
			xmlURI = xmlURI.substring(0, sep);
		    }
		    try {
			union.addIterator(document(uri, xmlURI, translet, dom));
		    }
		    catch (java.io.FileNotFoundException e) {
			union.addIterator(document(uri, xslURI, translet, dom));
		    }
		}
		return(union);
	    }
	    else {
		final String err = "document("+arg.toString()+")";
		throw new IllegalArgumentException(err);
	    }
	}
	catch (TransletException e) {
	    throw e;
	}
	catch (Exception e) {
	    throw new TransletException(e);
	}
    }

}
