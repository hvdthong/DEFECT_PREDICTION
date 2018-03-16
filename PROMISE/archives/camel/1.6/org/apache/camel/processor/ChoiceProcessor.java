package org.apache.camel.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.impl.ServiceSupport;
import org.apache.camel.util.ServiceHelper;

/**
 * Implements a Choice structure where one or more predicates are used which if
 * they are true their processors are used, with a default otherwise clause used
 * if none match.
 * 
 * @version $Revision: 674278 $
 */
public class ChoiceProcessor extends ServiceSupport implements Processor {
    private List<FilterProcessor> filters = new ArrayList<FilterProcessor>();
    private Processor otherwise;

    public ChoiceProcessor(List<FilterProcessor> filters, Processor otherwise) {
        this.filters = filters;
        this.otherwise = otherwise;
    }

    public void process(Exchange exchange) throws Exception {
        for (FilterProcessor filterProcessor : filters) {
            Predicate<Exchange> predicate = filterProcessor.getPredicate();
            if (predicate != null && predicate.matches(exchange)) {
                filterProcessor.processNext(exchange);
                return;
            }
        }
        if (otherwise != null) {
            otherwise.process(exchange);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("choice{");
        boolean first = true;
        for (FilterProcessor processor : filters) {
            if (first) {
                first = false;
            } else {
                builder.append(", ");
            }
            builder.append("when ");
            builder.append(processor.getPredicate().toString());
            builder.append(": ");
            builder.append(processor.getProcessor());
        }
        if (otherwise != null) {
            builder.append(", otherwise: ");
            builder.append(otherwise);
        }
        builder.append("}");
        return builder.toString();
    }

    public List<FilterProcessor> getFilters() {
        return filters;
    }

    public Processor getOtherwise() {
        return otherwise;
    }

    protected void doStart() throws Exception {
        ServiceHelper.startServices(filters);
        ServiceHelper.startServices(otherwise);
    }

    protected void doStop() throws Exception {
        ServiceHelper.stopServices(otherwise);
        ServiceHelper.stopServices(filters);
    }
}
