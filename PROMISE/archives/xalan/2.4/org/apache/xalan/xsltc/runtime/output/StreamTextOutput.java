package org.apache.xalan.xsltc.runtime.output;

import java.io.Writer;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.xalan.xsltc.TransletException;

public class StreamTextOutput extends StreamOutput {

    public StreamTextOutput(Writer writer, String encoding) {
	super(writer, encoding);
    }

    public StreamTextOutput(OutputStream out, String encoding) 
	throws IOException
    {
	super(out, encoding);
    }

    public void startDocument() throws TransletException { 
    }

    public void endDocument() throws TransletException { 
	outputBuffer();
    }

    public void startElement(String elementName) 
	throws TransletException 
    {
    }

    public void endElement(String elementName) 
	throws TransletException 
    {
    }

    public void characters(String characters) 
	throws TransletException 
    { 
	_buffer.append(characters);
    }

    public void characters(char[] characters, int offset, int length)
	throws TransletException 
    { 
	_buffer.append(characters, offset, length);
    }

    public void comment(String comment) throws TransletException {
    }

    public void attribute(String name, String value) 
	throws TransletException 
    {
    }

    public void processingInstruction(String target, String data) 
	throws TransletException
    {
    }
}

