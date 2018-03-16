package org.apache.camel.processor.loadbalancer;

import org.apache.camel.Processor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.List;

/**
 * A strategy for load balancing across a number of {@link Processor} instances
 *
 * @version $Revision: 1.1 $
 */
public interface LoadBalancer extends Processor {
    /**
     * Adds a new processor to the load balancer
     *
     * @param processor the processor to be added to the load balancer
     */
    void addProcessor(Processor processor);

    /**
     * Removes the given processor from the load balancer
     *
     * @param processor the processor to be removed from the load balancer
     */
    void removeProcessor(Processor processor);

    /**
     * Returns the current processors available to this load balancer
     *
     * @return the processors available
     */
    List<Processor> getProcessors();
}
