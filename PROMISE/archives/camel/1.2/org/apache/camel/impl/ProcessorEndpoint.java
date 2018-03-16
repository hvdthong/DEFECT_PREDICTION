package org.apache.camel.impl;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.ExchangePattern;
import org.apache.camel.PollingConsumer;

/**
 * An endpoint which allows exchanges to be sent into it which just invokes a
 * given {@link Processor}. This component does not support the use of
 * consumers.
 * 
 * @version $Revision: 1.1 $
 */
public class ProcessorEndpoint extends DefaultPollingEndpoint<Exchange> {
    private final Processor processor;

    public ProcessorEndpoint(String endpointUri, CamelContext context, Processor processor) {
        super(endpointUri, context);
        this.processor = processor;
    }

    public ProcessorEndpoint(String endpointUri, Component component, Processor processor) {
        super(endpointUri, component);
        this.processor = processor;
    }


    public Producer<Exchange> createProducer() throws Exception {
        return new DefaultProducer<Exchange>(this) {
            public void process(Exchange exchange) throws Exception {
                onExchange(exchange);
            }
        };
    }

    @Override
    public PollingConsumer<Exchange> createPollingConsumer() throws Exception {
        return new ProcessorPollingConsumer(this, getProcessor());
    }

    public Processor getProcessor() {
        if (processor == null) {
            return new Processor() {
                public void process(Exchange exchange) throws Exception {
                    onExchange(exchange);
                }
            };
        }
        return processor;
    }

    protected void onExchange(Exchange exchange) throws Exception {
        processor.process(exchange);
    }

    public boolean isSingleton() {
        return true;
    }
}
