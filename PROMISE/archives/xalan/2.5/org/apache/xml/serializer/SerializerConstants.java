package org.apache.xml.serializer;

/**
 * @author minchau
 *
 * Constants used in serialization, such as the string "xmlns"
 */
public interface SerializerConstants
{

    /** To insert ]]> in a CDATA section by ending the last CDATA section with
     * ]] and starting the next CDATA section with >
     */
    static final String CDATA_CONTINUE = "]]]]><![CDATA[>";
    /**
     * The constant "]]>"
     */
    static final String CDATA_DELIMITER_CLOSE = "]]>";
    static final String CDATA_DELIMITER_OPEN = "<![CDATA[";

    static final char[] CNTCDATA = CDATA_CONTINUE.toCharArray();
    static final char[] BEGCDATA = CDATA_DELIMITER_OPEN.toCharArray();
    static final char[] ENDCDATA = CDATA_DELIMITER_CLOSE.toCharArray();

    static final String EMPTYSTRING = "";

    static final String ENTITY_AMP = "&amp;";
    static final String ENTITY_CRLF = "&#xA;";
    static final String ENTITY_GT = "&gt;";
    static final String ENTITY_LT = "&lt;";
    static final String ENTITY_QUOT = "&quot;";

    static final String XML_PREFIX = "xml";
    static final String XMLNS_PREFIX = "xmlns";
   
    public static final String DEFAULT_SAX_SERIALIZER="org.apache.xml.serializer.ToXMLSAXHandler";
}
