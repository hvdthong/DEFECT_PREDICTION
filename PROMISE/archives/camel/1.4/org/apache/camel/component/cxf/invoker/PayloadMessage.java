package org.apache.camel.component.cxf.invoker;

import java.util.List;

import org.w3c.dom.Element;

public class PayloadMessage {
    private List<Element> payload;
    private Element header;
    
    public PayloadMessage(List<Element> payload, Element header) {
        this.payload = payload;
        this.header = header;
    }
    
    public List<Element> getPayload() {
        return payload;
    }
    
    public Element getHeader() {
        return header;
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("payload: " + payload);
        buf.append(" header: " + header);
        return buf.toString();
    }
}
