package org.apache.camel.component.http;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @version $Revision: 640731 $
 */
public class CamelServlet extends HttpServlet {

    private ConcurrentHashMap<String, HttpConsumer> consumers = new ConcurrentHashMap<String, HttpConsumer>();

    public CamelServlet() {
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            HttpConsumer consumer = resolve(request);
            if (consumer == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            HttpExchange exchange = new HttpExchange(consumer.getEndpoint(), request, response);
            consumer.getProcessor().process(exchange);


            consumer.getBinding().writeResponse(exchange, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected HttpConsumer resolve(HttpServletRequest request) {
        String path = request.getPathInfo();
        return consumers.get(path);
    }

    public void connect(HttpConsumer consumer) {
        consumers.put(consumer.getPath(), consumer);
    }

    public void disconnect(HttpConsumer consumer) {
        consumers.remove(consumer.getPath());
    }

}
