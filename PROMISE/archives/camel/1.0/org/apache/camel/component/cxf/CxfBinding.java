package org.apache.camel.component.cxf;

import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;

import java.io.InputStream;
import java.util.Set;

/**
 * The binding of how Camel messages get mapped to Apache CXF and back again
 *
 * @version $Revision: 525898 $
 */
public class CxfBinding {

    public Object extractBodyFromCxf(CxfExchange exchange, Message message) {
        return getBody(message);
    }

    protected Object getBody(Message message) {
        Set<Class<?>> contentFormats = message.getContentFormats();
        for (Class<?> contentFormat : contentFormats) {
            Object answer = message.getContent(contentFormat);
            if (answer != null) {
                return answer;
            }
        }
        return null;
    }

    public MessageImpl createCxfMessage(CxfExchange exchange) {
        MessageImpl answer = (MessageImpl) exchange.getInMessage();

        CxfMessage in = exchange.getIn();
        Object body = in.getBody(InputStream.class);
        if (body == null) {
            body = in.getBody();
        }
        answer.setContent(InputStream.class, body);

        /*
        Set<Map.Entry<String, Object>> entries = in.getHeaders().entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            answer.put(entry.getKey(), entry.getValue());
        }
        */
        return answer;
    }

    public void storeCxfResponse(CxfExchange exchange, Message response) {
        CxfMessage out = exchange.getOut();
        if (response != null) {
            out.setMessage(response);
            out.setBody(getBody(response));
        }
    }

    public void storeCxfResponse(CxfExchange exchange, Object response) {
        CxfMessage out = exchange.getOut();
        if (response != null) {
            out.setBody(response);
        }
    }
}
