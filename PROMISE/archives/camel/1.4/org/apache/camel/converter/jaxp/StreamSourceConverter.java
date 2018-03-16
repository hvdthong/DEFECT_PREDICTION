package org.apache.camel.converter.jaxp;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.apache.camel.Converter;

/**
 * A converter from {@link StreamSource} objects
 *
 * @version $Revision: 669864 $
 */
@Converter
public class StreamSourceConverter {

    @Converter
    public InputStream toInputStream(StreamSource source) {
        return source.getInputStream();
    }

    @Converter
    public Reader toReader(StreamSource source) {
        return source.getReader();
    }
}
