package org.apache.xalan.serialize;

import java.io.IOException;

import org.w3c.dom.Node;

/**
 * Interface for a DOM serializer implementation.
 * <p>
 * The DOM serializer is a facet of a serializer. A serializer may or may
 * not support a DOM serializer.
 * <p>
 * Example:
 * <pre>
 * Document     doc;
 * Serializer   ser;
 * OutputStream os;
 *
 * ser.setOutputStream( os );
 * ser.asDOMSerializer( doc );
 * </pre>
 *
 *
 * @version Alpha
 * @author <a href="mailto:Scott_Boag/CAM/Lotus@lotus.com">Scott Boag</a>
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 */
public interface DOMSerializer
{
  /**
   * Serializes the DOM node. Throws an exception only if an I/O
   * exception occured while serializing.
   *
   * @param elem The element to serialize
   * @throws IOException An I/O exception occured while serializing
   */
  public void serialize(Node node) throws IOException;
}
