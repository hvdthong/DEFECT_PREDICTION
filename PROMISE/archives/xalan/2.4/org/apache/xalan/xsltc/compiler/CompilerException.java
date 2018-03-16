package org.apache.xalan.xsltc.compiler;

public final class CompilerException extends Exception {

    private String _msg;

    public CompilerException() {
	super();
    }
    
    public CompilerException(Exception e) {
	super(e.toString());
	_msg = e.toString(); 
    }
    
    public CompilerException(String message) {
	super(message);
	_msg = message;
    }

    public String getMessage() {
	final int col = _msg.indexOf(':');

	if (col > -1)
	    return(_msg.substring(col));
	else
	    return(_msg);
    }
}
