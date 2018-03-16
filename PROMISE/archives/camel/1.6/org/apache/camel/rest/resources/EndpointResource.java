package org.apache.camel.rest.resources;

import org.apache.camel.Endpoint;
import org.apache.camel.rest.model.EndpointLink;

/**
 * @version $Revision: 699772 $
 */
public class EndpointResource {

    private final Endpoint endpoint;

    public EndpointResource(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public String getHref() {
        return new EndpointLink(endpoint).getHref();
    }
    public String getUri() {
        return  endpoint.getEndpointUri();
    }
}
