package org.apache.xalan.xsltc.runtime.output;

import java.util.Stack;
import java.io.IOException;

import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.runtime.BasisLibrary;
import org.apache.xalan.xsltc.runtime.AttributeList;

public class SAXHTMLOutput extends SAXOutput { 

    public SAXHTMLOutput(ContentHandler handler, String encoding) 
	throws IOException 
    {
	super(handler, encoding);
    }

    public SAXHTMLOutput(ContentHandler handler, LexicalHandler lex, 
	String encoding) throws IOException
    {
	super(handler, lex, encoding);
    }
   
    public void endDocument() throws TransletException {
        try {
            if (_startTagOpen) {
		closeStartTag();
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

            if (_lexHandler != null) {
                if ((_doctypeSystem != null) || (_doctypePublic != null))
                    _lexHandler.startDTD(elementName,
                             _doctypePublic,_doctypeSystem);
                _lexHandler = null;
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
     * End an element or CDATA section in the output document
     */
    public void endElement(String elementName) throws TransletException {
        try {
            if (_startTagOpen) {
		closeStartTag();
	    }
            _saxHandler.endElement(EMPTYSTRING, elementName, elementName);
        } 
	catch (SAXException e) {
            throw new TransletException(e);
        }

    }

    public void attribute(String name, final String value) 
	throws TransletException
    {
	if (_startTagOpen) {
	    final String patchedName = patchName(name);
	    final String localName = getLocalName(patchedName);
	    final int index = _attributes.getIndex(name); 

	    if (index >= 0) {
		_attributes.setAttribute(index, EMPTYSTRING, localName,
			name, "CDATA", value);
	    }
	    else {
		_attributes.addAttribute(EMPTYSTRING, localName,
                name, "CDATA", value);
	    }
	}
    }

    /**
    * Send characters to the output document
    */
    public void characters(char[] ch, int off, int len)
        throws TransletException 
    {
	try {
            if (_startTagOpen) closeStartTag();
            _saxHandler.characters(ch, off, len);
        }
        catch (SAXException e) {
            throw new TransletException(e);
        }
    }

    /**
     * This method is called when all the data needed for a call to the
     * SAX handler's startElement() method has been gathered.
     */
    protected void closeStartTag() throws TransletException {
        try {
            _startTagOpen = false;

            _saxHandler.startElement(EMPTYSTRING, _elementName, 
		_elementName, _attributes);
        }
        catch (SAXException e) {
            throw new TransletException(e);
        }
    }
}
