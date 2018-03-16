package org.apache.camel.processor;

import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.Exchange;
import org.apache.camel.impl.ServiceSupport;
import org.apache.camel.util.ServiceHelper;

/**
 * @version $Revision: 534941 $
 */
public class FilterProcessor extends ServiceSupport implements Processor {
    private Predicate<Exchange> predicate;
    private Processor processor;

    public FilterProcessor(Predicate<Exchange> predicate, Processor processor) {
        this.predicate = predicate;
        this.processor = processor;
    }

    public void process(Exchange exchange) throws Exception {
        if (predicate.matches(exchange)) {
            processor.process(exchange);
        }
    }

    @Override
    public String toString() {
        return "filter (" + predicate + ") " + processor;
    }

    public Predicate<Exchange> getPredicate() {
        return predicate;
    }

    public Processor getProcessor() {
        return processor;
    }

    protected void doStart() throws Exception {
        ServiceHelper.startServices(processor);
    }

    protected void doStop() throws Exception {
        ServiceHelper.stopServices(processor);
    }
}
