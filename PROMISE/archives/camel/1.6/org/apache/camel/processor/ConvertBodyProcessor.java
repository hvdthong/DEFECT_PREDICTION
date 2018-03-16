package org.apache.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.NoTypeConversionAvailableException;
import org.apache.camel.Processor;
import org.apache.camel.util.ExchangeHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A processor which converts the payload of the input message to be of the given type
 *
 * @version $Revision: 700474 $
 */
public class ConvertBodyProcessor implements Processor {
    private static final transient Log LOG = LogFactory.getLog(ConvertBodyProcessor.class);
    private final Class type;

    public ConvertBodyProcessor(Class type) {
        this.type = type;
    }

    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();        
        Object value = null;
        try {
            value = in.getBody(type);
        } catch (NoTypeConversionAvailableException e) {
            LOG.warn("Could not convert body of IN message: " + in + " to type: " + type.getName());
        }
        if (exchange.getPattern().isOutCapable()) {
            Message out = exchange.getOut();
            out.copyFrom(in);
            out.setBody(value);
        } else {
            in.setBody(value);
        }
    }
}
