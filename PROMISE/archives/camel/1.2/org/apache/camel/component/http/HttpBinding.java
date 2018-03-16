package org.apache.camel.component.http;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.Message;

/**
 * @version $Revision: 579718 $
 */
public class HttpBinding {

    /**
     * Writes the exchange to the servlet response
     * 
     * @param response
     * @throws IOException
     */
    public void writeResponse(HttpExchange exchange, HttpServletResponse response) throws IOException {
        Message out = exchange.getOut();
        if (out != null) {
            
            for (String key : out.getHeaders().keySet()) {
                String value = out.getHeader(key, String.class);
                if (value != null) {
                    response.setHeader(key, value);
                }
            }
            
            if (out.getBody() != null) {

                InputStream is = out.getBody(InputStream.class);
                if (is != null) {
                    ServletOutputStream os = response.getOutputStream();
                    int c;
                    while ((c = is.read()) >= 0) {
                        os.write(c);
                    }
                } else {
                    String data = out.getBody(String.class);
                    if (data != null) {
                        response.getWriter().print(data);
                    }
                }
            }
        }
    }

    /**
     * Parses the body from a HTTP message
     */
    public Object parseBody(HttpMessage httpMessage) throws IOException {
        HttpServletRequest request = httpMessage.getRequest();
        return request.getReader();
    }
}
