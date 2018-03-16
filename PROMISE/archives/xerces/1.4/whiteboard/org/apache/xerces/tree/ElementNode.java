package org.apache.xerces.tree;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;

import java.util.Enumeration;

import org.apache.xerces.tree.XmlNames;

import org.w3c.dom.*;


/**
 * This class represents XML elements in a parse tree, and is often
 * subclassed to add custom behaviors.  When an XML Document object
 * is built using an <em>XmlDocumentBuilder</em> instance, simple
 * declarative configuration information may be used to control whether
 * this class, or some specialized subclass (e.g. supporting HTML DOM
 * methods) is used for elements in the resulting tree.
 *
 * <P> As well as defining new methods to provide behaviors which are
 * specific to application frameworks, such as Servlets or Swing, such
 * subclasses may also override methods such as <em>doneParse</em>
 * and <em>appendChild</em> to perform some kinds of processing during
 * tree construction.  Such processing can include transforming tree
 * structure to better suit the needs of a given application.  When
 * such transformation is done, the <em>XmlWritable</em> methods
 * may need to be overridden to make elements transform themselves back
 * to XML without losing information.  (One common transformation is
 * eliminating redundant representations of data; attributes of an XML
 * element may correspond to defaultable object properties, and so on.)
 *
 * <P> Element nodes also support a single <em>userObject</em> property,
 * which may be used to bind objects to elements where subclassing is
 * either not possible or is inappropriate.  For example, user interface
 * objects often derive from <code>java.awt.Component</code>, so that
 * they can't extend a different class (<em>ElementNode</em>).
 *
 * @see XmlDocumentBuilder
 *
 * @author David Brownell
 * @version $Revision: 315677 $
 */
public class ElementNode extends NamespacedNode implements ElementEx
{
    private AttributeSet	attributes;
    private String		idAttributeName;
    private Object		userObject;

    private static final char	tagStart [] = { '<', '/' };
    private static final char	tagEnd [] = { ' ', '/', '>' };
    private static final String
    
    /**
     * Partially constructs an element; its tag will be assigned
     * by the factory (or subclass), while attributes and the
     * parent (and implicitly, siblings) will assigned when it is
     * joined to a DOM document.
     */
    public ElementNode () { }

    public ElementNode(String namespaceURI, String qualifiedName)
        throws DomEx
    {
        checkArguments(namespaceURI, qualifiedName);

        name = qualifiedName;
        this.namespaceURI = namespaceURI;
    }

    private void checkArguments(String namespaceURI, String qualifiedName)
        throws DomEx
    {

	if (qualifiedName == null) {
            throw new DomEx(DomEx.NAMESPACE_ERR);
        }

	int first = qualifiedName.indexOf(':');

        if (first <= 0) {
            if (!XmlNames.isUnqualifiedName(qualifiedName)) {
                throw new DomEx(DomEx.INVALID_CHARACTER_ERR);
            }
            return;
        }


	int last = qualifiedName.lastIndexOf(':');
	if (last != first) {
            throw new DomEx(DomEx.NAMESPACE_ERR);
        }
	
        String prefix = qualifiedName.substring(0, first);
        String localName = qualifiedName.substring(first + 1);
	if (!XmlNames.isUnqualifiedName(prefix)
                || !XmlNames.isUnqualifiedName(localName)) {
            throw new DomEx(DomEx.INVALID_CHARACTER_ERR);
        }

        if (namespaceURI == null || namespaceURI.equals("")
                || (prefix.equals("xml") &&
                    !namespaceURI.equals(DEFAULT_XML_NS_URI))) {
            throw new DomEx(DomEx.NAMESPACE_ERR);
        }
    }

    public void trimToSize ()
    {
	super.trimToSize ();
	if (attributes != null)
	    attributes.trimToSize ();
    }

    /**
     * Assigns the element's name, when the element has been
     * constructed using the default constructor.  For use by
     * element factories potentially by custom subclasses. 
     */
    protected void setTag (String t) { name = t; }

