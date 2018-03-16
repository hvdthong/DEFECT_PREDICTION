package org.apache.camel;

/**
 * An exception thrown if an InOut exchange times out receiving the OUT message
 *
 * @version $Revision: 630591 $
 */
public class ExchangeTimedOutException extends CamelExchangeException {
    private final long timeout;

    public ExchangeTimedOutException(Exchange exchange, long timeout) {
        super("The OUT message was not received within: " + timeout + " millis", exchange);
        this.timeout = timeout;
    }

    /**
     * Return the timeout which expired in milliseconds
     */
    public long getTimeout() {
        return timeout;
    }
}
