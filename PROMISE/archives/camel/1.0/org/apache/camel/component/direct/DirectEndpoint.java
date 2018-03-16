package org.apache.camel.component.direct;

import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.util.ProducerCache;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Represents a direct endpoint that synchronously invokes the consumers of the endpoint when a producer 
 * sends a message to it.
 *
 * @org.apache.xbean.XBean
 * @version $Revision: 519973 $
 */
public class DirectEndpoint<E extends Exchange> extends DefaultEndpoint<E> {
    private static final Log log = LogFactory.getLog(DirectEndpoint.class);

	private final CopyOnWriteArrayList<DefaultConsumer<E>> consumers = new CopyOnWriteArrayList<DefaultConsumer<E>>();
	
	boolean allowMultipleConsumers=true;	
	
    public DirectEndpoint(String uri, DirectComponent<E> component) {
        super(uri, component);
    }

    public Producer createProducer() throws Exception {
        return new DefaultProducer(this) {
            public void process(Exchange exchange) throws Exception {
            	DirectEndpoint.this.process(exchange);
            }
        };    	
    }

    protected void process(Exchange exchange) throws Exception {
    	if (consumers.isEmpty()) {
    		log.warn("No consumers available on " + this + " for " + exchange);
    	}
    	else {
	    	for (DefaultConsumer<E> consumer : consumers) {
				consumer.getProcessor().process(exchange);
			}
    	}
    }

	public Consumer<E> createConsumer(Processor processor) throws Exception {
		return new DefaultConsumer<E>(this, processor) {
			@Override
			public void start() throws Exception {
				if( !allowMultipleConsumers && !consumers.isEmpty() )
					throw new IllegalStateException("Endpoint "+getEndpointUri()+" only allows 1 active consumer but you attempted to start a 2nd consumer.");
				
				consumers.add(this);
				super.start();
			}
			
			@Override
			public void stop() throws Exception {
				super.stop();
				consumers.remove(this);
			}
		};
    }

    public E createExchange() {
        return (E) new DefaultExchange(getContext());
    }

	public boolean isAllowMultipleConsumers() {
		return allowMultipleConsumers;
	}
	public void setAllowMultipleConsumers(boolean allowMutlipleConsumers) {
		this.allowMultipleConsumers = allowMutlipleConsumers;
	}

	public boolean isSingleton() {
		return true;
	}

}
