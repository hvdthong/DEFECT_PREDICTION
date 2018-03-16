package org.apache.camel.component.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.Message;
import org.apache.camel.spi.HeaderFilterStrategy;

/**
 * Binding between {@link HttpMessage} and {@link HttpServletResponse}.
 *
 * @version $Revision: 719673 $
 */
public class DefaultHttpBinding implements HttpBinding {

    private boolean useReaderForPayload;
    private HeaderFilterStrategy headerFilterStrategy = new HttpHeaderFilterStrategy();

    public DefaultHttpBinding() {
    }

    public DefaultHttpBinding(HeaderFilterStrategy headerFilterStrategy) {
        this.headerFilterStrategy = headerFilterStrategy;
    }

    public void readRequest(HttpServletRequest request, HttpMessage message) {
        message.getBody();
        message.getHeaders();
    }

    public void writeResponse(HttpExchange exchange, HttpServletResponse response) throws IOException {
        if (exchange.isFailed()) {
            Message fault = exchange.getFault(false);
            if (fault != null) {
                doWriteFaultResponse(fault, response);
            } else {
                doWriteExceptionResponse(exchange.getException(), response);
            }
        } else {
            Message out = exchange.getOut();
            if (out != null) {
                doWriteResponse(out, response);
            }
        }
    }

    public void doWriteExceptionResponse(Throwable exception, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        PrintWriter pw = response.getWriter();
        exception.printStackTrace(pw);

        pw.flush();
    }

    public void doWriteFaultResponse(Message message, HttpServletResponse response) throws IOException {
        doWriteResponse(message, response);
    }

    public void doWriteResponse(Message message, HttpServletResponse response) throws IOException {
        if (message.getHeader(HttpProducer.HTTP_RESPONSE_CODE) != null) {
            int code = message.getHeader(HttpProducer.HTTP_RESPONSE_CODE, Integer.class);
            response.setStatus(code);
        }
        if (message.getHeader("Content-Type") != null) {
            String contentType = message.getHeader("Content-Type", String.class);
            response.setContentType(contentType);
        }

        for (String key : message.getHeaders().keySet()) {
            String value = message.getHeader(key, String.class);
            if (headerFilterStrategy != null
                    && !headerFilterStrategy.applyFilterToCamelHeaders(key, value)) {
                response.setHeader(key, value);
            }
        }

        if (message.getBody() != null) {
            InputStream is = message.getBody(InputStream.class);
            int length = 0;
            if (is != null) {
                ServletOutputStream os = null;
                try {
                    os = response.getOutputStream();
                    int c;
                    while ((c = is.read()) >= 0) {
                        os.write(c);
                        length++;
                    }
                    response.setContentLength(length);
                    os.flush();
                } finally {
                    os.close();
                    is.close();
                }
            } else {
                String data = message.getBody(String.class);
                if (data != null) {
                    response.setContentLength(data.length());
                    response.getWriter().print(data);
                    response.getWriter().flush();
                }
            }

        }
    }

    public Object parseBody(HttpMessage httpMessage) throws IOException {
        HttpServletRequest request = httpMessage.getRequest();
        if (isUseReaderForPayload()) {
            return request.getReader();
        } else {
            return request.getInputStream();
        }
    }

    public boolean isUseReaderForPayload() {
        return useReaderForPayload;
    }

    public void setUseReaderForPayload(boolean useReaderForPayload) {
        this.useReaderForPayload = useReaderForPayload;
    }

    public HeaderFilterStrategy getHeaderFilterStrategy() {
        return headerFilterStrategy;
    }

    public void setHeaderFilterStrategy(HeaderFilterStrategy headerFilterStrategy) {
        this.headerFilterStrategy = headerFilterStrategy;
    }
}
