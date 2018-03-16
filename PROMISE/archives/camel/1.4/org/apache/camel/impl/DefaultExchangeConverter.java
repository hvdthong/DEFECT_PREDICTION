package org.apache.camel.impl;

import org.apache.camel.Exchange;
import org.apache.camel.spi.ExchangeConverter;

/**
 * Default implementation of {@link org.apache.camel.spi.ExchangeConverter}.
 *
 * @version $Revision: 659798 $
 */
public class DefaultExchangeConverter implements ExchangeConverter {

    public <T> T convertTo(Class<T> type, Exchange exchange) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
}
