package org.apache.xalan.xsltc;

import org.xml.sax.SAXException;

public final class TransletException extends SAXException {

    public TransletException() {
	super("Translet error");
    }
    
    public TransletException(Exception e) {
	super(e.toString());
    }
    
    public TransletException(String message) {
	super(message);
    }
}
