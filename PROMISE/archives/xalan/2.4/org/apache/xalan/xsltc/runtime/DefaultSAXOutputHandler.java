package org.apache.xalan.xsltc.runtime;

import java.util.Vector;

import java.io.IOException;
import java.io.Writer;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;

import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public class DefaultSAXOutputHandler implements ContentHandler, LexicalHandler {

    private Writer _writer;

    private Hashtable _endTags = new Hashtable();
    private Hashtable _attributeTemplates = new Hashtable();
    private Hashtable _emptyElements = new Hashtable();
    private String _element = null;

    private int	    _outputType = TextOutput.UNKNOWN;
    private String  _encoding   = "UTF-8";
    private String  _version    = "1.0";
    private String  _standalone = null;
    private boolean _indent = false;
    private boolean _omitHeader = false;

    private boolean _startTagOpen = false;

    private static final char[] BEGPI    = "<?".toCharArray();
    private static final char[] ENDPI    = "?>".toCharArray();
    private static final char[] GT_CR    = ">".toCharArray();
    private static final char[] GT_LT_SL = "></".toCharArray();
    private static final char[] SL_GT    = "/>".toCharArray();
    private static final char[] XMLNS    = " xmlns".toCharArray();

    private static final char[] INDENT = "                    ".toCharArray();
    private static final int MAX_INDENT_LEVEL = (INDENT.length >> 1);
    private static final int MAX_INDENT       = INDENT.length;

    private static final String EMPTYSTRING = "";

    private boolean _lineFeedNextStartTag = false;
    private boolean _linefeedNextEndTag = false;
    private boolean _indentNextEndTag = false;
    private int     _indentLevel = 0;

    private Vector _namespaceDecls = null;

    /**
     * Constructor - set Writer to send output to and output encoding
     */
    public DefaultSAXOutputHandler(Writer writer, String encoding)
	throws IOException {
	_writer = writer;
	_encoding = encoding;
	init();
    }

    /**
     * Constructor - simple, initially for use in servlets
     */
    public DefaultSAXOutputHandler(Writer writer) throws IOException {
	this(writer, "UTF-8");
    }

    /**
     * Constructor - set output-stream & output encoding. 
     */
    public DefaultSAXOutputHandler(OutputStream out, String encoding)
	throws IOException {

        OutputStreamWriter writer;
        try {
            writer = new OutputStreamWriter(out, _encoding = encoding);
        }
        catch (java.io.UnsupportedEncodingException e) {
            writer = new OutputStreamWriter(out, _encoding = "UTF-8" );
        }
        _writer = new BufferedWriter(writer);
	init();
    }

    /**
     * Constructor - set output file and output encoding
     */
    public DefaultSAXOutputHandler(String filename, String encoding)
	throws IOException {
	this(new FileOutputStream(filename), encoding);
    }

    /**
     * Utility method: Initialise the output handler 
     */
    private void init() throws IOException {

        final String[] tags = { "area", "base", "basefont", "br",
				"col", "frame", "hr", "img", "input",
				"isindex", "link", "meta", "param" };
        for (int i = 0; i < tags.length; i++)
            _emptyElements.put(tags[i],tags[i]);

	_endTags.clear();
	_outputType = TextOutput.UNKNOWN;
	_indent = false;
	_indentNextEndTag = false;
	_indentLevel = 0;
	_startTagOpen = false;
    }

    /**
     * Close the output stream
     */
    public void close() {
	try {
	    if (_writer != null) _writer.close();
	}
	catch (IOException e) {
	}
    }

    /**
     * Utility method - outputs an XML header
     */
    private void emitHeader() throws SAXException {
	if (_omitHeader) return;

	StringBuffer buffer = new StringBuffer();
	buffer.append("<?xml version=\"");
	buffer.append(_version);
	buffer.append("\" encoding=\"");
	buffer.append(_encoding);
	if ( _standalone != null ) {
	    buffer.append("\" standalone=\"");
	    buffer.append(_standalone);
	}
	buffer.append("\"?>\n");
	characters(buffer.toString());
    }

    /**
     * Utility method - determine output type; XML or HTML
     */
    private void determineOutputType(String element) throws SAXException {
	if ((element != null) && (element.toLowerCase().equals("html"))) {
	    _outputType = TextOutput.HTML;
	}
	else {
	    _outputType = TextOutput.XML;
	    emitHeader();
	}
    }

    /**
     * SAX2: Receive notification of the beginning of a document.
     */
    public void startDocument() throws SAXException { 
    }

    /**
     * SAX2: Receive notification of the end of an element.
     */
    public void endDocument() throws SAXException  { 
        try {
	    _writer.flush();
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    /**
     * SAX2: Receive notification of the beginning of an element.
     */
    public void startElement(String uri, String localname,
			     String elementName, Attributes attrs)
	throws SAXException {
	try {
	    if (_outputType == TextOutput.UNKNOWN)
		determineOutputType(elementName);


            if (_indent) {
		if (!_emptyElements.containsKey(elementName.toLowerCase())) {
		    indent(_lineFeedNextStartTag);
		    _lineFeedNextStartTag = true;
		    _indentNextEndTag = false;
		}
		_indentLevel++;
            }

	    _writer.write('<');
	    _writer.write(elementName);
	    _startTagOpen = true;
	    _indentNextEndTag = false;

	    if (_namespaceDecls != null) {
		int nDecls = _namespaceDecls.size();
		for (int i = 0; i < nDecls; i++) {
		    final String prefix = (String) _namespaceDecls.elementAt(i++);
		    _writer.write(XMLNS);
		    if ((prefix != null) && (prefix != EMPTYSTRING)) {
			_writer.write(':');
			_writer.write(prefix);
		    }
		    _writer.write('=');
		    _writer.write('\"');
		    _writer.write((String) _namespaceDecls.elementAt(i));
		    _writer.write('\"');
		}
		_namespaceDecls.clear();
	    }

	    int attrCount = attrs.getLength();
	    for (int i = 0; i < attrCount; i++) {
		_writer.write(' ');
		_writer.write(attrs.getQName(i));
		_writer.write('=');
		_writer.write('\"');
		_writer.write(attrs.getValue(i));
		_writer.write('\"');
            }
	} catch (IOException e) {
            throw new SAXException(e);
        }
    }

    /**
     * SAX2: Receive notification of the end of an element.
     */
    public void endElement(String uri, String localname,
			   String elementName)  throws SAXException {
	try {

            if (_indent) _indentLevel--;

            if (_startTagOpen) {
                closeStartTag(false);
            }
            else {
                if ((_indent) && (_indentNextEndTag)) {
		    indent(_indentNextEndTag);
		    _indentNextEndTag = true;
		}
                char[] endTag = (char[])_endTags.get(elementName);
                if (endTag == null) {
		    final int len = elementName.length();
		    final char[] src = elementName.toCharArray();
		    endTag = new char[len+3];
		    System.arraycopy(src, 0, endTag, 2, len);
		    endTag[0] = '<';
		    endTag[1] = '/';
		    endTag[len+2] = '>';
                    _endTags.put(elementName,endTag);
                }
                _writer.write(endTag);
            }
	    /* Will not add this code for performance reasons.
	       The purpose of the code is to avoid line feeds and whitespaces
	       after <img> elements inside <a>...</a> elements in HTML.
	    if (elementName.toLowerCase().equals("img")) {
		_linefeedNextEndTag = false;
		_indentNextEndTag = false;
	    }
	    else {
		_indentNextEndTag = true;
	    }
	    */
	    _indentNextEndTag = true;
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    /**
     * Utility method - pass a string to the SAX handler's characters() method
     */
    private void characters(String str) throws SAXException{
	final char[] ch = str.toCharArray();
	characters(ch, 0, ch.length);
    }

    /**
     * Utility method - pass a whole character array to the SAX handler
     */
    private void characters(char[] ch) throws SAXException{
	characters(ch, 0, ch.length);
    }

    /**
     * SAX2: Receive notification of character data.
     */
    public void characters(char[] ch, int off, int len) throws SAXException {
        try {
	    if (_outputType == TextOutput.UNKNOWN)
		determineOutputType(null);
	    
            if (len == 0) return;

            if (_startTagOpen) closeStartTag(true);

	    _writer.write(ch, off, len);
        }
        catch (IOException e) {
            throw new SAXException(e);
        }
    }

    /**
     * SAX2: Receive notification of a processing instruction.
     */
    public void processingInstruction(String target, String data)
	throws SAXException {
	try {
            if (_startTagOpen) closeStartTag(true);
            _writer.write(BEGPI);
            _writer.write(target);
            _writer.write(' ');
            _writer.write(data);
            if (_outputType == TextOutput.HTML)
                _writer.write('>');
            else
                _writer.write(ENDPI);

        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    /**
     * SAX2: Receive notification of ignorable whitespace in element content.
     */
    public void ignorableWhitespace(char[] ch, int start, int len) { }

    /**
     * SAX2: Receive an object for locating the origin of SAX document events.
     */
    public void setDocumentLocator(Locator locator) { } 

    /**
     * SAX2: Receive notification of a skipped entity.
     */
    public void skippedEntity(String name) { }

    /**
     * SAX2: Begin the scope of a prefix-URI Namespace mapping.
     *       Namespace declarations are output in startElement()
     */
    public void startPrefixMapping(String prefix, String uri) {
	if (_namespaceDecls == null) {
	    _namespaceDecls = new Vector(2);
	}
	_namespaceDecls.addElement(prefix);
	_namespaceDecls.addElement(uri);
    }

    /**
     * SAX2: End the scope of a prefix-URI Namespace mapping.
     */
    public void endPrefixMapping(String prefix) {
    }

    public void startCDATA() { }
    public void endCDATA() { }
    public void comment(char[] ch, int start, int length) { }
    public void startEntity(java.lang.String name) { }
    public void endDTD() { }
    public void endEntity(String name) { }

    /**
     * This method is part of the LexicalHandler interface. It is only used to
     * pass DOCTYPE declarations (based on the doctype-system/public attributes
     * in the <xsl:output> element) to the output handler.
     * @param name     The document type name (name of first element)
     * @param publicId <xsl:output doctype-public="..."/>
     * @param systemId <xsl:output doctype-system="..."/>
     * @throws SAXException Whenever
     */
    public void startDTD(String name, String publicId, String systemId)
	throws SAXException {
	try {
	    StringBuffer buf = new StringBuffer("<!DOCTYPE ");
	    buf.append(name);
	    if (publicId == null) {
		buf.append(" SYSTEM");
	    }
	    else {
		buf.append(" PUBLIC \"");
		buf.append(publicId);
		buf.append("\"");
	    }
	    if (systemId != null) {
		buf.append(" \"");
		buf.append(systemId);
		buf.append("\">\n");
	    }
	    else {
		buf.append(">\n");
	    }
	    _writer.write(buf.toString());
	}
        catch (IOException e) {
            throw new SAXException(e);
        }
    }

    /**
     * Adds a newline in the output stream and indents to correct level
     */
    private void indent(boolean linefeed) throws IOException {
        if (linefeed)
            _writer.write('\n');
        if (_indentLevel < MAX_INDENT_LEVEL)
            _writer.write(INDENT, 0, (_indentLevel+_indentLevel));
        else
            _writer.write(INDENT, 0, MAX_INDENT);
    }

    /**
     * Closes a start tag of an element
     */
    private void closeStartTag(boolean content) throws SAXException {
        try {
            if (!content) {
                if (_outputType == TextOutput.HTML) {
                    if (!_emptyElements.containsKey(_element.toLowerCase())){
                        _writer.write(GT_LT_SL);
                        _writer.write(_element);
			_writer.write('>');
                    }
		    else {
			_writer.write(GT_CR);
		    }
                }
                else {
                    _writer.write(SL_GT);
                }
            }
            else {
		_writer.write('>');
            }
            _startTagOpen = false;
        }
        catch (IOException e) {
            throw new SAXException(e);
        }
    }

    /**
     * Turns output indentation on/off (used with XML and HTML output only)
     * Breaks the SAX HandlerBase interface, but TextOutput will only call
     * this method of the SAX handler is an instance of this class.
     */
    public void setIndent(boolean indent) {
        _indent = indent;
    }

    /**
     * Sets the version number that will be output in the XML header.
     */
    public void setVersion(String version) {
	_version = version;
    }

    /**
     * Sets the 'standalone' attribute that will be output in the XML header.
     * The attribute will be omitted unless this method is called.
     */
    public void setStandalone(String standalone) {
	_standalone = standalone;
    }

    /**
     * Turns xml declaration generation on/off, dependent on the attribute
     * omit-xml-declaration in any xsl:output element. 
     * Breaks the SAX HandlerBase interface, but TextOutput will only call
     * this method of the SAX handler is an instance of this class.
     */
    public void omitHeader(boolean value) {
        _omitHeader = value;
    }

    /**
     * Set the output type (either TEXT, HTML or XML)
     * Breaks the SAX HandlerBase interface, but TextOutput will only call
     * this method of the SAX handler is an instance of this class.
     */
    public void setOutputType(int type) throws SAXException {
	_outputType = type;
	if (_outputType == TextOutput.XML ) {
	    emitHeader();
	}
    }

}
