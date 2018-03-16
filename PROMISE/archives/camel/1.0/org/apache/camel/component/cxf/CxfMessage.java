package org.apache.camel.component.cxf;

import org.apache.camel.impl.DefaultMessage;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;

import java.util.Map;

/**
 * An Apache CXF {@link Message} which provides access to the underlying CXF features
 *
 * @version $Revision: 524027 $
 */
public class CxfMessage extends DefaultMessage {
    private Message cxfMessage;

    public CxfMessage() {
        this(new MessageImpl());
    }

    public CxfMessage(Message cxfMessage) {
        this.cxfMessage = cxfMessage;
    }

    @Override
    public String toString() {
        if (cxfMessage != null) {
            return "CxfMessage: " + cxfMessage;
        }
        else {
            return "CxfMessage: " + getBody();
        }
    }

    @Override
    public CxfExchange getExchange() {
        return (CxfExchange) super.getExchange();
    }

    /**
     * Returns the underlying CXF message
     *
     * @return the CXF message
     */
    public Message getMessage() {
        return cxfMessage;
    }

    public void setMessage(Message cxfMessage) {
        this.cxfMessage = cxfMessage;
    }

    public Object getHeader(String name) {
        return cxfMessage.get(name);
    }

    @Override
    public void setHeader(String name, Object value) {
        cxfMessage.put(name, value);
    }

    @Override
    public Map<String, Object> getHeaders() {
        return cxfMessage;
    }

    @Override
    public CxfMessage newInstance() {
        return new CxfMessage();
    }

    @Override
    protected Object createBody() {
        return getExchange().getBinding().extractBodyFromCxf(getExchange(), cxfMessage);
    }
}
