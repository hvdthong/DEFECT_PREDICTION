package org.apache.camel.component.cxf.interceptors;

import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.Element;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.staxutils.StaxUtils;

public class PayloadContentRedirectInterceptor extends AbstractPhaseInterceptor<Message> {

    public PayloadContentRedirectInterceptor() {
        super(Phase.POST_STREAM);        
    }

    @SuppressWarnings("unchecked")
    public void handleMessage(Message message) throws Fault {
        XMLStreamWriter out = message.getContent(XMLStreamWriter.class);
        List<Element> in = message.get(List.class);
        try {
            for (Element el : in) {
                StaxUtils.writeElement(el, out, false, true);
            }
        } catch (XMLStreamException e) {
            throw new Fault(e);
        }
    }
}
