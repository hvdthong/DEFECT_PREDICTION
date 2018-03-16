package org.apache.xalan.xsltc.runtime.output;

import java.io.Writer;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import java.util.Vector;

import org.apache.xalan.xsltc.TransletException;

abstract class StreamOutput extends OutputBase {

    protected static final String AMP      = "&amp;";
    protected static final String LT       = "&lt;";
    protected static final String GT       = "&gt;";
    protected static final String CRLF     = "&#xA;";
    protected static final String APOS     = "&apos;";
    protected static final String QUOT     = "&quot;";
    protected static final String NBSP     = "&nbsp;";

    protected static final String CHAR_ESC_START  = "&#";

    protected static final char[] INDENT = "                    ".toCharArray();
    protected static final int MAX_INDENT_LEVEL = (INDENT.length >> 1);
    protected static final int MAX_INDENT       = INDENT.length;

    protected static final int BUFFER_SIZE = 32 * 1024;
    protected static final int OUTPUT_BUFFER_SIZE = 4 * 1024;

    protected Writer  _writer;
    protected StringBuffer _buffer;

    protected boolean _is8859Encoded = false;
    protected boolean _indent     = false;
    protected boolean _omitHeader = false;
    protected String  _standalone = null;
    protected String  _version    = "1.0";

    protected boolean _lineFeedNextStartTag = false;
    protected boolean _linefeedNextEndTag   = false;
    protected boolean _indentNextEndTag     = false;
    protected int     _indentLevel          = 0;

    protected boolean _escaping     = true;
    protected String  _encoding     = "UTF-8";

    protected int     _indentNumber = 2;

    protected Vector _attributes = new Vector();

    static class Attribute {
	public String name, value;

	Attribute(String name, String value) {
	    this.name = name; 
	    this.value = value;
	}

	public int hashCode() {
	    return name.hashCode();
	}

	public boolean equals(Object obj) {
	    try {
		return name.equalsIgnoreCase(((Attribute) obj).name);
	    }
	    catch (ClassCastException e) {
		return false;
	    }
	}
    }

    protected StreamOutput(StreamOutput output) {
	_writer = output._writer;
	_encoding = output._encoding;
	_is8859Encoded = output._is8859Encoded;
	_buffer = output._buffer;
	_indentNumber = output._indentNumber;
    }

    protected StreamOutput(Writer writer, String encoding) {
	_writer = writer;
	_encoding = encoding;
	_is8859Encoded = encoding.equalsIgnoreCase("iso-8859-1");
	_buffer = new StringBuffer(BUFFER_SIZE);
    }

    protected StreamOutput(OutputStream out, String encoding) 
	throws IOException
    {
	try {
	    _writer = new OutputStreamWriter(out, _encoding = encoding);
	    _is8859Encoded = encoding.equalsIgnoreCase("iso-8859-1");
	}
	catch (UnsupportedEncodingException e) {
	    _writer = new OutputStreamWriter(out, _encoding = "utf-8");
	}
	_buffer = new StringBuffer(BUFFER_SIZE);
    }

    public void setIndentNumber(int value) {
	_indentNumber = value;
    }

    /**
     * Set the output document system/public identifiers
     */
    public void setDoctype(String system, String pub) {
	_doctypeSystem = system;
	_doctypePublic = pub;
    }

    public void setIndent(boolean indent) { 
	_indent = indent;
    }

    public void omitHeader(boolean value) {
        _omitHeader = value;
    }

    public void setStandalone(String standalone) {
	_standalone = standalone;
    }

    public void setVersion(String version) { 
	_version = version;
    }

    protected void outputBuffer() {
	try {
	    int n = 0;
	    final int length = _buffer.length();
	    final String output = _buffer.toString();

	    if (length > OUTPUT_BUFFER_SIZE) {
		do {
		    _writer.write(output, n, OUTPUT_BUFFER_SIZE);
		    n += OUTPUT_BUFFER_SIZE;
		} while (n + OUTPUT_BUFFER_SIZE < length);
	    }
	    _writer.write(output, n, length - n);
	    _writer.flush();
	}
	catch (IOException e) {
	}
    }

    protected void appendDTD(String name) {
	_buffer.append("<!DOCTYPE ").append(name);
	if (_doctypePublic == null) {
	    _buffer.append(" SYSTEM");
	}
	else {
	    _buffer.append(" PUBLIC \"").append(_doctypePublic).append("\"");
	}
	if (_doctypeSystem != null) {
	    _buffer.append(" \"").append(_doctypeSystem).append("\">\n");
	}
	else {
	    _buffer.append(">\n");
	}
    }

    /**
     * Adds a newline in the output stream and indents to correct level
     */
    protected void indent(boolean linefeed) {
        if (linefeed) {
            _buffer.append('\n');
	}

	_buffer.append(INDENT, 0, 
	    _indentLevel < MAX_INDENT_LEVEL ? _indentLevel * _indentNumber 
		: MAX_INDENT);
    }

    /**
     * This method escapes special characters used in text nodes. It
     * is overriden for XML and HTML output.
     */
    protected void escapeCharacters(char[] ch, int off, int len) {
    }

    protected void appendAttributes() {
	if (!_attributes.isEmpty()) {
	    int i = 0;
	    final int length = _attributes.size();

	    do {
		final Attribute attr = (Attribute) _attributes.elementAt(i);
		_buffer.append(' ').append(attr.name).append("=\"")
		       .append(attr.value).append('"');
	    } while (++i < length);

	    _attributes.clear();
	}
    }

    protected void closeStartTag() throws TransletException {
	appendAttributes();
	_buffer.append('>');
	_startTagOpen = false;
    }

    /**
     * Ensure that comments do not include the sequence "--" and
     * that they do not end with "-".
     */
    protected void appendComment(String comment) 
	throws TransletException 
    {
	boolean lastIsDash = false;
	final int n = comment.length();

	_buffer.append("<!--");
	for (int i = 0; i < n; i++) {
	    final char ch = comment.charAt(i);
	    final boolean isDash = (ch == '-');

	    if (lastIsDash && isDash) {
		_buffer.append(" -");
	    }
	    else {
		_buffer.append(ch);
	    }
	    lastIsDash = isDash;
	}
	if (lastIsDash) {
	    _buffer.append(' ');
	}
	_buffer.append("-->");
    }
}
