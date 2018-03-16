package org.apache.camel.component.http;

import org.apache.camel.Message;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.impl.PollingConsumerSupport;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;

/**
 * A polling HTTP consumer which by default performs a GET
 *
 * @version $Revision: 1.1 $
 */
public class HttpPollingConsumer extends PollingConsumerSupport<HttpExchange> {
    private final HttpEndpoint endpoint;
    private HttpClient httpClient = new HttpClient();

    public HttpPollingConsumer(HttpEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public HttpExchange receive() {
        return receiveNoWait();
    }

    public HttpExchange receive(long timeout) {
        return receiveNoWait();
    }

    public HttpExchange receiveNoWait() {
        try {
            HttpExchange exchange = endpoint.createExchange();
            HttpMethod method = createMethod();
            int responseCode = httpClient.executeMethod(method);

            byte[] responseBody = method.getResponseBody();
            Message message = exchange.getIn();
            message.setBody(responseBody);

            Header[] headers = method.getResponseHeaders();
            for (Header header : headers) {
                String name = header.getName();
                String value = header.getValue();
                message.setHeader(name, value);
            }

            message.setHeader("http.responseCode", responseCode);
            return exchange;
        }
        catch (IOException e) {
            throw new RuntimeCamelException(e);
        }
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    protected HttpMethod createMethod() {
        String uri = endpoint.getEndpointUri();
        return new GetMethod(uri);
    }

    protected void doStart() throws Exception {
    }

    protected void doStop() throws Exception {
    }
}
