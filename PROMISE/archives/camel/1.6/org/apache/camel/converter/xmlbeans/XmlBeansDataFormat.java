package org.apache.camel.converter.xmlbeans;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.util.ExchangeHelper;
import org.apache.xmlbeans.XmlObject;

/**
 * ({@link DataFormat}) using XmlBeans to marshal to and from XML
 *
 * @version $Revision: 641680 $
 */
public class XmlBeansDataFormat implements DataFormat {

    public void marshal(Exchange exchange, Object body, OutputStream stream) throws Exception {
        XmlObject object = ExchangeHelper.convertToMandatoryType(exchange, XmlObject.class, body);
        object.save(stream);
    }

    public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
        return XmlObject.Factory.parse(stream);
    }
}
