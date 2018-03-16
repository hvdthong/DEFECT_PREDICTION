package org.apache.camel.dataformat.xstream;


import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxReader;
import com.thoughtworks.xstream.io.xml.StaxWriter;

import org.apache.camel.Exchange;
import org.apache.camel.converter.jaxp.StaxConverter;
import org.apache.camel.spi.DataFormat;

/**
 * ({@link DataFormat}) using XmlBeans to marshal to and from XML
 *
 * @version $Revision: 641680 $
 */
public class XStreamDataFormat implements DataFormat {

    private XStream xstream;
    private StaxConverter staxConverter;

    public void marshal(Exchange exchange, Object body, OutputStream stream) throws Exception {
        HierarchicalStreamWriter writer = createHierarchicalStreamWriter(exchange, body, stream);
        getXStream().marshal(body, writer);
    }

    public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
        HierarchicalStreamReader reader = createHierarchicalStreamReader(exchange, stream);
        return getXStream().unmarshal(reader);
    }

    public XStream getXStream() {
        if (xstream == null) {
            xstream = createXStream();
        }
        return xstream;
    }

    public void setXStream(XStream xstream) {
        this.xstream = xstream;
    }

    public StaxConverter getStaxConverter() {
        if (staxConverter == null) {
            staxConverter = new StaxConverter();
        }
        return staxConverter;
    }

    public void setStaxConverter(StaxConverter staxConverter) {
        this.staxConverter = staxConverter;
    }

    protected XStream createXStream() {
        return new XStream();
    }

    protected HierarchicalStreamWriter createHierarchicalStreamWriter(Exchange exchange, Object body, OutputStream stream) throws XMLStreamException {
        XMLStreamWriter xmlWriter = getStaxConverter().createXMLStreamWriter(stream);
        return new StaxWriter(new QNameMap(), xmlWriter);
    }

    protected HierarchicalStreamReader createHierarchicalStreamReader(Exchange exchange, InputStream stream) throws XMLStreamException {
        XMLStreamReader xmlReader = getStaxConverter().createXMLStreamReader(stream);
        return new StaxReader(new QNameMap(), xmlReader);
    }
}
