package org.apache.xalan.xsltc.dom;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.DOMCache;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.dtm.ref.DTMDefaultBase;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public final class LoadDocument {

    private static final String NAMESPACE_FEATURE =

    /**
     * Returns an iterator containing a set of nodes from an XML document
     * loaded by the document() function.
     */
    public static DTMAxisIterator document(String uri, String base,
					AbstractTranslet translet, DOM dom)
	throws Exception 
    {
        final String originalUri = uri;
        MultiDOM multiplexer = (MultiDOM)dom;

        if (uri == null || uri.equals("")) {
            return(new SingletonIterator(DTM.NULL,true));
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
            DOM newDom = ((DOMAdapter)multiplexer.getDOMAdapter(uri))
                                       .getDOMImpl();
            if (newDom instanceof SAXImpl) {
                return new SingletonIterator(((SAXImpl)newDom).getDocument(),
                                             true);
            } 
        }

        DOMCache cache = translet.getDOMCache();
        DOM newdom;


        if (cache != null) {
            newdom = cache.retrieveDocument(uri, mask, translet);
            if (newdom == null) {
                final Exception e = new FileNotFoundException(originalUri);
                throw new TransletException(e);
            }
        } else {
            final SAXParserFactory factory = SAXParserFactory.newInstance();
            final SAXParser parser = factory.newSAXParser();
            final XMLReader reader = parser.getXMLReader();
            try {
                reader.setFeature(NAMESPACE_FEATURE,true);
            }
            catch (Exception e) {
                throw new TransletException(e);
            }

            XSLTCDTMManager dtmManager = (XSLTCDTMManager)
                        ((DTMDefaultBase)((DOMAdapter)multiplexer.getMain())
                                               .getDOMImpl()).m_mgr;
            newdom = (SAXImpl)dtmManager.getDTM(
                                 new SAXSource(reader, new InputSource(uri)),
                                 false, null, true, false, translet.hasIdCall());

            translet.prepassDocument(newdom);

            ((SAXImpl)newdom).setDocumentURI(uri);
        }

        final DOMAdapter domAdapter = translet.makeDOMAdapter(newdom);
        multiplexer.addDOMAdapter(domAdapter);

        translet.buildKeys(domAdapter, null, null, ((SAXImpl)newdom).getDocument());

        return new SingletonIterator(((SAXImpl)newdom).getDocument(), true);
    }

    /**
     * Interprets the arguments passed from the document() function (see
     * org/apache/xalan/xsltc/compiler/DocumentCall.java) and returns an
     * iterator containing the requested nodes. Builds a union-iterator if
     * several documents are requested.
     */
    public static DTMAxisIterator document(Object arg,String xmlURI,String xslURI,
					AbstractTranslet translet, DOM dom)
	throws TransletException {
	try {

	    if (xmlURI != null) {
		int sep = xmlURI.lastIndexOf('\\') + 1;
		if (sep <= 0) {
		    sep = xmlURI.lastIndexOf('/') + 1;
	        }
	    }
	    else {
		xmlURI = "";
	    }

	    if (xslURI != null) {
		int sep = xslURI.lastIndexOf('\\') + 1;
		if (sep <= 0) {
		    sep = xslURI.lastIndexOf('/') + 1;
	        }
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
	    else if (arg instanceof DTMAxisIterator) {
		UnionIterator union = new UnionIterator(dom);
		DTMAxisIterator iterator = (DTMAxisIterator)arg;
		int node;

		while ((node = iterator.next()) != DTM.NULL) {
		    String uri = dom.getStringValueX(node);
		    if ((xmlURI == null) || xmlURI.equals("")) {
			xmlURI = dom.getDocumentURI(node);
			int sep = xmlURI.lastIndexOf('\\') + 1;
			if (sep <= 0) {
			    sep = xmlURI.lastIndexOf('/') + 1;
		        }
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
