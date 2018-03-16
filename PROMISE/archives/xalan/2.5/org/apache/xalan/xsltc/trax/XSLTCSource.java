package org.apache.xalan.xsltc.trax;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.dom.DOMBuilder;
import org.apache.xalan.xsltc.dom.SAXImpl;
import org.apache.xalan.xsltc.dom.XSLTCDTMManager;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMManager;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public final class XSLTCSource implements Source {

    private String     _systemId = null;
    private DOM        _dom      = null;

    private final static String LEXICAL_HANDLER_PROPERTY =

    /**
     * Create a new XSLTC-specific DOM source
     * @param size The estimated node-count for this DOM. A good guess here
     * speeds up the DOM build process.
     */
    public XSLTCSource(int size) 
    {
      XSLTCDTMManager dtmManager =
                XSLTCDTMManager.newInstance();
      int dtmPos = dtmManager.getFirstFreeDTMID();
      int documentID = dtmPos << DTMManager.IDENT_DTM_NODE_BITS;
      _dom = (DOM)new SAXImpl(dtmManager, this, documentID, null,
                              null, false, size, true);
      dtmManager.addDTM((DTM)_dom, dtmPos);
    }

    /**
     * Create a new XSLTC-specific DOM source
     */
    public XSLTCSource() 
    {
      XSLTCDTMManager dtmManager =
                XSLTCDTMManager.newInstance();
      int dtmPos = dtmManager.getFirstFreeDTMID();
      int documentID = dtmPos << DTMManager.IDENT_DTM_NODE_BITS;
      _dom = (DOM)new SAXImpl(dtmManager, this, documentID, null,
                              null, false, true);
      dtmManager.addDTM((DTM)_dom, dtmPos);
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

        ((SAXImpl)_dom).setDocumentURI(_systemId);
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

	    DOMBuilder builder;
            builder = ((SAXImpl)_dom).getBuilder();

	    reader.setContentHandler(builder);
	    reader.setDTDHandler(builder);
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
    protected DOM getDOM() {
	return(_dom);
    }
}
