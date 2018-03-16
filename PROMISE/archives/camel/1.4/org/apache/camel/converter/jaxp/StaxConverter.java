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

import org.apache.camel.Converter;

/**
 * A converter of StAX objects
 *
 * @version $Revision: 640438 $
 */
@Converter
public class StaxConverter {
    private XMLInputFactory inputFactory;
    private XMLOutputFactory outputFactory;

    @Converter
    public XMLEventWriter createXMLEventWriter(OutputStream out) throws XMLStreamException {
        return getOutputFactory().createXMLEventWriter(out);
    }

    @Converter
    public XMLEventWriter createXMLEventWriter(Writer writer) throws XMLStreamException {
        return getOutputFactory().createXMLEventWriter(writer);
    }

    @Converter
    public XMLEventWriter createXMLEventWriter(Result result) throws XMLStreamException {
        return getOutputFactory().createXMLEventWriter(result);
    }

    @Converter
    public XMLStreamWriter createXMLStreamWriter(OutputStream outputStream) throws XMLStreamException {
        return getOutputFactory().createXMLStreamWriter(outputStream);
    }

    @Converter
    public XMLStreamWriter createXMLStreamWriter(Writer writer) throws XMLStreamException {
        return getOutputFactory().createXMLStreamWriter(writer);
    }

    @Converter
    public XMLStreamWriter createXMLStreamWriter(Result result) throws XMLStreamException {
        return getOutputFactory().createXMLStreamWriter(result);
    }

    @Converter
    public XMLStreamReader createXMLStreamReader(InputStream in) throws XMLStreamException {
        return getInputFactory().createXMLStreamReader(in);
    }

    @Converter
    public XMLStreamReader createXMLStreamReader(Reader in) throws XMLStreamException {
        return getInputFactory().createXMLStreamReader(in);
    }

    @Converter
    public XMLStreamReader createXMLStreamReader(Source in) throws XMLStreamException {
        return getInputFactory().createXMLStreamReader(in);
    }

    @Converter
    public XMLEventReader createXMLEventReader(InputStream in) throws XMLStreamException {
        return getInputFactory().createXMLEventReader(in);
    }

    @Converter
    public XMLEventReader createXMLEventReader(Reader in) throws XMLStreamException {
        return getInputFactory().createXMLEventReader(in);
    }

    @Converter
    public XMLEventReader createXMLEventReader(XMLStreamReader in) throws XMLStreamException {
        return getInputFactory().createXMLEventReader(in);
    }

    @Converter
    public XMLEventReader createXMLEventReader(Source in) throws XMLStreamException {
        return getInputFactory().createXMLEventReader(in);
    }


    public XMLInputFactory getInputFactory() {
        if (inputFactory == null) {
            inputFactory = XMLInputFactory.newInstance();
        }
        return inputFactory;
    }

    public void setInputFactory(XMLInputFactory inputFactory) {
        this.inputFactory = inputFactory;
    }

    public XMLOutputFactory getOutputFactory() {
        if (outputFactory == null) {
            outputFactory = XMLOutputFactory.newInstance();
        }
        return outputFactory;
    }

    public void setOutputFactory(XMLOutputFactory outputFactory) {
        this.outputFactory = outputFactory;
    }
}
