package org.apache.xalan.xsltc.runtime;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.apache.xalan.xsltc.*;
import org.apache.xalan.xsltc.dom.DOMAdapter;
import org.apache.xalan.xsltc.dom.DOMImpl;
import org.apache.xalan.xsltc.dom.KeyIndex;
import org.apache.xalan.xsltc.dom.DTDMonitor;
import org.apache.xalan.xsltc.util.IntegerArray;
import org.apache.xalan.xsltc.runtime.output.*;

public abstract class AbstractTranslet implements Translet {

    public String  _version = "1.0";
    public String  _method = null;
    public String  _encoding = "UTF-8";
    public boolean _omitHeader = false;
    public String  _standalone = null;
    public String  _doctypePublic = null;
    public String  _doctypeSystem = null;
    public boolean _indent = false;
    public String  _mediaType = null;
    public Hashtable _cdata = null;

    protected String[] namesArray;
    protected String[] namespaceArray;

    protected StringValueHandler stringValueHandler = new StringValueHandler();

    private final static String EMPTYSTRING = "";

    
    /************************************************************************
     * Debugging
     ************************************************************************/
    public void printInternalState() {
	System.out.println("-------------------------------------");
	System.out.println("AbstractTranslet this = " + this);
	System.out.println("pbase = " + pbase);
	System.out.println("vframe = " + pframe);
	System.out.println("paramsStack.size() = " + paramsStack.size());
	System.out.println("namesArray.size = " + namesArray.length);
	System.out.println("namespaceArray.size = " + namespaceArray.length);
	System.out.println("");
	System.out.println("Total memory = " + Runtime.getRuntime().totalMemory());
    }

    /**
     * Wrap the initial input DOM in a dom adapter. This adapter is wrapped in
     * a DOM multiplexer if the document() function is used (handled by compiled
     * code in the translet - see compiler/Stylesheet.compileTransform()).
     */
    public final DOMAdapter makeDOMAdapter(DOM dom)
	throws TransletException {
	if (dom instanceof DOMImpl)
	    return new DOMAdapter((DOMImpl)dom, namesArray, namespaceArray);
	BasisLibrary.runTimeError(BasisLibrary.DOM_ADAPTER_INIT_ERR);
	return null;
    }

    /************************************************************************
     * Parameter handling
     ************************************************************************/

    protected int pbase = 0, pframe = 0;
    protected ArrayList paramsStack = new ArrayList();

    /**
     * Push a new parameter frame.
     */
    public final void pushParamFrame() {
	paramsStack.add(pframe, new Integer(pbase));
	pbase = ++pframe;
    }

    /**
     * Pop the topmost parameter frame.
     */
    public final void popParamFrame() {
	if (pbase > 0) {
	    final int oldpbase = ((Integer)paramsStack.get(--pbase)).intValue();
	    for (int i = pframe - 1; i >= pbase; i--) {
		paramsStack.remove(i);
	    }
	    pframe = pbase; pbase = oldpbase;
	}
    }

    /**
     * Add a new global parameter if not already in the current frame.
     */
    public final Object addParameter(String name, Object value) {
	name = BasisLibrary.replace(name, ".-", 
				    new String[] { "$dot$", "$dash$" });
	return addParameter(name, value, false);
    }

    /**
     * Add a new global or local parameter if not already in the current frame.
     * The 'isDefault' parameter is set to true if the value passed is the
     * default value from the <xsl:parameter> element's select attribute or
     * element body.
     */
    public final Object addParameter(String name, Object value, 
	boolean isDefault) 
    {
	for (int i = pframe - 1; i >= pbase; i--) {
	    final Parameter param = (Parameter) paramsStack.get(i);

	    if (param._name.equals(name)) {
		if (param._isDefault || !isDefault) {
		    param._value = value;
		    param._isDefault = isDefault;
		    return value;
		}
		return param._value;
	    }
	}

	paramsStack.add(pframe++, new Parameter(name, value, isDefault));
	return value;
    }

    /**
     * Clears the parameter stack.
     */
    public void clearParameters() {  
	pbase = pframe = 0;
	paramsStack.clear();
    }

    /**
     * Get the value of a parameter from the current frame or
     * <tt>null</tt> if undefined.
     */
    public final Object getParameter(String name) {
	for (int i = pframe - 1; i >= pbase; i--) {
	    final Parameter param = (Parameter)paramsStack.get(i);
	    if (param._name.equals(name)) return param._value;
	}
	return null;
    }

    /************************************************************************
     * Message handling - implementation of <xsl:message>
     ************************************************************************/

    private MessageHandler _msgHandler = null;

