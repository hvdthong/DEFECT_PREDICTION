package org.apache.camel.impl;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Exchange;
import org.apache.camel.PollingConsumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;

/**
 * An endpoint which allows exchanges to be sent into it which just invokes a
 * given {@link Processor}. This component does not support the use of
 * consumers.
 *
 * @version $Revision: 656397 $
 */
public class ProcessorEndpoint extends DefaultPollingEndpoint<Exchange> {
    private Processor processor;

    protected ProcessorEndpoint() {
    }

    protected ProcessorEndpoint(String endpointUri) {
        super(endpointUri);
    }

    public ProcessorEndpoint(String endpointUri, CamelContext context, Processor processor) {
        super(endpointUri, context);
        this.processor = processor;
    }

    public ProcessorEndpoint(String endpointUri, Component component, Processor processor) {
        super(endpointUri, component);
        this.processor = processor;
    }

    public ProcessorEndpoint(String endpointUri, Processor processor) {
        super(endpointUri);
        this.processor = processor;
    }


    protected ProcessorEndpoint(String endpointUri, Component component) {
        super(endpointUri, component);
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

    public Processor getProcessor() throws Exception {
        if (processor == null) {
            processor = createProcessor();
        }
        return processor;
    }

    protected Processor createProcessor() throws Exception {
        return new Processor() {
            public void process(Exchange exchange) throws Exception {
                onExchange(exchange);
            }
        };
    }

    protected void onExchange(Exchange exchange) throws Exception {
        getProcessor().process(exchange);
    }

    public boolean isSingleton() {
        return true;
    }
}
