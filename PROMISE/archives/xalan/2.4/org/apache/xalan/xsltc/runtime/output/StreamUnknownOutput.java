package org.apache.xalan.xsltc.runtime.output;

import java.util.ArrayList;

import java.io.Writer;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.apache.xalan.xsltc.*;
import org.apache.xalan.xsltc.runtime.*;
import org.apache.xalan.xsltc.runtime.Hashtable;

public class StreamUnknownOutput extends StreamOutput {

    private StreamOutput _handler;

    private boolean      _isHtmlOutput = false;
    private boolean      _firstTagOpen = false;
    private boolean      _firstElement = true;
    private String       _firstTagPrefix, _firstTag;

    private ArrayList    _attributes = null;
    private ArrayList    _namespaces = null;

    private String       _mediaType          = null;
    private boolean      _callStartDocument  = false;
    private boolean      _callSetVersion     = false;
    private boolean      _callSetDoctype     = false;

    static class Pair {
	public String name, value;

	public Pair(String name, String value) {
	    this.name = name;
	    this.value = value;
	}
    }

    public StreamUnknownOutput(Writer writer, String encoding) {
	super(writer, encoding);
	_handler = new StreamXMLOutput(writer, encoding);
    }

    public StreamUnknownOutput(OutputStream out, String encoding) 
	throws IOException
    {
	super(out, encoding);
	_handler = new StreamXMLOutput(out, encoding);
    }

    public void startDocument() 
	throws TransletException 
    { 
	_callStartDocument = true;
    }

    public void endDocument() 
	throws TransletException 
    { 
	if (_firstTagOpen) {
	    initStreamOutput();
	}
	else if (_callStartDocument) {
	    _handler.startDocument();
	}
	_handler.endDocument();
    }

    public void startElement(String elementName) 
	throws TransletException 
    { 
	if (_firstElement) {
	    _firstElement = false;

	    _firstTag = elementName;
	    _firstTagPrefix = BasisLibrary.getPrefix(elementName);
	    if (_firstTagPrefix == null) {
		_firstTagPrefix = EMPTYSTRING;
	    }

	    _firstTagOpen = true;
	    _isHtmlOutput = BasisLibrary.getLocalName(elementName)
				        .equalsIgnoreCase("html");
	}
	else {
	    if (_firstTagOpen) {
		initStreamOutput();
	    }
	    _handler.startElement(elementName);
	}
    }

    public void endElement(String elementName) 
	throws TransletException 
    { 
	if (_firstTagOpen) {
	    initStreamOutput();
	}
	_handler.endElement(elementName);
    }

    public void characters(String characters) 
	throws TransletException 
    { 
	if (_firstTagOpen) {
	    initStreamOutput();
	}
	_handler.characters(characters);
    }

    public void characters(char[] characters, int offset, int length)
	throws TransletException 
    { 
	if (_firstTagOpen) {
	    initStreamOutput();
	}
	_handler.characters(characters, offset, length);
    }

    public void attribute(String name, String value)
	throws TransletException 
    { 
	if (_firstTagOpen) {
	    if (_attributes == null) {
		_attributes = new ArrayList();
	    }
	    _attributes.add(new Pair(name, value));
	}
	else {
	    _handler.attribute(name, value);
	}
    }

    public void namespace(String prefix, String uri)
	throws TransletException 
    {
	if (_firstTagOpen) {
	    if (_namespaces == null) {
		_namespaces = new ArrayList();
	    }
	    _namespaces.add(new Pair(prefix, uri));

	    if (_firstTagPrefix.equals(prefix) && !uri.equals(EMPTYSTRING)) {
		_isHtmlOutput = false;
	    }
	}
	else {
	    _handler.namespace(prefix, uri);
	}
    }

    public void comment(String comment) 
	throws TransletException 
    { 
	if (_firstTagOpen) {
	    initStreamOutput();
	}
	_handler.comment(comment);
    }

    public void processingInstruction(String target, String data)
	throws TransletException 
    { 
	if (_firstTagOpen) {
	    initStreamOutput();
	}
	_handler.processingInstruction(target, data);
    }

    public void setDoctype(String system, String pub) {
	_handler.setDoctype(system, pub);

	super.setDoctype(system, pub);
	_callSetDoctype = true;
    }

    /**
     * This method cannot be cached because default is different in 
     * HTML and XML (we need more than a boolean).
     */
    public void setIndent(boolean indent) { 
	_handler.setIndent(indent);
    }

    public void setVersion(String version) { 
	_handler.setVersion(version);

	super.setVersion(version);
	_callSetVersion = true;
    }

    public void omitHeader(boolean value) {
	_handler.omitHeader(value);
    }

    public void setStandalone(String standalone) {
	_handler.setStandalone(standalone);
    }

    public void setMediaType(String mediaType) { 
	_handler.setMediaType(mediaType);
	_mediaType = mediaType;
    }

    public boolean setEscaping(boolean escape) 
	throws TransletException 
    { 
	return _handler.setEscaping(escape);
    }

    public void setCdataElements(Hashtable elements) { 
	_handler.setCdataElements(elements);
    }

    public void setIndentNumber(int value) {
	_handler.setIndentNumber(value);
    }

    private void initStreamOutput() 
	throws TransletException 
    {
	if (_isHtmlOutput) {
	    _handler = new StreamHTMLOutput(_handler);

	    if (_callSetVersion) {
		_handler.setVersion(_version);
	    }
	    if (_callSetDoctype) {
		_handler.setDoctype(_doctypeSystem, _doctypePublic);
	    }
	    if (_mediaType != null) {
		_handler.setMediaType(_mediaType);
	    }
	}

	if (_callStartDocument) {
	    _handler.startDocument();
	    _callStartDocument = false;
	}

	_handler.startElement(_firstTag);

	if (_namespaces != null) {
	    final int n = _namespaces.size();
	    for (int i = 0; i < n; i++) {
		final Pair pair = (Pair) _namespaces.get(i);
		_handler.namespace(pair.name, pair.value);
	    }
	}

	if (_attributes != null) {
	    final int n = _attributes.size();
	    for (int i = 0; i < n; i++) {
		final Pair pair = (Pair) _attributes.get(i);
		_handler.attribute(pair.name, pair.value);
	    }
	}

	_firstTagOpen = false;
    }
}
