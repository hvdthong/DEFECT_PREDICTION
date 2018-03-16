package org.apache.xalan.xsltc.dom;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
public interface Filter {
    public boolean test(int node);
}
