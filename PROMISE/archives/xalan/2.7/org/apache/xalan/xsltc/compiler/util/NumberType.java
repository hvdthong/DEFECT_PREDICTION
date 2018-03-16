package org.apache.xalan.xsltc.compiler.util;


/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
public abstract class NumberType extends Type {
    public boolean isNumber() {
	return true;
    }

    public boolean isSimple() {
	return true;
    }
}

