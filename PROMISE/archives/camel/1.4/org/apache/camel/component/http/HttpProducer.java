package org.apache.camel.component.http;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Producer;
import org.apache.camel.component.http.helper.LoadingByteArrayOutputStream;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;


import static org.apache.camel.component.http.HttpMethods.HTTP_METHOD;

/**
 * @version $Revision: 651239 $
 */
public class HttpProducer extends DefaultProducer<HttpExchange> implements Producer<HttpExchange> {
    public static final String HTTP_RESPONSE_CODE = "http.responseCode";
    public static final String QUERY = "org.apache.camel.component.http.query";

    public static final Set<String> HEADERS_TO_SKIP = new HashSet<String>(Arrays.asList("content-length",
                                                                                        "content-type",
                                                                                        HTTP_RESPONSE_CODE
                                                                                            .toLowerCase()));
    private HttpClient httpClient;

    public HttpProducer(HttpEndpoint endpoint) {
        super(endpoint);
        httpClient = endpoint.createHttpClient();
    }

    public void process(Exchange exchange) throws Exception {
        HttpMethod method = createMethod(exchange);
        Message in = exchange.getIn();
        HttpBinding binding = ((HttpEndpoint)getEndpoint()).getBinding();
        for (String headerName : in.getHeaders().keySet()) {
            String headerValue = in.getHeader(headerName, String.class);
            if (binding.shouldHeaderBePropagated(headerName, headerValue)) {
                method.addRequestHeader(headerName, headerValue);
            }
        }

        Message out = exchange.getOut(true);
        try {
            int responseCode = httpClient.executeMethod(method);
            out.setHeaders(in.getHeaders());
            out.setHeader(HTTP_RESPONSE_CODE, responseCode);
            LoadingByteArrayOutputStream bos = new LoadingByteArrayOutputStream();
            InputStream is = method.getResponseBodyAsStream();
            IOUtils.copy(is, bos);
            bos.flush();
            is.close();
            out.setBody(bos.createInputStream());
        } finally {
            method.releaseConnection();
        }

        Header[] headers = method.getResponseHeaders();
        for (Header header : headers) {
            String name = header.getName();
            String value = header.getValue();
            out.setHeader(name, value);
        }

    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    protected HttpMethod createMethod(Exchange exchange) {
        String uri = ((HttpEndpoint)getEndpoint()).getHttpUri().toString();

        RequestEntity requestEntity = createRequestEntity(exchange);
        Object m = exchange.getIn().getHeader(HTTP_METHOD);
        HttpMethods ms = m instanceof HttpMethods
            ? (HttpMethods)m : HttpMethods.valueOf(m == null
                                                       ? requestEntity == null
                                                           ? "GET" : "POST"
                                                               : m.toString());

        HttpMethod method = ms.createMethod(uri);

        if (exchange.getIn().getHeader(QUERY) != null) {
            method.setQueryString(exchange.getIn().getHeader(QUERY, String.class));
        }
        if (ms.isEntityEnclosing()) {
            ((EntityEnclosingMethod)method).setRequestEntity(requestEntity);
        }
        return method;
    }

    protected RequestEntity createRequestEntity(Exchange exchange) {
        Message in = exchange.getIn();
        if (in.getBody() == null) {
            return null;
        }
        RequestEntity entity = in.getBody(RequestEntity.class);
        if (entity == null) {

            String data = in.getBody(String.class);
            String contentType = in.getHeader("Content-Type", String.class);
            try {
                if (contentType != null) {
                    return new StringRequestEntity(data, contentType, null);
                }
                return new StringRequestEntity(data, null, null);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return entity;
    }

    protected boolean shouldHeaderBePropagated(String headerName, String headerValue) {
        if (headerValue == null) {
            return false;
        }
        if (HTTP_METHOD.equals(headerName)) {
            return false;
        }
        if (headerName.startsWith("org.apache.camel")) {
            return false;
        }
        if (HEADERS_TO_SKIP.contains(headerName.toLowerCase())) {
            return false;
        }
        return true;
    }
}
