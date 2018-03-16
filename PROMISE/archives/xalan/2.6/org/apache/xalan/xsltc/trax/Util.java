package org.apache.xalan.xsltc.trax;

import java.io.InputStream;
import java.io.Reader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;

import org.w3c.dom.Document;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * @author Santiago Pericas-Geertsen
 */
public final class Util {

    public static String baseName(String name) {
	return org.apache.xalan.xsltc.compiler.util.Util.baseName(name);
    }
    
    public static String noExtName(String name) {
	return org.apache.xalan.xsltc.compiler.util.Util.noExtName(name);
    }

    public static String toJavaName(String name) {
	return org.apache.xalan.xsltc.compiler.util.Util.toJavaName(name);
    }

     

    
    /**
     * Creates a SAX2 InputSource object from a TrAX Source object
     */
    public static InputSource getInputSource(XSLTC xsltc, Source source)
	throws TransformerConfigurationException 
    {
	InputSource input = null;

	String systemId = source.getSystemId();         

	try {
	    if (source instanceof SAXSource) {
		final SAXSource sax = (SAXSource)source;
		input = sax.getInputSource();
                try {
                    XMLReader reader = sax.getXMLReader();

                     /*
                      * Fix for bug 24695
                      * According to JAXP 1.2 specification if a SAXSource
                      * is created using a SAX InputSource the Transformer or
                      * TransformerFactory creates a reader via the
                      * XMLReaderFactory if setXMLReader is not used
                      */

                    if (reader == null) {
                       try {
                           reader= XMLReaderFactory.createXMLReader();
                       } catch (Exception e ) {
                           try {

                               SAXParserFactory parserFactory = 
                                      SAXParserFactory.newInstance();
                               parserFactory.setNamespaceAware(true);
                               reader = parserFactory.newSAXParser()
                                     .getXMLReader();

                               
                           } catch (ParserConfigurationException pce ) {
                               throw new TransformerConfigurationException
                                 ("ParserConfigurationException" ,pce);
                           }
                       }
                    }
                    reader.setFeature
                    reader.setFeature

                    xsltc.setXMLReader(reader);
                }catch (SAXNotRecognizedException snre ) {
                  throw new TransformerConfigurationException
                       ("SAXNotRecognizedException ",snre);
                }catch (SAXNotSupportedException snse ) {
                  throw new TransformerConfigurationException
                       ("SAXNotSupportedException ",snse);
                }catch (SAXException se ) {
                  throw new TransformerConfigurationException
                       ("SAXException ",se);
                }

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
	return input;
    }

}

