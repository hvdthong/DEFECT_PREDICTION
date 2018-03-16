package org.apache.camel.impl;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.PollingConsumer;
import org.apache.camel.Processor;

/**
 * A base class for an endpoint which the default consumer mode is to use a {@link PollingConsumer}
 *
 * @version $Revision: 656397 $
 */
public abstract class DefaultPollingEndpoint<E extends Exchange> extends ScheduledPollEndpoint<E>  {

    protected DefaultPollingEndpoint() {
    }

    protected DefaultPollingEndpoint(String endpointUri) {
        super(endpointUri);
    }

    protected DefaultPollingEndpoint(String endpointUri, Component component) {
        super(endpointUri, component);
    }

    protected DefaultPollingEndpoint(String endpointUri, CamelContext context) {
        super(endpointUri, context);
    }

    public Consumer<E> createConsumer(Processor processor) throws Exception {
        DefaultScheduledPollConsumer result = new DefaultScheduledPollConsumer(this, processor);
        configureConsumer(result);
        return result;
    }
}
