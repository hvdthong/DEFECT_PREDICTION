package org.apache.camel.builder;

import org.apache.camel.processor.FilterProcessor;
import org.apache.camel.Predicate;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * @version $Revision: 534145 $
 */
public class FilterBuilder extends FromBuilder {
    private Predicate predicate;

    public FilterBuilder(FromBuilder builder, Predicate predicate) {
        super(builder);
        this.predicate = predicate;
    }

    /**
     * Adds another predicate using a logical AND
     */
    public FilterBuilder and(Predicate predicate) {
        this.predicate = PredicateBuilder.and(this.predicate, predicate);
        return this;
    }

    /**
     * Adds another predicate using a logical OR
     */
    public FilterBuilder or(Predicate predicate) {
        this.predicate = PredicateBuilder.or(this.predicate, predicate);
        return this;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public FilterProcessor createProcessor() throws Exception {
        Processor childProcessor = super.createProcessor();
        return new FilterProcessor(predicate, childProcessor);
    }

}
