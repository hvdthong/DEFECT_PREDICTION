package org.apache.xalan.xsltc.runtime.output;

import java.util.Stack;
import java.util.Vector;

import java.io.Writer;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.apache.xalan.xsltc.*;
import org.apache.xalan.xsltc.runtime.*;
import org.apache.xalan.xsltc.runtime.Hashtable;

public class StreamXMLOutput extends StreamOutput {

    private static final String BEGCDATA = "<![CDATA[";
    private static final String ENDCDATA = "]]>";
    private static final String CNTCDATA = "]]]]><![CDATA[>";
    private static final String BEGCOMM  = "<!--";
    private static final String ENDCOMM  = "-->";
    private static final String CDATA_ESC_START = "]]>&#";
    private static final String CDATA_ESC_END   = ";<![CDATA[";

    private String _elementName;

    public StreamXMLOutput(Writer writer, String encoding) {
	super(writer, encoding);
	initCDATA();
	initNamespaces();
    }

    public StreamXMLOutput(OutputStream out, String encoding) 
	throws IOException
    {
	super(out, encoding);
	initCDATA();
	initNamespaces();
    }

    public void startDocument() throws TransletException { 
	if (!_omitHeader) {
	    final StringBuffer header = new StringBuffer("<?xml version=\"");
	    header.append(_version).append("\" encoding=\"").append(_encoding);
	    if (_standalone != null) {
		header.append("\" standalone=\"").append(_standalone);
	    }
	    header.append("\"?>\n");

	    _buffer.insert(0, header.toString());
	}
    }

    public void endDocument() throws TransletException { 
	outputBuffer();
    }

    public void startElement(String elementName) throws TransletException { 
	if (_startTagOpen) {
	    closeStartTag();
	}
	else if (_cdataTagOpen) {
	    endCDATA();
	}

	if (_firstElement) {
	    if (_doctypeSystem != null) {
		appendDTD(elementName);
	    }
	    _firstElement = false;
	}

	if (_indent) {
	    indent(_lineFeedNextStartTag);
	    _lineFeedNextStartTag = true;
	    _indentNextEndTag = false;
	    _indentLevel++;
	}

	_buffer.append('<').append(elementName);

	_depth++;
	_startTagOpen = true;
	_elementName = elementName;
    }

    public void endElement(String elementName) throws TransletException { 
	if (_cdataTagOpen) {
	    endCDATA();
	}

	if (_startTagOpen) {
	    appendAttributes();
	    _buffer.append("/>");
	    _startTagOpen = false;

	    if (_indent) {
		_indentLevel--;
		_indentNextEndTag = true;
	    }
	}
	else {
	    if (_indent) {
		_indentLevel--;

		if (_indentNextEndTag) {
		    indent(_indentNextEndTag);
		    _indentNextEndTag = true;
		    _lineFeedNextStartTag = true;
		}
	    }
	    _buffer.append("</").append(elementName).append('>');
	    _indentNextEndTag = true;
	}

	if (((Integer) _cdataStack.peek()).intValue() == _depth) {
	    _cdataStack.pop();
	}

	popNamespaces();
	_depth--;
    }

    public void characters(String characters) throws TransletException { 
	characters(characters.toCharArray(), 0, characters.length());
    }

    public void characters(char[] characters, int offset, int length)
	throws TransletException 
    {
	if (length <= 0) return;

	if (_startTagOpen) {
	    closeStartTag();
	}

	final Integer I = (Integer) _cdataStack.peek();
	if (I.intValue() == _depth && !_cdataTagOpen) {
	    startCDATA(characters, offset, length);
	} 
	else if (_escaping) {
	    if (_cdataTagOpen) {
		escapeCDATA(characters, offset, length);
	    } 
	    else {
		escapeCharacters(characters, offset, length);
	    }
	} 
	else {
	    _buffer.append(characters, offset, length);
	}
    }

    public void attribute(String name, String value)
	throws TransletException 
    { 
	if (_startTagOpen) {
	    int k;
	    final Attribute attr = 
		new Attribute(patchName(name), escapeString(value));

	    if ((k = _attributes.indexOf(attr)) >= 0) {
		_attributes.setElementAt(attr, k);
	    }
	    else {
		_attributes.add(attr);
	    }
	}
    }

    public void comment(String comment) throws TransletException { 
	if (_startTagOpen) {
	    closeStartTag();
	}
	else if (_cdataTagOpen) {
	    endCDATA();
	}
	appendComment(comment);
    }

    public void processingInstruction(String target, String data)
	throws TransletException 
    { 
	if (_startTagOpen) {
	    closeStartTag();
	}
	else if (_cdataTagOpen) {
	    endCDATA();
	}

	_buffer.append("<?").append(target).append(' ')
	       .append(data).append("?>");
    }

    public boolean setEscaping(boolean escape) throws TransletException 
    { 
	final boolean temp = _escaping;
	_escaping = escape;
	return temp; 
    }

