package org.apache.xerces.tree;

import org.w3c.dom.Node;


/**
 * This interface defines accessors to inherited attributes of nodes,
 * and provides support for using XML Namespaces.
 *
 * @author David Brownell
 * @version $Revision: 315388 $
 */
public interface NodeEx extends Node, XmlWritable
{
    /**
     * Returns the value of a given attribute, tracing up through
     * ancestors if needed.  In the XML standard, two attributes are
     * inherited:  <em>xml:lang</em> and <em>xml:space</em>.  A very
     * similar mechanism is involved with Cascading Style Sheets (CSS).
     * XML Namespaces also use inheritance, using attributes with
     * names like <em>xmlns:foo</em> to declare namespace prefixes.
     *
     * @param name The name of the attribute to be found.  Colons in
     *	this are ignored.
     * @return the value of the identified attribute, or null if no
     *	such attribute is found.
     */
    public String getInheritedAttribute (String name);

    /**
     * Returns the value of a given attribute, tracing up through
     * ancestors if needed and conforming to the XML Namespaces
     * draft for associating URIs with name prefixes.
     *
     * @param uri The namespace for the name; may be null to indicate
     *	the document's default namespace.
     * @param name The "local part" of the name, without a colon.
     * @return the value of the identified attribute, or null if no
     *	such attribute is found.
     */
    public String getInheritedAttribute (String uri, String name);

    /**
     * Returns the language id (value of <code>xml:lang</code>
     * attribute) applicable to this node, if known.  Traces up
     * through ancestors as needed.
     * @return the value of the <em>xml:lang</em> attribute, or
     *  null if no such attribute is found.
     */
    public String getLanguage ();

    /**
     * Returns the index of the node in the list of children, such
     * that <em>item()</em> will return that child.
     *
     * @param maybeChild the node which may be a child of this one
     * @return the index of the node in the set of children, or
     *	else -1 if that node is not a child of this node.
     */
    public int getIndexOf (Node maybeChild);

    /**
     * Sets the node to be readonly; applies recursively to the children
     * of this node if the parameter is true.
     *
     * @param deep If <code> true </code> recursively set the nodes in the
     * subtree under the current node to be read only.
     * If <code> false </code> then set only the current node to be
     * readonly
     */
    public void setReadonly (boolean deep);

    /**
     * Method to allow easy determination of whether a node is read only.
     *
     * @return <code> true </code> if the node is read only
     */
    public boolean isReadonly ();
}
