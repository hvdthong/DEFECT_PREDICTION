package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.DOM;
import org.apache.xml.dtm.DTM;

public interface NodeTest {
    public static final int TEXT      = DTM.TEXT_NODE;
    public static final int COMMENT   = DTM.COMMENT_NODE;
    public static final int PI        = DTM.PROCESSING_INSTRUCTION_NODE;
    public static final int ROOT      = DTM.DOCUMENT_NODE;
    public static final int ELEMENT   = DTM.ELEMENT_NODE;
    public static final int ATTRIBUTE = DTM.ATTRIBUTE_NODE;
    
    public static final int GTYPE     = DTM.NTYPES;
    
    public static final int ANODE     = DOM.FIRST_TYPE - 1;
}
