package org.apache.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

/**
 * The processor which implements the ThrowFault DSL
 */
public class ThrowFaultProcessor implements Processor {
    private Throwable fault;

    public ThrowFaultProcessor(Throwable fault) {
        this.fault = fault;
    }

    /**
     * Set the fault message in the exchange
     * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
     */
    public void process(Exchange exchange) throws Exception {
        Message message = exchange.getFault();
        message.setBody(fault);
    }

}
