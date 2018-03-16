package org.apache.camel.processor;

import java.io.ByteArrayOutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.spi.DataFormat;

/**
 * Marshals the body of the incoming message using the given
 *
 * @version $Revision: 640438 $
 */
public class MarshalProcessor implements Processor {
    private final DataFormat dataFormat;

    public MarshalProcessor(DataFormat dataFormat) {
        this.dataFormat = dataFormat;
    }

    public void process(Exchange exchange) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Message in = exchange.getIn();
        Object body = in.getBody();

        Message out = exchange.getOut(true);
        out.copyFrom(in);

        dataFormat.marshal(exchange, body, buffer);
        byte[] data = buffer.toByteArray();
        out.setBody(data);
    }
}
