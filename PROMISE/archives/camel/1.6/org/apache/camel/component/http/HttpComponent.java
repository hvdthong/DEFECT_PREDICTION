package org.apache.camel.component.http;

import java.net.URI;
import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.HeaderFilterStrategyAware;
import org.apache.camel.ResolveEndpointFailedException;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.spi.HeaderFilterStrategy;
import org.apache.camel.util.CamelContextHelper;
import org.apache.camel.util.IntrospectionSupport;
import org.apache.camel.util.URISupport;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpClientParams;

/**
 * Component</a>
 *
 * @version $Revision: 731164 $
 */
public class HttpComponent extends DefaultComponent<HttpExchange> implements HeaderFilterStrategyAware {
    protected HttpClientConfigurer httpClientConfigurer;
    protected HttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
    protected HeaderFilterStrategy headerFilterStrategy;
    protected HttpBinding httpBinding;

    public HttpComponent() {
        this.setHeaderFilterStrategy(new HttpHeaderFilterStrategy());
    }
    
    /**
     * Connects the URL specified on the endpoint to the specified processor.
     *
     * @param  consumer the consumer
     * @throws Exception can be thrown
     */
    public void connect(HttpConsumer consumer) throws Exception {
    }

    /**
     * Disconnects the URL specified on the endpoint from the specified processor.
     *
     * @param  consumer the consumer
     * @throws Exception can be thrown
     */
    public void disconnect(HttpConsumer consumer) throws Exception {
    }

    /** 
     * Setting http binding and http client configurer according to the parameters
     * Also setting the BasicAuthenticationHttpClientConfigurer if the username 
     * and password option are not null.
     * 
     * @param parameters the map of parameters 
     * 
     */
    protected void configureParameters(Map parameters) {
        String ref = getAndRemoveParameter(parameters, "httpBindingRef", String.class);
        if (ref != null) {
            httpBinding = CamelContextHelper.mandatoryLookup(getCamelContext(), ref, HttpBinding.class);
        }
        
        String username = getAndRemoveParameter(parameters, "username", String.class);
        String password = getAndRemoveParameter(parameters, "password", String.class);
        if (username != null && password != null) {
            httpClientConfigurer = new BasicAuthenticationHttpClientConfigurer(username, password);
        }
        
        ref = getAndRemoveParameter(parameters, "httpClientConfigurerRef", String.class);
        if (ref != null) {
            httpClientConfigurer = CamelContextHelper.mandatoryLookup(getCamelContext(), ref, HttpClientConfigurer.class);
        }
    }
    
    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters)
        throws Exception {

        HttpClientParams params = new HttpClientParams();
        IntrospectionSupport.setProperties(params, parameters, "httpClient.");        
        
        configureParameters(parameters);

        URI httpUri = URISupport.createRemainingURI(new URI(uri), parameters);
        uri = httpUri.toString();

        String part = httpUri.getSchemeSpecificPart();
        if (part != null) {
            part = part.toLowerCase();
                throw new ResolveEndpointFailedException(uri,
                        "The uri part is not configured correctly. You have duplicated the http(s) protocol.");
            }
        }

        HttpEndpoint endpoint = new HttpEndpoint(uri, this, httpUri, params, httpConnectionManager, httpClientConfigurer);
        if (httpBinding != null) {
            endpoint.setBinding(httpBinding);
        }
        return endpoint;
    }

    @Override
    protected boolean useIntrospectionOnEndpoint() {
        return false;
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

    public HeaderFilterStrategy getHeaderFilterStrategy() {
        return headerFilterStrategy;
    }

    public void setHeaderFilterStrategy(HeaderFilterStrategy strategy) {
        headerFilterStrategy = strategy;
    }

    public HttpBinding getHttpBinding() {
        return httpBinding;
    }

    public void setHttpBinding(HttpBinding httpBinding) {
        this.httpBinding = httpBinding;
    }
}
