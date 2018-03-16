package org.apache.camel.impl;

import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.PollingConsumer;
import org.apache.camel.Processor;

import java.util.concurrent.ScheduledExecutorService;

/**
 * A default implementation of an event driven {@link Consumer} which uses the {@link PollingConsumer}
 *
 * @version $Revision: 1.1 $
 */
public class DefaultScheduledPollConsumer<E extends Exchange> extends ScheduledPollConsumer<E> {
    private PollingConsumer<E> pollingConsumer;

    public DefaultScheduledPollConsumer(DefaultEndpoint<E> defaultEndpoint, Processor processor) {
        super(defaultEndpoint, processor);
    }

    public DefaultScheduledPollConsumer(Endpoint<E> endpoint, Processor processor, ScheduledExecutorService executor) {
        super(endpoint, processor, executor);
    }

    protected void poll() throws Exception {
        while (true) {
            E exchange = pollingConsumer.receiveNoWait();
            if (exchange == null) {
                break;
            }
            getProcessor().process(exchange);
        }
    }

    @Override
    protected void doStart() throws Exception {
        pollingConsumer = getEndpoint().createPollingConsumer();
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
        if (pollingConsumer != null) {
            pollingConsumer.stop();
        }
    }
}
