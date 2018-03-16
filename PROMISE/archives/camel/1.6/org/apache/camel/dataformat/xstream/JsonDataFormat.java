package org.apache.camel.dataformat.xstream;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import javax.xml.stream.XMLStreamException;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxReader;
import com.thoughtworks.xstream.io.xml.StaxWriter;

import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;
import org.codehaus.jettison.mapped.MappedXMLInputFactory;
import org.codehaus.jettison.mapped.MappedXMLOutputFactory;

/**
 * ({@link DataFormat}) using XStream and Jettison to marshal to and from JSON
 *
 * @version $Revision: 711862 $
 */

public class JsonDataFormat extends AbstractXStreamWrapper {
    private final MappedXMLOutputFactory mof;
    private final MappedXMLInputFactory mif;
    
    public JsonDataFormat() {
        final HashMap nstjsons = new HashMap();
        mof = new MappedXMLOutputFactory(nstjsons);
        mif = new MappedXMLInputFactory(nstjsons);
    }
    
    
    
    protected HierarchicalStreamWriter createHierarchicalStreamWriter(Exchange exchange, Object body, OutputStream stream) throws XMLStreamException {        
        return new StaxWriter(new QNameMap(), mof.createXMLStreamWriter(stream));
    }

    protected HierarchicalStreamReader createHierarchicalStreamReader(Exchange exchange, InputStream stream) throws XMLStreamException {        
        return new StaxReader(new QNameMap(), mif.createXMLStreamReader(stream));
    }

}
