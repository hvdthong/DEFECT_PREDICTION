package org.apache.camel.impl;

import org.apache.camel.Exchange;
import org.apache.camel.spi.ExchangeConverter;

/**
 * @version $Revision: 525537 $
 */
public class DefaultExchangeConverter implements ExchangeConverter {
    public <T> T convertTo(Class<T> type, Exchange exchange) {
        return null;
    }
}
