package org.apache.camel.component.jetty;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Producer;
import org.apache.camel.component.http.HttpBinding;
import org.apache.camel.component.http.HttpEndpoint;
import org.apache.camel.component.http.HttpExchange;
import org.apache.camel.impl.DefaultProducer;
import org.mortbay.io.Buffer;
import org.mortbay.jetty.HttpFields;
import org.mortbay.jetty.HttpFields.Field;
import org.mortbay.jetty.HttpMethods;
import org.mortbay.jetty.HttpURI;

import org.mortbay.jetty.client.HttpClient;
import org.mortbay.jetty.client.HttpExchange.ContentExchange;

public class JettyHttpProducer extends DefaultProducer<HttpExchange> implements Producer<HttpExchange>, AsyncProcessor {

    private final class CamelContentExchange extends ContentExchange {

        private final AsyncCallback callback;
        private final Exchange exchange;
        private HttpFields responseFields;

        private CamelContentExchange(Exchange exchange, AsyncCallback callback) {
            this.exchange = exchange;
            this.callback = callback;
            responseFields = new HttpFields();
        }

        protected void onResponseComplete() throws IOException {
            super.onRequestComplete();
            try {
                Message out = exchange.getOut(true);
                out.setBody(getResponseContent());
                for (Iterator i = responseFields.getFields(); i.hasNext();) {
                    Field field = (Field)i.next();
                    out.setHeader(field.getName(), field.getValue());
                }
            } catch (Throwable e) {
                exchange.setException(e);
            }
            callback.done(false);
        }

        public HttpFields getResponseFields() {
            return responseFields;
        }

        protected void onResponsetHeader(Buffer name, Buffer value) throws IOException {
            responseFields.add(name, value);
        }

    }

    private HttpClient httpClient;
    private String address;

    public JettyHttpProducer(HttpEndpoint endpoint) {
        super(endpoint);
        httpClient = ((JettyHttpComponent)endpoint.getComponent()).getHttpClient();

        address = endpoint.getHttpUri().toString();

        HttpURI uri = new HttpURI(address);
        if (uri.getCompletePath() == null) {
            address += "/";
        }
    }

    public void process(Exchange exchange) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        process(exchange, new AsyncCallback() {
            public void done(boolean sync) {
                latch.countDown();
            }
        });
        latch.await();
    }

    public boolean process(final Exchange exchange, final AsyncCallback callback) {

        ContentExchange jettyExchange = new CamelContentExchange(exchange, callback);

        jettyExchange.setURL(address);

        Message in = exchange.getIn();
        InputStream is = in.getBody(InputStream.class);
        if (is != null) {
            jettyExchange.setMethod(HttpMethods.POST);
            jettyExchange.setRequestContentSource(is);
        } else {
            Buffer buffer = in.getBody(Buffer.class);
            if (buffer != null) {
                jettyExchange.setMethod(HttpMethods.POST);
                jettyExchange.setRequestContent(buffer);
            } else {
                jettyExchange.setMethod(HttpMethods.GET);
            }
        }

        HttpBinding binding = ((HttpEndpoint)getEndpoint()).getBinding();
        for (String name : in.getHeaders().keySet()) {
            String value = in.getHeader(name, String.class);
            if ("Content-Type".equals(name)) {
                jettyExchange.setRequestContentType(value);
            } else if (binding.shouldHeaderBePropagated(name, value)) {
                jettyExchange.addRequestHeader(name, value);
            }
        }
        try {
            httpClient.send(jettyExchange);
        } catch (IOException e) {
            exchange.setException(e);
            return true;
        }
        return false;
    }

}
