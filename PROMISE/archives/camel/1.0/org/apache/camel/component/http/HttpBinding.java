package org.apache.camel.component.http;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @version $Revision: 549890 $
 */
public class HttpBinding {

    /**
     * Writes the  exchange to the servlet response
     */
    public void writeResponse(HttpExchange exchange) {
        /** TODO */
    }

    /**
     * Parses the body from a HTTP message
     */
    public Object parseBody(HttpMessage httpMessage) throws IOException {
        HttpServletRequest request = httpMessage.getRequest();
        return request.getReader();
    }
}
