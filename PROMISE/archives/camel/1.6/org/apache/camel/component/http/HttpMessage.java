package org.apache.camel.component.http;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.camel.RuntimeCamelException;
import org.apache.camel.impl.DefaultMessage;

/**
 * @version $Revision: 719673 $
 */
public class HttpMessage extends DefaultMessage {
    private HttpServletRequest request;

    public HttpMessage(HttpExchange exchange, HttpServletRequest request) {
        setExchange(exchange);
        this.request = request;

        getExchange().getEndpoint().getBinding().readRequest(request, this);
    }

    @Override
    public HttpExchange getExchange() {
        return (HttpExchange)super.getExchange();
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    @Override
    protected Object createBody() {
        try {
            return getExchange().getEndpoint().getBinding().parseBody(this);
        } catch (IOException e) {
            throw new RuntimeCamelException(e);
        }
    }

    @Override
    protected void populateInitialHeaders(Map<String, Object> map) {
        Enumeration names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = (String)names.nextElement();
            Object value = request.getHeader(name);
            map.put(name, value);
        }

        if (request.getMethod().equalsIgnoreCase("GET")) {
            names = request.getParameterNames();
            while (names.hasMoreElements()) {
                String name = (String)names.nextElement();
                Object value = request.getParameter(name);
                map.put(name, value);
            }
        }

        map.put(HttpMethods.HTTP_METHOD, request.getMethod());
        map.put(HttpProducer.QUERY, request.getQueryString());
    }
}
