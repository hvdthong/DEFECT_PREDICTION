package org.apache.camel.component.seda;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.BrowsableEndpoint;
import org.apache.camel.util.ObjectHelper;

/**
 * An implementation of the <a
 * asynchronous SEDA exchanges on a {@link BlockingQueue} within a CamelContext
 *
 * @version $Revision: 707305 $
 */
public class SedaEndpoint extends DefaultEndpoint<Exchange> implements BrowsableEndpoint<Exchange> {
    private BlockingQueue<Exchange> queue;

    public SedaEndpoint(String endpointUri, Component component, BlockingQueue<Exchange> queue) {
        super(endpointUri, component);
        this.queue = queue;
    }

    public SedaEndpoint(String uri, SedaComponent component, Map parameters) {
        this(uri, component, component.createQueue(uri, parameters));
    }

    public SedaEndpoint(String endpointUri, BlockingQueue<Exchange> queue) {
        super(endpointUri);
        ObjectHelper.notNull(queue, "queue");
        this.queue = queue;
    }

    public Producer createProducer() throws Exception {
        return new CollectionProducer(this, getQueue());
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

    public List<Exchange> getExchanges() {
        return new ArrayList<Exchange>(getQueue());
    }
}
