package org.apache.camel;


/**
 * A runtime exception thrown if a routing processor such as a
 * {@link org.apache.camel.processor.RecipientList RecipientList} is unable to resolve an
 * {@link Endpoint} from a URI.
 *
 * @version $Revision: 642753 $
 */
public class NoSuchEndpointException extends RuntimeCamelException {
    private static final long serialVersionUID = -8721487431101572630L;
    private final String uri;

    public NoSuchEndpointException(String uri) {
        super("No endpoint could be found for: " + uri);
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
}
