package org.apache.xerces.tree;


import java.io.IOException;

import java.net.URL;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.EntityReference;
import org.w3c.dom.DOMException;

import org.xml.sax.AttributeList;
import org.xml.sax.DocumentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.ext.DeclHandler;

import org.apache.xerces.tree.AttributeListEx;
import org.apache.xerces.tree.DtdEventListener;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.tree.Resolver;


/**
 * This class is a SAX DocumentHandler which converts a stream of parse
 * events into an in-memory DOM document.  After each <em>Parser.parse()</em>
 * invocation returns, a resulting DOM Document may be accessed via the
 * <em>getDocument</em> method.  The parser and its builder should be used
 * together; the builder may be used with only one parser at a time.
 *
 * <P> This builder optionally does XML namespace processing, reporting
 * conformance problems as recoverable errors using the parser's error
 * handler.  
 *
 * <P> To customize the document, a powerful technique involves using
 * an element factory specifying what element tags (from a given XML
 * namespace) correspond to what implementation classes.  Parse trees
 * produced by such a builder can have nodes which add behaviors to
 * achieve application-specific functionality, such as modifing the tree
 * as it is parsed.
 *
 * <P> The object model here is that XML elements are polymorphic, with
 * semantic intelligence embedded through customized internal nodes.
 * Those nodes are created as the parse tree is built.  Such trees now
 * build on the W3C Document Object Model (DOM), and other models may be
 * supported by the customized nodes.  This allows both generic tools
 * (understanding generic interfaces such as the DOM core) and specialized
 * tools (supporting specialized behaviors, such as the HTML extensions
 * to the DOM core; or for XSL elements) to share data structures.
 *
 * <P> Normally only "model" semantics are in document data structures,
 * but "view" or "controller" semantics can be supported if desired.
 *
 * <P> Elements may choose to intercept certain parsing events directly.
 * They do this by overriding the default implementations of methods
 * in the <em>XmlReadable</em> interface.  This is normally done to make
 * the DOM tree represent application level modeling requirements, rather
 * than matching an XML structure that may not be optimized appropriately.
 *
 * @author David Brownell
 * @version $Revision: 315419 $
 */
