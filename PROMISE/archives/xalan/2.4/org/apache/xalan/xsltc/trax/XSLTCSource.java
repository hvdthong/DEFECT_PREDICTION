package org.apache.xalan.xsltc.trax;

import java.io.File;
import java.io.IOException;

import org.xml.sax.XMLReader;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.xml.sax.ext.LexicalHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.Source;

import org.apache.xalan.xsltc.*;
import org.apache.xalan.xsltc.dom.DOMImpl;
import org.apache.xalan.xsltc.dom.DOMBuilder;
import org.apache.xalan.xsltc.dom.DTDMonitor;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;

public final class XSLTCSource implements Source {

    private String     _systemId = null;
    private DOMImpl    _dom      = null;
    private DTDMonitor _dtd      = null;

    private final static String LEXICAL_HANDLER_PROPERTY =

    /**
     * Create a new XSLTC-specific DOM source
     * @param size The estimated node-count for this DOM. A good guess here
     * speeds up the DOM build process.
     */
    public XSLTCSource(int size) {
	_dom = new DOMImpl(size);
	_dtd = new DTDMonitor();
    }

    /**
     * Create a new XSLTC-specific DOM source
     */
    public XSLTCSource() {
	_dom = new DOMImpl();
	_dtd = new DTDMonitor();
    }

    /**
     * Implements javax.xml.transform.Source.setSystemId()
     * Set the system identifier for this Source. 
     * This Source can get its input either directly from a file (in this case
     * it will instanciate and use a JAXP parser) or it can receive it through
     * ContentHandler/LexicalHandler interfaces.
     * @param systemId The system Id for this Source
     */
    public void setSystemId(String systemId) {
	if ((new File(systemId)).exists())
	    _systemId = "file:"+systemId;
	else
	    _systemId = systemId;
	_dom.setDocumentURI(_systemId);
    }

    /**
     * Implements javax.xml.transform.Source.getSystemId()
     * Get the system identifier that was set with setSystemId.
     * @return The system identifier that was set with setSystemId,
     *         or null if setSystemId was not called.
     */
    public String getSystemId() {
	return(_systemId);
    }

    /**
     * Build the internal XSLTC-specific DOM.
     * @param reader An XMLReader that will pass the XML contents to the DOM
     * @param systemId Specifies the input file
     * @throws SAXException
     */
    public void build(XMLReader reader, String systemId) throws SAXException {
	try {
	    if ((systemId == null) && (_systemId == null)) {
		ErrorMsg err = new ErrorMsg(ErrorMsg.XSLTC_SOURCE_ERR);
		throw new SAXException(err.toString());
	    }

	    if (systemId == null) systemId = _systemId;
	    setSystemId(systemId);

	    InputSource input = new InputSource(systemId);

	    _dtd.handleDTD(reader);

	    DOMBuilder builder = _dom.getBuilder();

	    reader.setContentHandler(builder);
	    try {
		reader.setProperty(LEXICAL_HANDLER_PROPERTY, builder);
	    }
	    catch (SAXException e) {
	    }

	    reader.parse(input);
	}
	catch (IOException e) {
	    throw new SAXException(e);
	}
    }

    /**
     * Build the internal XSLTC-specific DOM.
     * @param systemId Specifies the input file
     * @throws SAXException
     */
    public void build(String systemId) throws SAXException {
	try {
	    final SAXParserFactory factory = SAXParserFactory.newInstance();
	    final SAXParser parser = factory.newSAXParser();
	    final XMLReader reader = parser.getXMLReader();

	    build(reader, systemId);
	}
	catch (ParserConfigurationException e) {
	    throw new SAXException(e);
	}
    }

    /**
     * Build the internal XSLTC-specific DOM.
     * @param reader An XMLReader that will pass the XML contents to the DOM
     * @throws SAXException
     */
    public void build(XMLReader reader) throws SAXException {
	build(reader, _systemId);
    }

    /**
     * Build the internal XSLTC-specific DOM.
     * The setSystemId() must be called prior to this method.
     * @throws SAXException
     */
    public void build() throws SAXException {
	build(_systemId);
    }    

    /**
     * Returns the internal DOM that is encapsulated in this Source
     */
    protected DOMImpl getDOM() {
	return(_dom);
    }

    /**
     * Returns the internal DTD that is encapsulated in this Source
     */
    protected DTDMonitor getDTD() {
	return(_dtd);
    }

}
