package org.apache.xalan.xsltc.compiler;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
final class ArgumentList {
    private final Expression   _arg;
    private final ArgumentList _rest;
	
    public ArgumentList(Expression arg, ArgumentList rest) {
	_arg = arg;
	_rest = rest;
    }
		
    public String toString() {
	return _rest == null
	    ? _arg.toString()
	    : _arg.toString() + ", " + _rest.toString();
    }
}
