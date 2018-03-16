package org.apache.xml.serialize;


import java.util.Hashtable;


/**
 * Holds the state of the currently serialized element.
 *
 *
 * @version $Revision: 317383 $ $Date: 2001-07-21 04:37:06 +0800 (周六, 2001-07-21) $
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @see BaseMarkupSerializer
 */
public class ElementState
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
     * True if the last serialized node was a comment node.
     */
    boolean afterComment;


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
