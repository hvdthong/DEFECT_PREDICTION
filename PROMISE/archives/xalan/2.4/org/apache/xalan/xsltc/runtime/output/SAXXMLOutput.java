package org.apache.xalan.xsltc.runtime.output;

import java.util.Stack;
import java.io.IOException;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.runtime.Hashtable;
import org.xml.sax.SAXException;
import org.apache.xalan.xsltc.runtime.BasisLibrary;

public class SAXXMLOutput extends SAXOutput {

    private static final char[] BEGCDATA = "<![CDATA[".toCharArray();
    private static final char[] ENDCDATA = "]]>".toCharArray();
    private static final char[] CNTCDATA = "]]]]><![CDATA[>".toCharArray();

    public SAXXMLOutput(ContentHandler handler, String encoding) 
	throws IOException 
    {
    	super(handler, encoding);
	initCDATA();
	initNamespaces();
    }

    public SAXXMLOutput(ContentHandler handler, LexicalHandler lex, 
        String encoding) throws IOException
    {
        super(handler, lex, encoding);
	initCDATA();
	initNamespaces();
    }

    public void endDocument() throws TransletException {
	try {
            if (_startTagOpen) {
		closeStartTag();
	    }
            else if (_cdataTagOpen) {
		closeCDATA();
	    }

            _saxHandler.endDocument();
        } 
	catch (SAXException e) {
            throw new TransletException(e);
        }
    }

    /**
     * Start an element in the output document. This might be an XML
     * element (<elem>data</elem> type) or a CDATA section.
     */
    public void startElement(String elementName) throws TransletException {
	try {
            if (_startTagOpen) {
		closeStartTag();
	    }
            else if (_cdataTagOpen) {
		closeCDATA();
	    }

            if (_firstElement) {
                if (_doctypeSystem != null) {
                    _lexHandler.startDTD(elementName, _doctypePublic,
			_doctypeSystem);
		}
                _firstElement = false;
            }

            _depth++;
            _elementName = elementName;
            _attributes.clear();
            _startTagOpen = true;
	}
        catch (SAXException e) {
            throw new TransletException(e);
        }
    }

