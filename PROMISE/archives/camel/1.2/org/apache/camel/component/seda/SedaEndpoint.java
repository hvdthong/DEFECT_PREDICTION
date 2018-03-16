package org.apache.camel.component.seda;

import java.util.concurrent.BlockingQueue;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultProducer;

/**
 * An implementation of the <a
 * asynchronous SEDA exchanges on a {@link BlockingQueue} within a CamelContext
 * 
 * @version $Revision: 519973 $
 */
public class SedaEndpoint extends DefaultEndpoint<Exchange> {
        
    private final class SedaProducer extends DefaultProducer implements AsyncProcessor {
        private SedaProducer(Endpoint endpoint) {
            super(endpoint);
        }
        public void process(Exchange exchange) {
            queue.add(exchange.copy());
        }
        public boolean process(Exchange exchange, AsyncCallback callback) {
            queue.add(exchange.copy());
            callback.done(true);
            return true;
        }
    }

    private BlockingQueue<Exchange> queue;

    public SedaEndpoint(String endpointUri, Component component, BlockingQueue<Exchange> queue) {
        super(endpointUri, component);
        this.queue = queue;
    }

    public SedaEndpoint(String uri, SedaComponent component) {
        this(uri, component, component.createQueue());
    }

    public Producer createProducer() throws Exception {
        return new SedaProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new SedaConsumer(this, processor);
    }

    public BlockingQueue<Exchange> getQueue() {
        return queue;
    }

    public boolean isSingleton() {
        return true;
    }

}
