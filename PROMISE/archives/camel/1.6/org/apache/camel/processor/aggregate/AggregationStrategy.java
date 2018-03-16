package org.apache.camel.processor.aggregate;

import org.apache.camel.Exchange;

/**
 * A strategy for aggregating two exchanges together into a single exchange.
 * Possible implementations include performing some kind of combining or delta
 * processing, such as adding line items together into an invoice or just using
 * the newest exchange and removing old exchanges such as for state tracking or
 * market data prices; where old values are of little use.
 * 
 * @version $Revision: 630568 $
 */
public interface AggregationStrategy {

    /**
     * Aggregates an old and new exchange together to create a single combined
     * exchange
     *
     * @param oldExchange the oldest exchange
     * @param newExchange the newest exchange
     * @return a combined composite of the two exchanges
     */
    Exchange aggregate(Exchange oldExchange, Exchange newExchange);
}
