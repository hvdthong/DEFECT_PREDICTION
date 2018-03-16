package org.apache.xerces.tree;

import org.w3c.dom.*;
import org.xml.sax.SAXException;


/**
 * This interface is supported by XML documents and elements which wish to
 * interact with DOM construction during parsing of XML documents.  The
 * parse context which is provided allows elements to determine the URI of
 * the document in which they are found, for interpreting relative URIs.
 * It also supports providing application level diagnostics for faulty
 * input.
 *
 * <P> When these methods are called, parent context is available for
 * elements so that "inherited" attributes may be queried, as well as
 * other information such as the types of any containing elements.
 *
 * @author David Brownell
 * @version $Revision: 315388 $
 */
public interface XmlReadable
{
    /**
     * This is called before object children are parsed.  For elements,
     * this is a natural time to perform tasks which relate to element
     * attributes, such as application level integrity checks or
     * associating them with object properties.
     */
    void startParse (ParseContext context)
    throws SAXException;

    /**
     * This is called when each child element has been 
     * fully constructed.  The object may choose to represent the
     * child's information in a manner which is more appropriate to a
     * particular application, or even discard that information if it
     * is not currently needed.  For example, this is a good time for
     * elements discard ignorable whitespace, filter out elements not matching
     * some search criteria, or map certain elements to object properties.
     */
    void doneChild (NodeEx newChild, ParseContext context)
    throws SAXException;

    /**
     * This is called when the object has been fully parsed, sometime after
     * startParse.  It is a natural time to perform tasks which relate to
     * all children, such as verifying application level integrity constraints
     * or associating an appropriate <em>userObject</em> with this element.
     * Documents may wish to perform ID/IDREF link fixup, or similar tasks.
     */
    void doneParse (ParseContext context)
    throws SAXException;
}
