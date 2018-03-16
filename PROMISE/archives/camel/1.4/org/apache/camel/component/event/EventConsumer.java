package org.apache.camel.component.event;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;

/**
 * for working with Spring ApplicationEvents
 *
 * @version $Revision: 673477 $
 */
public class EventConsumer extends DefaultConsumer<Exchange> {
    private EventEndpoint endpoint;

    public EventConsumer(EventEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        endpoint.consumerStarted(this);
    }

    @Override
    protected void doStop() throws Exception {
        endpoint.consumerStopped(this);
        super.doStop();
    }
}
