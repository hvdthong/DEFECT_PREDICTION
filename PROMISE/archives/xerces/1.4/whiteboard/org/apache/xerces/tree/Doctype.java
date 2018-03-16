package org.apache.xerces.tree;


import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.Writer;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import org.w3c.dom.*;

import org.xml.sax.SAXException;



/**
 * Class representing a DTD in DOM Level 1; this class exists purely
 * for editor support, and is of dubious interest otherwise.
 *
 * @author David Brownell
 * @version $Revision: 315388 $
 */
final class Doctype extends NodeBase implements DocumentType
{
    private String	name;
    private Nodemap	entities;
    private Nodemap	notations;

    private String	publicId;
    private String	systemId;
    private String	internalSubset;


    Doctype (String n)
    {
	name = n;
	entities = new Nodemap ();
	notations = new Nodemap ();
    }

    Doctype (String pub, String sys, String subset)
    {
	publicId = pub;
	systemId = sys;
	internalSubset = subset;
    }

    Doctype(String name, String publicId, String systemId,
            String internalSubset)
    {
        this.name = name;
        this.publicId = publicId;
	this.systemId = systemId;
	this.internalSubset = internalSubset;
    }

    void setPrintInfo (String pub, String sys, String subset)
    {
	publicId = pub;
	systemId = sys;
	internalSubset = subset;
    }


    /**
     * Writes out a textual DTD, trusting that any internal subset text
     * is in fact well formed XML.
     */
    public void writeXml (XmlWriteContext context) throws IOException
    {
	Writer	out = context.getWriter ();
	Element	root = getOwnerDocument ().getDocumentElement ();

	out.write ("<!DOCTYPE ");
	out.write (root == null ? "UNKNOWN-ROOT" : root.getNodeName ());

	if (systemId != null) {
	    if (publicId != null) {
		out.write (" PUBLIC '");
		out.write (publicId);
		out.write ("' '");
	    } else
		out.write (" SYSTEM '");
	    out.write (systemId);
	    out.write ("'");
	}
	if (internalSubset != null) {
	    out.write (XmlDocument.eol);
	    out.write ("[");
	    out.write (internalSubset);
	    out.write ("]");
	}
	out.write (">");
	out.write (XmlDocument.eol);
    }

    /** DOM: Returns DOCUMENT_TYPE_NODE */
    public short getNodeType ()
	{ return DOCUMENT_TYPE_NODE; }
    
    /** DOM:  Returns the name declared for the document root node. */
    public String getName ()
	{ return name; }
    
    /** DOM:  Returns the name declared for the document root node. */
    public String getNodeName ()
	{ return name; }
    
    /** DOM: NYI
     * @deprecated Not yet implemented.
     */
    public Node cloneNode (boolean deep)
    {
	throw new RuntimeException (getMessage ("DT-000"));
    }

    /**
     * DOM: Returns the internal, external, and unparsed entities
     * declared in this DTD.
     */
    public NamedNodeMap getEntities ()
	{ return entities; }

    /** DOM: Returns the notations declared in this DTD.  */
    public NamedNodeMap getNotations ()
	{ return notations; }
    
    /**
     * The public identifier of the external subset.
     * @since DOM Level 2
     */
    public String getPublicId() {
        return publicId;
    }

    /**
     * The system identifier of the external subset.
     * @since DOM Level 2
     */
    public String getSystemId() {
        return systemId;
    }

    /**
     * The internal subset as a string.
     * @since DOM Level 2
     */
    public String getInternalSubset() {
        return internalSubset;
    }

    void setOwnerDocument (XmlDocument doc)
    {
	super.setOwnerDocument (doc);
	if (entities != null)
	    for (int i = 0; entities.item (i) != null; i++)
		((NodeBase)entities.item (i)).setOwnerDocument (doc);
	if (notations != null)
	    for (int i = 0; notations.item (i) != null; i++)
		((NodeBase)notations.item (i)).setOwnerDocument (doc);
    }


    /** Adds a notation node. */
    void addNotation (String name, String pub, String sys)
    {
	NotationNode node = new NotationNode (name, pub, sys);
	node.setOwnerDocument ((XmlDocument)getOwnerDocument ());
	notations.setNamedItem (node);
    }

    /**
     * Adds an entity node for an external entity, which
     * could be parsed or unparsed.
     */
    void addEntityNode (String name, String pub, String sys, String not)
    {
	EntityNode node = new EntityNode (name, pub, sys, not);
	node.setOwnerDocument ((XmlDocument)getOwnerDocument ());
	entities.setNamedItem (node);
    }

    /** Adds an entity node for an  internal parsed entity. */
    void addEntityNode (String name, String value)
    {
	if ("lt".equals (name) || "gt".equals (name)
		|| "apos".equals (name) || "quot".equals (name)
		|| "amp".equals (name))

	EntityNode node = new EntityNode (name, value);
	node.setOwnerDocument ((XmlDocument)getOwnerDocument ());
	entities.setNamedItem (node);
    }

