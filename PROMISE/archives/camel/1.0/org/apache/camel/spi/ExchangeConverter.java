package org.apache.camel.spi;

import org.apache.camel.Exchange;

/**
 * This converter is capable of converting from an exchange to another type
 *
 * @version $Revision: 525537 $
 */
public interface ExchangeConverter {

     <T> T  convertTo(Class<T> type, Exchange exchange);
}
