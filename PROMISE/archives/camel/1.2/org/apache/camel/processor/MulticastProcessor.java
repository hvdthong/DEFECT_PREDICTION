package org.apache.camel.processor;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ServiceSupport;
import org.apache.camel.util.ServiceHelper;

/**
 * Implements the Multicast pattern to send a message exchange to a number of
 * endpoints, each endpoint receiving a copy of the message exchange.
 * 
 * @see Pipeline
 * @version $Revision: 563607 $
 */
public class MulticastProcessor extends ServiceSupport implements Processor {
    private Collection<Processor> processors;

    public MulticastProcessor(Collection<Processor> processors) {
        this.processors = processors;
    }

    /**
     * A helper method to convert a list of endpoints into a list of processors
     */
    public static <E extends Exchange> Collection<Processor> toProducers(Collection<Endpoint> endpoints)
        throws Exception {
        Collection<Processor> answer = new ArrayList<Processor>();
        for (Endpoint endpoint : endpoints) {
            answer.add(endpoint.createProducer());
        }
        return answer;
    }

    @Override
    public String toString() {
        return "Multicast" + getProcessors();
    }

    public void process(Exchange exchange) throws Exception {
        for (Processor producer : processors) {
            Exchange copy = copyExchangeStrategy(producer, exchange);
            producer.process(copy);
        }
    }

    protected void doStop() throws Exception {
        ServiceHelper.stopServices(processors);
    }

    protected void doStart() throws Exception {
        ServiceHelper.startServices(processors);
    }

    /**
     * Returns the producers to multicast to
     */
    public Collection<Processor> getProcessors() {
        return processors;
    }

    /**
     * Strategy method to copy the exchange before sending to another endpoint.
     * Derived classes such as the {@link Pipeline} will not clone the exchange
     * 
     * @param processor the processor that will send the exchange
     * @param exchange
     * @return the current exchange if no copying is required such as for a
     *         pipeline otherwise a new copy of the exchange is returned.
     */
    protected Exchange copyExchangeStrategy(Processor processor, Exchange exchange) {
        return exchange.copy();
    }
}
