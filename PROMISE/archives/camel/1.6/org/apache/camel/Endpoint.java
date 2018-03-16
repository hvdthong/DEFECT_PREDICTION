package org.apache.camel;

import java.util.Map;

/**
 * implements the <a
 * Endpoint</a> pattern and represents an endpoint that can send and receive
 * message exchanges
 *
 * @see Exchange
 * @see Message
 * @version $Revision: 684667 $
 */
public interface Endpoint<E extends Exchange> {

    /**
     * Returns if the endpoint should be a CamelContext singleton. If the
     * endpoint is a Singleton, then a single Endpoint instance will be shared
     * by all routes with the same URI. Because the endpoint is shared, it
     * should be treated as an immutable.
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
     * Create a new exchange for communicating with this endpoint
     * with the specified {@link ExchangePattern} such as whether its going
     * to be an {@link ExchangePattern#InOnly} or {@link ExchangePattern#InOut} exchange
     *
     * @param pattern the message exchange pattern for the exchange
     */
    E createExchange(ExchangePattern pattern);

    /**
     * Creates a new exchange for communicating with this exchange using the
     * given exchange to pre-populate the values of the headers and messages
     */
    E createExchange(Exchange exchange);

    /**
     * Returns the context which created the endpoint
     *
     * @return the context which created the endpoint
     */
    CamelContext getCamelContext();

    /**
     * Creates a new producer which is used send messages into the endpoint
     *
     * @return a newly created producer
     */
    Producer<E> createProducer() throws Exception;

    /**
     * Creates a new <a
     * Driven Consumer</a> which consumes messages from the endpoint using the
     * given processor
     *
     * @return a newly created consumer
     */
    Consumer<E> createConsumer(Processor processor) throws Exception;

    /**
     * Creates a new <a
     * Consumer</a> so that the caller can poll message exchanges from the
     * consumer using {@link PollingConsumer#receive()},
     * {@link PollingConsumer#receiveNoWait()} or
     * {@link PollingConsumer#receive(long)} whenever it is ready to do so
     * rather than using the <a
     * Based Consumer</a> returned by {@link #createConsumer(Processor)}
     *
     * @return a newly created pull consumer
     * @throws Exception if the pull consumer could not be created
     */
    PollingConsumer<E> createPollingConsumer() throws Exception;

    void configureProperties(Map options);

    void setCamelContext(CamelContext context);

    @Deprecated
    CamelContext getContext();
    
    @Deprecated
    void setContext(CamelContext context);

    /**
     * Should all properties be known or does the endpoint allow unknown options?
     * <p/>
     * <tt>Lenient = false</tt> means that the endpoint should validate that all
     * given options is known and configured properly
     * <tt>lenient = true</tt> means that the endpoint allows additional unknown options to
     * be passed to it but does not throw a ResolveEndpointFailedException when creating
     * the endpoint.
     * <p/>
     * This options is used by a few components for instance the HTTP based that can have
     * dynamic URI options appended that is targeted for an external system.
     * <p/>
     * Most endpoints is configued to be <b>not</b> lenient.
     */
    boolean isLenientProperties();
    
}
