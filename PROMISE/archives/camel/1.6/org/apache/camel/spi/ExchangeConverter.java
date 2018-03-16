package org.apache.camel.spi;

import org.apache.camel.Exchange;

/**
 * This converter is capable of converting from an exchange to another type
 *
 * @version $Revision: 688279 $
 */
public interface ExchangeConverter {

    /**
     * Converts the given exchange to the new type
     *
     * @param type  the new class type
     * @param exchange the exchange to converter
     * @param <T> the new type
     * @return  the converted exchange
     */
     <T> T  convertTo(Class<T> type, Exchange exchange);
}
