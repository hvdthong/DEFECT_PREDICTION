package org.apache.xml.serialize;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Enumeration;

import org.w3c.dom.*;
import org.xml.sax.DocumentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;


/**
 * Implements an XML serializer supporting both DOM and SAX pretty
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
 * <p>
 * For elements that are not specified as whitespace preserving,
 * the serializer will potentially break long text lines at space
 * boundaries, indent lines, and serialize elements on separate
 * lines. Line terminators will be regarded as spaces, and
 * spaces at beginning of line will be stripped.
 *
 *
 * @version $Revision: 317921 $ $Date: 2001-11-16 02:56:03 +0800 (周五, 2001-11-16) $
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @see Serializer
 */
public class XMLSerializer
    extends BaseMarkupSerializer
{


    /**
     * Constructs a new serializer. The serializer cannot be used without
     * calling {@link #setOutputCharStream} or {@link #setOutputByteStream}
     * first.
     */
    public XMLSerializer()
    {
        super( new OutputFormat( Method.XML, null, false ) );
    }


    /**
     * Constructs a new serializer. The serializer cannot be used without
     * calling {@link #setOutputCharStream} or {@link #setOutputByteStream}
     * first.
     */
    public XMLSerializer( OutputFormat format )
    {
        super( format != null ? format : new OutputFormat( Method.XML, null, false ) );
        _format.setMethod( Method.XML );
    }


    /**
     * Constructs a new serializer that writes to the specified writer
     * using the specified output format. If <tt>format</tt> is null,
     * will use a default output format.
     *
     * @param writer The writer to use
     * @param format The output format to use, null for the default
     */
    public XMLSerializer( Writer writer, OutputFormat format )
    {
        super( format != null ? format : new OutputFormat( Method.XML, null, false ) );
        _format.setMethod( Method.XML );
        setOutputCharStream( writer );
    }


    /**
     * Constructs a new serializer that writes to the specified output
     * stream using the specified output format. If <tt>format</tt>
     * is null, will use a default output format.
     *
     * @param output The output stream to use
     * @param format The output format to use, null for the default
     */
    public XMLSerializer( OutputStream output, OutputFormat format )
    {
        super( format != null ? format : new OutputFormat( Method.XML, null, false ) );
        _format.setMethod( Method.XML );
        setOutputByteStream( output );
    }


    public void setOutputFormat( OutputFormat format )
    {
        super.setOutputFormat( format != null ? format : new OutputFormat( Method.XML, null, false ) );
    }




    public void startElement( String namespaceURI, String localName,
                              String rawName, Attributes attrs )
        throws SAXException
    {
        int          i;
        boolean      preserveSpace;
        ElementState state;
        String       name;
        String       value;
        boolean      addNSAttr = false;

        try {
        if ( _printer == null )
            throw new IllegalStateException( "SER002 No writer supplied for serializer" );

        state = getElementState();
        if ( isDocumentState() ) {
            if ( ! _started )
                    startDocument( ( localName == null || localName.length() == 0 ) ? rawName : localName );
        } else {
            if ( state.empty )
                _printer.printText( '>' );
            if ( state.inCData )
            {
                _printer.printText( "]]>" );
                state.inCData = false;
            }
            if ( _indenting && ! state.preserveSpace &&
                 ( state.empty || state.afterElement || state.afterComment) )
                _printer.breakLine();
        }
        preserveSpace = state.preserveSpace;

            attrs = extractNamespaces(attrs);

            if ( rawName == null || rawName.length() == 0 ) {
                if ( localName == null )
                    throw new SAXException( "No rawName and localName is null" );
                if ( namespaceURI != null && ! namespaceURI.equals( "" ) ) {
                String prefix;
                prefix = getPrefix( namespaceURI );
                    if ( prefix != null && prefix.length() > 0 )
                    rawName = prefix + ":" + localName;
                    else
                        rawName = localName;
                } else
                    rawName = localName;
            addNSAttr = true;
        }

        _printer.printText( '<' );
        _printer.printText( rawName );
        _printer.indent();

        if ( attrs != null ) {
            for ( i = 0 ; i < attrs.getLength() ; ++i ) {
                _printer.printSpace();

                name = attrs.getQName( i );
                    if ( name != null && name.length() == 0 ) {
                    String prefix;
                    String attrURI;

                    name = attrs.getLocalName( i );
                    attrURI = attrs.getURI( i );
                        if ( ( attrURI != null && attrURI.length() != 0 ) &&
                             ( namespaceURI == null || namespaceURI.length() == 0 ||
                                              ! attrURI.equals( namespaceURI ) ) ) {
                        prefix = getPrefix( attrURI );
                        if ( prefix != null && prefix.length() > 0 )
                            name = prefix + ":" + name;
                    }
                }

                value = attrs.getValue( i );
                if ( value == null )
                    value = "";
                _printer.printText( name );
                _printer.printText( "=\"" );
                printEscaped( value );
                _printer.printText( '"' );

                if ( name.equals( "xml:space" ) ) {
                    if ( value.equals( "preserve" ) )
                        preserveSpace = true;
                    else
                        preserveSpace = _format.getPreserveSpace();
                }
            }
        }

            if ( _prefixes != null ) {
            Enumeration enum;

            enum = _prefixes.keys();
            while ( enum.hasMoreElements() ) {
                _printer.printSpace();
                value = (String) enum.nextElement();
                name = (String) _prefixes.get( value );
                if ( name.length() == 0 ) {
                    _printer.printText( "xmlns=\"" );
                    printEscaped( value );
                    _printer.printText( '"' );
                } else {
                    _printer.printText( "xmlns:" );
                    _printer.printText( name );
                    _printer.printText( "=\"" );
                    printEscaped( value );
                    _printer.printText( '"' );
                }
            }
        }

        state = enterElementState( namespaceURI, localName, rawName, preserveSpace );
            name = ( localName == null || localName.length() == 0 ) ? rawName : namespaceURI + "^" + localName;
            state.doCData = _format.isCDataElement( name );
            state.unescaped = _format.isNonEscapingElement( name );
        } catch ( IOException except ) {
            throw new SAXException( except );
        }
    }


    public void endElement( String namespaceURI, String localName,
                            String rawName )
        throws SAXException
    {
        try {
            endElementIO( namespaceURI, localName, rawName );
        } catch ( IOException except ) {
            throw new SAXException( except );
        }
    }


    public void endElementIO( String namespaceURI, String localName,
                            String rawName )
        throws IOException
    {
        ElementState state;

        _printer.unindent();
        state = getElementState();
        if ( state.empty ) {
            _printer.printText( "/>" );
        } else {
            if ( state.inCData )
                _printer.printText( "]]>" );
            if ( _indenting && ! state.preserveSpace && (state.afterElement || state.afterComment) )
                _printer.breakLine();
            _printer.printText( "</" );
            _printer.printText( state.rawName );
            _printer.printText( '>' );
        }
        state = leaveElementState();
        state.afterElement = true;
        state.afterComment = false;
        state.empty = false;
        if ( isDocumentState() )
            _printer.flush();
    }




    public void startElement( String tagName, AttributeList attrs )
        throws SAXException
    {
        int          i;
        boolean      preserveSpace;
        ElementState state;
        String       name;
        String       value;

        try {
        if ( _printer == null )
            throw new IllegalStateException( "SER002 No writer supplied for serializer" );

        state = getElementState();
        if ( isDocumentState() ) {
            if ( ! _started )
                startDocument( tagName );
        } else {
            if ( state.empty )
                _printer.printText( '>' );
            if ( state.inCData )
            {
                _printer.printText( "]]>" );
                state.inCData = false;
            }
            if ( _indenting && ! state.preserveSpace &&
                 ( state.empty || state.afterElement || state.afterComment) )
                _printer.breakLine();
        }
        preserveSpace = state.preserveSpace;


        _printer.printText( '<' );
        _printer.printText( tagName );
        _printer.indent();

        if ( attrs != null ) {
            for ( i = 0 ; i < attrs.getLength() ; ++i ) {
                _printer.printSpace();
                name = attrs.getName( i );
                value = attrs.getValue( i );
                if ( value != null ) {
                    _printer.printText( name );
                    _printer.printText( "=\"" );
                    printEscaped( value );
                    _printer.printText( '"' );
                }

                if ( name.equals( "xml:space" ) ) {
                    if ( value.equals( "preserve" ) )
                        preserveSpace = true;
                    else
                        preserveSpace = _format.getPreserveSpace();
                }
            }
        }
        state = enterElementState( null, null, tagName, preserveSpace );
        state.doCData = _format.isCDataElement( tagName );
        state.unescaped = _format.isNonEscapingElement( tagName );
        } catch ( IOException except ) {
            throw new SAXException( except );
    }

    }


    public void endElement( String tagName )
        throws SAXException
    {
        endElement( null, null, tagName );
    }





    /**
     * Called to serialize the document's DOCTYPE by the root element.
     * The document type declaration must name the root element,
     * but the root element is only known when that element is serialized,
     * and not at the start of the document.
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
        int    i;
        String dtd;

        dtd = _printer.leaveDTD();
        if ( ! _started ) {

            if ( ! _format.getOmitXMLDeclaration() ) {
                StringBuffer    buffer;

                buffer = new StringBuffer( "<?xml version=\"" );
                if ( _format.getVersion() != null )
                    buffer.append( _format.getVersion() );
                else
                    buffer.append( "1.0" );
                buffer.append( '"' );
                if ( _format.getEncoding() != null ) {
                    buffer.append( " encoding=\"" );
                    buffer.append( _format.getEncoding() );
                    buffer.append( '"' );
                }
                if ( _format.getStandalone() && _docTypeSystemId == null &&
                     _docTypePublicId == null )
                    buffer.append( " standalone=\"yes\"" );
                buffer.append( "?>" );
                _printer.printText( buffer );
                _printer.breakLine();
            }

            if ( ! _format.getOmitDocumentType() ) {
                if ( _docTypeSystemId != null ) {
                    _printer.printText( "<!DOCTYPE " );
                    _printer.printText( rootTagName );
                    if ( _docTypePublicId != null ) {
                        _printer.printText( " PUBLIC " );
                        printDoctypeURL( _docTypePublicId );
                        if ( _indenting ) {
                            _printer.breakLine();
                            for ( i = 0 ; i < 18 + rootTagName.length() ; ++i )
                                _printer.printText( " " );
                        } else
                            _printer.printText( " " );
                    printDoctypeURL( _docTypeSystemId );
                    }
                    else {
                        _printer.printText( " SYSTEM " );
                        printDoctypeURL( _docTypeSystemId );
                    }

                    if ( dtd != null && dtd.length() > 0 ) {
                        _printer.printText( " [" );
                        printText( dtd, true, true );
                        _printer.printText( ']' );
                    }

                    _printer.printText( ">" );
                    _printer.breakLine();
                } else if ( dtd != null && dtd.length() > 0 ) {
                    _printer.printText( "<!DOCTYPE " );
                    _printer.printText( rootTagName );
                    _printer.printText( " [" );
                    printText( dtd, true, true );
                    _printer.printText( "]>" );
                    _printer.breakLine();
                }
            }
        }
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
        Attr         attr;
        NamedNodeMap attrMap;
        int          i;
        Node         child;
        ElementState state;
        boolean      preserveSpace;
        String       name;
        String       value;
        String       tagName;

        tagName = elem.getTagName();
        state = getElementState();
        if ( isDocumentState() ) {
            if ( ! _started )
                startDocument( tagName );
        } else {
            if ( state.empty )
                _printer.printText( '>' );
            if ( state.inCData )
            {
                _printer.printText( "]]>" );
                state.inCData = false;
            }
            if ( _indenting && ! state.preserveSpace &&
                 ( state.empty || state.afterElement || state.afterComment) )
                _printer.breakLine();
        }
        preserveSpace = state.preserveSpace;


        _printer.printText( '<' );
        _printer.printText( tagName );
        _printer.indent();

        attrMap = elem.getAttributes();
        if ( attrMap != null ) {
            for ( i = 0 ; i < attrMap.getLength() ; ++i ) {
                attr = (Attr) attrMap.item( i );
                name = attr.getName();
                value = attr.getValue();
                if ( value == null )
                    value = "";
                if ( attr.getSpecified() ) {
                    _printer.printSpace();
                    _printer.printText( name );
                    _printer.printText( "=\"" );
                    printEscaped( value );
                    _printer.printText( '"' );
                }
                if ( name.equals( "xml:space" ) ) {
                    if ( value.equals( "preserve" ) )
                        preserveSpace = true;
                    else
                        preserveSpace = _format.getPreserveSpace();
                }
            }
        }

        if ( elem.hasChildNodes() ) {
            state = enterElementState( null, null, tagName, preserveSpace );
            state.doCData = _format.isCDataElement( tagName );
            state.unescaped = _format.isNonEscapingElement( tagName );
            child = elem.getFirstChild();
            while ( child != null ) {
                serializeNode( child );
                child = child.getNextSibling();
            }
            endElementIO( null, null, tagName );
        } else {
            _printer.unindent();
            _printer.printText( "/>" );
            state.afterElement = true;
            state.afterComment = false;
            state.empty = false;
            if ( isDocumentState() )
                _printer.flush();
        }
    }


    protected String getEntityRef( int ch )
    {
        switch ( ch ) {
        case '<':
            return "lt";
        case '>':
            return "gt";
        case '"':
            return "quot";
        case '\'':
            return "apos";
        case '&':
            return "amp";
        }
        return null;
    }


    /** Retrieve and remove the namespaces declarations from the list of attributes.
     *
     */
    private Attributes extractNamespaces( Attributes attrs )
        throws SAXException
    {
        AttributesImpl attrsOnly;
        String         rawName;
        int            i;
        int            indexColon;
        String         prefix;
        int            length;

        length = attrs.getLength();
        attrsOnly = new AttributesImpl( attrs );

        for ( i = length - 1 ; i >= 0 ; --i ) {
            rawName = attrsOnly.getQName( i );

            if ( rawName.startsWith( "xmlns" ) ) {
                if (rawName.length() == 5) {
                    startPrefixMapping( "", attrs.getValue( i ) );
                    attrsOnly.removeAttribute( i );
                } else if (rawName.charAt(5) == ':') {
                    startPrefixMapping(rawName.substring(6), attrs.getValue(i));
                    attrsOnly.removeAttribute( i );
                }
            }
        }
        return attrsOnly;
    }
}


