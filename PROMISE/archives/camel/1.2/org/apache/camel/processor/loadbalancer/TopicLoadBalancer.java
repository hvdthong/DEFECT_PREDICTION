package org.apache.camel.processor.loadbalancer;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * A {@link LoadBalancer} implementations which sends to all destinations
 * (rather like JMS Topics)
 * 
 * @version $Revision: 1.1 $
 */
public class TopicLoadBalancer extends LoadBalancerSupport {
    public void process(Exchange exchange) throws Exception {
        List<Processor> list = getProcessors();
        for (Processor processor : list) {
            Exchange copy = copyExchangeStrategy(processor, exchange);
            processor.process(copy);
        }
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
