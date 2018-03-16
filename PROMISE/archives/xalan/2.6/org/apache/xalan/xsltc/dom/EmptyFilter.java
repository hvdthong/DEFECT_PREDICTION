package org.apache.xalan.xsltc.dom;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
public final class EmptyFilter implements Filter {
    public boolean test(int node) {
	return true;
    }
}