    /** Marks the sets of entities and notations as readonly. */
    void setReadonly ()
    {
	entities.readonly = true;
	notations.readonly = true;
    }


    static class NotationNode extends NodeBase implements Notation
    {
	private String	notation;
	private String	publicId;
	private String	systemId;

	NotationNode (String name, String pub, String sys)
	{
	    notation = name;
	    publicId = pub;
	    systemId = sys;
	}

	public String getPublicId ()
	    { return publicId; }

	public String getSystemId ()
	    { return systemId; }
	
	public short getNodeType ()
	    { return NOTATION_NODE; }
	
	public String getNodeName ()
	    { return notation; }
	
	public Node cloneNode (boolean ignored)
	{
	    NotationNode retval;

	    retval = new NotationNode (notation, publicId, systemId);
	    retval.setOwnerDocument ((XmlDocument)getOwnerDocument ());
	    return retval;
	}

	public void writeXml (XmlWriteContext context) throws IOException
	{
	    Writer out = context.getWriter ();
	    out.write ("<!NOTATION ");
	    out.write (notation);
	    if (publicId != null) {
		out.write (" PUBLIC '");
		out.write (publicId);
		if (systemId != null) {
		    out.write ("' '");
		    out.write (systemId);
		}
	    } else {
		out.write (" SYSTEM '");
		out.write (systemId);
	    }
	    out.write ("'>");
	}
    }


    static class EntityNode extends NodeBase implements Entity
    {
	private String	entityName;

	private String	publicId;
	private String	systemId;
	private String	notation;

	private String	value;

	EntityNode (String name, String pub, String sys, String not)
	{
	    entityName = name;
	    publicId = pub;
	    systemId = sys;
	    notation = not;
	}

	EntityNode (String name, String value)
	{
	    entityName = name;
	    this.value = value;
	}

	public String getNodeName ()
	    { return entityName; }
	
	public short getNodeType ()
	    { return ENTITY_NODE; }

	public String getPublicId ()
	    { return publicId; }

	public String getSystemId ()
	    { return systemId; }

	public String getNotationName ()
	    { return notation; }
	
	public Node cloneNode (boolean ignored)
	{
	    EntityNode retval;

	    retval = new EntityNode (entityName, publicId, systemId,
			    notation);
	    retval.setOwnerDocument ((XmlDocument)getOwnerDocument ());
	    return retval;
	}

	public void writeXml (XmlWriteContext context) throws IOException
	{
	    Writer out = context.getWriter ();
	    out.write ("<!ENTITY ");
	    out.write (entityName);

	    if (value == null) {
		if (publicId != null) {
		    out.write (" PUBLIC '");
		    out.write (publicId);
		    out.write ("' '");
		} else
		    out.write (" SYSTEM '");
		out.write (systemId);
		out.write ("'");
		if (notation != null) {
		    out.write (" NDATA ");
		    out.write (notation);
		}
	    } else {
		out.write (" \"");
		int length = value.length ();
		for (int i = 0; i < length; i++) {
		    char c = value.charAt (i);
		    if (c == '"')
			out.write ("&quot;");
		    else
			out.write (c);
		}
		out.write ('"');
	    }
	    out.write (">");
	}
    }

    static class Nodemap implements NamedNodeMap
    {
	boolean	readonly;

	java.util.Vector	list = new java.util.Vector ();

	public Node getNamedItem (String name)
	{
	    int	length = list.size ();
	    Node	value;

	    for (int i = 0; i < length; i++) {
		value = item (i);
		if (value.getNodeName ().equals (name))
		    return value;
	    }
	    return null;
	}

        /**
         * <b>DOM2:</b>
         */
        public Node getNamedItemNS(String namespaceURI, String localName) {
            return null;
        }
        
	public int getLength ()
	{
	    return list.size ();
	}

	public Node item (int index)
	{
	    if (index < 0 || index >= list.size ())
		return null;
	    return (Node) list.elementAt (index);
	}


	public Node removeNamedItem (String name)
	throws DOMException
	{
	    throw new DomEx (DOMException.NO_MODIFICATION_ALLOWED_ERR);
	}

        /**
         * <b>DOM2:</b>
         */
        public Node removeNamedItemNS(String namespaceURI, String localName)
            throws DOMException
        {
	    throw new DomEx(DOMException.NO_MODIFICATION_ALLOWED_ERR);
        }

	public Node setNamedItem (Node item) throws DOMException
	{
	    if (readonly)
		throw new DomEx (DOMException.NO_MODIFICATION_ALLOWED_ERR);
	    list.addElement (item);
	    return null;
	}

        /**
         * <b>DOM2:</b>
         */
        public Node setNamedItemNS(Node arg) throws DOMException {
            if (readonly) {
                throw new DomEx(DomEx.NO_MODIFICATION_ALLOWED_ERR);
            }

            list.addElement(arg);
            return null;
        }

    }
}
