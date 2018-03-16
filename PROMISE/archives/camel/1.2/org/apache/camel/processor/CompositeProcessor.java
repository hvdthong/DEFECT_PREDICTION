package org.apache.camel.processor;

import java.util.Collection;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ServiceSupport;
import org.apache.camel.util.ServiceHelper;

/**
 * Represents a composite pattern, aggregating a collection of processors
 * together as a single processor
 * 
 * @version $Revision: 563607 $
 */
public class CompositeProcessor extends ServiceSupport implements Processor {
    private final Collection<Processor> processors;

    public CompositeProcessor(Collection<Processor> processors) {
        this.processors = processors;
    }

    public void process(Exchange exchange) throws Exception {
        for (Processor processor : processors) {
            processor.process(exchange);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[ ");
        boolean first = true;
        for (Processor processor : processors) {
            if (first) {
                first = false;
            } else {
                builder.append(", ");
            }
            builder.append(processor.toString());
        }
        builder.append(" ]");
        return builder.toString();
    }

    public Collection<Processor> getProcessors() {
        return processors;
    }

    protected void doStart() throws Exception {
        ServiceHelper.startServices(processors);
    }

    protected void doStop() throws Exception {
        ServiceHelper.stopServices(processors);
    }
}
