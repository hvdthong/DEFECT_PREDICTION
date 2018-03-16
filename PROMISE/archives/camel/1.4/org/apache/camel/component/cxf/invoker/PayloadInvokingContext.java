package org.apache.camel.component.cxf.invoker;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;

import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;



public class PayloadInvokingContext extends AbstractInvokingContext {
    private static final Logger LOG = LogUtils.getL7dLogger(PayloadInvokingContext.class);


    public PayloadInvokingContext() {

    }

    public void setRequestOutMessageContent(Message message, Map<Class, Object> contents) {

        PayloadMessage request = (PayloadMessage)contents.get(PayloadMessage.class);

        Element header = request.getHeader();
        List<Element> payload = request.getPayload();

        if (LOG.isLoggable(Level.FINEST)) {
            LOG.finest("header = " + header + ", paylaod = " + payload);
        }

        message.put(Element.class, header);
        message.put(List.class, payload);
    }

    @SuppressWarnings("unchecked")
    public Object getResponseObject(Exchange exchange, Map<String, Object> responseContext) {
        PayloadMessage payloadMsg = null;

        Message msg = exchange.getInMessage();
        List<Element> payload = getResponseObject(msg , responseContext, List.class);
        Element header = exchange.getInMessage().get(Element.class);
        payloadMsg = new PayloadMessage(payload, header);

        if (LOG.isLoggable(Level.FINEST)) {
            LOG.finest(payloadMsg.toString());
        }

        return payloadMsg;
    }

    @Override
    protected <T> T getResponseObject(Message inMessage, Map<String, Object> responseContext,
                                      Class <T> clazz) {

        T retval = null;
        if (inMessage != null) {
            if (null != responseContext) {
                responseContext.putAll(inMessage);
                LOG.info("set responseContext to be" + responseContext);
            }
            retval = inMessage.get(clazz);
        }
        return retval;
    }

    public void setResponseContent(Message outMessage, Object resultPayload) {
        if (resultPayload != null) {
            PayloadMessage payloadMessage = (PayloadMessage) resultPayload;
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest(payloadMessage.toString());
            }
            outMessage.put(List.class, payloadMessage.getPayload());
            outMessage.put(Element.class, payloadMessage.getHeader());
        }
    }


    @SuppressWarnings("unchecked")
    public Map<Class, Object> getRequestContent(Message inMessage) {
        List<Element> payload = inMessage.get(List.class);
        Element header = inMessage.get(Element.class);

        if (LOG.isLoggable(Level.FINEST)) {
            LOG.finest("Header = " + header + ", Payload = " + payload);
        }

        Map<Class, Object> contents = new IdentityHashMap<Class, Object>();
        contents.put(PayloadMessage.class, new PayloadMessage(payload, header));

        return contents;
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

}
