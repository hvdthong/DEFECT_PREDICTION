package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.runtime.AbstractTranslet;
import org.apache.xml.dtm.DTMAxisIterator;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 */
public interface CurrentNodeListFilter {
    public abstract boolean test(int node, int position, int last, int current,
				 AbstractTranslet translet, DTMAxisIterator iter);
}
