package org.apache.camel.processor.loadbalancer;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.List;

/**
 * A base class for {@link LoadBalancer} implementations which choose a single destination for each exchange
 * (rather like JMS Queues)
 *
 * @version $Revision: 1.1 $
 */
public abstract class QueueLoadBalancer extends LoadBalancerSupport {

    public void process(Exchange exchange) throws Exception {
        List<Processor> list = getProcessors();
        if (list.isEmpty()) {
            throw new IllegalStateException("No processors available to process " + exchange);
        }
        Processor processor = chooseProcessor(list, exchange);
        if (processor == null) {
            throw new IllegalStateException("No processors could be chosen to process " + exchange);
        }
        else {
            processor.process(exchange);
        }
    }

    protected abstract Processor chooseProcessor(List<Processor> processors, Exchange exchange);
}
