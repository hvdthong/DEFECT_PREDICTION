package org.apache.html.dom;


import java.util.Vector;
import org.w3c.dom.*;
import org.w3c.dom.html.*;
import org.xml.sax.*;
import org.apache.xerces.dom.TextImpl;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.dom.ProcessingInstructionImpl;


/**
 * This is a SAX document handler that is used to build an HTML document.
 * It can build a document from any SAX parser, but is specifically tuned
 * for working with the OpenXML HTML parser.
 * 
 * 
 * @version $Revision: 316747 $ $Date: 2000-12-21 08:33:45 +0800 (周四, 2000-12-21) $
 * @author <a href="mailto:arkin@openxml.org">Assaf Arkin</a>
 */
public class HTMLBuilder
    implements DocumentHandler
{


    /**
     * The document that is being built.
     */
    protected HTMLDocumentImpl    _document;
    
    
    /**
     * The current node in the document into which elements, text and
     * other nodes will be inserted. This starts as the document iself
     * and reflects each element that is currently being parsed.
     */
    protected ElementImpl        _current;
    
    /**
     * A reference to the current locator, this is generally the parser
     * itself. The locator is used to locate errors and identify the
     * source locations of elements.
     */
    private Locator         _locator;


    /**
     * Applies only to whitespace appearing between element tags in element content,
     * as per the SAX definition, and true by default.
     */
    private boolean         _ignoreWhitespace = true;


    /**
     * Indicates whether finished building a document. If so, can start building
     * another document. Must be initially true to get the first document processed.
     */
    private boolean         _done = true;


    /**    
     * The document is only created the same time as the document element, however, certain
     * nodes may precede the document element (comment and PI), and they are accumulated
     * in this vector.
     */
    protected Vector         _preRootNodes;

    
    public void startDocument()
        throws SAXException
    {
        if ( ! _done )
	    throw new SAXException( "HTM001 State error: startDocument fired twice on one builder." );
	_document = null;
	_done = false;
    }


    public void endDocument()
        throws SAXException
    {
        if ( _document == null )
            throw new SAXException( "HTM002 State error: document never started or missing document element." );
	if ( _current != null )
	    throw new SAXException( "HTM003 State error: document ended before end of document element." );
        _current = null;
	_done = true;
    }


    public synchronized void startElement( String tagName, AttributeList attrList )
        throws SAXException
    {
        ElementImpl elem;
        int         i;
        
	if ( tagName == null )
	    throw new SAXException( "HTM004 Argument 'tagName' is null." );

	if ( _document == null )
	{
	    _document = new HTMLDocumentImpl();
	    elem = (ElementImpl) _document.getDocumentElement();
	    _current = elem;
	    if ( _current == null )
		throw new SAXException( "HTM005 State error: Document.getDocumentElement returns null." );

	    if ( _preRootNodes != null )
	    {
		for ( i = _preRootNodes.size() ; i-- > 0 ; )
		    _document.insertBefore( (Node) _preRootNodes.elementAt( i ), elem );
		_preRootNodes = null;
	    }
	     
	}
	else
	{
	    if ( _current == null )
		throw new SAXException( "HTM006 State error: startElement called after end of document element." );
	    elem = (ElementImpl) _document.createElement( tagName );
	    _current.appendChild( elem );
	    _current = elem;
	}

        if ( attrList != null )
        {
            for ( i = 0 ; i < attrList.getLength() ; ++ i )
                elem.setAttribute( attrList.getName( i ), attrList.getValue( i ) );
        }
    }

    
    public void endElement( String tagName )
        throws SAXException
    {

        if ( _current == null )
            throw new SAXException( "HTM007 State error: endElement called with no current node." );
	if ( ! _current.getNodeName().equals( tagName ) )
	    throw new SAXException( "HTM008 State error: mismatch in closing tag name " + tagName + "\n" + tagName);

	if ( _current.getParentNode() == _current.getOwnerDocument() )
	    _current = null;
	else
	    _current = (ElementImpl) _current.getParentNode();
    }


    public void characters( String text )
        throws SAXException
    {
	if ( _current == null )
            throw new SAXException( "HTM009 State error: character data found outside of root element." );
	_current.appendChild( new TextImpl( _document, text ) );
    }

    
    public void characters( char[] text, int start, int length )
        throws SAXException
    {
	if ( _current == null )
            throw new SAXException( "HTM010 State error: character data found outside of root element." );
	_current.appendChild( new TextImpl( _document, new String( text, start, length ) ) );
    }
    
    
    public void ignorableWhitespace( char[] text, int start, int length )
        throws SAXException
    {
        Node    node;
        
        if ( ! _ignoreWhitespace )
	    _current.appendChild( new TextImpl( _document, new String( text, start, length ) ) );
     }
    
    
    public void processingInstruction( String target, String instruction )
        throws SAXException
    {
        Node    node;
        
        if ( _current == null && _document == null )
	{
	    if ( _preRootNodes == null )
		_preRootNodes = new Vector();
	    _preRootNodes.addElement( new ProcessingInstructionImpl( null, target, instruction ) );
	}
	else
        if ( _current == null && _document != null )
	    _document.appendChild( new ProcessingInstructionImpl( _document, target, instruction ) );
	else
	    _current.appendChild( new ProcessingInstructionImpl( _document, target, instruction ) );
    }
    
    
    public HTMLDocument getHTMLDocument()
    {
        return (HTMLDocument) _document;
    }

    
    public void setDocumentLocator( Locator locator )
    {
        _locator = locator;
    }


}
