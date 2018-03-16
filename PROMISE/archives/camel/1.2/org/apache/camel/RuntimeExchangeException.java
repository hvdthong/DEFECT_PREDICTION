package org.apache.camel;

/**
 * A runtime exception caused by a specific message {@ilnk Exchange}
 *
 * @version $Revision: 1.1 $
 */
public class RuntimeExchangeException extends RuntimeCamelException {
    private static final long serialVersionUID = -8721487431101572630L;
    private final Exchange exchange;

    public RuntimeExchangeException(String message, Exchange exchange) {
        super(message + " on the exchange: " +  exchange);
        this.exchange = exchange;
    }

    public RuntimeExchangeException(Exception e, Exchange exchange) {
        super(e.getMessage(), e);
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