   public void namespace(final String prefix, final String uri)
	throws TransletException 
    {
	String escaped = escapeString(uri);
	if (_startTagOpen) {
	    if (pushNamespace(prefix, escaped)) {
		_buffer.append(' ').append(XMLNS_PREFIX);
		if (prefix != null && prefix != EMPTYSTRING) {
		    _buffer.append(':').append(prefix);
		}
		_buffer.append("=\"").append(escaped).append('"');
	    }
	}
	else if (prefix != EMPTYSTRING || uri != EMPTYSTRING) {
	    BasisLibrary.runTimeError(BasisLibrary.STRAY_NAMESPACE_ERR,
				      prefix, escaped);
	}
    }

    protected void closeStartTag() throws TransletException {
	super.closeStartTag();

	if (_cdata != null) {
	    final String localName = getLocalName(_elementName);
	    final String uri = getNamespaceURI(_elementName, true);

	    final StringBuffer expandedName = (uri == EMPTYSTRING) ? 
		new StringBuffer(_elementName) :
		new StringBuffer(uri).append(':').append(localName);

	    if (_cdata.containsKey(expandedName.toString())) {
		_cdataStack.push(new Integer(_depth));
	    }
	}
    }

    /**
     * Utility method - pass a whole charactes as CDATA to SAX handler
     */
    private void startCDATA(char[] ch, int off, int len) {
	final int limit = off + len;
	int offset = off;

	_buffer.append(BEGCDATA);

	for (int i = offset; i < limit - 2; i++) {
	    if (ch[i] == ']' && ch[i + 1] == ']' && ch[i + 2] == '>') {
		_buffer.append(ch, offset, i - offset)
		       .append(CNTCDATA);
		offset = i + 3;
	    }
	}

	if (offset < limit) {
	    _buffer.append(ch, offset, limit - offset);
	}
	_cdataTagOpen = true;
    }

    public void startCDATA() throws TransletException {
	_buffer.append(BEGCDATA);
	_cdataTagOpen = true;
    }

    public void endCDATA() throws TransletException {
	_buffer.append(ENDCDATA);
	_cdataTagOpen = false;
    }

    /**
     * Utility method - escape special characters and pass to SAX handler
     */
    private void escapeCDATA(char[] ch, int off, int len) {
	int offset = off;
	int limit = off + len;

	if (limit > ch.length) {
	    limit = ch.length;
	}

	for (int i = off; i < limit; i++) {
	    final char current = ch[i];

	    if (current > '\u00ff') {
		_buffer.append(ch, offset, i - offset)
		       .append(CDATA_ESC_START)
		       .append(Integer.toString((int) current))
		       .append(CDATA_ESC_END);
		offset = i + 1;
	    }
	}
	if (offset < limit) {
	    _buffer.append(ch, offset, limit - offset);
	}
    }

    /**
     * This method escapes special characters used in attribute values
     */
    private String escapeString(String value) {
	final char[] ch = value.toCharArray();
	final int limit = ch.length;
	StringBuffer result = new StringBuffer();
	
	int offset = 0;
	for (int i = 0; i < limit; i++) {
	    switch (ch[i]) {
	    case '&':
		result.append(ch, offset, i - offset).append(AMP);
		offset = i + 1;
		break;
	    case '"':
		result.append(ch, offset, i - offset).append(QUOT);
		offset = i + 1;
		break;
	    case '<':
		result.append(ch, offset, i - offset).append(LT);
		offset = i + 1;
		break;
	    case '>':
		result.append(ch, offset, i - offset).append(GT);
		offset = i + 1;
		break;
	    case '\n':
		result.append(ch, offset, i - offset).append(CRLF);
		offset = i + 1;
		break;
	    }
	}

	if (offset < limit) {
	    result.append(ch, offset, limit - offset);
	}
	return result.toString();
    }

    /**
     * This method escapes special characters used in text nodes
     */
    protected void escapeCharacters(char[] ch, int off, int len) {
	int limit = off + len;
	int offset = off;

	if (limit > ch.length) {
	    limit = ch.length;
	}

	for (int i = off; i < limit; i++) {
	    final char current = ch[i];

	    switch (current) {
	    case '&':
		_buffer.append(ch, offset, i - offset).append(AMP);
		offset = i + 1;
		break;
	    case '<':
		_buffer.append(ch, offset, i - offset).append(LT);
		offset = i + 1;
		break;
	    case '>':
		_buffer.append(ch, offset, i - offset).append(GT);
		offset = i + 1;
		break;
	    default:
		if ((current >= '\u007F' && current < '\u00A0') ||
		    (_is8859Encoded && current > '\u00FF'))
		{
		    _buffer.append(ch, offset, i - offset)
			   .append(CHAR_ESC_START)
			   .append(Integer.toString((int)ch[i]))
			   .append(';');
		    offset = i + 1;
		}
	    }
	}
	if (offset < limit) {
	    _buffer.append(ch, offset, limit - offset);
	}
    }
}
