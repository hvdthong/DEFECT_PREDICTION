package org.apache.camel.processor.aggregate;

import org.apache.camel.Exchange;

/**
 * An {@link AggregationStrategy} which just uses the latest exchange which is useful
 * for status messages where old status messages have no real value. Another example is things
 * like market data prices, where old stock prices are not that relevant, only the current price is.
 *
 * @version $Revision: 727409 $
 */
public class UseLatestAggregationStrategy implements AggregationStrategy {

    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        newExchange.setException(checkException(oldExchange, newExchange));
        return newExchange;
    }
    
    protected Throwable checkException(Exchange oldExchange, Exchange newExchange) {
        return newExchange.getException() != null
                ? newExchange.getException()
                : oldExchange.getException();
    }
}
