package org.apache.camel.processor.loadbalancer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.camel.Processor;

/**
 * A default base class for a {@link LoadBalancer} implementation
 *
 * @version $Revision: 1.1 $
 */
public abstract class LoadBalancerSupport implements LoadBalancer {
    private List<Processor> processors = new CopyOnWriteArrayList<Processor>();

    public void addProcessor(Processor processor) {
        processors.add(processor);
    }

    public void removeProcessor(Processor processor) {
        processors.remove(processor);
    }

    public List<Processor> getProcessors() {
        return processors;
    }
}
