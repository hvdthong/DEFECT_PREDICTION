package org.apache.xerces.tree;

import org.w3c.dom.*;
import org.apache.xerces.tree.XmlNames;

/**
 * This adds the notion of namespaces to the ParentNode class for certain
 * types of nodes such as DOM Element and Attr nodes.
 *
 * @author Edwin Goei
 * @version $Revision: 315388 $
 */
abstract class NamespacedNode extends ParentNode {
    protected String name;
    protected String namespaceURI;

    /**
     * The namespace URI of this node, or <code>null</code> if it is
     * unspecified.  This is not a computed value.
     *
     * @since DOM Level 2
     */
    public String getNamespaceURI() {
        return namespaceURI;
    }

    /**
     * The namespace prefix of this node, or <code>null</code> if it is
     * unspecified.
     *
     * @since DOM Level 2
     */
    public String getPrefix() {
        return XmlNames.getPrefix(name);
    }

    /**
     * Sets the namespace prefix of this node.
     *
     * @since DOM Level 2
     */
    public void setPrefix(String prefix) throws DOMException {
        if (readonly) {
            throw new DomEx(DomEx.NO_MODIFICATION_ALLOWED_ERR);
        }

	int index = name.indexOf(':');

	if (prefix == null) {
	    if (index >= 0) {
	    	name = name.substring(index + 1);
            }
	    return;
	}

   	StringBuffer tmp = new StringBuffer(prefix);
	tmp.append(':');
	if (index < 0 ) {
	    tmp.append(name);
	} else {
	    tmp.append(name.substring(index + 1));
        }
	name = tmp.toString();
    }

    /**
     * Returns the local part of the qualified name of this node.
     *
     * @since DOM Level 2
     */
    public String getLocalName() {
        return XmlNames.getLocalPart(name);
    }
}
