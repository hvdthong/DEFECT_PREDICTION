package org.apache.camel.converter.xmlbeans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import org.apache.camel.Converter;
import org.apache.camel.converter.IOConverter;
import org.apache.camel.converter.NIOConverter;
import org.apache.camel.converter.jaxp.XmlConverter;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.piccolo.xml.XMLStreamReader;

/**
 * of XMLBeans objects
 *
 * @version $Revision: 641680 $
 */
@Converter
public class XmlBeansConverter {
    private XmlConverter xmlConverter = new XmlConverter();

    @Converter
    public static XmlObject toXmlObject(File value) throws IOException, XmlException {
        return XmlObject.Factory.parse(value);
    }

    @Converter
    public static XmlObject toXmlObject(Reader value) throws IOException, XmlException {
        return XmlObject.Factory.parse(value);
    }

    @Converter
    public static XmlObject toXmlObject(Node value) throws IOException, XmlException {
        return XmlObject.Factory.parse(value);
    }

    @Converter
    public static XmlObject toXmlObject(InputStream value) throws IOException, XmlException {
        return XmlObject.Factory.parse(value);
    }

    @Converter
    public static XmlObject toXmlObject(String value) throws IOException, XmlException {
        return toXmlObject(IOConverter.toInputStream(value));
    }

    @Converter
    public static XmlObject toXmlObject(byte[] value) throws IOException, XmlException {
        return toXmlObject(IOConverter.toInputStream(value));
    }

    @Converter
    public static XmlObject toXmlObject(ByteBuffer value) throws IOException, XmlException {
        return toXmlObject(NIOConverter.toInputStream(value));
    }

    @Converter
    public static XmlObject toXmlObject(XMLStreamReader value) throws IOException, XmlException {
        return XmlObject.Factory.parse(value);
    }

    @Converter
    public XmlObject toXmlObject(Source value) throws IOException, XmlException, TransformerException, ParserConfigurationException, SAXException {
        Document document = getXmlConverter().toDOMDocument(value);
        return toXmlObject(document);
    }

    public XmlConverter getXmlConverter() {
        return xmlConverter;
    }

    public void setXmlConverter(XmlConverter xmlConverter) {
        this.xmlConverter = xmlConverter;
    }
}
