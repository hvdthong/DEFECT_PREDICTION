package org.apache.camel.component.cxf;

import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

/**
 * The binding of how Camel messages get mapped to Apache CXF and back again
 *
 * @version $Revision: 580963 $
 */
public class CxfBinding {
    public Object extractBodyFromCxf(CxfExchange exchange, Message message) {
        return getBody(message);
    }

    protected Object getBody(Message message) {
        Set<Class<?>> contentFormats = message.getContentFormats();
        if (contentFormats != null) {
            for (Class<?> contentFormat : contentFormats) {            
                Object answer = message.getContent(contentFormat);
                if (answer != null) {
                    return answer;
                }
            }
        }    
        return null;
    }

    public Message createCxfMessage(CxfExchange exchange) {
        Message answer = exchange.getInMessage();


        CxfMessage in = exchange.getIn();
        Object body = in.getBody(InputStream.class);
        if (body == null) {
            body = in.getBody();
        }
        if (body instanceof InputStream) {
        	answer.setContent(InputStream.class, body);
        } else if (body instanceof List) {
        	answer.setContent(List.class, body);
                answer.setContent(String.class, in.getHeader(CxfConstants.OPERATION_NAME));
        }
        
        
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
    
    public void storeCxfFault(CxfExchange exchange, Message message) {
        CxfMessage fault = exchange.getFault();
        if (fault != null) {
            fault.setBody(getBody(message));
        }
    }
}
