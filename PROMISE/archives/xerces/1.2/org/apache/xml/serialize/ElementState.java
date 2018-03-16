package org.apache.xml.serialize;


import java.util.Hashtable;


/**
 * Holds the state of the currently serialized element.
 *
 *
 * @version $Revision: 316040 $ $Date: 2000-08-31 02:59:22 +0800 (周四, 2000-08-31) $
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @see BaseMarkupSerializer
 */
class ElementState
{


    /**
     * The element's raw tag name (local or prefix:local).
     */
    String rawName;


    /**
     * The element's local tag name.
     */
    String localName;


    /**
     * The element's namespace URI.
     */
    String namespaceURI;


    /**
     * True if element is space preserving.
     */
    boolean preserveSpace;


    /**
     * True if element is empty. Turns false immediately
     * after serializing the first contents of the element.
     */
    boolean empty;


    /**
     * True if the last serialized node was an element node.
     */
    boolean afterElement;


    /**
     * True if textual content of current element should be
     * serialized as CDATA section.
     */
    boolean doCData;


    /**
     * True if textual content of current element should be
     * serialized as raw characters (unescaped).
     */
    boolean unescaped;


    /**
     * True while inside CData and printing text as CData.
     */
    boolean inCData;


    /**
     * Association between namespace URIs (keys) and prefixes (values).
     */
    Hashtable prefixes;


}
