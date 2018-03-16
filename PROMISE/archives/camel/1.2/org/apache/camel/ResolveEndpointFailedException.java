package org.apache.camel;

/**
 * A runtime exception thrown if an {@link Endpoint} can not be resolved via URI
 * 
 * @version $Revision: 563607 $
 */
public class ResolveEndpointFailedException extends RuntimeCamelException {
    private final String uri;

    public ResolveEndpointFailedException(String uri, Throwable cause) {
        super("Failed to resolve endpoint: " + uri + " due to: " + cause, cause);
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
}
