package org.apache.camel.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.converter.IOConverter;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.util.ExchangeHelper;

/**
 * charset encoding.
 *
 * @version $Revision: 647575 $
 */
public class StringDataFormat implements DataFormat {

    private String charset;

    public StringDataFormat(String charset) {
        this.charset = charset;
    }

    public void marshal(Exchange exchange, Object graph, OutputStream stream) throws IOException {
        String text = ExchangeHelper.convertToType(exchange, String.class, graph);

        byte[] bytes;
        if (charset != null) {
            bytes = text.getBytes(charset);
        } else {
            bytes = text.getBytes();
        }
        stream.write(bytes);
    }

    public Object unmarshal(Exchange exchange, InputStream stream) throws IOException, ClassNotFoundException {
        byte[] bytes = IOConverter.toBytes(stream);

        String answer;
        if (charset != null) {
            answer = new String(bytes, charset);
        } else {
            answer = new String(bytes);
        }

        return answer;
    }
    
}