    /**
     * Put an attribute and its value in the start tag of an element.
     * Signal an exception if this is attempted done outside a start tag.
     */
    public void attribute(String name, final String value)
        throws TransletException 
    {
	if (_startTagOpen) {
	    final String patchedName = patchName(name);
	    final String localName = getLocalName(patchedName);
	    final String uri = getNamespaceURI(patchedName, false);

	    final int index = (localName == null) ?
		    _attributes.getIndex(name) :    /* don't use patchedName */
		    _attributes.getIndex(uri, localName);

		_attributes.setAttribute(index, uri, localName,
		    patchedName, "CDATA", value);
	    }
	    else {
		_attributes.addAttribute(uri, localName, patchedName,
		    "CDATA", value);
	    }
	}
    }

    public void characters(char[] ch, int off, int len)
        throws TransletException 
    {

	try {
            if (_startTagOpen) {
		closeStartTag();
	    }

            Integer I = (Integer)_cdataStack.peek();
            if ((I.intValue() == _depth) && (!_cdataTagOpen)) {
                startCDATA(ch, off, len);
            }
            else {
                _saxHandler.characters(ch, off, len);
            }
	}
        catch (SAXException e) {
            throw new TransletException(e);
        }
    }

    public void endElement(String elementName) throws TransletException {
        try {
            if (_startTagOpen) {
		closeStartTag();
	    }
            else if (_cdataTagOpen) {
		closeCDATA();
	    }

            _saxHandler.endElement(getNamespaceURI(elementName, true),
                getLocalName(elementName), elementName);

            popNamespaces();
            if (((Integer)_cdataStack.peek()).intValue() == _depth){
                _cdataStack.pop();
	    }
            _depth--;

    	} 
	catch (SAXException e) {
            throw new TransletException(e);
    	}
    }

    /**
     * Send a namespace declaration in the output document. The namespace
     * declaration will not be include if the namespace is already in scope
     * with the same prefix.
     */
    public void namespace(final String prefix, final String uri)
        throws TransletException 
    {
	if (_startTagOpen) {
	    pushNamespace(prefix, uri);
	}
	else {
	    if ((prefix == EMPTYSTRING) && (uri == EMPTYSTRING)) return;
	    BasisLibrary.runTimeError(BasisLibrary.STRAY_NAMESPACE_ERR,
				      prefix, uri);
	}
    }

    /**
     * Send a processing instruction to the output document
     */
    public void processingInstruction(String target, String data)
        throws TransletException {
        try {
            if (_startTagOpen) {
		closeStartTag();
	    }
            else if (_cdataTagOpen) {
		closeCDATA();
	    }

            _saxHandler.processingInstruction(target, data);
        }
        catch (SAXException e) {
            throw new TransletException(e);
        }
    }

    /**
     * Declare a prefix to point to a namespace URI. Inform SAX handler
     * if this is a new prefix mapping.
     */
    protected boolean pushNamespace(String prefix, String uri) {
	try {
	    if (super.pushNamespace(prefix, uri)) {
		_saxHandler.startPrefixMapping(prefix, uri);
		return true;
	    }
	} 
	catch (SAXException e) {
	}
	return false;
    }

    /**
     * Undeclare the namespace that is currently pointed to by a given 
     * prefix. Inform SAX handler if prefix was previously mapped.
     */
    protected boolean popNamespace(String prefix) {
	try {
	    if (super.popNamespace(prefix)) {
		_saxHandler.endPrefixMapping(prefix);
		return true;
	    }
	}
	catch (SAXException e) {
	}
	return false;
    }

    /**
     * This method is called when all the data needed for a call to the
     * SAX handler's startElement() method has been gathered.
     */
    protected void closeStartTag() throws TransletException {
        try {
            _startTagOpen = false;

	    final String localName = getLocalName(_elementName);
	    final String uri = getNamespaceURI(_elementName, true);

            _saxHandler.startElement(uri, localName, _elementName, 
		_attributes);

	    if (_cdata != null) {
		final StringBuffer expandedName = (uri == EMPTYSTRING) ? 
		    new StringBuffer(_elementName) :
		    new StringBuffer(uri).append(':').append(localName);

		if (_cdata.containsKey(expandedName.toString())) {
		    _cdataStack.push(new Integer(_depth));
		}
	    }
        }
        catch (SAXException e) {
            throw new TransletException(e);
        }
    }

    public void startCDATA() throws TransletException {
	try {
	    _saxHandler.characters(BEGCDATA, 0, BEGCDATA.length);
	    _cdataTagOpen = true;
	}
	catch (SAXException e) {
            throw new TransletException(e);
	}
    }

    public void closeCDATA() throws TransletException {
	try {
	    _saxHandler.characters(ENDCDATA, 0, ENDCDATA.length);
	    _cdataTagOpen = false;
	}
	catch (SAXException e) {
            throw new TransletException(e);
	}
    }

    /**
     * Utility method - pass a whole charactes as CDATA to SAX handler
     */
    private void startCDATA(char[] ch, int off, int len) throws SAXException {
        final int limit = off + len;
        int offset = off;

        _saxHandler.characters(BEGCDATA, 0, BEGCDATA.length);

        for (int i = offset; i < limit - 2; i++) {
            if (ch[i] == ']' && ch[i + 1] == ']' && ch[i + 2] == '>') {
                _saxHandler.characters(ch, offset, i - offset);
                _saxHandler.characters(CNTCDATA, 0, CNTCDATA.length);
                offset = i+3;
            }
        }

        if (offset < limit) {
	    _saxHandler.characters(ch, offset, limit - offset);
	}	    
        _cdataTagOpen = true;
    }
}

