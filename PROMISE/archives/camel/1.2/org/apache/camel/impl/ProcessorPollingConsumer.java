package org.apache.camel.impl;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.PollingConsumer;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeExchangeException;
import org.apache.camel.util.ServiceHelper;

/**
 * A simple implementation of {@link PollingConsumer} which just uses
 * a {@link Processor}. This implementation does not support timeout based
 * receive methods such as {@link #receive(long)}
 *
 * @version $Revision: 1.1 $
 */
public class ProcessorPollingConsumer extends PollingConsumerSupport {
    private Processor processor;

    public ProcessorPollingConsumer(Endpoint endpoint, Processor processor) {
        super(endpoint);
        this.processor = processor;
    }

    protected void doStart() throws Exception {
        ServiceHelper.startService(processor);
    }

    protected void doStop() throws Exception {
        ServiceHelper.stopService(processor);
    }

    public Exchange receive() {
        Exchange exchange = getEndpoint().createExchange();
        try {
            processor.process(exchange);
        }
        catch (Exception e) {
            throw new RuntimeExchangeException(e, exchange);
        }
        return exchange;
    }

    public Exchange receiveNoWait() {
        return receive();
    }

    public Exchange receive(long timeout) {
        return receive();
    }
}
