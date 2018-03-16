package org.apache.camel.impl;

import java.util.concurrent.ScheduledExecutorService;

import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.PollingConsumer;
import org.apache.camel.Processor;

/**
 * A default implementation of an event driven {@link Consumer} which uses the {@link PollingConsumer}
 *
 * @version $Revision: 640438 $
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

            Message out = exchange.getOut(false);
            if (out != null) {
                E newExchange = getEndpoint().createExchange();
                newExchange.getIn().copyFrom(out);
                exchange = newExchange;
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
