package org.apache.camel.processor.interceptor;

import org.apache.camel.Exchange;

/**
 * A plugin used to turn an {@link Exchange} into a String
 * so it can be logged to a file
 *
 * @version $Revision: 696610 $
 */
public interface ExchangeFormatter {

    /**
     * Generates a string representation of the exchange
     */
    Object format(Exchange exchange);
}
