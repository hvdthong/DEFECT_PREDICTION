package org.apache.camel;

/**
 * Provides a channel on which clients can create and invoke message exchanges
 * on an {@link Endpoint}
 * 
 * @version $Revision: 572712 $
 */
public interface Producer<E extends Exchange> extends Processor, Service {

    Endpoint<E> getEndpoint();

    /**
     * Creates a new exchange to send to this endpoint
     * 
     * @return a newly created exchange
     */
    E createExchange();

    /**
     * Creates a new exchange of the given pattern to send to this endpoint
     *
     * @return a newly created exchange
     */
    E createExchange(ExchangePattern pattern);

    /**
     * Creates a new exchange for communicating with this exchange using the
     * given exchange to pre-populate the values of the headers and messages
     */
    E createExchange(E exchange);
}
