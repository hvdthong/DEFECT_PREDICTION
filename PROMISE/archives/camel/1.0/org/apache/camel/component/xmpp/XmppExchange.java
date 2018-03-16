package org.apache.camel.component.xmpp;

import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.jivesoftware.smack.packet.Message;

/**
 * Represents an {@ilnk Exchange} for working with XMPP
 *
 * @version $Revision:520964 $
 */
public class XmppExchange extends DefaultExchange {
    private XmppBinding binding;

    public XmppExchange(CamelContext context, XmppBinding binding) {
        super(context);
        this.binding = binding;
    }

    public XmppExchange(CamelContext context, XmppBinding binding, Message message) {
        this(context, binding);
        setIn(new XmppMessage(message));
    }

    @Override
    public XmppMessage getIn() {
        return (XmppMessage) super.getIn();
    }

    @Override
    public XmppMessage getOut() {
        return (XmppMessage) super.getOut();
    }

    @Override
    public XmppMessage getOut(boolean lazyCreate) {
        return (XmppMessage) super.getOut(lazyCreate);
    }

    @Override
    public XmppMessage getFault() {
        return (XmppMessage) super.getFault();
    }

    public XmppBinding getBinding() {
        return binding;
    }

    @Override
    public Exchange newInstance() {
        return new XmppExchange(getContext(), binding);
    }


    /**
     * Return the underlying XMPP In message
     *
     * @return the XMPP In message
     */
    public Message getInMessage() {
        return getIn().getXmppMessage();
    }

    /**
     * Return the underlying XMPP Out message
     *
     * @return the XMPP out message
     */
    public Message getOutMessage() {
        return getOut().getXmppMessage();
    }

    /**
     * Return the underlying XMPP Fault message
     *
     * @return the XMPP fault message
     */
    public Message getFaultMessage() {
        return getOut().getXmppMessage();
    }


    @Override
    protected XmppMessage createInMessage() {
        return new XmppMessage();
    }

    @Override
    protected XmppMessage createOutMessage() {
        return new XmppMessage();
    }
}
