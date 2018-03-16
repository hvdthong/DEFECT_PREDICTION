package org.apache.html.dom;


import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import org.w3c.dom.*;
import org.w3c.dom.html.*;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.dom.NodeImpl;
import org.apache.xerces.dom.AttrImpl;
import org.w3c.dom.DOMException;


/**
 * Implements an HTML document. Provides access to the top level element in the
 * document, its body and title.
 * <P>
 * Several methods create new nodes of all basic types (comment, text, element,
 * etc.). These methods create new nodes but do not place them in the document
 * tree. The nodes may be placed in the document tree using {@link
 * org.w3c.dom.Node#appendChild} or {@link org.w3c.dom.Node#insertBefore}, or
 * they may be placed in some other document tree.
 * <P>
 * Note: &lt;FRAMESET&gt; documents are not supported at the moment, neither
 * are direct document writing ({@link #open}, {@link #write}) and HTTP attribute
 * methods ({@link #getURL}, {@link #getCookie}).
 *
 *
 * @version $Revision: 316203 $ $Date: 2000-10-04 16:53:21 +0800 (周三, 2000-10-04) $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLDocument
 */
public class HTMLDocumentImpl
    extends DocumentImpl
    implements HTMLDocument
{


    /**
     * Holds {@link HTMLCollectionImpl} object with live collection of all
     * anchors in document. This reference is on demand only once.
     */
    private HTMLCollectionImpl    _anchors;


    /**
     * Holds {@link HTMLCollectionImpl} object with live collection of all
     * forms in document. This reference is on demand only once.
     */
    private HTMLCollectionImpl    _forms;


    /**
     * Holds {@link HTMLCollectionImpl} object with live collection of all
     * images in document. This reference is on demand only once.
     */
    private HTMLCollectionImpl    _images;


    /**
     * Holds {@link HTMLCollectionImpl} object with live collection of all
     * links in document. This reference is on demand only once.
     */
    private HTMLCollectionImpl    _links;


    /**
     * Holds {@link HTMLCollectionImpl} object with live collection of all
     * applets in document. This reference is on demand only once.
     */
    private HTMLCollectionImpl    _applets;


    /**
     * Holds string writer used by direct manipulation operation ({@link #open}.
     * {@link #write}, etc) to write new contents into the document and parse
     * that text into a document tree.
     */
    private StringWriter        _writer;


    /**
     * Holds names and classes of HTML element types. When an element with a
     * particular tag name is created, the matching {@link java.lang.Class}
     * is used to create the element object. For example, &lt;A&gt; matches
     * {@link HTMLAnchorElementImpl}. This static table is shared across all
     * HTML documents.
     *
     * @see #createElement
     */
    private static Hashtable        _elementTypesHTML;


    /**
     * Signature used to locate constructor of HTML element classes. This
     * static array is shared across all HTML documents.
     *
     * @see #createElement
     */
    private static final Class[]    _elemClassSigHTML =
                new Class[] { HTMLDocumentImpl.class, String.class };


    /**
     */
    public HTMLDocumentImpl()
    {
        super();
        populateElementTypes();
    }


    public synchronized Element getDocumentElement()
    {
        Node    html;
        Node    child;
        Node    next;

        html = getFirstChild();
        while ( html != null )
        {
            if ( html instanceof HTMLHtmlElement )
            {
                synchronized ( html )
                {
                    child = getFirstChild();
                    while ( child != null && child != html )
                    {
                        next = child.getNextSibling();
                        html.appendChild( child );
                        child = next;
                    }
                }
                return (HTMLElement) html;
            }
            html = html.getNextSibling();
        }

        html = new HTMLHtmlElementImpl( this, "HTML" );
        child = getFirstChild();
        while ( child != null )
        {
            next = child.getNextSibling();
            html.appendChild( child );
            child = next;
        }
        appendChild( html );
        return (HTMLElement) html;
    }


    /**
     * Obtains the &lt;HEAD&gt; element in the document, creating one if does
     * not exist before. The &lt;HEAD&gt; element is the first element in the
     * &lt;HTML&gt; in the document. The &lt;HTML&gt; element is obtained by
     * calling {@link #getDocumentElement}. If the element does not exist, one
     * is created.
     * <P>
     * Called by {@link #getTitle}, {@link #setTitle}, {@link #getBody} and
     * {@link #setBody} to assure the document has the &lt;HEAD&gt; element
     * correctly placed.
     *
     * @return The &lt;HEAD&gt; element
     */
    public synchronized HTMLElement getHead()
    {
        Node    head;
        Node    html;
        Node    child;
        Node    next;

        html = getDocumentElement();
        synchronized ( html )
        {
            head = html.getFirstChild();
            while ( head != null && ! ( head instanceof HTMLHeadElement ) )
                head = head.getNextSibling();
            if ( head != null )
            {
                synchronized ( head )
                {
                    child = html.getFirstChild();
                    while ( child != null && child != head )
                    {
                        next = child.getNextSibling();
                        head.insertBefore( child, head.getFirstChild() );
                        child = next;
                    }
                }
                return (HTMLElement) head;
            }

            head = new HTMLHeadElementImpl( this, "HEAD" );
            html.insertBefore( head, html.getFirstChild() );
        }
        return (HTMLElement) head;
    }


    public synchronized String getTitle()
    {
        HTMLElement head;
        NodeList    list;
        Node        title;

        head = getHead();
        title = head.getElementsByTagName( "TITLE" ).item( 0 );
        list = head.getElementsByTagName( "TITLE" );
        if ( list.getLength() > 0 ) {
            title = list.item( 0 );
            return ( (HTMLTitleElement) title ).getText();
        }
        return "";
    }


    public synchronized void setTitle( String newTitle )
    {
        HTMLElement head;
        NodeList    list;
        Node        title;

        head = getHead();
        list = head.getElementsByTagName( "TITLE" );
        if ( list.getLength() > 0 ) {
            title = list.item( 0 );
            if ( title.getParentNode() != head )
                head.appendChild( title );
            ( (HTMLTitleElement) title ).setText( newTitle );
        }
        else
        {
            title = new HTMLTitleElementImpl( this, "TITLE" );
            ( (HTMLTitleElement) title ).setText( newTitle );
            head.appendChild( title );
        }
    }


    public synchronized HTMLElement getBody()
    {
        Node    html;
        Node    head;
        Node    body;
        Node    child;
        Node    next;

        html = getDocumentElement();
        head = getHead();
        synchronized ( html )
        {
            body = head.getNextSibling();
            while ( body != null && ! ( body instanceof HTMLBodyElement )
                    && ! ( body instanceof HTMLFrameSetElement ) )
                body = body.getNextSibling();

            if ( body != null )
            {
                synchronized ( body )
                {
                    child = head.getNextSibling();
                    while ( child != null && child != body )
                    {
                        next = child.getNextSibling();
                        body.insertBefore( child, body.getFirstChild() );
                        child = next;
                    }
                }
                return (HTMLElement) body;
            }

            body = new HTMLBodyElementImpl( this, "BODY" );
            html.appendChild( body );
        }
        return (HTMLElement) body;
    }


    public synchronized void setBody( HTMLElement newBody )
    {
        Node    html;
        Node    body;
        Node    head;
        Node    child;
        NodeList list;

        synchronized ( newBody )
        {
            html = getDocumentElement();
            head = getHead();
            synchronized ( html )
            {
                list = this.getElementsByTagName( "BODY" );
                if ( list.getLength() > 0 ) {
                    body = list.item( 0 );
                    synchronized ( body )
                    {
                        child = head;
                        while ( child != null )
                        {
                            if ( child instanceof Element )
                            {
                                if ( child != body )
                                    html.insertBefore( newBody, child );
                                else
                                    html.replaceChild( newBody, body );
                                return;
                            }
                            child = child.getNextSibling();
                        }
                        html.appendChild( newBody );
                    }
                    return;
                }
                html.appendChild( newBody );
            }
        }
    }


    public synchronized Element getElementById( String elementId )
    {
        return getElementById( elementId, this );
    }


    public NodeList getElementsByName( String elementName )
    {
        return new NameNodeListImpl( this, elementName );
    }


    public final NodeList getElementsByTagName( String tagName )
    {
        return super.getElementsByTagName( tagName.toUpperCase() );
    }


    public final NodeList getElementsByTagNameNS( String namespaceURI,
                                                  String localName )
    {
        if ( namespaceURI != null && namespaceURI.length() > 0 )
            return super.getElementsByTagNameNS( namespaceURI, localName.toUpperCase() );
        else
            return super.getElementsByTagName( localName.toUpperCase() );
    } 


    public Element createElementNS( String namespaceURI, String qualifiedName )
    {
        if ( namespaceURI == null || namespaceURI.length() == 0 )
            return createElement( qualifiedName );
        else
            return super.createElementNS( namespaceURI, qualifiedName );
    }


    public Element createElement( String tagName )
        throws DOMException
    {
        Class        elemClass;
        Constructor    cnst;

        tagName = tagName.toUpperCase();
        elemClass = (Class) _elementTypesHTML.get( tagName );
        if ( elemClass != null )
        {
            try
            {
                cnst = elemClass.getConstructor( _elemClassSigHTML );
                return (Element) cnst.newInstance( new Object[] { this, tagName } );
            }
            catch ( Exception except )
            {
                Throwable thrw;

                if ( except instanceof java.lang.reflect.InvocationTargetException )
                    thrw = ( (java.lang.reflect.InvocationTargetException) except ).getTargetException();
                else
                    thrw = except;

                throw new IllegalStateException( "HTM15 Tag '" + tagName + "' associated with an Element class that failed to construct.\n" + tagName);
            }
        }
        return new HTMLElementImpl( this, tagName );
    }


    /**
     * Creates an Attribute having this Document as its OwnerDoc.
     * Overrides {@link DocumentImpl#createAttribute} and returns
     * and attribute whose name is lower case.
     *
     * @param name The name of the attribute
     * @return An attribute whose name is all lower case
     * @throws DOMException(INVALID_NAME_ERR) if the attribute name
     *   is not acceptable
     */
    public Attr createAttribute( String name )
        throws DOMException
    {
        return super.createAttribute( name.toLowerCase() );
    }


    public String getReferrer()
    {
        return null;
    }


    public String getDomain()
    {
        return null;
    }


    public String getURL()
    {
        return null;
    }


    public String getCookie()
    {
        return null;
    }


    public void setCookie( String cookie )
    {
    }


    public HTMLCollection getImages()
    {
        if ( _images == null )
            _images = new HTMLCollectionImpl( getBody(), HTMLCollectionImpl.IMAGE );
        return _images;
    }


    public HTMLCollection getApplets()
    {
        if ( _applets == null )
            _applets = new HTMLCollectionImpl( getBody(), HTMLCollectionImpl.APPLET );
        return _applets;
    }


    public HTMLCollection getLinks()
    {
        if ( _links == null )
            _links = new HTMLCollectionImpl( getBody(), HTMLCollectionImpl.LINK );
        return _links;
    }


    public HTMLCollection getForms()
    {
        if ( _forms == null )
            _forms = new HTMLCollectionImpl( getBody(), HTMLCollectionImpl.FORM );
        return _forms;
    }


    public HTMLCollection getAnchors()
    {
        if ( _anchors == null )
            _anchors = new HTMLCollectionImpl( getBody(), HTMLCollectionImpl.ANCHOR );
        return _anchors;
    }


    public void open()
    {
        if ( _writer == null )
            _writer = new StringWriter();
    }


    public void close()
    {
        if ( _writer != null )
        {
            _writer = null;
        }
    }


    public void write( String text )
    {
        if ( _writer != null )
            _writer.write( text );
    }


    public void writeln( String text )
    {
        if ( _writer != null )
            _writer.write( text + "\n" );
    }


    public Node cloneNode( boolean deep )
    {
        HTMLDocumentImpl    clone;
        NodeImpl            node;
        
        clone = new HTMLDocumentImpl();
        if ( deep ) {
            node = (NodeImpl) getFirstChild();
            while ( node != null ) {
                clone.appendChild( clone.importNode( node, true ) );
                node = (NodeImpl) node.getNextSibling();
            }
        }
        return clone;
    }


    /**
     * Recursive method retreives an element by its <code>id</code> attribute.
     * Called by {@link #getElementById(String)}.
     *
     * @param elementId The <code>id</code> value to look for
     * @return The node in which to look for
     */
    private Element getElementById( String elementId, Node node )
    {
        Node    child;
        Element    result;

        child = node.getFirstChild();
        while ( child != null )
        {
            if ( child instanceof Element )
            {
                if ( elementId.equals( ( (Element) child ).getAttribute( "id" ) ) )
                    return (Element) child;
                result = getElementById( elementId, child );
                if ( result != null )
                    return result;
            }
            child = child.getNextSibling();
        }
        return null;
    }


    /**
     * Called by the constructor to populate the element types list (see {@link
     * #_elementTypesHTML}). Will be called multiple times but populate the list
     * only the first time. Replacement for static constructor.
     */
    private static void populateElementTypes()
    {
        
        if ( _elementTypesHTML != null )
            return;
        _elementTypesHTML = new Hashtable( 63 );
        populateElementType( "A", "HTMLAnchorElementImpl" );
        populateElementType( "APPLET", "HTMLAppletElementImpl" );
        populateElementType( "AREA", "HTMLAreaElementImpl" );
        populateElementType( "BASE",  "HTMLBaseElementImpl" );
        populateElementType( "BASEFONT", "HTMLBaseFontElementImpl" );
        populateElementType( "BLOCKQUOTE", "HTMLQuoteElementImpl" );
        populateElementType( "BODY", "HTMLBodyElementImpl" );
        populateElementType( "BR", "HTMLBRElementImpl" );
        populateElementType( "BUTTON", "HTMLButtonElementImpl" );
        populateElementType( "DEL", "HTMLModElementImpl" );
        populateElementType( "DIR", "HTMLDirectoryElementImpl" );
        populateElementType( "DIV",  "HTMLDivElementImpl" );
        populateElementType( "DL", "HTMLDListElementImpl" );
        populateElementType( "FIELDSET", "HTMLFieldSetElementImpl" );
        populateElementType( "FONT", "HTMLFontElementImpl" );
        populateElementType( "FORM", "HTMLFormElementImpl" );
        populateElementType( "FRAME","HTMLFrameElementImpl" );
        populateElementType( "FRAMESET", "HTMLFrameSetElementImpl" );
        populateElementType( "HEAD", "HTMLHeadElementImpl" );
        populateElementType( "H1", "HTMLHeadingElementImpl" );
        populateElementType( "H2", "HTMLHeadingElementImpl" );
        populateElementType( "H3", "HTMLHeadingElementImpl" );
        populateElementType( "H4", "HTMLHeadingElementImpl" );
        populateElementType( "H5", "HTMLHeadingElementImpl" );
        populateElementType( "H6", "HTMLHeadingElementImpl" );
        populateElementType( "HR", "HTMLHRElementImpl" );
        populateElementType( "HTML", "HTMLHtmlElementImpl" );
        populateElementType( "IFRAME", "HTMLIFrameElementImpl" );
        populateElementType( "IMG", "HTMLImageElementImpl" );
        populateElementType( "INPUT", "HTMLInputElementImpl" );
        populateElementType( "INS", "HTMLModElementImpl" );
        populateElementType( "ISINDEX", "HTMLIsIndexElementImpl" );
        populateElementType( "LABEL", "HTMLLabelElementImpl" );
        populateElementType( "LEGEND", "HTMLLegendElementImpl" );
        populateElementType( "LI", "HTMLLIElementImpl" );
        populateElementType( "LINK", "HTMLLinkElementImpl" );
        populateElementType( "MAP", "HTMLMapElementImpl" );
        populateElementType( "MENU", "HTMLMenuElementImpl" );
        populateElementType( "META", "HTMLMetaElementImpl" );
        populateElementType( "OBJECT", "HTMLObjectElementImpl" );
        populateElementType( "OL", "HTMLOListElementImpl" );
        populateElementType( "OPTGROUP", "HTMLOptGroupElementImpl" );
        populateElementType( "OPTION", "HTMLOptionElementImpl" );
        populateElementType( "P", "HTMLParagraphElementImpl" );
        populateElementType( "PARAM", "HTMLParamElementImpl" );
        populateElementType( "PRE", "HTMLPreElementImpl" );
        populateElementType( "Q", "HTMLQuoteElementImpl" );
        populateElementType( "SCRIPT", "HTMLScriptElementImpl" );
        populateElementType( "SELECT", "HTMLSelectElementImpl" );
        populateElementType( "STYLE", "HTMLStyleElementImpl" );
        populateElementType( "TABLE", "HTMLTableElementImpl" );
        populateElementType( "CAPTION", "HTMLTableCaptionElementImpl" );
        populateElementType( "TD", "HTMLTableCellElementImpl" );
        populateElementType( "TH", "HTMLTableCellElementImpl" );
        populateElementType( "COL", "HTMLTableColElementImpl" );
        populateElementType( "COLGROUP", "HTMLTableColElementImpl" );
        populateElementType( "TR", "HTMLTableRowElementImpl" );
        populateElementType( "TBODY", "HTMLTableSectionElementImpl" );
        populateElementType( "THEAD", "HTMLTableSectionElementImpl" );
        populateElementType( "TFOOT", "HTMLTableSectionElementImpl" );
        populateElementType( "TEXTAREA", "HTMLTextAreaElementImpl" );
        populateElementType( "TITLE", "HTMLTitleElementImpl" );
        populateElementType( "UL", "HTMLUListElementImpl" );
    }
    
    
    private static void populateElementType( String tagName, String className )
    {
        try {
            _elementTypesHTML.put( tagName, Class.forName( "org.apache.html.dom." + className ) );
        } catch ( ClassNotFoundException except ) {
            new RuntimeException( "HTM019 OpenXML Error: Could not find class " + className + " implementing HTML element " + tagName
                                  + "\n" + className + "\t" + tagName);
        }
    }


}