    void setAttributes (AttributeSet a)
    {
	AttributeSet oldAtts = attributes;

	if (oldAtts != null && oldAtts.isReadonly ())
	    throw new DomEx (DomEx.NO_MODIFICATION_ALLOWED_ERR);

	if (a != null)
	    a.setNameScope (this);
	attributes = a;
	if (oldAtts != null)
	    oldAtts.setNameScope (null);
    }

    void checkChildType (int type)
    throws DOMException
    {
	switch (type) {
	  case ELEMENT_NODE:
	  case TEXT_NODE:
	  case COMMENT_NODE:
	  case PROCESSING_INSTRUCTION_NODE:
	  case CDATA_SECTION_NODE:
	  case ENTITY_REFERENCE_NODE:
	    return;
	  default:
	    throw new DomEx (DomEx.HIERARCHY_REQUEST_ERR);
	}
    }

    public void setReadonly (boolean deep)
    {
	if (attributes != null)
	    attributes.setReadonly ();
	super.setReadonly (deep);
    }

    public String getNamespace ()
    {
	String	prefix;
	String	value;

	if ((prefix = getPrefix ()) == null)
	    return getInheritedAttribute ("xmlns");

	if ("xml".equals (prefix) || "xmlns".equals (prefix))
	    return null;

	value = getInheritedAttribute ("xmlns:" + prefix);
	if (value == null)
	    throw new IllegalStateException (getMessage ("EN-000",
					     new Object [] { prefix }));

	return value;
    }

    /** <b>DOM:</b> Returns the attributes of this element. */
    public NamedNodeMap getAttributes ()
    {
	if (attributes == null)
	    attributes = new AttributeSet (this);
        return attributes;
    }
        
    /**
     * Returns the element and its content as a string, which includes
     * all the markup embedded in this element.  If the element is not
     * fully constructed, the content will not be an XML tag.
     */
    public String toString ()
    {
	try {
	    CharArrayWriter	out = new CharArrayWriter ();
	    XmlWriteContext	x = new XmlWriteContext (out);
	    writeXml (x);
	    return out.toString ();
	} catch (Exception e) {
	    return super.toString ();
	}
    }
    
    
    /**
     * Writes this element and all of its children out, as well
     * formed XML.
     */
    public void writeXml (XmlWriteContext context) throws IOException
    {
	Writer	out = context.getWriter ();

	if (name == null)
	   throw new IllegalStateException ( getMessage ("EN-002"));
	   
	out.write (name);
	
        if (attributes != null)
	    attributes.writeXml (context);

	if (!hasChildNodes ())
	else  {
	    writeChildrenXml (context);
	    out.write (name);
	}
    }

    /**
     * Assigns the name of the element's ID attribute; only one attribute
     * may have the ID type.  XML supports a kind of validatable internal
     * linking using ID attributes, with IDREF attributes identifying
     * specific nodes (and IDREFS attributes identifying sets of them).
     */
    public void setIdAttributeName (String attName)
    {
	if (readonly)
	    throw new DomEx (DomEx.NO_MODIFICATION_ALLOWED_ERR);
	idAttributeName = attName;
    }

    /**
     * Returns the name of the element's ID attribute, if one is known.
     */
    public String getIdAttributeName ()
	{ return idAttributeName; }

    
    public void setUserObject (Object userObject)
	{ this.userObject = userObject; }

    public Object getUserObject ()
	{ return userObject; }


    /** <b>DOM:</b> Returns the ELEMENT_NODE node type. */
    public short getNodeType ()  { return ELEMENT_NODE; }

    /** <b>DOM:</b> Returns the name of the XML tag for this element. */
    public String getTagName () { return name; }
    
    /** <b>DOM:</b> Returns the name of the XML tag for this element. */
    public String getNodeName () { return name; }

    /** <b>DOM:</b> Returns the value of the named attribute, or an empty
     * string 
     */
    public String getAttribute (String name)
    {
	return (attributes == null)
	    ? ""
	    : attributes.getValue (name);
    }

