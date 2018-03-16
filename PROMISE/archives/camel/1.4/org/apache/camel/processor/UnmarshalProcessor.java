package org.apache.camel.processor;

import java.io.InputStream;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.util.ExchangeHelper;

/**
 * Unmarshals the body of the incoming message using the given
 *
 * @version $Revision: 640438 $
 */
public class UnmarshalProcessor implements Processor {
    private final DataFormat dataFormat;

    public UnmarshalProcessor(DataFormat dataFormat) {
        this.dataFormat = dataFormat;
    }

    public void process(Exchange exchange) throws Exception {
        InputStream stream = ExchangeHelper.getMandatoryInBody(exchange, InputStream.class);
        try {

            Message out = exchange.getOut(true);
            out.copyFrom(exchange.getIn());

            Object result = dataFormat.unmarshal(exchange, stream);
            out.setBody(result);
        } finally {
            if (null != stream) {
                stream.close();
            }
        }
    }
}
