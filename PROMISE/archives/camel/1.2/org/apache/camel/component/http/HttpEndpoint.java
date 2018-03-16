package org.apache.camel.component.http;

import org.apache.camel.PollingConsumer;
import org.apache.camel.Producer;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultPollingEndpoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * endpoint</a>
 *
 * @version $Revision: 572625 $
 */
public class HttpEndpoint extends DefaultPollingEndpoint<HttpExchange> {

    private HttpBinding binding;
    private HttpComponent component;
    private URI httpUri;

    public HttpEndpoint(String endPointURI, HttpComponent component, URI httpURI) throws URISyntaxException {
        super(endPointURI, component);
        this.component = component;
        this.httpUri = httpURI;
    }

    public Producer<HttpExchange> createProducer() throws Exception {
        return new HttpProducer(this);
    }

    @Override
    public PollingConsumer<HttpExchange> createPollingConsumer() throws Exception {
        return new HttpPollingConsumer(this);
    }

    public HttpExchange createExchange(ExchangePattern pattern) {
        return new HttpExchange(this, pattern);
    }

    public HttpExchange createExchange(HttpServletRequest request, HttpServletResponse response) {
        return new HttpExchange(this, request, response);
    }

    public HttpBinding getBinding() {
        if (binding == null) {
            binding = new HttpBinding();
        }
        return binding;
    }

    public void setBinding(HttpBinding binding) {
        this.binding = binding;
    }

    public boolean isSingleton() {
        return true;
    }

    public void connect(HttpConsumer consumer) throws Exception {
        component.connect(consumer);
    }

    public void disconnect(HttpConsumer consumer) throws Exception {
        component.disconnect(consumer);
    }

    public String getPath() {
        return httpUri.getPath();
    }

    public int getPort() {
        if (httpUri.getPort() == -1) {
            if ("https".equals(getProtocol())) {
                return 443;
            } else {
                return 80;
            }
        }
        return httpUri.getPort();
    }

    public String getProtocol() {
        return httpUri.getScheme();
    }

    public URI getHttpUri() {
        return httpUri;
    }
}
