package org.apache.xml.serializer;

import java.io.IOException;

import org.w3c.dom.Node;

/**
 * Interface for a DOM serializer implementation.
 * <p>
 * The DOMSerializer is a facet of a serializer and is obtained from the
 * asDOMSerializer() method of the Serializer interface. 
 * A serializer may or may not support a DOM serializer, if it does not then the 
 * return value from asDOMSerializer() is null.
 * <p>
 * Example:
 * <pre>
 * Document     doc;
 * Serializer   ser;
 * OutputStream os;
 * 
 * ser = ...;
 * os = ...;
 *
 * ser.setOutputStream( os );
 * DOMSerialzier dser = ser.asDOMSerializer();
 * dser.serialize(doc);
 * </pre>
 * 
 * @see Serializer
 * 
 * @xsl.usage general
 *
 */
public interface DOMSerializer
{
    /**
     * Serializes the DOM node. Throws an exception only if an I/O
     * exception occured while serializing.
     * 
     * This interface is a public API.
     *
     * @param node the DOM node to serialize
     * @throws IOException if an I/O exception occured while serializing
     */
    public void serialize(Node node) throws IOException;
}
