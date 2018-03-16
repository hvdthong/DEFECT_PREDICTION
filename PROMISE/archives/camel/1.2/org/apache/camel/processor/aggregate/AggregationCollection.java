package org.apache.camel.processor.aggregate;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;

/**
 * A {@link Collection} which aggregates exchanges together using a correlation
 * expression so that there is only a single message exchange sent for a single
 * correlation key.
 * 
 * @version $Revision: 1.1 $
 */
public class AggregationCollection extends AbstractCollection<Exchange> {
    private final Expression<Exchange> correlationExpression;
    private final AggregationStrategy aggregationStrategy;
    private Map<Object, Exchange> map = new LinkedHashMap<Object, Exchange>();

    public AggregationCollection(Expression<Exchange> correlationExpression,
                                 AggregationStrategy aggregationStrategy) {
        this.correlationExpression = correlationExpression;
        this.aggregationStrategy = aggregationStrategy;
    }

    @Override
    public boolean add(Exchange exchange) {
        Object correlationKey = correlationExpression.evaluate(exchange);
        Exchange oldExchange = map.get(correlationKey);
        Exchange newExchange = exchange;
        if (oldExchange != null) {
            newExchange = aggregationStrategy.aggregate(oldExchange, newExchange);
        }

        if (newExchange != oldExchange) {
            map.put(correlationKey, newExchange);
        }
        return true;
    }

    public Iterator<Exchange> iterator() {
        return map.values().iterator();
    }

    public int size() {
        return map.size();
    }
}
