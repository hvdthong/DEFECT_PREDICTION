package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.*;
import org.apache.xalan.xsltc.runtime.AbstractTranslet;

public interface CurrentNodeListFilter {
    public abstract boolean test(int node, int position, int last, int current,
				 AbstractTranslet translet, NodeIterator iter);
}
