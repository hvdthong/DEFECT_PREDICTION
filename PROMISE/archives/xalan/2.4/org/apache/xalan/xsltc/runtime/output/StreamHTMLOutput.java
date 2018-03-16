package org.apache.xalan.xsltc.runtime.output;

import java.util.Vector;

import java.io.Writer;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.apache.xalan.xsltc.*;
import org.apache.xalan.xsltc.runtime.*;
import org.apache.xalan.xsltc.runtime.Hashtable;

public class StreamHTMLOutput extends StreamOutput {

    private static final String HREF_STR = "href";
    private static final String CITE_STR = "cite";
    private static final String SRC_STR  = "src";

    private static final Hashtable _emptyElements = new Hashtable();
    private static final String[] tags = { "area", "base", "basefont", "br",
					   "col", "frame", "hr", "img", "input",
					   "isindex", "link", "meta", "param" };
    static {
        for (int i = 0; i < tags.length; i++) {
            _emptyElements.put(tags[i], "");
	}
    }

    private boolean _headTagOpen = false;
    private boolean _inStyleScript = false;
    private String  _mediaType     = "text/html";

    public StreamHTMLOutput(StreamOutput output) {
	super(output);
    }

    public StreamHTMLOutput(Writer writer, String encoding) {
	super(writer, encoding);
    }

    public StreamHTMLOutput(OutputStream out, String encoding) 
	throws IOException
    {
	super(out, encoding);
    }

    public void startDocument() throws TransletException { 
    }

    public void endDocument() throws TransletException { 
	outputBuffer();
    }

    public void startElement(String elementName) throws TransletException { 
	if (_startTagOpen) {
	    closeStartTag();
	}

	if (_firstElement) {
	    if (_doctypeSystem != null || _doctypePublic != null) {
		appendDTD(elementName);
	    }
	    _firstElement = false;
	}

	if (_indent) {
	    if (!_emptyElements.containsKey(elementName.toLowerCase())) {
		indent(_lineFeedNextStartTag);
		_lineFeedNextStartTag = true;
		_indentNextEndTag = false;
	    }
	    _indentLevel++;
	}

	_buffer.append('<').append(elementName);
	_startTagOpen = true;
	_indentNextEndTag = false;

	if (elementName.equalsIgnoreCase("head")) {
	    _headTagOpen = true;
	}
	else if (elementName.equalsIgnoreCase("style") || 
		 elementName.equalsIgnoreCase("script")) 
	{
	    _inStyleScript = true;
	}
    }

    public void endElement(String elementName) 
	throws TransletException 
    { 
	if (_inStyleScript && 
	    (elementName.equalsIgnoreCase("style") || 
	     elementName.equalsIgnoreCase("script"))) 
	{
	    _inStyleScript = false;
	}

	if (_startTagOpen) {
	    appendAttributes();
	    if (_emptyElements.containsKey(elementName.toLowerCase())) {
		_buffer.append('>');
	    }
	    else {
		closeStartTag();
		_buffer.append("</").append(elementName).append('>');
	    }
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
    }

    public void characters(String characters)
	throws TransletException 
    { 
	if (_startTagOpen) {
	    closeStartTag();
	}

	if (_escaping && !_inStyleScript) {
	    escapeCharacters(characters.toCharArray(), 0, characters.length());
	}
	else {
	    _buffer.append(characters);
	}
    }

    public void characters(char[] characters, int offset, int length)
	throws TransletException 
    { 
	if (_startTagOpen) {
	    closeStartTag();
	}

	if (_escaping && !_inStyleScript) {
	    escapeCharacters(characters, offset, length);
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
	    Attribute attr;

	    if (name.equalsIgnoreCase(HREF_STR) || 
		name.equalsIgnoreCase(SRC_STR)  || 
		name.equals(CITE_STR)) 
	    {
		attr = new Attribute(name, escapeURL(value));
	    }
	    else {
		attr = new Attribute(name, escapeNonURL(value));
	    }

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
	appendComment(comment);
    }

    public void processingInstruction(String target, String data)
	throws TransletException 
    { 
	if (_startTagOpen) {
	    closeStartTag();
	}

	if (_firstElement) {
	    if (_doctypeSystem != null || _doctypePublic != null) {
		appendDTD("html");
	    }
	    _firstElement = false;
	}

	_buffer.append("<?").append(target).append(' ')
	    .append(data).append('>');
    }

    public boolean setEscaping(boolean escape) throws TransletException 
    { 
	final boolean temp = _escaping;
	_escaping = escape;
	return temp; 
    }

    public void close() { 
	try {
	    _writer.close();
	}
	catch (Exception e) {
	}
    }

    public void namespace(String prefix, String uri) throws TransletException 
    { 
    }

    public void setCdataElements(Hashtable elements) { 
    }

    public void setType(int type) { 
    }

    /**
     * Set the output media type - only relevant for HTML output
     */
    public void setMediaType(String mediaType) {
	_mediaType = mediaType;
    }

    /**
     * Escape non ASCII characters (> u007F) as &#XXX; entities.
     */
    private String escapeNonURL(String base) {
	final int length = base.length();
	final StringBuffer result = new StringBuffer();

        for (int i = 0; i < length; i++){
	    final char ch = base.charAt(i);

	    if ((ch >= '\u007F' && ch < '\u00A0') ||
		(_is8859Encoded && ch > '\u00FF'))
	    {
	        result.append(CHAR_ESC_START)
		      .append(Integer.toString((int) ch))
		      .append(';');
	    }
	    else {
	        result.append(ch); 
	    } 
  	}
	return result.toString();
    }

    /**
     * This method escapes special characters used in HTML attribute values
     */
    private String escapeURL(String base) {
	final char[] chs = base.toCharArray();
	final StringBuffer result = new StringBuffer();

	final int length = chs.length;
        for (int i = 0; i < length; i++) {
	    final char ch = chs[i];

	    if (ch <= 0x20) {
		result.append('%').append(makeHHString(ch));
	    } 
	    else if (ch > '\u007F') {
		result.append('%')
		      .append(makeHHString((ch >> 6) | 0xC0))
		      .append('%')
		      .append(makeHHString((ch & 0x3F) | 0x80));
	    }
	    else {
	        switch (ch) {
		    case '\u007F' :
		    case '\u007B' :
		    case '\u007D' :
		    case '\u007C' :
		    case '\\'     :
		    case '\t'     :
		    case '\u005E' :
		    case '\u007E' :
		    case '\u005B' :
		    case '\u005D' :
		    case '\u0060' :
		    case '\u0020' :
		        result.append('%')
		              .append(Integer.toHexString((int) ch));
		        break;
		    case '"':
			result.append("%22");
			break;
		    default:	
		        result.append(ch); 
			break;
	        }
	    } 
  	}
	return result.toString();
    }

    private String makeHHString(int i) {
	final String s = Integer.toHexString(i).toUpperCase();
	return (s.length() == 1) ? "0" + s : s;
    }

    /**
     * Emit HTML meta info
     */
    private void appendHeader() {
	_buffer.append("<meta http-equiv=\"Content-Type\" content=\"")
	       .append(_mediaType).append("; charset=")
	       .append(_encoding).append("\">");
    }

    protected void closeStartTag() throws TransletException {
	super.closeStartTag();

	if (_headTagOpen) {
	    appendHeader();
	    _headTagOpen = false;
	}
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
	    case '\u00A0':
		_buffer.append(ch, offset, i - offset).append(NBSP);
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
