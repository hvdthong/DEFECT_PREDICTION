package org.apache.camel.component.cxf.invoker;

import java.util.List;
import org.w3c.dom.Element;
import org.apache.cxf.binding.soap.SoapHeader;



public class PayloadMessage {
    private List<Element> payload;
    private List<SoapHeader> headers;
    
    public PayloadMessage(List<Element> payload, List<SoapHeader> headers) {
        this.payload = payload;
        this.headers = headers;
    }
    
    public List<Element> getPayload() {
        return payload;
    }
    
    public List<SoapHeader> getHeaders() {
        return headers;
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("payload: " + payload);
        buf.append(" headers: " + headers);
        return buf.toString();
    }
}
