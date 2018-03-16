package org.apache.xml.serialize;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.OutputStream;
import java.io.Writer;

import org.w3c.dom.*;
import org.xml.sax.DocumentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;


/**
 * Implements a text serializer supporting both DOM and SAX
 * serializing. For usage instructions see {@link Serializer}.
 * <p>
 * If an output stream is used, the encoding is taken from the
 * output format (defaults to <tt>UTF-8</tt>). If a writer is
 * used, make sure the writer uses the same encoding (if applies)
 * as specified in the output format.
 * <p>
 * The serializer supports both DOM and SAX. DOM serializing is done
 * by calling {@link #serialize} and SAX serializing is done by firing
 * SAX events and using the serializer as a document handler.
 * <p>
 * If an I/O exception occurs while serializing, the serializer
 * will not throw an exception directly, but only throw it
 * at the end of serializing (either DOM or SAX's {@link
 * org.xml.sax.DocumentHandler#endDocument}.
 *
 *
 * @version $Revision: 316040 $ $Date: 2000-08-31 02:59:22 +0800 (周四, 2000-08-31) $
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @see Serializer
 */
public final class TextSerializer
    extends BaseMarkupSerializer
{


    /**
     * Constructs a new serializer. The serializer cannot be used without
     * calling {@link #setOutputCharStream} or {@link #setOutputByteStream}
     * first.
     */
    public TextSerializer()
    {
        super( new OutputFormat( Method.TEXT, null, false ) );
    }


    public void setOutputFormat( OutputFormat format )
    {
        super.setOutputFormat( format != null ? format : new OutputFormat( Method.TEXT, null, false ) );
    }




    public void startElement( String namespaceURI, String localName,
                              String rawName, Attributes attrs )
    {
        startElement( rawName == null ? localName : rawName, null );
    }


    public void endElement( String namespaceURI, String localName,
                            String rawName )
    {
        endElement( rawName == null ? localName : rawName );
    }



    
    public void startElement( String tagName, AttributeList attrs )
    {
        boolean      preserveSpace;
        ElementState state;
        
        state = getElementState();
        if ( isDocumentState() ) {
            if ( ! _started )
                startDocument( tagName );
        }
        preserveSpace = state.preserveSpace;

        
        
        state = enterElementState( null, null, tagName, preserveSpace );
    }
    
    
    public void endElement( String tagName )
    {
        ElementState state;
        
        state = getElementState();
        state = leaveElementState();
        state.afterElement = true;
        state.empty = false;
        if ( isDocumentState() )
            _printer.flush();
    }


    public void processingInstruction( String target, String code )
    {
    }


    public void comment( String text )
    {
    }


    public void comment( char[] chars, int start, int length )
    {
    }


    public void characters( char[] chars, int start, int length )
    {
        ElementState state;
        
        state = content();
        state.doCData = state.inCData = false;
        printText( chars, start, length, true, true );
    }


    protected void characters( String text, boolean unescaped )
    {
        ElementState state;
        
        state = content();
        state.doCData = state.inCData = false;
        printText( text, true, true );
    }




    /**
     * Called to serialize the document's DOCTYPE by the root element.
     * <p>
     * This method will check if it has not been called before ({@link #_started}),
     * will serialize the document type declaration, and will serialize all
     * pre-root comments and PIs that were accumulated in the document
     * (see {@link #serializePreRoot}). Pre-root will be serialized even if
     * this is not the first root element of the document.
     */
    protected void startDocument( String rootTagName )
    {
        _printer.leaveDTD();
        
        _started = true;
        serializePreRoot();
    }


    /**
     * Called to serialize a DOM element. Equivalent to calling {@link
     * #startElement}, {@link #endElement} and serializing everything
     * inbetween, but better optimized.
     */
    protected void serializeElement( Element elem )
    {
        Node         child;
        ElementState state;
        boolean      preserveSpace;
        String       tagName;
        
        tagName = elem.getTagName();
        state = getElementState();
        if ( isDocumentState() ) {
            if ( ! _started )
                startDocument( tagName );
        }
        preserveSpace = state.preserveSpace;

        
        
        if ( elem.hasChildNodes() ) {
            state = enterElementState( null, null, tagName, preserveSpace );
            child = elem.getFirstChild();
            while ( child != null ) {
                serializeNode( child );
                child = child.getNextSibling();
            }
            endElement( tagName );
        } else {
            if ( ! isDocumentState() ) {
                state.afterElement = true;
                state.empty = false;
            }
        }
    }


    /**
     * Serialize the DOM node. This method is unique to the Text serializer.
     *
     * @param node The node to serialize
     */
    protected void serializeNode( Node node )
    {
        switch ( node.getNodeType() ) {
        case Node.TEXT_NODE : {
            String text;

            text = node.getNodeValue();
            if ( text != null )
                characters( node.getNodeValue(), true );
            break;
        }
            
        case Node.CDATA_SECTION_NODE : {
            String text;

            text = node.getNodeValue();
            if ( text != null )
                characters( node.getNodeValue(), true );
            break;
        }
            
        case Node.COMMENT_NODE :
            break;
            
        case Node.ENTITY_REFERENCE_NODE :
            break;
            
        case Node.PROCESSING_INSTRUCTION_NODE :
            break;
            
        case Node.ELEMENT_NODE :
            serializeElement( (Element) node );
            break;
            
        case Node.DOCUMENT_NODE :
        case Node.DOCUMENT_FRAGMENT_NODE : {
            Node         child;
            
            child = node.getFirstChild();
            while ( child != null ) {
                serializeNode( child );
                child = child.getNextSibling();
            }
            break;
        }
        
        default:
            break;
        }
    }
    
    
    protected ElementState content()
    {
        ElementState state;
        
        state = getElementState();
        if ( ! isDocumentState() ) {
            if ( state.empty )
                state.empty = false;
            state.afterElement = false;
        }
        return state;
    }
    
    
    protected String getEntityRef( char ch )
    {
        return null;
    }


}


