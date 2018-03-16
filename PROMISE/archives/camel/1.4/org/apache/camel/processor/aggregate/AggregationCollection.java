package org.apache.camel.processor.aggregate;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A {@link Collection} which aggregates exchanges together using a correlation
 * expression so that there is only a single message exchange sent for a single
 * correlation key.
 *
 * @version $Revision: 640438 $
 */
public class AggregationCollection extends AbstractCollection<Exchange> {
    private static final transient Log LOG = LogFactory.getLog(AggregationCollection.class);
    private final Expression<Exchange> correlationExpression;
    private final AggregationStrategy aggregationStrategy;
    private Map<Object, Exchange> map = new LinkedHashMap<Object, Exchange>();

    public AggregationCollection(Expression<Exchange> correlationExpression,
                                 AggregationStrategy aggregationStrategy) {
        this.correlationExpression = correlationExpression;
        this.aggregationStrategy = aggregationStrategy;
    }

    protected Map<Object, Exchange> getMap() {
        return map;
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
            LOG.debug("put exchange:" + newExchange + " for key:"  + correlationKey);
            map.put(correlationKey, newExchange);
        }
        onAggregation(correlationKey, newExchange);
        return true;
    }

    public Iterator<Exchange> iterator() {
        return map.values().iterator();
    }

    public int size() {
        return map.size();
    }

    @Override
    public void clear() {
        map.clear();
    }

    /**
     * A strategy method allowing derived classes such as {@link PredicateAggregationCollection}
     * to check to see if the aggregation has completed
     */
    protected void onAggregation(Object correlationKey, Exchange newExchange) {
    }
}
