package org.apache.camel.util;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.PollingConsumer;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Some helper methods for working with {@link Endpoint} instances
 *
 * @version $Revision: 640438 $
 */
public final class EndpointHelper {
    private static final transient Log LOG = LogFactory.getLog(EndpointHelper.class);

    private EndpointHelper() {
    }
    /**
     * Creates a {@link PollingConsumer} and polls all pending messages on the endpoint
     * and invokes the given {@link Processor} to process each {@link Exchange} and then closes
     * down the consumer and throws any exceptions thrown.
     *
     * @param endpoint
     * @param processor
     */
    public static void pollEndpoint(Endpoint endpoint, Processor processor, long timeout) throws Exception {
        PollingConsumer consumer = endpoint.createPollingConsumer();
        try {
            consumer.start();

            while (true) {
                Exchange exchange = consumer.receive(timeout);
                if (exchange == null) {
                    break;
                } else {
                    processor.process(exchange);
                }
            }
        } finally {
            try {
                consumer.stop();
            } catch (Exception e) {
                LOG.warn("Failed to stop PollingConsumer: " + e, e);
            }
        }
    }

    /**
     * Creates a {@link PollingConsumer} and polls all pending messages on the
     * endpoint and invokes the given {@link Processor} to process each
     * {@link Exchange} and then closes down the consumer and throws any
     * exceptions thrown.
     *
     * @param endpoint
     * @param processor
     */
    public static void pollEndpoint(Endpoint endpoint, Processor processor) throws Exception {
        pollEndpoint(endpoint, processor, 1000L);
    }
}
