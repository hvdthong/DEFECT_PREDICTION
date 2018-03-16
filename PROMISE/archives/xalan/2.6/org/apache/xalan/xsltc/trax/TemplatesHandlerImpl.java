package org.apache.xalan.xsltc.trax;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.TemplatesHandler;

import org.apache.xalan.xsltc.compiler.CompilerException;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.SourceLoader;
import org.apache.xalan.xsltc.compiler.Stylesheet;
import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;
import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

import java.util.Vector;

/**
 * Implementation of a JAXP1.1 TemplatesHandler
 * @author Morten Jorgensen
 * @author Santiago Pericas-Geertsen
 */
public class TemplatesHandlerImpl
    implements ContentHandler, TemplatesHandler, SourceLoader
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
     * A reference to XSLTC's parser object.
     */
    private Parser _parser = null;

    /**
     * The created Templates object.
     */
    private TemplatesImpl _templates = null;

    /**
     * Default constructor
     */
    protected TemplatesHandlerImpl(int indentNumber,
	TransformerFactoryImpl tfactory)
    {
	_indentNumber = indentNumber;
	_tfactory = tfactory;

        _parser = new XSLTC().getParser();
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
        return _templates;
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


    /**
     * Re-initialize parser and forward SAX2 event.
     */
    public void startDocument() {
        XSLTC xsltc = _parser.getXSLTC();
        xsltc.setOutputType(XSLTC.BYTEARRAY_OUTPUT);
        _parser.startDocument();
    }

    /**
     * Just forward SAX2 event to parser object.
     */
    public void endDocument() throws SAXException {
        _parser.endDocument();

        try {
            XSLTC xsltc = _parser.getXSLTC();

            String transletName = null;
            if (_systemId != null) {
                transletName = Util.baseName(_systemId);
            }
            else {
                transletName = (String)_tfactory.getAttribute("translet-name");
            }
            xsltc.setClassName(transletName);

            transletName = xsltc.getClassName();

            Stylesheet stylesheet = null;
            SyntaxTreeNode root = _parser.getDocumentRoot();

            if (!_parser.errorsFound() && root != null) {
                stylesheet = _parser.makeStylesheet(root);
                stylesheet.setSystemId(_systemId);
                stylesheet.setParentStylesheet(null);

                if (_uriResolver != null) {
                    stylesheet.setSourceLoader(this);
                }

                _parser.setCurrentStylesheet(stylesheet);

                xsltc.setStylesheet(stylesheet);

                _parser.createAST(stylesheet);
            }

            if (!_parser.errorsFound() && stylesheet != null) {
                stylesheet.setMultiDocument(xsltc.isMultiDocument());
                stylesheet.setHasIdCall(xsltc.hasIdCall());

                synchronized (xsltc.getClass()) {
                    stylesheet.translate();
                }
            }

            if (!_parser.errorsFound()) {
                final byte[][] bytecodes = xsltc.getBytecodes();
                if (bytecodes != null) {
                    _templates =
                    new TemplatesImpl(xsltc.getBytecodes(), transletName,
                        _parser.getOutputProperties(), _indentNumber, _tfactory);

                    if (_uriResolver != null) {
                        _templates.setURIResolver(_uriResolver);
                    }
                }
            }
            else {
                StringBuffer errorMessage = new StringBuffer();
                Vector errors = _parser.getErrors();
                final int count = errors.size();
                for (int i = 0; i < count; i++) {
                    if (errorMessage.length() > 0)
                        errorMessage.append('\n');
                    errorMessage.append(errors.elementAt(i).toString());
                }
                throw new SAXException(ErrorMsg.JAXP_COMPILE_ERR, new TransformerException(errorMessage.toString()));
            }
        }
        catch (CompilerException e) {
            throw new SAXException(ErrorMsg.JAXP_COMPILE_ERR, e);
        }
    }

    /**
     * Just forward SAX2 event to parser object.
     */
    public void startPrefixMapping(String prefix, String uri) {
        _parser.startPrefixMapping(prefix, uri);
    }

    /**
     * Just forward SAX2 event to parser object.
     */
    public void endPrefixMapping(String prefix) {
        _parser.endPrefixMapping(prefix);
    }

    /**
     * Just forward SAX2 event to parser object.
     */
    public void startElement(String uri, String localname, String qname,
        Attributes attributes) throws SAXException
    {
        _parser.startElement(uri, localname, qname, attributes);
    }

    /**
     * Just forward SAX2 event to parser object.
     */
    public void endElement(String uri, String localname, String qname) {
        _parser.endElement(uri, localname, qname);
    }

    /**
     * Just forward SAX2 event to parser object.
     */
    public void characters(char[] ch, int start, int length) {
        _parser.characters(ch, start, length);
    }

    /**
     * Just forward SAX2 event to parser object.
     */
    public void processingInstruction(String name, String value) {
        _parser.processingInstruction(name, value);
    }

    /**
     * Just forward SAX2 event to parser object.
     */
    public void ignorableWhitespace(char[] ch, int start, int length) {
        _parser.ignorableWhitespace(ch, start, length);
    }

    /**
     * Just forward SAX2 event to parser object.
     */
    public void skippedEntity(String name) {
        _parser.skippedEntity(name);
    }

    /**
     * Set internal system Id and forward SAX2 event to parser object.
     */
    public void setDocumentLocator(Locator locator) {
        setSystemId(locator.getSystemId());
        _parser.setDocumentLocator(locator);
    }
}


