package org.apache.xalan.xsltc.dom;

import org.apache.xalan.xsltc.DOM;

public interface StripWhitespaceFilter {
    public boolean stripSpace(DOM dom, int node, int type);
}
