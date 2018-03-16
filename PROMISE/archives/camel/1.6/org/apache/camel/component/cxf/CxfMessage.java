package org.apache.camel.component.cxf;

import org.apache.camel.NoTypeConversionAvailableException;
import org.apache.camel.impl.DefaultMessage;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageContentsList;
import org.apache.cxf.message.MessageImpl;

/**
 * An Apache CXF {@link Message} which provides access to the underlying CXF
 * features
 *
 * @version $Revision: 698831 $
 */
public class CxfMessage extends DefaultMessage {
    private Message cxfMessage;

    public CxfMessage() {
        this(new MessageImpl());
    }

    public CxfMessage(Message cxfMessage) {
        if (cxfMessage == null) {
            this.cxfMessage = new MessageImpl();
        } else {
            this.cxfMessage = cxfMessage;
        }
    }

    @Override
    public void copyFrom(org.apache.camel.Message that) {
        setMessageId(that.getMessageId());
        setBody(that.getBody());
        if (that.getBody() instanceof Message) {
            setMessage((Message)that.getBody());
        }
        getHeaders().putAll(that.getHeaders());
        if (that instanceof CxfMessage) {
            CxfMessage orig = (CxfMessage) that;
            setMessage(orig.getMessage());
        }
    }

    @Override
    public String toString() {
        if (cxfMessage != null) {
            return "CxfMessage: " + cxfMessage;
        } else {
            return "CxfMessage: " + getBody();
        }
    }

    @Override
    public CxfExchange getExchange() {
        return (CxfExchange)super.getExchange();
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

    @Override
    public CxfMessage newInstance() {
        return new CxfMessage();
    }

    @Override
    protected Object createBody() {
        return CxfBinding.extractBodyFromCxf(getExchange(), cxfMessage);
    }

    @Override
    public void setBody(Object body) {
        super.setBody(body);
        if (body instanceof Message) {
            setMessage((Message) body);
        }
    }

    public <T> T getBody(Class<T> type) {
        if (!(MessageContentsList.class.isAssignableFrom(type)) && getBody() instanceof MessageContentsList) {
            MessageContentsList list = (MessageContentsList)getBody();
            for (int i = 0; i < list.size(); i++) {
                Object value = list.get(i);
                try {
                    T answer = getBody(type, value);
                    if (answer != null) {
                        return answer;
                    }
                } catch (NoTypeConversionAvailableException ex) {
                }
            }
        }

        return super.getBody(type);
    }

}
