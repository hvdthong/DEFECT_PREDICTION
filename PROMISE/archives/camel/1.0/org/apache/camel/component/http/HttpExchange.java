package org.apache.camel.component.http;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultExchange;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Represents a HTTP exchange which exposes the underlying HTTP abtractions via
 * {@link #getRequest()} and {@link #getResponse()} 
 *
 * @version $Revision: 549890 $
 */
public class HttpExchange extends DefaultExchange {
    private final HttpEndpoint endpoint;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public HttpExchange(HttpEndpoint endpoint) {
        super(endpoint.getContext());
        this.endpoint = endpoint;
    }

    public HttpExchange(HttpEndpoint endpoint, HttpServletRequest request, HttpServletResponse response) {
        this(endpoint);
        this.request = request;
        this.response = response;
        setIn(new HttpMessage(this, request));
    }


    /**
     * Returns the underlying Servlet request for inbound HTTP requests
     *
     * @return the underlying Servlet request for inbound HTTP requests
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * Returns the underlying Servlet response for inbound HTTP requests
     *
     * @return the underlying Servlet response for inbound HTTP requests
     */
    public HttpServletResponse getResponse() {
        return response;
    }

    public HttpEndpoint getEndpoint() {
        return endpoint;
    }
}
