package org.apache.camel.processor.loadbalancer;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;

/**
 * Represents a consumer which on starting registers itself with a {@link LoadBalancer} and on closing unregisters
 * itself with a load balancer
 *
 * @version $Revision: 640438 $
 */
public class LoadBalancerConsumer extends DefaultConsumer<Exchange> {
    private final LoadBalancer loadBalancer;

    public LoadBalancerConsumer(Endpoint endpoint, Processor processor, LoadBalancer loadBalancer) {
        super(endpoint, processor);
        this.loadBalancer = loadBalancer;
    }

    @Override
    protected void doStart() throws Exception {
        loadBalancer.addProcessor(getProcessor());
    }

    @Override
    protected void doStop() throws Exception {
        loadBalancer.removeProcessor(getProcessor());
    }
}
