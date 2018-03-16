package org.apache.xalan.xsltc;

import org.apache.xalan.xsltc.DOM;

public interface StripFilter {
    public boolean stripSpace(DOM dom, int node, int type);
}
