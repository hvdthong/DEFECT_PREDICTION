package org.apache.camel.component.http;

import org.apache.camel.impl.DefaultMessage;
import org.apache.camel.Exchange;
import org.apache.camel.RuntimeCamelException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Enumeration;
import java.io.IOException;

/**
 * @version $Revision: 550444 $
 */
public class HttpMessage extends DefaultMessage {
    private HttpServletRequest request;


    public HttpMessage(HttpExchange exchange, HttpServletRequest request) {
        setExchange(exchange);
        this.request = request;

        getBody();
        getHeaders();
    }

    @Override
    public HttpExchange getExchange() {
        return (HttpExchange) super.getExchange();
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    @Override
    protected Object createBody() {
        try {
            return getExchange().getEndpoint().getBinding().parseBody(this);
        }
        catch (IOException e) {
            throw new RuntimeCamelException(e);
        }
    }

    @Override
    protected void populateInitialHeaders(Map<String, Object> map) {
        Enumeration names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            Object value = request.getHeader(name);
            map.put(name, value);
        }
    }
}
