package org.apache.camel.component.cxf.invoker;

import java.io.InputStream;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;

public class RawMessageInvokingContext extends AbstractInvokingContext {
    private static final Logger LOG = LogUtils.getL7dLogger(RawMessageInvokingContext.class);

    public RawMessageInvokingContext() {

    }

    public void setRequestOutMessageContent(Message message, Map<Class, Object> contents) {
        Set entries = contents.keySet();
        Iterator iter = entries.iterator();
        while (iter.hasNext()) {
            Object obj = iter.next();
            if (obj instanceof Class) {
                message.setContent((Class<?>)obj, contents.get((Class<?>)obj));
            }
        }
    }

    public Object getResponseObject(Exchange exchange, Map<String, Object> responseContext) {

        return getResponseObject(exchange.getInMessage(), responseContext, InputStream.class);
    }

    public void setResponseContent(Message outMessage, Object resultPayload) {
        LOG.info("Set content: " + resultPayload);
        outMessage.setContent(InputStream.class, resultPayload);
    }

    public Map<Class, Object> getRequestContent(Message inMessage) {

        IdentityHashMap<Class, Object> contents = new IdentityHashMap<Class, Object>();

        Set set = inMessage.getContentFormats();
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            Object obj = iter.next();
            if (obj instanceof Class) {
                contents.put((Class<?>)obj, inMessage.getContent((Class<?>)obj));
            }
        }

        return contents;
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

}
