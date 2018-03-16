package org.apache.xml.serialize;


import java.io.Writer;
import java.io.OutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.xml.sax.DocumentHandler;
import org.xml.sax.ContentHandler;


/**
 * Interface for a DOM serializer implementation, factory for DOM and SAX
 * serializers, and static methods for serializing DOM documents.
 * <p>
 * To serialize a document using SAX events, create a compatible serializer
 * using {@link #makeSAXSerializer} and pass it around as a {@link
 * DocumentHandler}. If an I/O error occurs while serializing, it will
 * be thrown by {@link DocumentHandler#endDocument}. The SAX serializer
 * may also be used as {@link DTDHandler}, {@link DeclHandler} and
 * {@link LexicalHandler}.
 * <p>
 * To serialize a DOM document or DOM element, create a compatible
 * serializer using {@link #makeSerializer} and call it's {@link
 * #serialize(Document)} or {@link #serialize(Element)} methods.
 * Both methods would produce a full XML document, to serizlie only
 * the portion of the document use {@link OutputFormat#setOmitXMLDeclaration}
 * and specify no document type.
 * <p>
 * The convenience method {@link #serialize(Document,Writer,OutputFormat)}
 * creates a serializer and calls {@link #serizlie(Document)} on that
 * serialized.
 * <p>
 * The {@link OutputFormat} dictates what underlying serialized is used
 * to serialize the document based on the specified method. If the output
 * format or method are missing, the default is an XML serializer with
 * UTF-8 encoding and now indentation.
 * 
 *
 * @version $Revision: 316040 $ $Date: 2000-08-31 02:59:22 +0800 (周四, 2000-08-31) $
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @author <a href="mailto:Scott_Boag/CAM/Lotus@lotus.com">Scott Boag</a>
 * @see DocumentHandler
 * @see ContentHandler
 * @see OutputFormat
 * @see DOMSerializer
 */
public interface Serializer
{


    /**
     * Specifies an output stream to which the document should be
     * serialized. This method should not be called while the
     * serializer is in the process of serializing a document.
     */
    public void setOutputByteStream(OutputStream output);
    

    /**
     * Specifies a writer to which the document should be serialized.
     * This method should not be called while the serializer is in
     * the process of serializing a document.
     */
    public void setOutputCharStream( Writer output );


    /**
     * Specifies an output format for this serializer. It the
     * serializer has already been associated with an output format,
     * it will switch to the new format. This method should not be
     * called while the serializer is in the process of serializing
     * a document.
     *
     * @param format The output format to use
     */
    public void setOutputFormat( OutputFormat format );


    /**
     * Return a {@link DocumentHandler} interface into this serializer.
     * If the serializer does not support the {@link DocumentHandler}
     * interface, it should return null.
     */
    public DocumentHandler asDocumentHandler()
        throws IOException;


    /**
     * Return a {@link ContentHandler} interface into this serializer.
     * If the serializer does not support the {@link ContentHandler}
     * interface, it should return null.
     */
    public ContentHandler asContentHandler()
        throws IOException;


    /**
     * Return a {@link DOMSerializer} interface into this serializer.
     * If the serializer does not support the {@link DOMSerializer}
     * interface, it should return null.
     */
    public DOMSerializer asDOMSerializer()
        throws IOException;


}





