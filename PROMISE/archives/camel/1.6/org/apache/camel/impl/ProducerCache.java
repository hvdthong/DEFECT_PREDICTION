package org.apache.camel.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.FailedToCreateProducerException;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.converter.AsyncProcessorTypeConverter;
import org.apache.camel.util.ServiceHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static org.apache.camel.util.ObjectHelper.wrapRuntimeCamelException;


/**
 * Cache containing created {@link Producer}.
 *
 * @version $Revision: 695721 $
 */
public class ProducerCache<E extends Exchange> extends ServiceSupport {
    private static final transient Log LOG = LogFactory.getLog(ProducerCache.class);

    private Map<String, Producer<E>> producers = new HashMap<String, Producer<E>>();

    public synchronized Producer<E> getProducer(Endpoint<E> endpoint) {
        String key = endpoint.getEndpointUri();
        Producer<E> answer = producers.get(key);
        if (answer == null) {
            try {
                answer = endpoint.createProducer();
                answer.start();
            } catch (Exception e) {
                throw new FailedToCreateProducerException(endpoint, e);
            }
            producers.put(key, answer);
        }
        return answer;
    }

    /**
     * Sends the exchange to the given endpoint
     *
     * @param endpoint the endpoint to send the exchange to
     * @param exchange the exchange to send
     */
    public void send(Endpoint<E> endpoint, E exchange) {
        try {
            Producer<E> producer = getProducer(endpoint);
            producer.process(exchange);
        } catch (Exception e) {
            throw wrapRuntimeCamelException(e);
        }
    }

    /**
     * Sends an exchange to an endpoint using a supplied
     * {@link Processor} to populate the exchange
     *
     * @param endpoint the endpoint to send the exchange to
     * @param processor the transformer used to populate the new exchange
     */
    public E send(Endpoint<E> endpoint, Processor processor) {
        try {
            Producer<E> producer = getProducer(endpoint);
            E exchange = producer.createExchange();
            return sendExchange(endpoint, producer, processor, exchange);
        } catch (Exception e) {
            throw wrapRuntimeCamelException(e);
        }
    }

    /**
     * Sends an exchange to an endpoint using a supplied
     * {@link Processor} to populate the exchange.  The callback
     * will be called when the exchange is completed.
     *
     * @param endpoint the endpoint to send the exchange to
     * @param processor the transformer used to populate the new exchange
     */
    public E send(Endpoint<E> endpoint, Processor processor, AsyncCallback callback) {
        try {
            Producer<E> producer = getProducer(endpoint);
            E exchange = producer.createExchange();
            boolean sync = sendExchange(endpoint, producer, processor, exchange, callback);
            setProcessedSync(exchange, sync);
            return exchange;
        } catch (Exception e) {
            throw wrapRuntimeCamelException(e);
        }
    }

    public static boolean isProcessedSync(Exchange exchange) {
        Boolean rc = exchange.getProperty(ProducerCache.class.getName() + ".SYNC", Boolean.class);
        return rc == null ? false : rc;
    }

    public static void setProcessedSync(Exchange exchange, boolean b) {
        exchange.setProperty(ProducerCache.class.getName() + ".SYNC", b ? Boolean.TRUE : Boolean.FALSE);
    }

    /**
     * Sends an exchange to an endpoint using a supplied
     * {@link Processor} to populate the exchange
     *
     * @param endpoint the endpoint to send the exchange to
     * @param pattern the message {@link ExchangePattern} such as
     *   {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut}
     * @param processor the transformer used to populate the new exchange
     */
    public E send(Endpoint<E> endpoint, ExchangePattern pattern, Processor processor) {
        try {
            Producer<E> producer = getProducer(endpoint);
            E exchange = producer.createExchange(pattern);
            return sendExchange(endpoint, producer, processor, exchange);
        } catch (Exception e) {
            throw wrapRuntimeCamelException(e);
        }
    }


    protected E sendExchange(Endpoint<E> endpoint, Producer<E> producer, Processor processor, E exchange) throws Exception {
        processor.process(exchange);

        if (LOG.isDebugEnabled()) {
            LOG.debug(">>>> " + endpoint + " " + exchange);
        }
        producer.process(exchange);
        return exchange;
    }

    protected boolean sendExchange(Endpoint<E> endpoint, Producer<E> producer, Processor processor, E exchange, AsyncCallback callback) throws Exception {
        processor.process(exchange);

        if (LOG.isDebugEnabled()) {
            LOG.debug(">>>> " + endpoint + " " + exchange);
        }
        return AsyncProcessorTypeConverter.convert(producer).process(exchange, callback);
    }

    protected void doStop() throws Exception {
        ServiceHelper.stopServices(producers.values());
        producers.clear();
    }

    protected void doStart() throws Exception {
    }
}
