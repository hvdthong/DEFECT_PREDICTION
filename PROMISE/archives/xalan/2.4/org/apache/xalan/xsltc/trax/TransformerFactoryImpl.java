package org.apache.xalan.xsltc.trax;

import java.io.File;
import java.io.Reader;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Properties;

import javax.xml.transform.*;
import javax.xml.transform.sax.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.*;
import org.w3c.dom.Document;

import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;

import org.apache.xalan.xsltc.compiler.XSLTC;
import org.apache.xalan.xsltc.compiler.SourceLoader;
import org.apache.xalan.xsltc.compiler.CompilerException;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;

/**
 * Implementation of a JAXP1.1 TransformerFactory for Translets.
 */
public class TransformerFactoryImpl
    extends SAXTransformerFactory implements SourceLoader, ErrorListener 
{
    /**
     * This error listener is used only for this factory and is not passed to
     * the Templates or Transformer objects that we create.
     */
    private ErrorListener _errorListener = this; 

    /**
     * This URIResolver is passed to all created Templates and Transformers
     */
    private URIResolver _uriResolver = null;

    /** 
     * As Gregor Samsa awoke one morning from uneasy dreams he found himself
     * transformed in his bed into a gigantic insect. He was lying on his hard,
     * as it were armour plated, back, and if he lifted his head a little he
     * could see his big, brown belly divided into stiff, arched segments, on
     * top of which the bed quilt could hardly keep in position and was about
     * to slide off completely. His numerous legs, which were pitifully thin
     * compared to the rest of his bulk, waved helplessly before his eyes.
     * "What has happened to me?", he thought. It was no dream....
     */
    protected static String _defaultTransletName = "GregorSamsa";

    /**
     * This Hashtable is used to store parameters for locating
     * <?xml-stylesheet ...?> processing instructions in XML docs.
     */
    private Hashtable _piParams = null;

    /**
     * The above hashtable stores objects of this class.
     */
    private class PIParamWrapper {
	public String _media = null;
	public String _title = null;
	public String _charset = null;
	
	public PIParamWrapper(String media, String title, String charset) {
	    _media = media;
	    _title = title;
	    _charset = charset;
	}
    }

    /**
     * Set to <code>true</code> when debugging is enabled.
     */
    private boolean _debug = false;

    /**
     * Set to <code>true</code> when templates are not inlined.
     */
    private boolean _disableInlining = false;

    /**
     * Number of indent spaces when indentation is turned on.
     */
    private int _indentNumber = -1;

    /**
     * A reference to an XML reader for parsing.
     */
    private XMLReader _xmlReader = null;

    /**
     * javax.xml.transform.sax.TransformerFactory implementation.
     * Contains nothing yet
     */
    public TransformerFactoryImpl() {
    }

    /**
     * javax.xml.transform.sax.TransformerFactory implementation.
     * Set the error event listener for the TransformerFactory, which is used
     * for the processing of transformation instructions, and not for the
     * transformation itself.
     *
     * @param listener The error listener to use with the TransformerFactory
     * @throws IllegalArgumentException
     */
    public void setErrorListener(ErrorListener listener) 
	throws IllegalArgumentException 
    {
	if (listener == null) {
	    ErrorMsg err = new ErrorMsg(ErrorMsg.ERROR_LISTENER_NULL_ERR,
					"TransformerFactory");
            throw new IllegalArgumentException(err.toString());
	}
	_errorListener = listener;
    }

    /**
     * javax.xml.transform.sax.TransformerFactory implementation.
     * Get the error event handler for the TransformerFactory.
     *
     * @return The error listener used with the TransformerFactory
     */
    public ErrorListener getErrorListener() { 
	return _errorListener;
    }

    /**
     * javax.xml.transform.sax.TransformerFactory implementation.
     * Returns the value set for a TransformerFactory attribute
     *
     * @param name The attribute name
     * @return An object representing the attribute value
     * @throws IllegalArgumentException
     */
    public Object getAttribute(String name) 
	throws IllegalArgumentException 
    { 
	if (name.equals("translet-name")) {
	    return _defaultTransletName;
	}

	ErrorMsg err = new ErrorMsg(ErrorMsg.JAXP_INVALID_ATTR_ERR, name);
	throw new IllegalArgumentException(err.toString());
    }

    /**
     * javax.xml.transform.sax.TransformerFactory implementation.
     * Sets the value for a TransformerFactory attribute.
     *
     * @param name The attribute name
     * @param value An object representing the attribute value
     * @throws IllegalArgumentException
     */
    public void setAttribute(String name, Object value) 
	throws IllegalArgumentException 
    { 
	if (name.equals("translet-name") && value instanceof String) {
	    _defaultTransletName = (String) value;
	    return;
	}
	else if (name.equals("debug")) {
	    if (value instanceof Boolean) {
		_debug = ((Boolean) value).booleanValue();
		return;
	    }
	    else if (value instanceof String) {
		_debug = ((String) value).equalsIgnoreCase("true");
		return;
	    }
	}
	else if (name.equals("disable-inlining")) {
	    if (value instanceof Boolean) {
		_disableInlining = ((Boolean) value).booleanValue();
		return;
	    }
	    else if (value instanceof String) {
		_disableInlining = ((String) value).equalsIgnoreCase("true");
		return;
	    }
	}
	else if (name.equals("indent-number")) {
	    if (value instanceof String) {
		try {
		    _indentNumber = Integer.parseInt((String) value);
		    return;
		}
		catch (NumberFormatException e) {
		}
	    }
	    else if (value instanceof Integer) {
		_indentNumber = ((Integer) value).intValue();
		return;
	    }
	}

	final ErrorMsg err 
	    = new ErrorMsg(ErrorMsg.JAXP_INVALID_ATTR_ERR, name);
	throw new IllegalArgumentException(err.toString());
    }

    /**
     * javax.xml.transform.sax.TransformerFactory implementation.
     * Look up the value of a feature (to see if it is supported).
     * This method must be updated as the various methods and features of this
     * class are implemented.
     *
     * @param name The feature name
     * @return 'true' if feature is supported, 'false' if not
     */
    public boolean getFeature(String name) { 
	String[] features = {
	    DOMSource.FEATURE,
	    DOMResult.FEATURE,
	    SAXSource.FEATURE,
	    SAXResult.FEATURE,
	    StreamSource.FEATURE,
	    StreamResult.FEATURE
	};

	for (int i =0; i < features.length; i++) {
	    if (name.equals(features[i])) {
		return true;
	    }
	}
	return false;
    }

    /**
     * javax.xml.transform.sax.TransformerFactory implementation.
     * Get the object that is used by default during the transformation to
     * resolve URIs used in document(), xsl:import, or xsl:include.
     *
     * @return The URLResolver used for this TransformerFactory and all
     * Templates and Transformer objects created using this factory
     */    
    public URIResolver getURIResolver() {
	return _uriResolver;
    } 

    /**
     * javax.xml.transform.sax.TransformerFactory implementation.
     * Set the object that is used by default during the transformation to
     * resolve URIs used in document(), xsl:import, or xsl:include. Note that
     * this does not affect Templates and Transformers that are already
     * created with this factory.
     *
     * @param resolver The URLResolver used for this TransformerFactory and all
     * Templates and Transformer objects created using this factory
     */    
    public void setURIResolver(URIResolver resolver) {
	_uriResolver = resolver;
    }

    /**
     * javax.xml.transform.sax.TransformerFactory implementation.
     * Get the stylesheet specification(s) associated via the xml-stylesheet
     * the document document specified in the source parameter, and that match
     * the given criteria.
     *
     * @param source The XML source document.
     * @param media The media attribute to be matched. May be null, in which
     * case the prefered templates will be used (i.e. alternate = no).
     * @param title The value of the title attribute to match. May be null.
     * @param charset The value of the charset attribute to match. May be null.
     * @return A Source object suitable for passing to the TransformerFactory.
     * @throws TransformerConfigurationException
     */
    public Source getAssociatedStylesheet(Source source, String media,
					  String title, String charset)
	throws TransformerConfigurationException 
    {
	if (_piParams == null) {
	    _piParams = new Hashtable();
	}
	_piParams.put(source, new PIParamWrapper(media, title, charset));
	return source;
    }

    /**
     * javax.xml.transform.sax.TransformerFactory implementation.
     * Create a Transformer object that copies the input document to the result.
     *
     * @return A Transformer object that simply copies the source to the result.
     * @throws TransformerConfigurationException
     */    
    public Transformer newTransformer()
	throws TransformerConfigurationException 
    { 
	TransformerImpl result = new TransformerImpl(new Properties(), 
	    _indentNumber, this);
	if (_uriResolver != null) {
	    result.setURIResolver(_uriResolver);
	}
	return result;
    }

    /**
     * javax.xml.transform.sax.TransformerFactory implementation.
     * Process the Source into a Templates object, which is a a compiled
     * representation of the source. Note that this method should not be
     * used with XSLTC, as the time-consuming compilation is done for each
     * and every transformation.
     *
     * @return A Templates object that can be used to create Transformers.
     * @throws TransformerConfigurationException
     */
    public Transformer newTransformer(Source source) throws
	TransformerConfigurationException 
    {
	final Templates templates = newTemplates(source);
	final Transformer transformer = templates.newTransformer();
	if (_uriResolver != null) {
	    transformer.setURIResolver(_uriResolver);
	}
	return(transformer);
    }

    /**
     * Pass warning messages from the compiler to the error listener
     */
    private void passWarningsToListener(Vector messages) 
	throws TransformerException 
    {
	if (_errorListener == null || messages == null) {
	    return;
	}
	final int count = messages.size();
	for (int pos = 0; pos < count; pos++) {
	    String message = messages.elementAt(pos).toString();
	    _errorListener.error(
		new TransformerConfigurationException(message));
	}
    }

    /**
     * Pass error messages from the compiler to the error listener
     */
    private void passErrorsToListener(Vector messages) {
	try {
	    if (_errorListener == null || messages == null) {
		return;
	    }
	    final int count = messages.size();
	    for (int pos = 0; pos < count; pos++) {
		String message = messages.elementAt(pos).toString();
		_errorListener.error(new TransformerException(message));
	    }
	}
	catch (TransformerException e) {
	}
    }

    /**
     * javax.xml.transform.sax.TransformerFactory implementation.
     * Process the Source into a Templates object, which is a a compiled
     * representation of the source.
     *
     * @param stylesheet The input stylesheet - DOMSource not supported!!!
     * @return A Templates object that can be used to create Transformers.
     * @throws TransformerConfigurationException
     */
    public Templates newTemplates(Source source)
	throws TransformerConfigurationException 
    {
	final XSLTC xsltc = new XSLTC();
	if (_debug) xsltc.setDebug(true);
	if (_disableInlining) xsltc.setTemplateInlining(false);
	xsltc.init();

	if (_uriResolver != null) {
	    xsltc.setSourceLoader(this);
	}

	if ((_piParams != null) && (_piParams.get(source) != null)) {
	    PIParamWrapper p = (PIParamWrapper)_piParams.get(source);
	    if (p != null) {
		xsltc.setPIParameters(p._media, p._title, p._charset);
	    }
	}

	final InputSource input = Util.getInputSource(xsltc, source);
	byte[][] bytecodes = xsltc.compile(null, input);
	final String transletName = xsltc.getClassName();

	if (_errorListener != this) {
	    try {
		passWarningsToListener(xsltc.getWarnings());
	    }
	    catch (TransformerException e) {
		throw new TransformerConfigurationException(e);
	    }
	}
	else {
	    xsltc.printWarnings();
	}

	if (bytecodes == null) {
	    if (_errorListener != null) {
		passErrorsToListener(xsltc.getErrors());
	    }
	    else {
		xsltc.printErrors();
	    }
	    ErrorMsg err = new ErrorMsg(ErrorMsg.JAXP_COMPILE_ERR);
	    throw new TransformerConfigurationException(err.toString());
	}

	return new TemplatesImpl(bytecodes, transletName, 
	    xsltc.getOutputProperties(), _indentNumber, this);
    }

    /**
     * javax.xml.transform.sax.SAXTransformerFactory implementation.
     * Get a TemplatesHandler object that can process SAX ContentHandler
     * events into a Templates object.
     *
     * @return A TemplatesHandler object that can handle SAX events
     * @throws TransformerConfigurationException
     */
    public TemplatesHandler newTemplatesHandler() 
	throws TransformerConfigurationException 
    { 
	final TemplatesHandlerImpl handler = 
	    new TemplatesHandlerImpl(_indentNumber, this);
	handler.init();
	if (_uriResolver != null) {
	    handler.setURIResolver(_uriResolver);
	}
	return handler;
    }

    /**
     * javax.xml.transform.sax.SAXTransformerFactory implementation.
     * Get a TransformerHandler object that can process SAX ContentHandler
     * events into a Result. This method will return a pure copy transformer.
     *
     * @return A TransformerHandler object that can handle SAX events
     * @throws TransformerConfigurationException
     */
    public TransformerHandler newTransformerHandler() 
	throws TransformerConfigurationException 
    {
	final Transformer transformer = newTransformer();
	if (_uriResolver != null) {
	    transformer.setURIResolver(_uriResolver);
	}
	return new TransformerHandlerImpl((TransformerImpl) transformer);
    }

    /**
     * javax.xml.transform.sax.SAXTransformerFactory implementation.
     * Get a TransformerHandler object that can process SAX ContentHandler
     * events into a Result, based on the transformation instructions
     * specified by the argument.
     *
     * @param src The source of the transformation instructions.
     * @return A TransformerHandler object that can handle SAX events
     * @throws TransformerConfigurationException
     */
    public TransformerHandler newTransformerHandler(Source src) 
	throws TransformerConfigurationException 
    { 
	final Transformer transformer = newTransformer(src);
	if (_uriResolver != null) {
	    transformer.setURIResolver(_uriResolver);
	}
	return new TransformerHandlerImpl((TransformerImpl) transformer);
    }

    /**
     * javax.xml.transform.sax.SAXTransformerFactory implementation.
     * Get a TransformerHandler object that can process SAX ContentHandler
     * events into a Result, based on the transformation instructions
     * specified by the argument.
     *
     * @param templates Represents a pre-processed stylesheet
     * @return A TransformerHandler object that can handle SAX events
     * @throws TransformerConfigurationException
     */    
    public TransformerHandler newTransformerHandler(Templates templates) 
	throws TransformerConfigurationException  
    {
	final Transformer transformer = templates.newTransformer();
	final TransformerImpl internal = (TransformerImpl)transformer;
	return new TransformerHandlerImpl(internal);
    }

    /**
     * javax.xml.transform.sax.SAXTransformerFactory implementation.
     * Create an XMLFilter that uses the given source as the
     * transformation instructions.
     *
     * @param src The source of the transformation instructions.
     * @return An XMLFilter object, or null if this feature is not supported.
     * @throws TransformerConfigurationException
     */
    public XMLFilter newXMLFilter(Source src) 
	throws TransformerConfigurationException 
    {
	Templates templates = newTemplates(src);
	if (templates == null) return null; 
	return newXMLFilter(templates);
    }

    /**
     * javax.xml.transform.sax.SAXTransformerFactory implementation.
     * Create an XMLFilter that uses the given source as the
     * transformation instructions.
     *
     * @param src The source of the transformation instructions.
     * @return An XMLFilter object, or null if this feature is not supported.
     * @throws TransformerConfigurationException
     */
    public XMLFilter newXMLFilter(Templates templates) 
	throws TransformerConfigurationException 
    {
	try {
      	    return new org.apache.xalan.xsltc.trax.TrAXFilter(templates);
    	}
	catch (TransformerConfigurationException e1) {
      	    if (_errorListener != null) {
                try {
          	    _errorListener.fatalError(e1);
          	    return null;
        	}
		catch (TransformerException e2) {
          	    new TransformerConfigurationException(e2);
        	}
      	    }
      	    throw e1;
    	}
    }

    /**
     * Receive notification of a recoverable error. 
     * The transformer must continue to provide normal parsing events after
     * invoking this method. It should still be possible for the application
     * to process the document through to the end.
     *
     * @param exception The warning information encapsulated in a transformer 
     * exception.
     * @throws TransformerException if the application chooses to discontinue
     * the transformation (always does in our case).
     */
    public void error(TransformerException e)
	throws TransformerException 
    {
	System.err.println("ERROR: "+e.getMessageAndLocation());
	Throwable wrapped = e.getException();
	if (wrapped != null) {
	    System.err.println("     : "+wrapped.getMessage());
	}
	throw e; 	
    }

    /**
     * Receive notification of a non-recoverable error. 
     * The application must assume that the transformation cannot continue
     * after the Transformer has invoked this method, and should continue
     * (if at all) only to collect addition error messages. In fact,
     * Transformers are free to stop reporting events once this method has
     * been invoked.
     *
     * @param exception The warning information encapsulated in a transformer
     * exception.
     * @throws TransformerException if the application chooses to discontinue
     * the transformation (always does in our case).
     */
    public void fatalError(TransformerException e)
	throws TransformerException 
    {
	System.err.println("FATAL: "+e.getMessageAndLocation());
	Throwable wrapped = e.getException();
	if (wrapped != null) {
	    System.err.println("     : "+wrapped.getMessage());
	}
	throw e;
    }

    /**
     * Receive notification of a warning.
     * Transformers can use this method to report conditions that are not
     * errors or fatal errors. The default behaviour is to take no action.
     * After invoking this method, the Transformer must continue with the
     * transformation. It should still be possible for the application to
     * process the document through to the end.
     *
     * @param exception The warning information encapsulated in a transformer
     * exception.
     * @throws TransformerException if the application chooses to discontinue
     * the transformation (never does in our case).
     */
    public void warning(TransformerException e)
	throws TransformerException 
    {
	System.err.println("WARNING: "+e.getMessageAndLocation());
	Throwable wrapped = e.getException();
	if (wrapped != null) {
	    System.err.println("       : "+wrapped.getMessage());
	}
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
	    if (_uriResolver != null) {
		final Source source = _uriResolver.resolve(href, context);
		if (source != null) {
		    return Util.getInputSource(xsltc, source);
		}
	    }
	}
	catch (TransformerException e) {
	}
	return null;
    }

    public XMLReader getXMLReader() throws Exception {
	if (_xmlReader == null) {
	    final SAXParserFactory pfactory 
		= SAXParserFactory.newInstance();
	    pfactory.setNamespaceAware(true);
	    _xmlReader = pfactory.newSAXParser().getXMLReader();
	}
	return _xmlReader;
    }
}
