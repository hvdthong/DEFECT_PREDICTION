package org.apache.camel.component.processor;

import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.Exchange;
import org.apache.camel.Consumer;
import org.apache.camel.Producer;
import org.apache.camel.Component;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.processor.loadbalancer.LoadBalancer;

/**
 * A base class for creating {@link Endpoint} implementations from a {@link Processor}
 *
 * @version $Revision: 1.1 $
 */
public class ProcessorEndpoint extends DefaultEndpoint<Exchange> {
    private final Processor processor;
    private final LoadBalancer loadBalancer;

    protected ProcessorEndpoint(String endpointUri, Component component, Processor processor, LoadBalancer loadBalancer) {
        super(endpointUri, component);
        this.processor = processor;
        this.loadBalancer = loadBalancer;
    }

    public Exchange createExchange() {
        return new DefaultExchange(getContext());
    }

    public Producer<Exchange> createProducer() throws Exception {
        return new DefaultProducer<Exchange>(this) {
            public void process(Exchange exchange) throws Exception {
                onExchange(exchange);
            }
        };
    }

    public Consumer<Exchange> createConsumer(Processor processor) throws Exception {
        return new ProcessorEndpointConsumer(this, processor);
    }

    public Processor getProcessor() {
        return processor;
    }

    public LoadBalancer getLoadBalancer() {
        return loadBalancer;
    }

    protected void onExchange(Exchange exchange) throws Exception {
        processor.process(exchange);

        loadBalancer.process(exchange);
    }

	public boolean isSingleton() {
		return true;
	}
}
