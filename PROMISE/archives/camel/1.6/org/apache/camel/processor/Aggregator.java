package org.apache.camel.processor;

import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.processor.aggregate.AggregationCollection;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.processor.aggregate.DefaultAggregationCollection;
import org.apache.camel.processor.aggregate.PredicateAggregationCollection;

/**
 * An implementation of the <a
 * pattern where a batch of messages are processed (up to a maximum amount or
 * until some timeout is reached) and messages for the same correlation key are
 * combined together using some kind of {@link AggregationStrategy}
 * (by default the latest message is used) to compress many message exchanges
 * into a smaller number of exchanges.
 * <p/>
 * A good example of this is stock market data; you may be receiving 30,000
 * messages/second and you may want to throttle it right down so that multiple
 * messages for the same stock are combined (or just the latest message is used
 * and older prices are discarded). Another idea is to combine line item messages
 * together into a single invoice message.
 *
 * @version $Revision: 726941 $
 */
public class Aggregator extends BatchProcessor {
    private Predicate aggregationCompletedPredicate;

    public Aggregator(Processor processor, Expression correlationExpression,
                      AggregationStrategy aggregationStrategy) {
        this(processor, new DefaultAggregationCollection(correlationExpression, aggregationStrategy));
    }

    public Aggregator(Processor processor, Expression correlationExpression,
                      AggregationStrategy aggregationStrategy, Predicate aggregationCompletedPredicate) {
        this(processor, new PredicateAggregationCollection(correlationExpression, aggregationStrategy, aggregationCompletedPredicate));
        this.aggregationCompletedPredicate = aggregationCompletedPredicate;
    }

    public Aggregator(Processor processor, AggregationCollection collection) {
        super(processor, collection);
    }

    @Override
    public String toString() {
        return "Aggregator[to: " + getProcessor() + "]";
    }

}
