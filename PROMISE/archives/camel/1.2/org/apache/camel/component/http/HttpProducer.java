package org.apache.camel.component.http;

import java.io.InputStream;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

/**
 * @version $Revision: 1.1 $
 */
public class HttpProducer extends DefaultProducer<HttpExchange> implements Producer<HttpExchange> {
    public static final String QUERY = "org.apache.camel.component.http.query";
    private HttpClient httpClient = new HttpClient();

    public HttpProducer(HttpEndpoint endpoint) {
        super(endpoint);
    }

    public void process(Exchange exchange) throws Exception {
        HttpMethod method = createMethod(exchange);
        int responseCode = httpClient.executeMethod(method);

        InputStream in = method.getResponseBodyAsStream();
        Message out = exchange.getOut(true);
        out.setBody(in);

        Header[] headers = method.getResponseHeaders();
        for (Header header : headers) {
            String name = header.getName();
            String value = header.getValue();
            out.setHeader(name, value);
        }

        out.setHeader("http.responseCode", responseCode);
    }

    protected HttpMethod createMethod(Exchange exchange) {
        String uri = ((HttpEndpoint)getEndpoint()).getHttpUri().toString();
        RequestEntity requestEntity = createRequestEntity(exchange);
        if (requestEntity == null) {
            GetMethod method = new GetMethod(uri);
            if (exchange.getIn().getHeader(QUERY) != null){
                method.setQueryString(exchange.getIn().getHeader(QUERY, String.class));
            }
            return method;
        }
        PostMethod method = new PostMethod(uri);
        method.setRequestEntity(requestEntity);
        return method;
    }

    protected RequestEntity createRequestEntity(Exchange exchange) {
        Message in = exchange.getIn();
        RequestEntity entity = in.getBody(RequestEntity.class);
        if (entity == null) {
            byte[] data = in.getBody(byte[].class);
            if (data == null) {
                return null;
            }
            String contentType = in.getHeader("Content-Type", String.class);
            if (contentType != null) {
                return new ByteArrayRequestEntity(data, contentType);
            } else {
                return new ByteArrayRequestEntity(data);
            }
        }
        return entity;
    }
}
