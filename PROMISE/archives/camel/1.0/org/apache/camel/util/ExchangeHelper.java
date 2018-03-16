package org.apache.camel.util;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.NoSuchEndpointException;

/**
 * Some helper methods for working with {@link Exchange} objects
 *
 * @version $Revision: 532790 $
 */
public class ExchangeHelper {

    /**
     * Attempts to resolve the endpoint for the given value
     *
     * @param exchange the message exchange being processed
     * @param value the value which can be an {@link Endpoint} or an object which provides a String representation
     * of an endpoint via {@link #toString()}
     *
     * @return the endpoint
     * @throws NoSuchEndpointException if the endpoint cannot be resolved
     */
    @SuppressWarnings({"unchecked"})
    public static <E extends Exchange> Endpoint<E> resolveEndpoint(E exchange, Object value) throws NoSuchEndpointException {
        Endpoint<E> endpoint;
        if (value instanceof Endpoint) {
            endpoint = (Endpoint<E>) value;
        }
        else {
            String uri = value.toString();
            endpoint = (Endpoint<E>) exchange.getContext().getEndpoint(uri);
            if (endpoint == null) {
                throw new NoSuchEndpointException(uri);
            }
        }
        return endpoint;
    }
}
