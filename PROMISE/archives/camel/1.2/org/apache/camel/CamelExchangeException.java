package org.apache.camel;

/**
 * An exception caused by a specific message {@ilnk Exchange}
 *
 * @version $Revision: 1.1 $
 */
public class CamelExchangeException extends CamelException {
    private static final long serialVersionUID = -8721487431101572630L;
    private final Exchange exchange;

    public CamelExchangeException(String message, Exchange exchange) {
        super(message + " on the exchange: " +  exchange);
        this.exchange = exchange;
    }

    /**
     * Returns the exchange which caused the exception
     *
     * @return the exchange which caused the exception
     */
    public Exchange getExchange() {
        return exchange;
    }

}
