package org.apache.xalan.xsltc.compiler.util;

import org.apache.xalan.xsltc.compiler.SyntaxTreeNode;

public class TypeCheckError extends Exception {
    ErrorMsg _error = null;
    SyntaxTreeNode _node = null;
	
    public TypeCheckError(SyntaxTreeNode node) {
	super();
	_node = node;
    }

    public TypeCheckError(ErrorMsg error) {
	super();
	_error = error;
    }
	
    public TypeCheckError(int code, Object param) {
	super();
	_error = new ErrorMsg(code, param);
    }

    public TypeCheckError(int code, Object param1, Object param2) {
	super();
	_error = new ErrorMsg(code, param1, param2);
    }

    public ErrorMsg getErrorMsg() {
        return _error;
    }

    public String toString() {
	String result;

	if (_error != null) {
	    result = _error.toString();
	}
	else if (_node != null) {
	    result = "Type check error in " + _node.toString() + ".";
	}
	else {
	    result = "Type check error (no line information).";
	}

	return result;
    }
}
