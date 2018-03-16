package org.apache.camel.component.jetty;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.AsyncCallback;
import org.apache.camel.component.http.CamelServlet;
import org.apache.camel.component.http.HttpConsumer;
import org.apache.camel.component.http.HttpExchange;
import org.mortbay.util.ajax.Continuation;
import org.mortbay.util.ajax.ContinuationSupport;

/**
 * @version $Revision: 718246 $
 */
public class CamelContinuationServlet extends CamelServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpConsumer consumer = resolve(request);
            if (consumer == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            final Continuation continuation = ContinuationSupport.getContinuation(request, null);
            if (continuation.isNew()) {

                final HttpExchange exchange = new HttpExchange(consumer.getEndpoint(), request, response);
                boolean sync = consumer.getAsyncProcessor().process(exchange, new AsyncCallback() {
                    public void done(boolean sync) {
                        if (sync) {
                            return;
                        }
                        continuation.setObject(exchange);
                        continuation.resume();
                    }
                });

                if (!sync) {
                    continuation.suspend(0);
                }


                consumer.getBinding().writeResponse(exchange, response);
                return;
            }

            if (continuation.isResumed()) {
                HttpExchange exchange = (HttpExchange)continuation.getObject();
                consumer.getBinding().writeResponse(exchange, response);
                return;
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}