    /**
     * <b>DOM2:</b>
     * @since DOM Level 2
     */
    public String getAttributeNS(String namespaceURI, String localName) {
	if (attributes == null) {
	    return "";
        }
	Attr attr = getAttributeNodeNS(namespaceURI, localName);
	if (attr == null) {
	    return "";
        }
	return attr.getValue();
    }

    /** <b>DOM2:</b> Returns the value of the named attribute, or an empty
     * string 
     */
    public boolean hasAttribute (String name)
    {
        if (attributes == null) {
	    return false;	
	} else if (attributes.getValue (name) != null) {
	    return true;
	}
	return false;
    }

    /**
     * <b>DOM2:</b>
     * @since DOM Level 2
     */
    public boolean hasAttributeNS(String namespaceURI, String localName) {
	if (attributes == null) {
	    return false;
        }
	Attr attr = getAttributeNodeNS(namespaceURI, localName);
	if (attr == null) {
	    return false;
        }
	return true;
    }

    public String getAttribute (String uri, String name)
    {
	Attr	attr;

	if (attributes == null)
	    return "";
	attr = getAttributeNode (uri, name);
	if (attr == null)
	    return "";
	return attr.getValue ();
    }

    /**
     * <b>DOM2:</b>
     * @since DOM Level 2
     */
    public Attr getAttributeNodeNS(String namespaceURI, String localName) {
	if (localName == null) {
	    return null;
        }
	if (attributes == null) {
            return null;
        }
        for (int i = 0; ; i++) {
            AttributeNode attr = (AttributeNode) attributes.item(i);
            if (attr == null) {
                return null;
            }
            if (attr.getLocalName().equals(localName)
                && attr.getNamespaceURI().equals(namespaceURI)) {
                return attr;
            }
        }
    }

    public Attr getAttributeNode (String uri, String name)
    {
	if (name == null)
	    return null;
	if (attributes != null) {
	    for (int i = 0; ; i++) {
		AttributeNode	attr;
		String		ns;

		attr = (AttributeNode) attributes.item (i);
		if (attr == null)
		    return null;
		if (!name.equals (attr.getName ()))
		    continue;
		ns = attr.getNamespace ();
		if (ns != null && ns.equals (uri))
		    return attr;
	    }
	}
	return null;
    }

    
    /**
     * <b>DOM:</b> Assigns or modifies the value of the specified attribute.
     */
    public void setAttribute (String name, String value)
    throws DOMException
    {
	AttributeNode	att;

	if (readonly)
	    throw new DomEx (DomEx.NO_MODIFICATION_ALLOWED_ERR);
	if (attributes == null)
	    attributes = new AttributeSet (this);
	if ((att = (AttributeNode) attributes.getNamedItem (name)) != null)
	    att.setNodeValue (value);
	else {
	    att = new AttributeNode (name, value, true, null);
	    att.setOwnerDocument ((XmlDocument) getOwnerDocument ());
	    att.setOwnerElement(this);
	    attributes.setNamedItem (att);
	}
    }
    
    /**
     * <b>DOM2:</b>
     * @since DOM Level 2
     * XXX Fix err handling and default DEFAULT_XML_NS_URI
     */
    public void setAttributeNS(String namespaceURI, String qualifiedName, 
                               String value)
        throws DOMException
    {
        String prefix = XmlNames.getPrefix(qualifiedName);
        if (prefix == null) {
            throw new DomEx(DomEx.NAMESPACE_ERR);
        }

        Attr attr = getAttributeNodeNS(namespaceURI,
                XmlNames.getLocalPart(qualifiedName));
        if (attr == null) {
            attr = new AttributeNode(namespaceURI, qualifiedName, value,
                                     true, null);
            setAttributeNodeNS(attr);
        } else {
            attr.setValue(value);
            attr.setPrefix(prefix);
        }
    }

