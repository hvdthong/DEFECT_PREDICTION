package org.apache.camel.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.converter.IOConverter;
import org.apache.camel.spi.DataFormat;

/**
 * using Java Serialiation.
 *
 * @version $Revision: 660275 $
 */
public class SerializationDataFormat implements DataFormat {

    public void marshal(Exchange exchange, Object graph, OutputStream stream) throws IOException {
        ObjectOutput out = IOConverter.toObjectOutput(stream);
        out.writeObject(graph);
        out.flush();
    }

    public Object unmarshal(Exchange exchange, InputStream stream) throws IOException, ClassNotFoundException {
        ObjectInput in = IOConverter.toObjectInput(stream);
        return in.readObject();
    }
}
