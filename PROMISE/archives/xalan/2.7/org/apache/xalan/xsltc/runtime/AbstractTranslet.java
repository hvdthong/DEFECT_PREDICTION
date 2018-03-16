package org.apache.xalan.xsltc.runtime;

import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import javax.xml.transform.Templates;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.DOMImplementation;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.dtm.DTM;

import org.apache.xalan.xsltc.DOM;
import org.apache.xalan.xsltc.DOMCache;
import org.apache.xalan.xsltc.DOMEnhancedForDTM;
import org.apache.xalan.xsltc.Translet;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.dom.DOMAdapter;
import org.apache.xalan.xsltc.dom.KeyIndex;
import org.apache.xalan.xsltc.runtime.output.TransletOutputHandlerFactory;
import org.apache.xml.dtm.DTMAxisIterator;
import org.apache.xml.serializer.SerializationHandler;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 * @author G. Todd Miller
 * @author John Howard, JohnH@schemasoft.com 
 */
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
    public Vector _cdata = null;
    public int _indentamount = -1;

    public static final int FIRST_TRANSLET_VERSION = 100;
    public static final int VER_SPLIT_NAMES_ARRAY = 101;
    public static final int CURRENT_TRANSLET_VERSION = VER_SPLIT_NAMES_ARRAY;

    protected int transletVersion = FIRST_TRANSLET_VERSION;

    protected String[] namesArray;
    protected String[] urisArray;
    protected int[]    typesArray;
    protected String[] namespaceArray;
    
    protected Templates _templates = null;
    
    protected boolean _hasIdCall = false;

    protected StringValueHandler stringValueHandler = new StringValueHandler();

    private final static String EMPTYSTRING = "";

    private final static String ID_INDEX_NAME = "##id";

    
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
	return new DOMAdapter(dom, namesArray, urisArray, typesArray, namespaceArray);
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
     * This needs to get mapped to an instance variable in the class
     * The mapping  created so that 
     * the global variables in the generated class become 
     * http$colon$$flash$$flash$foo$dot$bar$colon$xyz
     */
    public final Object addParameter(String name, Object value) {
        name = BasisLibrary.mapQNameToJavaName (name);
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

        name = BasisLibrary.mapQNameToJavaName (name);

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

    /**
     * Give the translet an opportunity to perform a prepass on the document
     * to extract any information that it can store in an optimized form.
     *
     * Currently, it only extracts information about attributes of type ID.
     */
    public final void prepassDocument(DOM document) {
        setIndexSize(document.getSize());
        buildIDIndex(document);
    }

    /**
     * Leverages the Key Class to implement the XSLT id() function.
     * buildIdIndex creates the index (##id) that Key Class uses.
     * The index contains the element node index (int) and Id value (String).
     */
    private final void buildIDIndex(DOM document) {
        
        if (document instanceof DOMEnhancedForDTM) {
            DOMEnhancedForDTM enhancedDOM = (DOMEnhancedForDTM)document;
            
            if (enhancedDOM.hasDOMSource()) {
                buildKeyIndex(ID_INDEX_NAME, document);
                return;
            }
            else {
                final Hashtable elementsByID = enhancedDOM.getElementsWithIDs();

                if (elementsByID == null) {
            	    return;
                }

                final Enumeration idValues = elementsByID.keys();
                boolean hasIDValues = false;

                while (idValues.hasMoreElements()) {
            	    final Object idValue = idValues.nextElement();
            	    final int element = ((Integer)elementsByID.get(idValue)).intValue();

            	    buildKeyIndex(ID_INDEX_NAME, element, idValue);
            	    hasIDValues = true;
                }

                if (hasIDValues) {
            	    setKeyIndexDom(ID_INDEX_NAME, document);
                }
            }
        }
    }

    /**
     * After constructing the translet object, this method must be called to
     * perform any version-specific post-initialization that's required.
     */
    public final void postInitialization() {
        if (transletVersion < VER_SPLIT_NAMES_ARRAY) {
            int arraySize = namesArray.length;
            String[] newURIsArray = new String[arraySize];
            String[] newNamesArray = new String[arraySize];
            int[] newTypesArray = new int[arraySize];

            for (int i = 0; i < arraySize; i++) {
                String name = namesArray[i];
                int colonIndex = name.lastIndexOf(':');
                int lNameStartIdx = colonIndex+1;

                if (colonIndex > -1) {
                    newURIsArray[i] = name.substring(0, colonIndex);
                }

               if (name.charAt(lNameStartIdx) == '@') {
                   lNameStartIdx++;
                   newTypesArray[i] = DTM.ATTRIBUTE_NODE;
               } else if (name.charAt(lNameStartIdx) == '?') {
                   lNameStartIdx++;
                   newTypesArray[i] = DTM.NAMESPACE_NODE;
               } else {
                   newTypesArray[i] = DTM.ELEMENT_NODE;
               }
               newNamesArray[i] =
                          (lNameStartIdx == 0) ? name
                                               : name.substring(lNameStartIdx);
            }

            namesArray = newNamesArray;
            urisArray  = newURIsArray;
            typesArray = newTypesArray;
        }

        if (transletVersion > CURRENT_TRANSLET_VERSION) {
            BasisLibrary.runTimeError(BasisLibrary.UNKNOWN_TRANSLET_VERSION_ERR,
                                      this.getClass().getName());
        }
    }

    /************************************************************************
     * Index(es) for <xsl:key> / key() / id()
     ************************************************************************/

    private Hashtable _keyIndexes = null;
    private KeyIndex  _emptyKeyIndex = null;
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
     *   @param name is the name of the index (the key or ##id)
     *   @param node is the node id of the node to insert
     *   @param value is the value that will look up the node in the given index
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
     * Create an empty KeyIndex in the DOM case
     *   @param name is the name of the index (the key or ##id)
     *   @param dom is the DOM
     */
    public void buildKeyIndex(String name, DOM dom) {
	if (_keyIndexes == null) _keyIndexes = new Hashtable();
	
	KeyIndex index = (KeyIndex)_keyIndexes.get(name);
	if (index == null) {
	    _keyIndexes.put(name, index = new KeyIndex(_indexSize));
	}
	index.setDom(dom);
    }

    /**
     * Returns the index for a given key (or id).
     * The index implements our internal iterator interface
     */
    public KeyIndex getKeyIndex(String name) {
	if (_keyIndexes == null) {
	    return (_emptyKeyIndex != null) 
	        ? _emptyKeyIndex
	        : (_emptyKeyIndex = new KeyIndex(1)); 
	} 

	final KeyIndex index = (KeyIndex)_keyIndexes.get(name);

	if (index == null) {
	    return (_emptyKeyIndex != null) 
	        ? _emptyKeyIndex
	        : (_emptyKeyIndex = new KeyIndex(1)); 
	}

	return(index);
    }

    /**
     * This method builds key indexes - it is overridden in the compiled
     * translet in cases where the <xsl:key> element is used
     */
    public void buildKeys(DOM document, DTMAxisIterator iterator,
			  SerializationHandler handler,
			  int root) throws TransletException {
			  	
    }
    
    /**
     * This method builds key indexes - it is overridden in the compiled
     * translet in cases where the <xsl:key> element is used
     */
    public void setKeyIndexDom(String name, DOM document) {
    	getKeyIndex(name).setDom(document);
			  	
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

    public SerializationHandler openOutputHandler(String filename, boolean append) 
	throws TransletException 
    {
	try {
	    final TransletOutputHandlerFactory factory 
		= TransletOutputHandlerFactory.newInstance();

            String dirStr = new File(filename).getParent();
            if ((null != dirStr) && (dirStr.length() > 0)) {
               File dir = new File(dirStr);
               dir.mkdirs();
            }

	    factory.setEncoding(_encoding);
	    factory.setOutputMethod(_method);
	    factory.setWriter(new FileWriter(filename, append));
	    factory.setOutputType(TransletOutputHandlerFactory.STREAM);

	    final SerializationHandler handler 
		= factory.getSerializationHandler();

	    transferOutputSettings(handler);
	    handler.startDocument();
	    return handler;
	}
	catch (Exception e) {
	    throw new TransletException(e);
	}
    }

    public SerializationHandler openOutputHandler(String filename) 
       throws TransletException 
    {
       return openOutputHandler(filename, false);
    }

    public void closeOutputHandler(SerializationHandler handler) {
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
    public abstract void transform(DOM document, DTMAxisIterator iterator,
				   SerializationHandler handler)
	throws TransletException;

    /**
     * Calls transform() with a given output handler
     */
    public final void transform(DOM document, SerializationHandler handler) 
	throws TransletException {
        try {
            transform(document, document.getIterator(), handler);
        } finally {
            _keyIndexes = null;
        }
    }
	
    /**
     * Used by some compiled code as a shortcut for passing strings to the
     * output handler
     */
    public final void characters(final String string,
				 SerializationHandler handler) 
	throws TransletException {
        if (string != null) {
           try {
               handler.characters(string);
           } catch (Exception e) {
               throw new TransletException(e);
           }
        }   
    }

    /**
     * Add's a name of an element whose text contents should be output as CDATA
     */
    public void addCdataElement(String name) {
	if (_cdata == null) {
            _cdata = new Vector();
        }

        int lastColon = name.lastIndexOf(':');

        if (lastColon > 0) {
            String uri = name.substring(0, lastColon);
            String localName = name.substring(lastColon+1);
	    _cdata.addElement(uri);
	    _cdata.addElement(localName);
        } else {
	    _cdata.addElement(null);
	    _cdata.addElement(name);
        }
    }

    /**
     * Transfer the output settings to the output post-processor
     */
    protected void transferOutputSettings(SerializationHandler handler) {
	if (_method != null) {
	    if (_method.equals("xml")) {
	        if (_standalone != null) {
		    handler.setStandalone(_standalone);
		}
		if (_omitHeader) {
		    handler.setOmitXMLDeclaration(true);
		}
		handler.setCdataSectionElements(_cdata);
		if (_version != null) {
		    handler.setVersion(_version);
		}
		handler.setIndent(_indent);
		handler.setIndentAmount(_indentamount);
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
	    handler.setCdataSectionElements(_cdata);
	    if (_version != null) {
		handler.setVersion(_version);
	    }
	    if (_standalone != null) {
		handler.setStandalone(_standalone);
	    }
	    if (_omitHeader) {
		handler.setOmitXMLDeclaration(true);
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

    public void setAuxiliaryClasses(Hashtable auxClasses) {
    	_auxClasses = auxClasses;
    }
    
    public Class getAuxiliaryClass(String className) {
	if (_auxClasses == null) return null;
	return((Class)_auxClasses.get(className));
    }

    public String[] getNamesArray() {
	return namesArray;
    }
    
    public String[] getUrisArray() {
    	return urisArray;
    }
    
    public int[] getTypesArray() {
    	return typesArray;
    }
    
    public String[] getNamespaceArray() {
	return namespaceArray;
    }
    
    public boolean hasIdCall() {
    	return _hasIdCall;
    }
    
    public Templates getTemplates() {
    	return _templates;
    }
    
    public void setTemplates(Templates templates) {
    	_templates = templates;
    }    
    
    /************************************************************************
     * DOMImplementation caching for basis library
     ************************************************************************/
    protected DOMImplementation _domImplementation = null;
    
    public Document newDocument(String uri, String qname) 
        throws ParserConfigurationException 
    {
        if (_domImplementation == null) {
            _domImplementation = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().getDOMImplementation();
        }
        return _domImplementation.createDocument(uri, qname, null);
    }
}