public class XmlDocumentBuilder implements
    ContentHandler, DocumentHandler, LexicalHandler, DTDHandler, DeclHandler
{
    private static final String

    private XmlDocument		document;
    private Locator		locator;
    private ParseContextImpl	context = new ParseContextImpl ();
    private Locale		locale = Locale.getDefault ();

    private ElementFactory	factory;
    private SAXParser		parser;
    private Vector		attrTmp = new Vector ();
    
    private ParentNode		elementStack [];
    private int         	topOfStack;
    private boolean		inDTD;
    private boolean		inCDataSection;

    private Doctype		doctype;
    private String		publicId;
    private String		systemId;
    private String		internalSubset;

    private boolean		ignoringLexicalInfo = true;
    private boolean		disableNamespaces = true;

    
    /**
     * Default constructor is for use in conjunction with a SAX
     * parser's <em>DocumentHandler</em> callback.
     */
    public XmlDocumentBuilder () { }

    
    /**
     * Returns true (the default) if certain lexical information is
     * automatically discarded when a DOM tree is built, producing
     * smaller parse trees that are easier to use.
     */
    public boolean isIgnoringLexicalInfo () {
	return ignoringLexicalInfo;
    }

    /**
     * Controls whether certain lexical information is discarded; by
     * default, that information is discarded.
     *
     * <P> That information includes whitespace in element content which
     * is ignorable (note that some nonvalidating XML parsers will not
     * report that information); all comments; which text is found in
     * CDATA sections; and boundaries of entity references.
     *
     * <P> "Ignorable whitespace" as reported by parsers is whitespace
     * used to format XML markup.  That is, all whitespace except that in
     * "mixed" or ANY content models is ignorable.  When it is discarded,
     * pretty-printing may be necessary to make the document be readable
     * again by humans.
     *
     * <P> Whitespace inside "mixed" and ANY content models needs different
     * treatment, since it could be part of the document content.  In such
     * cases XML defines a <em>xml:space</em> attribute which applications
     * should use to determine whether whitespace must be preserved (value
     * of the attribute is <em>preserve</em>) or whether default behavior
     * (such as eliminating leading and trailing space, and normalizing
     * consecutive internal whitespace to a single space) is allowed.
     *
     * @param value true indicates that such lexical information should
     *	be discarded during parsing.
     */
    public void setIgnoringLexicalInfo (boolean value) {
	ignoringLexicalInfo = value;
    }


    /**
     * Returns true if namespace conformance is not checked as the
     * DOM tree is built.
     */
    public boolean getDisableNamespaces () {
	return disableNamespaces;
    }

    /**
     * Controls whether namespace conformance is checked during DOM
     * tree construction, or (the default) not.  In this framework, the
     * DOM Builder is responsible for enforcing all namespace constraints.
     * When enabled, this makes constructing a DOM tree slightly slower.
     * (However, at this time it can't enforce the requirement that
     * parameter entity names not contain colons.)
     */
    public void setDisableNamespaces (boolean value) {
	disableNamespaces = value;
    }


    /**
     * Sets the parser used by this builder.  
     */
    public void setParser (org.xml.sax.Parser p)
    {
	p.setDocumentHandler (this);
	if (p instanceof SAXParser) {
	    parser = (SAXParser) p;
	    parser.setDTDHandler (new DtdListener ());
	} else
	    parser = null;
    }

    /**
     * Returns the parser used by this builder, if it is recorded.
     */
    public SAXParser getParser ()
	{ return parser; }
    
    
    /**
     * Returns the fruits of parsing, after a SAX parser has used this
     * as a document handler during parsing.
     */
    public XmlDocument getDocument () { return document; }
    

    /**
     * Returns the locale to be used for diagnostic messages by
     * this builder, and by documents it produces.  This uses
     * the locale of any associated parser.
     */
    /*public Locale	getLocale ()
    {
	if (parser != null)
	    return parser.getLocale ();
	else
	    return locale;
    }*/
    
    /**
     * Assigns the locale to be used for diagnostic messages.
     * Multi-language applications, such as web servers dealing with
     * clients from different locales, need the ability to interact
     * with clients in languages other than the server's default.
     *
     * <P>When an XmlDocument is created, its locale is the default
     * locale for the virtual machine.  If a parser was recorded,
     * the locale will be associated with that parser.
     *
     * @see #chooseLocale
     */
    public void	setLocale (Locale locale)
    throws SAXException
    {
	if (locale == null)
	    locale = Locale.getDefault ();
	if (parser != null)
	    parser.setLocale (locale);
	this.locale = locale;
    }

    /**
     * Chooses a client locale to use for diagnostics, using the first
     * language specified in the list that is supported by this builder.
     * That locale is then automatically assigned using <a
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
    public Locale chooseLocale (String languages [])
    throws SAXException
    {
	Locale	l = XmlDocument.catalog.chooseLocale (languages);

	if (l != null)
	    setLocale (l);
	return l;
    }

    

    /**
     * <b>SAX</b> DocumentHandler callback, not for general application
     * use.  Reports the locator object which will be used in reporting
     * diagnostics and interpreting relative URIs in attributes and text.
     *
     * @param locator used to identify a location in an XML document
     *	being parsed.
     */
    public void setDocumentLocator (Locator locator)
    {
	this.locator = locator;
    }

    /**
     * Returns the document locator provided by the SAX parser.  This
     * is commonly used in diagnostics, and when interpreting relative
     * URIs found in XML Processing Instructions or other parts of an
     * XML document.  This locator is only valid during document handler
     * callbacks.
     */
    public Locator getDocumentLocator ()
    {
	return locator;
    }
    
    
    /**
     * This is a factory method, used to create an XmlDocument.
     * Subclasses may override this method, for example to provide
     * document classes with particular behaviors, or provide
     * particular factory behaviours (such as returning elements
     * that support the HTML DOM methods, if they have the right
     * name and are in the right namespace).
     */
    public XmlDocument createDocument ()
    {
	XmlDocument retval = new XmlDocument ();

	if (factory != null)
	    retval.setElementFactory (factory);
	return retval;
    }


    /**
     * Assigns the factory to be associated with documents produced
     * by this builder.
     */
    final public void setElementFactory (ElementFactory factory)
	{ this.factory = factory; }


    /**
     * Returns the factory to be associated with documents produced
     * by this builder.
     */
    final public ElementFactory getElementFactory ()
	{ return factory; }


    /**
     * <b>SAX</b> DocumentHandler callback, not for general application
     * use.  Reports that the parser is beginning to process a document. 
     */
    public void startDocument () throws SAXException
    {
	document = createDocument ();

	if (locator != null)
	    document.setSystemId (locator.getSystemId ());

        elementStack = new ParentNode [200];
        topOfStack = 0;
        elementStack [topOfStack] = document;

	inDTD = false;
	document.startParse (context);
    }

    /**
     * <b>SAX</b> DocumentHandler callback, not for general application
     * use.  Reports that the document has been fully parsed.
     */
    public void endDocument () throws SAXException
    {
        if (topOfStack != 0)
            throw new IllegalStateException (getMessage ("XDB-000"));
	document.doneParse (context);
	document.trimToSize ();
    }
    
    private String getNamespaceURI (String prefix)
    {
	if ("xml".equals (prefix))
	    return xmlURI;
	else if ("xmlns".equals (prefix))
	    return null;
	else
	    return elementStack [topOfStack]
		    .getInheritedAttribute ("xmlns:" + prefix);
    }
    
    /**
     * <b>SAX</b> DocumentHandler callback, not for general application
     * use.  Reports that the parser started to parse a new element,
     * with the given tag and attributes, and call its <em>startParse</em>
     * method.
     *
     * @exception SAXParseException if XML namespace support is enabled
     *	and the tag or any attribute name contain more than one colon.
     * @exception SAXException as appropriate, such as if a faulty parser
     *	provides an element or attribute name which is illegal.
     */
    public void startElement (String tag, AttributeList attributes)
    throws SAXException
    {
	AttributeSet		attrs = null;
        ElementNode		e = null;
	int			length;

	length = (attributes == null) ? 0 : attributes.getLength ();
	if (length != 0) {
	    try {
		if (!disableNamespaces) {
		    for (int i = 0; i < length; i++) {
			String type = attributes.getType (i);
			if ("CDATA".equals (type)
				|| type.startsWith ("NMTOKEN"))
			    continue;
			if (attributes.getValue (i).indexOf (':') != -1)
			    error (new SAXParseException ((getMessage
			    	("XDB-001", new Object [] 
					{ attributes.getName (i) })), locator));
		    }
		}
		attrs = new AttributeSet (attributes);
	    } catch (DOMException ex) {
		fatal (new SAXParseException ((getMessage ("XDB-002", new
				Object[] { ex.getMessage () })), locator, ex));
	    }
	}

	try {
	    if (disableNamespaces)
		e = (ElementNode) document.createElementEx (tag);
	    else {
		int		index = tag.indexOf (':');
		String		attribute = "xmlns";
		String		namespace = "";
		String		localPart = tag;

		if (index != -1) {
		    attribute = "xmlns:" + tag.substring (0, index);
		    localPart = tag.substring (index + 1);
		    if (tag.lastIndexOf (':') != index)
			error (new SAXParseException ((getMessage ("XDB-003",
				new Object [] { tag })), locator));
		}

		if (attrs != null)
		    namespace = attrs.getValue (attribute);
		if ("".equals (namespace))
		    namespace = elementStack [topOfStack]
			    .getInheritedAttribute (attribute);
		e = (ElementNode) document.createElementEx (namespace,
			localPart);

		if (localPart != tag)
		    e.setTag (tag);
	    }

	} catch (DOMException ex) {
	    fatal (new SAXParseException ((getMessage ("XDB-004", new
	    		Object [] { ex.getMessage () })), locator, ex));
	}
	if (attributes != null && attributes instanceof AttributeListEx)
	    e.setIdAttributeName (
		((AttributeListEx)attributes).getIdAttributeName ());
	if (length != 0)
	    e.setAttributes (attrs);

	elementStack [topOfStack++].appendChild (e);
	elementStack [topOfStack] = e;
	e.startParse (context);

	if (!disableNamespaces) {
	    int		index;
	    String	prefix;

	    index = tag.indexOf (':');
	    if (index > 0) {
		prefix = tag.substring (0, index);
		if (getNamespaceURI (prefix) == null)
		    error (new SAXParseException ((getMessage ("XDB-005",
		    	new Object [] { prefix })), locator));
	    }

	    if (length != 0) {
		for (int i = 0; i < length; i++) {
		    String	name = attrs.item (i).getNodeName ();

		    index = name.indexOf (':');
		    if (index > 0) {
			String	uri;

			prefix = name.substring (0, index);
			if ("xmlns".equals (prefix))
			    continue;
			uri = getNamespaceURI (prefix);
			if (uri == null) {
			    error (new SAXParseException ((getMessage
			    	("XDB-006", new Object [] { prefix })),
				locator));
			    continue;
			}
			
			if (name.lastIndexOf (':') != index)
			    error (new SAXParseException ((getMessage
			    	("XDB-007", new Object [] { name })), locator));

			name = name.substring (index + 1);
			name = uri + '\uffff' + name;
			if (attrTmp.contains (name))
			    error (new SAXParseException ((getMessage
			    	("XDB-008", new Object [] { attrs.item
				(i).getNodeName () })), locator));
			else
			    attrTmp.addElement (name);
		    }
		}
		attrTmp.setSize (0);
	    }
	}
    }
    
    public void startElement(String namespaceURI, String localName,
                             String rawName, Attributes attributes)
	throws SAXException
    {
        AttributeSet attSet = null;
	int length = attributes.getLength();
	if (length != 0) {
	    try {
		attSet = new AttributeSet(attributes);
	    } catch (DOMException ex) {
		fatal (new SAXParseException ((getMessage ("XDB-002", new
				Object[] { ex.getMessage () })), locator, ex));
	    }
	}

        ElementNode e = null;
	try {
            if (namespaceURI.equals("")) {
                namespaceURI = null;
            }
            e = (ElementNode)document.createElementNS(namespaceURI, rawName);
	} catch (DOMException ex) {
	    fatal (new SAXParseException ((getMessage ("XDB-004", new
	    		Object [] { ex.getMessage () })), locator, ex));
	}
	if (attributes instanceof AttributeListEx) {
	    e.setIdAttributeName(
		((AttributeListEx)attributes).getIdAttributeName());
        }
	if (length != 0) {
	    e.setAttributes(attSet);
        }

	elementStack[topOfStack++].appendChild(e);
	elementStack[topOfStack] = e;
	e.startParse(context);

	if (!disableNamespaces) {
	}
    }

    /**
     * <b>SAX</b> DocumentHandler callback, not for general application
     * use.  Reports that the parser finished the current element.
     * The element's <em>doneParse</em> method is then called.
     *
     * @exception SAXException as appropriate
     */
    public void endElement (String tag)
    throws SAXException
    {
        ElementNode e = (ElementNode) elementStack [topOfStack];

        elementStack [topOfStack--] = null;

	try {
	    e.doneParse (context);
	    elementStack [topOfStack].doneChild (e, context);

	} catch (DOMException ex) {
	    fatal (new SAXParseException ((getMessage ("XDB-004", new
	    		Object [] { ex.getMessage () })), locator, ex));
	}
    }

    public void endElement(String namespaceURI, String localName,
                           String rawName)
	throws SAXException
    {
        ElementNode e = (ElementNode) elementStack[topOfStack];

        elementStack[topOfStack--] = null;

	try {
	    e.doneParse(context);
	    elementStack[topOfStack].doneChild(e, context);
	} catch (DOMException ex) {
	    fatal (new SAXParseException ((getMessage ("XDB-004", new
	    		Object [] { ex.getMessage () })), locator, ex));
	}
    }

    
    /**
     * LexicalEventListener callback, not for general application
     * use.  Reports that CDATA section was begun.
     *
     * <P>If this builder is set to record lexical information (by default
     * it ignores such information) then this callback arranges that
     * character data (and ignorable whitespace) be recorded as part of
     * a CDATA section, until the matching <em>endCDATA</em> method is
     * called.
     */
    public void startCDATA () throws SAXException
    {
	if (ignoringLexicalInfo)
	    return;

        CDATASection	text = document.createCDATASection ("");
        ParentNode	top = elementStack [topOfStack];
        
	try {
	    inCDataSection = true;
	    top.appendChild (text);
	} catch (DOMException ex) {
	    fatal (new SAXParseException ((getMessage ("XDB-004", new
	    		Object [] { ex.getMessage () })), locator, ex));
	}
    }
    
    /**
     * LexicalEventListener callback, not for general application
     * use.  Reports that CDATA section was completed.
     * This terminates any CDATA section that is being constructed.
     */
    public void endCDATA () throws SAXException
    {
	if (!inCDataSection)
	    return;

        ParentNode	top = elementStack [topOfStack];
        
	try {
	    inCDataSection = false;
	    top.doneChild ((NodeEx) top.getLastChild (), context);
	} catch (DOMException ex) {
	    fatal (new SAXParseException ((getMessage ("XDB-004", new
	    		Object [] { ex.getMessage () })), locator, ex));
	}
    }
    
    /**
     * <b>SAX</b> DocumentHandler callback, not for general application
     * use.  Reports text which is part of the document, and which will
     * be provided stored as a Text node.
     *
     * <P> Some parsers report "ignorable" whitespace through this interface,
     * which can cause portability problems.  That's because there is no safe
     * way to discard it from a parse tree without accessing DTD information,
     * of a type which DOM doesn't expose and most applications won't want
     * to deal with.  Avoid using such parsers.
     *
     * @param buf holds text characters
     * @param offset initial index of characters in <em>buf</em>
     * @param len how many characters are being passed
     * @exception SAXException as appropriate
     */
    public void characters (char buf [], int offset, int len)
    throws SAXException
    {
        ParentNode	top = elementStack [topOfStack];

	if (inCDataSection) {
	    String		temp = new String (buf, offset, len);
	    CDATASection	section;

	    section = (CDATASection) top.getLastChild ();
	    section.appendData (temp);
	    return;
	}

        
	try {
	    NodeBase lastChild = (NodeBase) top.getLastChild ();
	    if (lastChild instanceof TextNode) {
		String tmp  = new String (buf, offset, len);
	   	((TextNode)lastChild).appendData (tmp);
	    } else {
        	TextNode text = document.newText (buf, offset, len);
	        top.appendChild (text);
	        top.doneChild (text, context);
	    }
	} catch (DOMException ex) {
	    fatal (new SAXParseException ((getMessage ("XDB-004", new
	    		Object [] { ex.getMessage () })), locator, ex));
	}
    }
    
    /**
     * <b>SAX</b> DocumentHandler callback, not for general application
     * use.  Reports ignorable whitespace; if lexical information is
     * not ignored (by default, it is ignored) the whitespace reported
     * here is recorded in a DOM text (or CDATA, as appropriate) node.
     *
     * @param buf holds text characters
     * @param offset initial index of characters in <em>buf</em>
     * @param len how many characters are being passed
     * @exception SAXException as appropriate
     */
    public void ignorableWhitespace (char buf [], int offset, int len)
    throws SAXException
    {
	if (ignoringLexicalInfo)
	    return;

        ParentNode	top = elementStack [topOfStack];

	if (inCDataSection) {
	    String		temp = new String (buf, offset, len);
	    CDATASection	section;

	    section = (CDATASection) top.getLastChild ();
	    section.appendData (temp);
	    return;
	}

        TextNode	text = document.newText (buf, offset, len);
        
	try {
	    top.appendChild (text);
	    top.doneChild (text, context);
	} catch (DOMException ex) {
	    fatal (new SAXParseException ((getMessage ("XDB-004", new
	    		Object [] { ex.getMessage () })), locator, ex));
	}
    }
    
    /**
     * <b>SAX</b> DocumentHandler callback, not for general application
     * use.  Reports that a processing instruction was found.
     *
     * <P> Some applications may want to intercept processing instructions
     * by overriding this method as one way to make such instructions
     * take immediate effect during parsing, or to ensure that
     * processing instructions in DTDs aren't ignored.
     *
     * @param name the processor to which the instruction is directed
     * @param instruction the text of the instruction (no leading spaces)
     * @exception SAXParseException if XML namespace support is enabled
     *	and the name contains a colon.
     * @exception SAXException as appropriate
     */
    public void processingInstruction (String name, String instruction) 
    throws SAXException
    {
	if (!disableNamespaces && name.indexOf (':') != -1)
	    error (new SAXParseException ((getMessage ("XDB-010")), locator));

	if (inDTD)
	    return;

        ParentNode	top = elementStack [topOfStack];
        PINode		pi;
	
	try {
	    pi = (PINode) document.createProcessingInstruction (name,
		    instruction);
	    top.appendChild (pi);
	    top.doneChild (pi, context);

	} catch (DOMException ex) {
	    fatal (new SAXParseException ((getMessage ("XDB-004", new
	    		Object [] { ex.getMessage () })), locator, ex));
	}
    }

    private void error (SAXParseException err)
    throws SAXException
    {
	if (parser != null)
	    parser.getErrorHandler ().error (err);
	else
	    throw err;
    }

    private void fatal (SAXParseException err)
    throws SAXException
    {
	if (parser != null)
	    parser.getErrorHandler ().fatalError (err);
	throw err;
    }

    class ParseContextImpl implements ParseContext
    {
	public ErrorHandler getErrorHandler ()
	    { return (parser != null) ? parser.getErrorHandler () : null; }

	public Locale getLocale () { 
	    return Locale.getDefault();
	}

	public Locator getLocator ()
	    { return locator; }
    }

    class DtdListener implements DtdEventListener
    {
	private Doctype			doctype;
	private String			publicId;
	private String			systemId;
	private String			internalSubset;

	public void startDtd (String root)
	{
	    doctype = document.createDoctype (root);
	    XmlDocumentBuilder.this.inDTD = true;
	}

	public void externalDtdDecl (String p, String s)
	throws SAXException
	{
	    publicId = p;
	    systemId = s;
	}

	public void internalDtdDecl (String s)
	throws SAXException
	{
	    internalSubset = s;
	}

	public void externalEntityDecl (String n, String p, String s)
	throws SAXException
	{
	    if (!disableNamespaces && n.indexOf (':') != -1)
		error (new SAXParseException ((getMessage ("XDB-012")),
		    locator));

	    doctype.addEntityNode (n, p, s, null);
	}

	public void internalEntityDecl (String n, String v)
	throws SAXException
	{
	    if (!disableNamespaces && n.indexOf (':') != -1)
		error (new SAXParseException ((getMessage ("XDB-012")),
		    locator));

	    doctype.addEntityNode (n, v);
	}

	public void notationDecl (String n, String p, String s)
	throws SAXException
	{
	    if (!disableNamespaces && n.indexOf (':') != -1)
		error (new SAXParseException ((getMessage ("XDB-013")),
		    locator));

	    doctype.addNotation (n, p, s);
	}

	public void unparsedEntityDecl (String n, String p, String s, String t)
	throws SAXException
	{
	    if (!disableNamespaces && n.indexOf (':') != -1)
		error (new SAXParseException ((getMessage ("XDB-012")),
		    locator));

	    doctype.addEntityNode (n, p, s, t);
	}

	public void elementDecl (String elementName, String contentModel)
	throws SAXException
	{
	}

	public void attributeDecl (
	    String		elementName,
	    String		attributeName,
	    String		attributeType,
	    String		options [],
	    String		defaultValue,
	    boolean		isFixed,
	    boolean		isRequired
	) throws SAXException
	{
	}


	public void endDtd ()
	{
	    doctype.setPrintInfo (publicId, systemId, internalSubset);
	    document.appendChild (doctype);
	    XmlDocumentBuilder.this.inDTD = false;
	}
    }

    /*
     * Gets the messages from the resource bundles for the given messageId.
     */
    String getMessage (String messageId) {
   	return getMessage (messageId, null);
    }

    /*
     * Gets the messages from the resource bundles for the given messageId
     * after formatting it with the parameters passed to it.
     */
    String getMessage (String messageId, Object[] parameters) {
   	/*if (locale == null) {
		getLocale ();
	}*/
	return XmlDocument.catalog.getMessage (locale, messageId, parameters);
    }


    /*
     * ContentHandler methods (some already declared above)
     */

    public void skippedEntity(String name) throws SAXException {
    }

    public void startPrefixMapping(String prefix, String uri)
	throws SAXException
    {
    }

    public void endPrefixMapping(String prefix)	throws SAXException {
    }

    /*
     * org.xml.sax.ext.LexicalHandler methods (some already declared above)
     */

    public void startDTD(String name, String publicId, String systemId)
	throws SAXException
    {
        doctype = document.createDoctype(name);
        inDTD = true;
	this.systemId = systemId;
	this.publicId = publicId;
    }

    public void endDTD() throws SAXException {
        doctype.setPrintInfo(publicId, systemId, internalSubset);
        document.appendChild(doctype);
        inDTD = false;
    }

    public void startEntity(String name) throws SAXException {

	if (ignoringLexicalInfo || inDTD) {
	    return;
        }

        EntityReference	e = document.createEntityReference(name);
	elementStack[topOfStack++].appendChild(e);
	elementStack[topOfStack] = (ParentNode)e;
    }

    public void endEntity(String name) throws SAXException {
	if (inDTD) {
            return;
        }

        ParentNode entity = elementStack[topOfStack];

	if (!(entity instanceof EntityReference))
	    return;

	entity.setReadonly(true);
        elementStack[topOfStack--] = null;
        if (!name.equals(entity.getNodeName()))
            fatal(new SAXParseException ((getMessage ("XDB-011", new
                    Object[] { name, entity.getNodeName () })), locator));
	try {
	    elementStack[topOfStack].doneChild(entity, context);
	} catch (DOMException ex) {
	    fatal(new SAXParseException ((getMessage ("XDB-004", new
                    Object [] { ex.getMessage () })), locator, ex));
	}
    }



    public void comment(char[] ch, int start, int length) throws SAXException {
	if (ignoringLexicalInfo || inDTD) {
	    return;
        }

        String text = new String(ch, start, length);
        Comment comment = document.createComment(text);
        ParentNode top = elementStack[topOfStack];
        
	try {
	    top.appendChild(comment);
	    top.doneChild((NodeEx) comment, context);
	} catch (DOMException ex) {
	    fatal(new SAXParseException((getMessage("XDB-004", new
                    Object[] { ex.getMessage() })), locator, ex));
	}
    }


    /*
     * org.xml.sax.ext.DeclHandler methods
     */

    public void elementDecl(String name, String model) throws SAXException {
    }

    public void attributeDecl(String eName, String aName, String type,
                              String valueDefault, String value)
	throws SAXException
    {
    }

    public void internalEntityDecl(String name, String value)
	throws SAXException
    {
        if (!disableNamespaces && name.indexOf (':') != -1) {
            error(new SAXParseException((getMessage("XDB-012")), locator));
        }

        doctype.addEntityNode(name, value);
    }

    public void externalEntityDecl(String name, String publicId,
                                   String systemId)
	throws SAXException
    {
        if (!disableNamespaces && name.indexOf (':') != -1) {
            error(new SAXParseException((getMessage("XDB-012")), locator));
        }

        doctype.addEntityNode(name, publicId, systemId, null);
    }

    /*
     * DTDHandler methods
     */

    public void notationDecl (String n, String p, String s)
	throws SAXException
    {
        if (!disableNamespaces && n.indexOf (':') != -1)
            error (new SAXParseException ((getMessage ("XDB-013")),
                                          locator));

        doctype.addNotation (n, p, s);
    }

    public void unparsedEntityDecl (String name, String publicId, 
    				    String systemId, String notation)
	throws SAXException
    {
        if (!disableNamespaces && name.indexOf (':') != -1)
            error (new SAXParseException ((getMessage ("XDB-012")),
                                          locator));

        doctype.addEntityNode (name, publicId, systemId, notation);
    }
}
