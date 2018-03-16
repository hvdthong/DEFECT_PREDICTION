package org.apache.camel.spi;

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.camel.Exchange;

/**
 * Represents a
 * used to marshal objects to and from streams
 * such as Java Serialization or using JAXB2 to encode/decode objects using XML
 * or using SOAP encoding.
 *
 * @version $Revision: 1.1 $
 */
public interface DataFormat {

    /**
     * Marshals the object to the given Stream.
     */
    void marshal(Exchange exchange, Object graph, OutputStream stream) throws Exception;

    /**
     * Unmarshals the given stream into an object.
     */
    Object unmarshal(Exchange exchange, InputStream stream) throws Exception;
}