    /**
     * <b>DOM2:</b>
     * @since DOM Level 2
     */
    public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
	if (readonly) {
	    throw new DomEx(DomEx.NO_MODIFICATION_ALLOWED_ERR);
        }
        if (newAttr.getOwnerDocument() != getOwnerDocument()) {
	    throw new DomEx(DomEx.WRONG_DOCUMENT_ERR);
        }
        Element ownerElement = newAttr.getOwnerElement();
        if (ownerElement != null || ownerElement != this) {
	    throw new DomEx(DomEx.INUSE_ATTRIBUTE_ERR);
        }

	if (attributes == null) {
	    attributes = new AttributeSet(this);
        }

        ((AttributeNode)newAttr).setOwnerElement(this);

	return (Attr)attributes.setNamedItem(newAttr);
    }

    /** <b>DOM:</b> Remove the named attribute. */
    public void removeAttribute (String name)
    throws DOMException
    {
	if (readonly)
	    throw new DomEx (DomEx.NO_MODIFICATION_ALLOWED_ERR);
	if (attributes == null)
	    throw new DomEx (DomEx.NOT_FOUND_ERR);
        attributes.removeNamedItem (name);
    }

    /**
     * <b>DOM2:</b>
     * @since DOM Level 2
     */
    public void removeAttributeNS(String namespaceURI, String localName)
        throws DOMException
    {
	if (readonly) {
	    throw new DomEx(DomEx.NO_MODIFICATION_ALLOWED_ERR);
        }
        attributes.removeNamedItemNS(namespaceURI, localName);
    }

    /** <b>DOM:</b>  returns the attribute */
    public Attr getAttributeNode (String name)
    {
	if (attributes != null)
	    return (Attr) attributes.getNamedItem (name);
	else
	    return null;
    }
    
    /** <b>DOM:</b> assigns the attribute */
    public Attr setAttributeNode (Attr newAttr)
    throws DOMException
    {
	if (readonly)
	    throw new DomEx (DomEx.NO_MODIFICATION_ALLOWED_ERR);
	if (!(newAttr instanceof AttributeNode))
	    throw new DomEx (DomEx.WRONG_DOCUMENT_ERR);

	if (attributes == null) 
	    attributes = new AttributeSet (this);

        ((AttributeNode)newAttr).setOwnerElement(this);
	return (Attr) attributes.setNamedItem(newAttr);
    }
    
    /** <b>DOM:</b> removes the attribute with the same name as this one */
    public Attr removeAttributeNode (Attr oldAttr)
    throws DOMException
    {
	if (isReadonly ())
	    throw new DomEx (DomEx.NO_MODIFICATION_ALLOWED_ERR);

	Attr	attr = getAttributeNode (oldAttr.getNodeName ());
	if (attr == null)
	    throw new DomEx (DomEx.NOT_FOUND_ERR);
	removeAttribute (attr.getNodeName ());
	return attr;
    }

    
    /**
     * Creates a new unparented node whose attributes are the same as
     * this node's attributes; if <em>deep</em> is true, the children
     * of this node are cloned as children of the new node.
     */
    public Node cloneNode (boolean deep)
    {
	try {
	    ElementNode	retval;

	    retval = (ElementNode) getOwnerDocument().createElement (name);
	    if (attributes != null)
		retval.setAttributes (new AttributeSet (attributes, true));
	    if (deep) {
		for (int i = 0; true; i++) {
		    Node	node = item (i);
		    if (node == null)
			break;
		    retval.appendChild (node.cloneNode (true));
		}
	    }
	    return retval;
	} catch (DOMException e) {
	    throw new RuntimeException (getMessage ("EN-001"));
	}
    }

    /**
     * Convenience method to construct a non-prettyprinting XML write
     * context and call writeXml with it.  Subclasses may choose to
     * to override this method to generate non-XML text, 
     *
     * @param out where to emit the XML content of this node
     */
    public void write (Writer out) throws IOException
    {
	writeXml (new XmlWriteContext (out));
    }
}
