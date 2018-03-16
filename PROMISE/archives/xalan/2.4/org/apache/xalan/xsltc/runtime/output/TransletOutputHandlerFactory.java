package org.apache.xalan.xsltc.runtime.output;

import java.io.Writer;
import java.io.IOException;
import java.io.OutputStream;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;
import org.apache.xalan.xsltc.runtime.*;
import org.apache.xalan.xsltc.TransletException;
import org.apache.xalan.xsltc.TransletOutputHandler;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xalan.xsltc.trax.SAX2DOM;

public class TransletOutputHandlerFactory {

    public static final int STREAM = 0;
    public static final int SAX    = 1;
    public static final int DOM    = 2;

    private String _encoding       = "utf-8";
    private String _method         = null;
    private int    _outputType     = STREAM;
    private OutputStream _ostream  = System.out;
    private Writer _writer         = null;
    private Node           _node   = null;
    private int _indentNumber      = -1;
    private ContentHandler _handler    = null;
    private LexicalHandler _lexHandler = null;

    static public TransletOutputHandlerFactory newInstance() {
	return new TransletOutputHandlerFactory();
    }

    public void setOutputType(int outputType) {
	_outputType = outputType;
    }

    public void setEncoding(String encoding) {
	if (encoding != null) {
	    _encoding = encoding;
	}
    }

    public void setOutputMethod(String method) {
	_method = method;
    }

    public void setOutputStream(OutputStream ostream) {
	_ostream = ostream;
    }

    public void setWriter(Writer writer) {
	_writer = writer;
    }

    public void setHandler(ContentHandler handler) {
        _handler = handler;
    }

    public void setLexicalHandler(LexicalHandler lex) {
	_lexHandler = lex;
    }

    public void setNode(Node node) {
	_node = node;
    }

    public Node getNode() {
	return (_handler instanceof SAX2DOM) ? ((SAX2DOM)_handler).getDOM() 
	   : null;
    }

    public void setIndentNumber(int value) {
	_indentNumber = value;
    }

    public TransletOutputHandler getTransletOutputHandler() 
	throws IOException, ParserConfigurationException 
    {
	switch (_outputType) {
	    case STREAM:
		StreamOutput result = null;

		if (_method == null) {
		    result = (_writer == null) ? 
			new StreamUnknownOutput(_ostream, _encoding) :
			new StreamUnknownOutput(_writer, _encoding);
		}
		else if (_method.equalsIgnoreCase("xml")) {
		    result = (_writer == null) ? 
			new StreamXMLOutput(_ostream, _encoding) :
			new StreamXMLOutput(_writer, _encoding);
		}
		else if (_method.equalsIgnoreCase("html")) {
		    result = (_writer == null) ? 
			new StreamHTMLOutput(_ostream, _encoding) :
			new StreamHTMLOutput(_writer, _encoding);
		}
		else if (_method.equalsIgnoreCase("text")) {
		    result = (_writer == null) ? 
			new StreamTextOutput(_ostream, _encoding) :
			new StreamTextOutput(_writer, _encoding);
		}

		if (result != null && _indentNumber >= 0) {
		    result.setIndentNumber(_indentNumber);
		}
		return result;
	    case DOM:
		_handler = (_node != null) ? new SAX2DOM(_node) : 
					     new SAX2DOM();
		_lexHandler = (LexicalHandler)_handler;
	    case SAX:
		if (_method == null) {
		}

		if (_method.equalsIgnoreCase("xml")) {
		    return (_lexHandler == null) ? 
			new SAXXMLOutput(_handler, _encoding) :
			new SAXXMLOutput(_handler, _lexHandler, _encoding);
		}
		else if (_method.equalsIgnoreCase("html")) {
		    return (_lexHandler == null) ? 
			new SAXHTMLOutput(_handler, _encoding) :
			new SAXHTMLOutput(_handler, _lexHandler, _encoding);
		}
		else if (_method.equalsIgnoreCase("text")) {
		    return (_lexHandler == null) ? 
			new SAXTextOutput(_handler, _encoding) :
			new SAXTextOutput(_handler, _lexHandler, _encoding);
		}
	    break;
	}
	return null;
    }

    public TransletOutputHandler getOldTransletOutputHandler() throws IOException {
	DefaultSAXOutputHandler saxHandler =
	    new DefaultSAXOutputHandler(_ostream, _encoding);
	return new TextOutput((ContentHandler)saxHandler,
			      (LexicalHandler)saxHandler, _encoding);
    }
}
