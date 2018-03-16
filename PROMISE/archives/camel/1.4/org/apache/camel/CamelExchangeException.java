package org.apache.camel;

/**
 * An exception caused by a specific message {@link Exchange}
 *
 * @version $Revision: 642753 $
 */
public class CamelExchangeException extends CamelException {
    private static final long serialVersionUID = -8721487431101572630L;
    private final Exchange exchange;

    public CamelExchangeException(String message, Exchange exchange) {
        super(createMessage(message, exchange));
        this.exchange = exchange;
    }

    public CamelExchangeException(String message, Exchange exchange, Throwable cause) {
        super(createMessage(message, exchange), cause);
        this.exchange = exchange;
    }

    /**
     * Returns the exchange which caused the exception
     */
    public Exchange getExchange() {
        return exchange;
    }

    protected static String createMessage(String message, Exchange exchange) {
        return message + " on the exchange: " + exchange;
    }
}
