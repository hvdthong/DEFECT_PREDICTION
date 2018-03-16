package org.apache.xalan.xsltc;


/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
public interface StripFilter {
    public boolean stripSpace(DOM dom, int node, int type);
}
