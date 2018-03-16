package org.apache.xerces.tree;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;


/**
 * This extends the DOM Element interface with features including XML
 * namespace support.  An ID attribute may be visible, and applications
 * may request that memory usage be reduced.
 *
 * <P> There is also support for a single strongly associated object,
 * permitting trees of XML objects to be coupled to other frameworks
 * without requiring either subclassing or external tables to manage
 * such associations.  Such techniques will be required in some cases,
 * perhaps in conjunction with this <em>userObject</em>.
 *
 * @author David Brownell
 * @version $Revision: 315388 $
 */
public interface ElementEx
    extends Element, NodeEx, NamespaceScoped, XmlReadable
{
    /**
     * Returns the value of an element attribute, as named according
     * to the XML Namespaces draft specification.  If there is no
     * such attribute, an empty string is returned.
     *
     * @param uri The namespace for the name; may be null to indicate
     *	the document's default namespace.
     * @param name The "local part" of the name, without a colon.
     * @return the attribute value, or an empty string
     */
    public String getAttribute (String uri, String name);

    /**
     * Returns the value of an element attribute, as named according
     * to the XML Namespaces draft specification.  If there is no
     * such attribute, null is returned.
     *
     * @param uri The namespace for the name; may be null to indicate
     *	the document's default namespace.
     * @param name The "local part" of the name, without a colon.
     * @return the attribute node, or null
     */
    public Attr getAttributeNode (String uri, String name);

    /**
     * Returns the name of the attribute declared to hold the element's ID,
     * or null if no such declaration is known.  This is normally declared
     * in the Document Type Declaration (DTD).  Parsers are not required to
     * parse DTDs, and document trees constructed without a parser may not
     * have access to the DTD, so such declarations may often not be known.
     *
     * <P> ID attributes are used within XML documents to support links
     * using IDREF and IDREFS attributes.  They are also used in current
     * drafts of XPointer and XSL specifications.
     *
     * @return the name of the ID attribute
     */
    public String	getIdAttributeName ();

    /**
     * Returns the object associated with this element.  In cases where
     * more than one such object must be so associated, the association
     * must be maintained externally.
     */
    public Object	getUserObject ();

    /**
     * Assigns an object to be associated with this element.
     */
    public void		setUserObject (Object obj);

    /**
     * Requests that the element minimize the amount of space it uses,
     * to conserve memory.  Children are not affected.
     */
    public void		trimToSize ();
}
