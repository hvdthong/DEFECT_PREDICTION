package org.apache.xerces.tree;

import org.w3c.dom.Node;


/**
 * This interface is supported by elements and attributes whose names
 * are scoped according to the XML Namespaces specification.
 *
 * @author David Brownell
 * @version $Revision: 315388 $
 */
public interface NamespaceScoped extends NodeEx
{
    /**
     * Returns the "local part" of the object's scoped name, without
     * any namespace prefix.
     */
    public String	getLocalName ();

    /**
     * Return the XML namespace name (a URI) associated with this object,
     * or null for the case of the default document namespace.  This is
     * computed from this node and its ancestors.
     *
     * @exception IllegalStateException Thrown when the namespace
     *	prefix for this element is not known.
     */
    public String	getNamespace ();

    /**
     * Returns any prefix of the object's name.  This is only a
     * context-sensitive alias for the namespace URI.  If the name
     * is unqualified (e.g. <em>template</em> vs <em>xsl:template</em>),
     * the null string is returned.
     *
     * <P> The URI corresponding to that prefix may be retrieved by
     * getting the inherited value of the attribute named
     * <em>"xmlns:" + getPrefix ()</em> if the prefix is not null;
     * if there is no value for this URI, that indicates an error.
     * If the prefix is null, the URI is the inherited value of the
     * attribute named <em>"xmlns"</em>; if there is no value for
     * that URI, the default namespace has not been defined.
     *
     * @see #setPrefix
     */
    public String	getPrefix ();

    /**
     * Assigns a prefix to be used for the object's name.  This method
     * should be used with care, primarily to "patch up" elements after
     * they have been moved to a context where the correct namespace may
     * call for a different prefix.  This method does not check whether
     * the prefix is declared.  The return value of <em>getNodeName</em>
     * may change, if this prefix was not the one being used.
     *
     * <P> To assign the URI associated with this prefix, declare the
     * prefix by defining a value for the <em>"xmlns" + prefix</em>
     * attribute for this node's element or an ancestor element.  For
     * the null prefix, give a value for the <em>"xmlns"</em> attribute
     * instead.  All non-null prefixes must be declared.
     *
     * @see #getPrefix
     *
     * @param prefix null to remove any prefix, otherwise the unqualified
     *	name prefix to be be used.
     */
    public void		setPrefix (String prefix);

}
