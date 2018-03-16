package org.apache.camel.component.queue;

import java.util.concurrent.BlockingQueue;

import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultProducer;

/**
 * Represents a queue endpoint that uses a {@link BlockingQueue}
 * object to process inbound exchanges.
 *
 * @org.apache.xbean.XBean
 * @version $Revision: 519973 $
 */
public class QueueEndpoint<E extends Exchange> extends DefaultEndpoint<E> {
    private BlockingQueue<E> queue;

    public QueueEndpoint(String uri, QueueComponent<E> component) {
        super(uri, component);
        this.queue = component.createQueue();
    }

    public Producer<E> createProducer() throws Exception {
        return new DefaultProducer(this) {
            public void process(Exchange exchange) {
                queue.add(toExchangeType(exchange));
            }
        };
    }

    public Consumer<E> createConsumer(Processor processor) throws Exception {
        return new QueueEndpointConsumer<E>(this, processor);
    }

    public E createExchange() {
        return (E) new DefaultExchange(getContext());
    }

    public BlockingQueue<E> getQueue() {
        return queue;
    }
    
	public boolean isSingleton() {
		return true;
	}


}
