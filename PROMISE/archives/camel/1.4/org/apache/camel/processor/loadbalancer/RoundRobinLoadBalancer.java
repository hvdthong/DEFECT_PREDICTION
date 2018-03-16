package org.apache.camel.processor.loadbalancer;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Implements the round robin load balancing policy
 *
 * @version $Revision: 630568 $
 */
public class RoundRobinLoadBalancer extends QueueLoadBalancer {
    private int counter = -1;

    protected synchronized Processor chooseProcessor(List<Processor> processors, Exchange exchange) {
        int size = processors.size();
        if (++counter >= size) {
            counter = 0;
        }
        return processors.get(counter);
    }
}
