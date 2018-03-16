package org.apache.camel.processor.loadbalancer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.camel.Processor;
import org.apache.camel.impl.ServiceSupport;
import org.apache.camel.util.ServiceHelper;

/**
 * A default base class for a {@link LoadBalancer} implementation
 *
 * @version $Revision: 630568 $
 */
public abstract class LoadBalancerSupport extends ServiceSupport implements LoadBalancer {
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
    
    protected void doStart() throws Exception {
        ServiceHelper.startServices(processors);        
    }

    protected void doStop() throws Exception {
        ServiceHelper.stopServices(processors);       
    }
}
