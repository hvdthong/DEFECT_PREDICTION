package org.apache.camel.processor.aggregate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;

/**
 * An aggregator collection which uses a predicate to decide when an aggregation is completed for
 * a particular correlation key
 *
 * @version $Revision: 640438 $
 */
public class PredicateAggregationCollection extends AggregationCollection {
    private Predicate aggregationCompletedPredicate;
    private List<Exchange> collection = new ArrayList<Exchange>();

    public PredicateAggregationCollection(Expression<Exchange> correlationExpression, AggregationStrategy aggregationStrategy, Predicate aggregationCompletedPredicate) {
        super(correlationExpression, aggregationStrategy);
        this.aggregationCompletedPredicate = aggregationCompletedPredicate;
    }

    @Override
    protected void onAggregation(Object correlationKey, Exchange newExchange) {
        if (aggregationCompletedPredicate.matches(newExchange)) {
            super.getMap().remove(correlationKey);
            collection.add(newExchange);
        }
    }

    @Override
    public Iterator<Exchange> iterator() {
        return collection.iterator();
    }

    @Override
    public int size() {
        return collection.size();
    }

    @Override
    public void clear() {
        collection.clear();
        super.clear();
    }
}