    /**
     * Set the translet's message handler - must implement MessageHandler
     */
    public final void setMessageHandler(MessageHandler handler) {
	_msgHandler = handler;
    }

    /**
     * Pass a message to the message handler - used by Message class.
     */
    public final void displayMessage(String msg) {
	if (_msgHandler == null) {
            System.err.println(msg);
	}
	else {
	    _msgHandler.displayMessage(msg);
	}
    }

    /************************************************************************
     * Decimal number format symbol handling
     ************************************************************************/

    public Hashtable _formatSymbols = null;

    /**
     * Adds a DecimalFormat object to the _formatSymbols hashtable.
     * The entry is created with the input DecimalFormatSymbols.
     */
    public void addDecimalFormat(String name, DecimalFormatSymbols symbols) {
	if (_formatSymbols == null) _formatSymbols = new Hashtable();

	if (name == null) name = EMPTYSTRING;

	final DecimalFormat df = new DecimalFormat();
	if (symbols != null) {
	    df.setDecimalFormatSymbols(symbols);
	}
	_formatSymbols.put(name, df);
    }

    /**
     * Retrieves a named DecimalFormat object from _formatSymbols hashtable.
     */
    public final DecimalFormat getDecimalFormat(String name) {

	if (_formatSymbols != null) {
	    if (name == null) name = EMPTYSTRING;

	    DecimalFormat df = (DecimalFormat)_formatSymbols.get(name);
	    if (df == null) df = (DecimalFormat)_formatSymbols.get(EMPTYSTRING);
	    return df;
	}
	return(null);
    }

    /************************************************************************
     * Unparsed entity URI handling - implements unparsed-entity-uri()
     ************************************************************************/

    public Hashtable _unparsedEntities = null;

    /**
     * Get the value of an unparsed entity URI.
     * This method is used by the compiler/UnparsedEntityUriCall class.
     */
    public final String getUnparsedEntity(String name) {
	final String uri = (String)_unparsedEntities.get(name);
	return uri == null ? EMPTYSTRING : uri;
    }

    /**
     * Add an unparsed entity URI. The URI/value pairs are passed from the
     * DOM builder to the translet.
     */
    public final void addUnparsedEntity(String name, String uri) {
	if (_unparsedEntities == null)
	    _unparsedEntities = new Hashtable();
	if (_unparsedEntities.containsKey(name) == false)
	    _unparsedEntities.put(name, uri);
    }
    
    /**
     * Add an unparsed entity URI. The URI/value pairs are passed from the
     * DOM builder to the translet.
     */
    public final void setUnparsedEntityURIs(Hashtable table) {
	if (_unparsedEntities == null)
	    _unparsedEntities = table;
	else {
	    Enumeration keys = table.keys();
	    while (keys.hasMoreElements()) {
		String name = (String)keys.nextElement();
		_unparsedEntities.put(name,table.get(name));
	    }
	}
    }

    /**
     * The DTD monitor used by the DOM builder scans the input document DTD
     * for unparsed entity URIs. These are passed to the translet using
     * this method.
     */
    public final void setDTDMonitor(DTDMonitor monitor) {
	setUnparsedEntityURIs(monitor.getUnparsedEntityURIs());
    }

    /************************************************************************
     * Index(es) for <xsl:key> / key() / id()
     ************************************************************************/

    private Hashtable _keyIndexes = null;
    private KeyIndex  _emptyKeyIndex = new KeyIndex(1);
    private int       _indexSize = 0;

    /**
     * This method is used to pass the largest DOM size to the translet.
     * Needed to make sure that the translet can index the whole DOM.
     */
    public void setIndexSize(int size) {
	if (size > _indexSize) _indexSize = size;
    }

    /**
     * Creates a KeyIndex object of the desired size - don't want to resize!!!
     */
    public KeyIndex createKeyIndex() {
	return(new KeyIndex(_indexSize));
    }

    /**
     * Adds a value to a key/id index
     *   @name is the name of the index (the key or ##id)
     *   @node is the node id of the node to insert
     *   @value is the value that will look up the node in the given index
     */
    public void buildKeyIndex(String name, int node, Object value) {
	if (_keyIndexes == null) _keyIndexes = new Hashtable();
	
	KeyIndex index = (KeyIndex)_keyIndexes.get(name);
	if (index == null) {
	    _keyIndexes.put(name, index = new KeyIndex(_indexSize));
	}
	index.add(value, node);
    }

    /**
     * Returns the index for a given key (or id).
     * The index implements our internal iterator interface
     */
    public KeyIndex getKeyIndex(String name) {
	if (_keyIndexes == null) return(_emptyKeyIndex);

	final KeyIndex index = (KeyIndex)_keyIndexes.get(name);

	if (index == null) return(_emptyKeyIndex);

	return(index);
    }

