package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.DOM;

public interface NodeTest {
    public static final int TEXT      = DOM.TEXT;
    public static final int COMMENT   = DOM.COMMENT;
    public static final int PI        = DOM.PROCESSING_INSTRUCTION;
    public static final int ROOT      = DOM.ROOT;
    public static final int ELEMENT   = DOM.ELEMENT;
    public static final int ATTRIBUTE = DOM.ATTRIBUTE;
    
    public static final int GTYPE     = DOM.NTYPES;
    
    public static final int ANODE     = DOM.FIRST_TYPE - 1;
}
