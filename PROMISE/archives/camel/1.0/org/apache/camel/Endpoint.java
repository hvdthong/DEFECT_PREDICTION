package org.apache.camel;


/**
 * pattern and represents an endpoint that can send and receive message exchanges
 *
 * @see Exchange, Message
 * @version $Revision: 541693 $
 */
public interface Endpoint<E extends Exchange> {

    /**
     * Returns if the endpoint should be a CamelContext singleton.  If the endpoint is a Singleton,
     * then a single Endpoint instance will be shared by all routes with the same URI.  Because the endpoint
     * is shared, it should be treated as an immutable.
     */
    boolean isSingleton();

    /**
     * Returns the string representation of the endpoint URI
     */
    String getEndpointUri();
    
    /**
     * Create a new exchange for communicating with this endpoint
     */
    E createExchange();

    /**
     * Creates a new exchange for communicating with this exchange using the given exchange to pre-populate the values
     * of the headers and messages
     */
    E createExchange(Exchange exchange);

    /**
     * Converts the given exchange to this endpoints required type
     */
    E toExchangeType(Exchange exchange);

    /**
     * Returns the context which created the endpoint
     *
     * @return the context which created the endpoint
     */
    CamelContext getContext();

    /**
     * Creates a new producer which is used send messages into the endpoint
     *
     * @return a newly created producer
     */
    Producer<E> createProducer() throws Exception;

    /**
     * which consumes messages from the endpoint using the given processor
     *
     * @return a newly created consumer
     */
    Consumer<E> createConsumer(Processor processor) throws Exception;

    /**
     * so that the caller can poll message exchanges from the consumer
     * using {@link PollingConsumer#receive()}, {@link PollingConsumer#receiveNoWait()} or {@link PollingConsumer#receive(long)}
     * whenever it is ready to do so rather than using the
     * returned by {@link #createConsumer(Processor)}
     *
     * @return a newly created pull consumer
     * @throws Exception if the pull consumer could not be created
     */
    PollingConsumer<E> createPollingConsumer() throws Exception;
}
