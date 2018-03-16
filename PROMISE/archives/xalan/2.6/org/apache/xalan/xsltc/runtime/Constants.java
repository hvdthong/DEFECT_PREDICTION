package org.apache.xalan.xsltc.runtime;

import org.apache.xml.dtm.DTM;

/**
 * This class defines constants used by both the compiler and the 
 * runtime system.
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 */
public interface Constants {

    final static int ANY       = -1;
    final static int ATTRIBUTE = -2;
    final static int ROOT      = DTM.ROOT_NODE;
    final static int TEXT      = DTM.TEXT_NODE;
    final static int ELEMENT   = DTM.ELEMENT_NODE;
    final static int COMMENT   = DTM.COMMENT_NODE;
    final static int PROCESSING_INSTRUCTION = DTM.PROCESSING_INSTRUCTION_NODE;

    public static final String NAMESPACE_FEATURE =

    public static final String EMPTYSTRING = "";
    public static final String XML_PREFIX = "xml";
    public static final String XMLNS_PREFIX = "xmlns";
    public static final String XMLNS_STRING = "xmlns:";
}
