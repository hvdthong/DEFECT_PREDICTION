package org.apache.xalan.xsltc.compiler;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
class IllegalCharException extends Exception {
    static final long serialVersionUID = -667236676706226266L;
    public IllegalCharException(String s) {
	super(s);
    }
}
