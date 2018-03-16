package org.apache.camel;

/**
 * @version $Revision: 563607 $
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
