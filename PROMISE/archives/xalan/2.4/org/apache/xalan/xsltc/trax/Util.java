package org.apache.xalan.xsltc.trax;

import java.io.Reader;
import java.io.InputStream;

import javax.xml.transform.*;
import javax.xml.transform.sax.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.SourceLoader;

public final class Util {

    public static String baseName(String name) {
	return org.apache.xalan.xsltc.compiler.util.Util.baseName(name);
    }

    /**
     * Creates a SAX2 InputSource object from a TrAX Source object
     */
    public static InputSource getInputSource(XSLTC xsltc, Source source)
	throws TransformerConfigurationException 
    {
	InputSource input = null;

	String systemId = source.getSystemId();
	if (systemId == null) {
	    systemId = "";
	}

	try {
	    if (source instanceof SAXSource) {
		final SAXSource sax = (SAXSource)source;
		input = sax.getInputSource();
		xsltc.setXMLReader(sax.getXMLReader());
	    }
	    else if (source instanceof DOMSource) {
		final DOMSource domsrc = (DOMSource)source;
		final Document dom = (Document)domsrc.getNode();
		final DOM2SAX dom2sax = new DOM2SAX(dom);
		xsltc.setXMLReader(dom2sax);  

		input = SAXSource.sourceToInputSource(source);
		if (input == null){
		    input = new InputSource(domsrc.getSystemId());
		}
	    }
	    else if (source instanceof StreamSource) {
		final StreamSource stream = (StreamSource)source;
		final InputStream istream = stream.getInputStream();
		final Reader reader = stream.getReader();

		if (istream != null) {
		    input = new InputSource(istream);
		}
		else if (reader != null) {
		    input = new InputSource(reader);
		}
		else {
		    input = new InputSource(systemId);
		}
	    }
	    else {
		ErrorMsg err = new ErrorMsg(ErrorMsg.JAXP_UNKNOWN_SOURCE_ERR);
		throw new TransformerConfigurationException(err.toString());
	    }
	    input.setSystemId(systemId);
	}
	catch (NullPointerException e) {
 	    ErrorMsg err = new ErrorMsg(ErrorMsg.JAXP_NO_SOURCE_ERR,
					"TransformerFactory.newTemplates()");
	    throw new TransformerConfigurationException(err.toString());
	}
	catch (SecurityException e) {
 	    ErrorMsg err = new ErrorMsg(ErrorMsg.FILE_ACCESS_ERR, systemId);
	    throw new TransformerConfigurationException(err.toString());
	}
	finally {
	    return input;
	}
    }

}

