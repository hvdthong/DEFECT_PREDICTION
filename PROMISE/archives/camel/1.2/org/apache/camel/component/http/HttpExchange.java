package org.apache.camel.component.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.ExchangePattern;

/**
 * Represents a HTTP exchange which exposes the underlying HTTP abtractions via
 * {@link #getRequest()} and {@link #getResponse()} 
 *
 * @version $Revision: 572625 $
 */
public class HttpExchange extends DefaultExchange {
    private final HttpEndpoint endpoint;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public HttpExchange(HttpEndpoint endpoint, ExchangePattern pattern) {
        super(endpoint.getContext(), pattern);
        this.endpoint = endpoint;
    }

    public HttpExchange(HttpEndpoint endpoint, HttpServletRequest request, HttpServletResponse response) {
        this(endpoint, getPatternFromRequest(request));
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

    protected static ExchangePattern getPatternFromRequest(HttpServletRequest request) {
        return ExchangePattern.InOut;
    }
}
