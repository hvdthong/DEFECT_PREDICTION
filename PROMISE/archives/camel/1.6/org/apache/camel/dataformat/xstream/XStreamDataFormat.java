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
 * ({@link DataFormat}) using XStream to marshal to and from XML
 *
 * @version $Revision: 711862 $
 */
public class XStreamDataFormat extends AbstractXStreamWrapper  {
    
    public XStreamDataFormat() {
        super();
    }

    public XStreamDataFormat(XStream xstream) {
        super(xstream);
    }

    /**
     * A factory method which takes a collection of types to be annotated
     */
    public static XStreamDataFormat processAnnotations(Iterable<Class<?>> types) {
        XStreamDataFormat answer = new XStreamDataFormat();
        XStream xstream = answer.getXStream();
        for (Class<?> type : types) {
            xstream.processAnnotations(type);
        }
        return answer;
    }

    /**
     * A factory method which takes a number of types to be annotated
     */
    public static XStreamDataFormat processAnnotations(Class<?>... types) {
        XStreamDataFormat answer = new XStreamDataFormat();
        XStream xstream = answer.getXStream();
        for (Class<?> type : types) {
            xstream.processAnnotations(type);
        }
        return answer;
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
