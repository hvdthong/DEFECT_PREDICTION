package org.apache.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;

/**
 * @version $Revision: 563607 $
 */
public class FilterProcessor extends DelegateProcessor {
    private Predicate<Exchange> predicate;

    public FilterProcessor(Predicate<Exchange> predicate, Processor processor) {
        super(processor);
        this.predicate = predicate;
    }

    public void process(Exchange exchange) throws Exception {
        if (predicate.matches(exchange)) {
            super.process(exchange);
        }
    }

    @Override
    public String toString() {
        return "Filter[if: " + predicate + " do: " + getProcessor() + "]";
    }

    public Predicate<Exchange> getPredicate() {
        return predicate;
    }
}
