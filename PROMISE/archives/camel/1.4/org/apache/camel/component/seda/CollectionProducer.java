package org.apache.camel.component.seda;

import java.util.Collection;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultProducer;

/**
 * A simple {@link Producer} which just appends to a {@link Collection} the {@link Exchange} object.
 *
 * @version $Revision: 640438 $
 */
public class CollectionProducer extends DefaultProducer implements AsyncProcessor {
    private final Collection<Exchange> queue;

    public CollectionProducer(Endpoint endpoint, Collection<Exchange> queue) {
        super(endpoint);
        this.queue = queue;
    }

    public void process(Exchange exchange) throws Exception {
        queue.add(exchange.copy());
    }

    public boolean process(Exchange exchange, AsyncCallback callback) {
        queue.add(exchange.copy());
        callback.done(true);
        return true;
    }
}
