package org.apache.camel.processor;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.ServiceSupport;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Implements the Multicast pattern to send a message exchange to a number of endpoints, each endpoint receiving a copy of
 * the message exchange.
 *
 * @version $Revision: 534145 $
 */
public class MulticastProcessor extends ServiceSupport implements Processor {
    private Collection<Producer> producers;

    /**
     * A helper method to convert a list of endpoints into a list of processors
     */
    public static <E extends Exchange> Collection<Producer> toProducers(Collection<Endpoint> endpoints) throws Exception {
        Collection<Producer> answer = new ArrayList<Producer>();
        for (Endpoint endpoint : endpoints) {
            answer.add(endpoint.createProducer());
        }
        return answer;
    }

    public MulticastProcessor(Collection<Endpoint> endpoints) throws Exception {
        this.producers = toProducers(endpoints);
    }

    @Override
    public String toString() {
        return "Multicast" + getEndpoints();
    }

    public void process(Exchange exchange) throws Exception {
        for (Producer producer : producers) {
            Exchange copy = copyExchangeStrategy(producer, exchange);
            producer.process(copy);
        }
    }

    protected void doStop() throws Exception {
        for (Producer producer : producers) {
            producer.stop();
        }
    }

    protected void doStart() throws Exception {
        for (Producer producer : producers) {
            producer.start();
        }
    }

    /**
     * Returns the producers to multicast to
     */
    public Collection<Producer> getProducers() {
        return producers;
    }

    /**
     * Returns the list of endpoints
     */
    public Collection<Endpoint> getEndpoints() {
        Collection<Endpoint> answer = new ArrayList<Endpoint>();
        for (Producer producer : producers) {
            answer.add(producer.getEndpoint());
        }
        return answer;
    }

    /**
     * Strategy method to copy the exchange before sending to another endpoint. Derived classes such as the
     * {@link Pipeline} will not clone the exchange
     *
     * @param producer the producer that will send the exchange
     * @param exchange @return the current exchange if no copying is required such as for a pipeline otherwise a new copy of the exchange is returned.
     */
    protected Exchange copyExchangeStrategy(Producer producer, Exchange exchange) {
        return producer.createExchange(exchange);
    }
}
