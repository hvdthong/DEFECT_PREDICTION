package org.apache.xalan.xsltc.runtime;

import org.apache.xalan.xsltc.DOM;

/**
 * This class defines constants used by both the compiler and the 
 * runtime system.
 */
public interface Constants {

    final static int ANY       = -1;
    final static int ATTRIBUTE = -2;
    final static int ROOT      = DOM.ROOT;
    final static int TEXT      = DOM.TEXT;
    final static int ELEMENT   = DOM.ELEMENT;
    final static int COMMENT   = DOM.COMMENT;
    final static int PROCESSING_INSTRUCTION = DOM.PROCESSING_INSTRUCTION;

    public static final String NAMESPACE_FEATURE =

    public static final String EMPTYSTRING = "";
    public static final String XML_PREFIX = "xml";
    public static final String XMLNS_PREFIX = "xmlns";
    public static final String XMLNS_STRING = "xmlns:";
}
