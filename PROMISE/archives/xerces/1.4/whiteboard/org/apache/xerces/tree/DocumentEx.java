package org.apache.xerces.tree;

import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * This interface extends the DOM Document model in several useful ways.
 * It supports:  <UL>
 *
 * <LI> Application-specialized element creation and document customization
 *	at parse time;
 * <LI> Document printing;
 * <LI> The URI with which the document is associated;
 * <LI> Access to elements by their XML "ID" attributes (when the
 *	document was constructed with an appropriate XML parser);
 * <LI> Application-level control over the language in which
 *      diagnostics are provided (useful for multi-language applications
 *	such as servers);
 * <LI> Moving nodes between DOM Documents.  (DOM Level 1 only talks
 *	about nodes that are coupled to a single DOM document.)
 *
 * </UL>
 *
 *
 * @author David Brownell
 * @version $Revision: 315388 $
 */
public interface DocumentEx
    extends Document, ElementFactory,
	XmlReadable, XmlWritable
{
    /**
     * Returns the system ID (a URI) associated with the document,
     * or null if this is unknown.
     */
    public String getSystemId ();


    /**
     * Assigns the element factory to be used by this document.
     */
    public void setElementFactory (ElementFactory factory);


    /**
     * Returns the element factory to be used by this document.
     */
    public ElementFactory getElementFactory ();


    /**
     * Returns the element whose ID is given by the parameter; or null
     * if no such element is known.  Element IDs are declared by
     * attributes of type "ID", and are commonly used for internal
     * linking by using attributes of type IDREF or IDREFS to turn
     * XML's hierarchical data structure into a directed graph.
     *
     * <P> Note that DOM itself provides no way to identify which element
     * attributes are declared with the "ID" attribute type.  This feature
     * relies on interfaces which may not be publicly exposed, such as
     * XML processors telling a DOM builder about those attributes.
     *
     * @param id The value of the ID attribute which will be matched
     *	by any element which is returned. 
     */
    public ElementEx getElementExById (String id);


    /**
     * Returns the locale to be used for diagnostic messages.
     */
    public Locale	getLocale ();


    /**
     * Assigns the locale to be used for diagnostic messages.
     * Multi-language applications, such as web servers dealing with
     * clients from different locales, need the ability to interact
     * with clients in languages other than the server's default.
     * When a Document is created, its locale is the default
     * locale for the virtual machine.
     *
     * @see #chooseLocale
     */
    public void	setLocale (Locale locale);


    /**
     * Chooses a client locale to use for diagnostics, using the first
     * language specified in the list that is supported by this DOM
     * implementation. That locale is then automatically assigned using <a
     * href="#setLocale(java.util.Locale)">setLocale()</a>.  Such a list
     * could be provided by a variety of user preference mechanisms,
     * including the HTTP <em>Accept-Language</em> header field.
     *
     * @see org.apache.xerces.tree.MessageCatalog
     *
     * @param languages Array of language specifiers, ordered with the most
     *	preferable one at the front.  For example, "en-ca" then "fr-ca",
     *  followed by "zh_CN".  Both RFC 1766 and Java styles are supported.
     * @return The chosen locale, or null.
     */
    public Locale chooseLocale (String languages []);


    /**
     * Changes the "owner document" of the given node, and all child
     * and associated attribute nodes, to be this document.  If the
     * node has a parent, it is first removed from that parent.
     * 
     * @param node the node whose "owner" will be changed.
     * @exception DOMException WRONG_DOCUMENT_ERROR when attempting
     *	to change the owner for some other DOM implementation<P>
     *	HIERARCHY_REQUEST_ERROR when the node is a document, document
     *	type, entity, or notation; or when it is an attribute associated
     *  with an element whose owner is not being (recursively) changed.
     */
    public void changeNodeOwner (Node node);
}
