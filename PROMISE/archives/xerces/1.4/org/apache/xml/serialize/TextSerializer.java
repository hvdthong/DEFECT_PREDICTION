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
import org.xml.sax.SAXException;


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
 * @version $Revision: 317383 $ $Date: 2001-07-21 04:37:06 +0800 (周六, 2001-07-21) $
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @see Serializer
 */
public class TextSerializer
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
        throws SAXException
    {
        startElement( rawName == null ? localName : rawName, null );
    }


    public void endElement( String namespaceURI, String localName,
                            String rawName )
        throws SAXException
    {
        endElement( rawName == null ? localName : rawName );
    }




    public void startElement( String tagName, AttributeList attrs )
        throws SAXException
    {
        boolean      preserveSpace;
        ElementState state;

        try {
            state = getElementState();
            if ( isDocumentState() ) {
                if ( ! _started )
                    startDocument( tagName );
            }
            preserveSpace = state.preserveSpace;



            state = enterElementState( null, null, tagName, preserveSpace );
        } catch ( IOException except ) {
            throw new SAXException( except );
        }
    }


    public void endElement( String tagName )
        throws SAXException
    {
        try {
            endElementIO( tagName );
        } catch ( IOException except ) {
            throw new SAXException( except );
        }
    }


    public void endElementIO( String tagName )
        throws IOException
    {
        ElementState state;

        state = getElementState();
        state = leaveElementState();
        state.afterElement = true;
        state.empty = false;
        if ( isDocumentState() )
            _printer.flush();
    }


    public void processingInstructionIO( String target, String code ) throws IOException
    {
    }


    public void comment( String text )
    {
    }


    public void comment( char[] chars, int start, int length )
    {
    }


    public void characters( char[] chars, int start, int length )
        throws SAXException
    {
        ElementState state;

        try {
            state = content();
            state.doCData = state.inCData = false;
            printText( chars, start, length, true, true );
        } catch ( IOException except ) {
            throw new SAXException( except );
        }
    }


    protected void characters( String text, boolean unescaped )
        throws IOException
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
        throws IOException
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
        throws IOException
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
            endElementIO( tagName );
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
        throws IOException
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


    protected String getEntityRef( int ch )
    {
        return null;
    }


}


