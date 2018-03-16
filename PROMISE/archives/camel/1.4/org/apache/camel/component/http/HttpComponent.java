package org.apache.camel.component.http;

import java.net.URI;
import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.util.IntrospectionSupport;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpClientParams;

/**
 * Component</a>
 *
 * @version $Revision: 640731 $
 */
public class HttpComponent extends DefaultComponent<HttpExchange> {

    private HttpClientConfigurer httpClientConfigurer;

    private HttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();

    /**
     * Connects the URL specified on the endpoint to the specified processor.
     *
     * @throws Exception
     */
    public void connect(HttpConsumer consumer) throws Exception {
    }

    /**
     * Disconnects the URL specified on the endpoint from the specified
     * processor.
     *
     * @throws Exception
     */
    public void disconnect(HttpConsumer consumer) throws Exception {
    }

    public HttpClientConfigurer getHttpClientConfigurer() {
        return httpClientConfigurer;
    }

    public void setHttpClientConfigurer(HttpClientConfigurer httpClientConfigurer) {
        this.httpClientConfigurer = httpClientConfigurer;
    }

    public HttpConnectionManager getHttpConnectionManager() {
        return httpConnectionManager;
    }

    public void setHttpConnectionManager(HttpConnectionManager httpConnectionManager) {
        this.httpConnectionManager = httpConnectionManager;
    }

    @Override
    protected Endpoint<HttpExchange> createEndpoint(String uri, String remaining, Map parameters)
        throws Exception {
        HttpClientParams params = new HttpClientParams();
        IntrospectionSupport.setProperties(params, parameters, "httpClient.");
        return new HttpEndpoint(uri, this, new URI(uri), params, httpConnectionManager, httpClientConfigurer);
    }

    @Override
    protected boolean useIntrospectionOnEndpoint() {
        return false;
    }
}
