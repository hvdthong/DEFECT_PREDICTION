package org.apache.camel.component.xmpp;

import java.util.Map;

import org.apache.camel.impl.DefaultMessage;

import org.jivesoftware.smack.packet.Message;

/**
 * Represents a {@link org.apache.camel.Message} for working with XMPP
 *
 * @version $Revision:520964 $
 */
public class XmppMessage extends DefaultMessage {
    private Message xmppMessage;

    public XmppMessage() {
        this(new Message());
    }

    public XmppMessage(Message jmsMessage) {
        this.xmppMessage = jmsMessage;
    }

    @Override
    public String toString() {
        if (xmppMessage != null) {
            return "XmppMessage: " + xmppMessage;
        } else {
            return "XmppMessage: " + getBody();
        }
    }

    @Override
    public XmppExchange getExchange() {
        return (XmppExchange)super.getExchange();
    }

    /**
     * Returns the underlying XMPP message
     */
    public Message getXmppMessage() {
        return xmppMessage;
    }

    public void setXmppMessage(Message xmppMessage) {
        this.xmppMessage = xmppMessage;
    }
    
    @Override
    public XmppMessage newInstance() {
        return new XmppMessage();
    }

    @Override
    protected Object createBody() {
        if (xmppMessage != null) {
            return getExchange().getBinding().extractBodyFromXmpp(getExchange(), xmppMessage);
        }
        return null;
    }
    
    @Override
    protected void populateInitialHeaders(Map<String, Object> map) {
        if (xmppMessage != null) {
            map.putAll(getExchange().getBinding().extractHeadersFromXmpp(xmppMessage));
        }
    }
}