    /**
     * This method builds key indexes - it is overridden in the compiled
     * translet in cases where the <xsl:key> element is used
     */
    public void buildKeys(DOM document, NodeIterator iterator,
			  TransletOutputHandler handler,
			  int root) throws TransletException {
    }

    /************************************************************************
     * DOM cache handling
     ************************************************************************/

    private DOMCache _domCache = null;

    /**
     * Sets the DOM cache used for additional documents loaded using the
     * document() function.
     */
    public void setDOMCache(DOMCache cache) {
	_domCache = cache;
    }

    /**
     * Returns the DOM cache used for this translet. Used by the LoadDocument
     * class (if present) when the document() function is used.
     */
    public DOMCache getDOMCache() {
	return(_domCache);
    }

    /************************************************************************
     * Multiple output document extension.
     * See compiler/TransletOutput for actual implementation.
     ************************************************************************/

    public TransletOutputHandler openOutputHandler(String filename) 
	throws TransletException 
    {
	try {
	    final TransletOutputHandlerFactory factory 
		= TransletOutputHandlerFactory.newInstance();

	    factory.setEncoding(_encoding);
	    factory.setOutputMethod(_method);
	    factory.setWriter(new FileWriter(filename));
	    factory.setOutputType(TransletOutputHandlerFactory.STREAM);

	    final TransletOutputHandler handler 
		= factory.getTransletOutputHandler();

	    transferOutputSettings(handler);
	    handler.startDocument();
	    return handler;
	}
	catch (Exception e) {
	    throw new TransletException(e);
	}
    }

    public void closeOutputHandler(TransletOutputHandler handler) {
	try {
	    handler.endDocument();
	    handler.close();
	}
	catch (Exception e) {
	}
    }

    /************************************************************************
     * Native API transformation methods - _NOT_ JAXP/TrAX
     ************************************************************************/

    /**
     * Main transform() method - this is overridden by the compiled translet
     */
    public abstract void transform(DOM document, NodeIterator iterator,
				   TransletOutputHandler handler)
	throws TransletException;

    /**
     * Calls transform() with a given output handler
     */
    public final void transform(DOM document, TransletOutputHandler handler) 
	throws TransletException {
	transform(document, document.getIterator(), handler);
    }
	
    /**
     * Used by some compiled code as a shortcut for passing strings to the
     * output handler
     */
    public final void characters(final String string,
				 TransletOutputHandler handler) 
	throws TransletException {
	final int length = string.length();
	handler.characters(string.toCharArray(), 0, length);
    }

    /**
     * Add's a name of an element whose text contents should be output as CDATA
     */
    public void addCdataElement(String name) {
	if (_cdata == null) _cdata = new Hashtable();
	_cdata.put(name, name);
    }

    /**
     * Transfer the output settings to the output post-processor
     */
    protected void transferOutputSettings(TransletOutputHandler handler) {
	if (_method != null) {
	    if (_method.equals("xml")) {
	        if (_standalone != null) {
		    handler.setStandalone(_standalone);
		}
		if (_omitHeader) {
		    handler.omitHeader(true);
		}
		handler.setCdataElements(_cdata);
		if (_version != null) {
		    handler.setVersion(_version);
		}
		handler.setIndent(_indent);
		if (_doctypeSystem != null) {
		    handler.setDoctype(_doctypeSystem, _doctypePublic);
		}
	    }
	    else if (_method.equals("html")) {
		handler.setIndent(_indent);
		handler.setDoctype(_doctypeSystem, _doctypePublic);
		if (_mediaType != null) {
		    handler.setMediaType(_mediaType);
		}
	    }
	}
	else {
	    handler.setCdataElements(_cdata);
	    if (_version != null) {
		handler.setVersion(_version);
	    }
	    if (_standalone != null) {
		handler.setStandalone(_standalone);
	    }
	    if (_omitHeader) {
		handler.omitHeader(true);
	    }
	    handler.setIndent(_indent);
	    handler.setDoctype(_doctypeSystem, _doctypePublic);
	}
    }

    private Hashtable _auxClasses = null;

    public void addAuxiliaryClass(Class auxClass) {
	if (_auxClasses == null) _auxClasses = new Hashtable();
	_auxClasses.put(auxClass.getName(), auxClass);
    }

    public Class getAuxiliaryClass(String className) {
	if (_auxClasses == null) return null;
	return((Class)_auxClasses.get(className));
    }

    public String[] getNamesArray() {
	return namesArray;
    }
    public String[] getNamespaceArray() {
	return namespaceArray;
    }
}
