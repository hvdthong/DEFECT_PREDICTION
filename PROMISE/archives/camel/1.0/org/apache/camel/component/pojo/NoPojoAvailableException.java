package org.apache.camel.component.pojo;

import org.apache.camel.CamelException;

/**
 * @version $Revision: 1.1 $
 */
public class NoPojoAvailableException extends CamelException {
    private final PojoEndpoint endpoint;

    public NoPojoAvailableException(PojoEndpoint endpoint) {
        super("No POJO available for endpoint: " + endpoint);
        this.endpoint = endpoint;
    }

    public PojoEndpoint getEndpoint() {
        return endpoint;
    }
}
