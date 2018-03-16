package org.apache.camel.processor.loadbalancer;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Implements the random load balancing policy
 *
 * @version $Revision: 1.1 $
 */
public class RandomLoadBalancer extends QueueLoadBalancer {

    protected synchronized Processor chooseProcessor(List<Processor> processors, Exchange exchange) {
        int size = processors.size();
        while (true) {
            int index = (int) Math.round(Math.random() * size);
            if (index < size) {
                return processors.get(index);
            }
        }
    }
}
