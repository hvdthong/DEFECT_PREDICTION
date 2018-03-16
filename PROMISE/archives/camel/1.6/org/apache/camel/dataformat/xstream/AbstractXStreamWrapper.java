package org.apache.camel.dataformat.xstream;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.stream.XMLStreamException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import org.apache.camel.Exchange;
import org.apache.camel.converter.jaxp.StaxConverter;
import org.apache.camel.spi.DataFormat;

/**
 * ({@link DataFormat}) interface which leverage the XStream library for XML or JSON's marshaling and unmarshaling
 *
 * @version $Revision: 711862 $
 */

public abstract class AbstractXStreamWrapper implements DataFormat {
    
    private XStream xstream;
    private StaxConverter staxConverter;
    
    public AbstractXStreamWrapper() {
        
    }
    
    public AbstractXStreamWrapper(XStream xstream) {
        this.xstream = xstream;
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
    
    protected XStream createXStream() {
        return new XStream();
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
    
    public void marshal(Exchange exchange, Object body, OutputStream stream) throws Exception {
        HierarchicalStreamWriter writer = createHierarchicalStreamWriter(exchange, body, stream);
        try {
            getXStream().marshal(body, writer);
        } finally {
            writer.close();
        }
    }

    public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
        HierarchicalStreamReader reader = createHierarchicalStreamReader(exchange, stream);
        try {
            return getXStream().unmarshal(reader);
        } finally {
            reader.close();
        }
    }
    
    protected abstract HierarchicalStreamWriter createHierarchicalStreamWriter(Exchange exchange, Object body, OutputStream stream) throws XMLStreamException;

    protected abstract HierarchicalStreamReader createHierarchicalStreamReader(Exchange exchange, InputStream stream) throws XMLStreamException;
}
