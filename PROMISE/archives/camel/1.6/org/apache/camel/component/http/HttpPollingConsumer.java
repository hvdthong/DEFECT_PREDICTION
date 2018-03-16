package org.apache.camel.component.http;

import java.io.IOException;
import java.io.InputStream;

import org.apache.camel.Message;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.component.http.helper.LoadingByteArrayOutputStream;
import org.apache.camel.impl.PollingConsumerSupport;
import org.apache.camel.spi.HeaderFilterStrategy;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;

/**
 * A polling HTTP consumer which by default performs a GET
 *
 * @version $Revision: 682597 $
 */
public class HttpPollingConsumer extends PollingConsumerSupport<HttpExchange> {
    private final HttpEndpoint endpoint;
    private HttpClient httpClient;

    public HttpPollingConsumer(HttpEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
        httpClient = endpoint.createHttpClient();
    }

    public HttpExchange receive() {
        return receiveNoWait();
    }

    public HttpExchange receive(long timeout) {
        return receiveNoWait();
    }

    public HttpExchange receiveNoWait() {
        HttpExchange exchange = endpoint.createExchange();
        HttpMethod method = createMethod();

        try {
            int responseCode = httpClient.executeMethod(method);
            LoadingByteArrayOutputStream bos = new LoadingByteArrayOutputStream();
            InputStream is = method.getResponseBodyAsStream();
            IOUtils.copy(is, bos);
            bos.flush();
            is.close();
            Message message = exchange.getIn();
            message.setBody(bos.createInputStream());

            Header[] headers = method.getResponseHeaders();
            HeaderFilterStrategy strategy = endpoint.getHeaderFilterStrategy();
            for (Header header : headers) {
                String name = header.getName();
                String value = header.getValue();
                if (strategy != null && !strategy.applyFilterToExternalHeaders(name, value)) {
                    message.setHeader(name, value);
                }
            }
        
            message.setHeader("http.responseCode", responseCode);
            return exchange;
        } catch (IOException e) {
            throw new RuntimeCamelException(e);
        } finally {
            method.releaseConnection();
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
