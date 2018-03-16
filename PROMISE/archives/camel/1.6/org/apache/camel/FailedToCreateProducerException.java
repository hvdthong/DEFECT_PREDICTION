package org.apache.camel;

/**
 * Thrown if Camel failed to create a producer for a given endpoint.
 *
 * @version $Revision: 659422 $
 */
public class FailedToCreateProducerException extends RuntimeCamelException {
    private final Endpoint endpoint;

    public FailedToCreateProducerException(Endpoint endpoint, Throwable cause) {
        super("Failed to create Producer for endpoint: " + endpoint + ". Reason: " + cause, cause);
        this.endpoint = endpoint;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }
}
