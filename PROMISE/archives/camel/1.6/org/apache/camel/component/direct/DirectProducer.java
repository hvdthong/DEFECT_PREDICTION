package org.apache.camel.component.direct;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.impl.converter.AsyncProcessorTypeConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The direct producer.
 *
 * @version $Revision: 659771 $
 */
public class DirectProducer<E extends Exchange> extends DefaultProducer implements AsyncProcessor {
    private static final transient Log LOG = LogFactory.getLog(DirectProducer.class);
    private DirectEndpoint<E> endpoint;

    public DirectProducer(DirectEndpoint<E> endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
        if (endpoint.getConsumers().isEmpty()) {
            LOG.warn("No getConsumers() available on " + this + " for " + exchange);
        } else {
            for (DefaultConsumer<E> consumer : endpoint.getConsumers()) {
                consumer.getProcessor().process(exchange);
            }
        }
    }

    public boolean process(Exchange exchange, AsyncCallback callback) {
        int size = endpoint.getConsumers().size();
        if (size == 0) {
            LOG.warn("No getConsumers() available on " + this + " for " + exchange);
        } else if (size == 1) {
            DefaultConsumer<E> consumer = endpoint.getConsumers().get(0);
            AsyncProcessor processor = AsyncProcessorTypeConverter.convert(consumer.getProcessor());
            return processor.process(exchange, callback);
        } else if (size > 1) {
            try {
                for (DefaultConsumer<E> consumer : endpoint.getConsumers()) {
                    consumer.getProcessor().process(exchange);
                }
            } catch (Throwable error) {
                exchange.setException(error);
            }
        }
        callback.done(true);
        return true;
    }
}
