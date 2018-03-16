package org.apache.xalan.xsltc.trax;

import javax.xml.transform.*;
import javax.xml.transform.sax.*;

import org.xml.sax.Locator;
import org.xml.sax.InputSource;

import org.apache.xalan.xsltc.compiler.*;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;

/**
 * Implementation of a JAXP1.1 TemplatesHandler
 */
public class TemplatesHandlerImpl extends Parser 
    implements TemplatesHandler, SourceLoader 
{
    /**
     * System ID for this stylesheet.
     */
    private String _systemId;

    /**
     * Number of spaces to add for output indentation.
     */
    private int _indentNumber;

    /**
     * This URIResolver is passed to all Transformers.
     */
    private URIResolver _uriResolver = null;

    /**
     * A reference to the transformer factory that this templates
     * object belongs to.
     */
    private TransformerFactoryImpl _tfactory = null;

    /**
     * Default constructor
     */
    protected TemplatesHandlerImpl(int indentNumber, 
	TransformerFactoryImpl tfactory) 
    {
	super(null);
	_indentNumber = indentNumber;
	_tfactory = tfactory;
    }

    /**
     * Internal initialization
     */
    public void init() {
	final XSLTC xsltc = new XSLTC();
	super.setXSLTC(xsltc);
	xsltc.init();
	super.init();
	xsltc.setParser(this);
	xsltc.setOutputType(XSLTC.BYTEARRAY_OUTPUT);
    }

    /**
     * Implements javax.xml.transform.sax.TemplatesHandler.getSystemId()
     * Get the base ID (URI or system ID) from where relative URLs will be
     * resolved.
     * @return The systemID that was set with setSystemId(String id)
     */
    public String getSystemId() {
	return _systemId;
    }

    /**
     * Implements javax.xml.transform.sax.TemplatesHandler.setSystemId()
     * Get the base ID (URI or system ID) from where relative URLs will be
     * resolved.
     * @param id Base URI for this stylesheet
     */
    public void setSystemId(String id) {
	_systemId = id;
    }

    /**
     * Store URIResolver needed for Transformers.
     */
    public void setURIResolver(URIResolver resolver) {
	_uriResolver = resolver;
    }

    /**
     * Implements javax.xml.transform.sax.TemplatesHandler.getTemplates()
     * When a TemplatesHandler object is used as a ContentHandler or
     * DocumentHandler for the parsing of transformation instructions, it
     * creates a Templates object, which the caller can get once the SAX
     * events have been completed.
     * @return The Templates object that was created during the SAX event
     *         process, or null if no Templates object has been created.
     */
    public Templates getTemplates() {
	try {
	    final XSLTC xsltc = getXSLTC();

	    if (_uriResolver != null) {
		xsltc.setSourceLoader(this);
	    }

	    String transletName = TransformerFactoryImpl._defaultTransletName;
	    if (_systemId != null) {
		transletName = Util.baseName(_systemId);
	    }
	    xsltc.setClassName(transletName);

	    transletName = xsltc.getClassName();

	    Stylesheet stylesheet = null;
	    SyntaxTreeNode root = getDocumentRoot();

	    if (!errorsFound() && root != null) {
		stylesheet = makeStylesheet(root);
		stylesheet.setSystemId(_systemId);
		stylesheet.setParentStylesheet(null);
		setCurrentStylesheet(stylesheet);

		xsltc.setStylesheet(stylesheet);

		createAST(stylesheet);
	    }

	    if (!errorsFound() && stylesheet != null) {
		stylesheet.setMultiDocument(xsltc.isMultiDocument());
		stylesheet.translate();
	    }

	    if (!errorsFound()) {
		final byte[][] bytecodes = xsltc.getBytecodes();
		if (bytecodes != null) {
		    final TemplatesImpl templates = 
			new TemplatesImpl(xsltc.getBytecodes(), transletName, 
			    getOutputProperties(), _indentNumber, _tfactory);

		    if (_uriResolver != null) {
			templates.setURIResolver(_uriResolver);
		    }
		    return templates;
		}
	    }
	}
	catch (CompilerException e) {
	}
	return null;
    }

    /**
     * Recieve an object for locating the origin of SAX document events.
     * Most SAX parsers will use this method to inform content handler
     * of the location of the parsed document. 
     */
    public void setDocumentLocator(Locator locator) {
	super.setDocumentLocator(locator);
  	setSystemId(locator.getSystemId());
    }

    /**
     * This method implements XSLTC's SourceLoader interface. It is used to
     * glue a TrAX URIResolver to the XSLTC compiler's Input and Import classes.
     *
     * @param href The URI of the document to load
     * @param context The URI of the currently loaded document
     * @param xsltc The compiler that resuests the document
     * @return An InputSource with the loaded document
     */
    public InputSource loadSource(String href, String context, XSLTC xsltc) {
	try {
	    final Source source = _uriResolver.resolve(href, context);
	    if (source != null) {
		return Util.getInputSource(xsltc, source);
	    }
	}
	catch (TransformerException e) {
	}
	return null;
    }
}


