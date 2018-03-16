package org.apache.camel.component.jms;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultExchange;

import javax.jms.Message;

/**
 * Represents an {@ilnk Exchange} for working with JMS messages while exposing the inbound and outbound JMS {@link Message}
 * objects via {@link #getInMessage()} and {@link #getOutMessage()}
 *
 * @version $Revision:520964 $
 */
public class JmsExchange extends DefaultExchange {
    private JmsBinding binding;

    public JmsExchange(CamelContext context, ExchangePattern pattern, JmsBinding binding) {
        super(context, pattern);
        this.binding = binding;
    }

    public JmsExchange(CamelContext context, ExchangePattern pattern, JmsBinding binding, Message message) {
        this(context, pattern, binding);
        setIn(new JmsMessage(message));
    }

    @Override
    public JmsMessage getIn() {
        return (JmsMessage) super.getIn();
    }

    @Override
    public JmsMessage getOut() {
        return (JmsMessage) super.getOut();
    }

    @Override
    public JmsMessage getOut(boolean lazyCreate) {
        return (JmsMessage) super.getOut(lazyCreate);
    }

    @Override
    public JmsMessage getFault() {
        return (JmsMessage) super.getFault();
    }

    public JmsBinding getBinding() {
        return binding;
    }

    @Override
    public Exchange newInstance() {
        return new JmsExchange(getContext(), getPattern(), binding);
    }


    /**
     * Return the underlying JMS In message
     *
     * @return the JMS In message
     */
    public Message getInMessage() {
        return getIn().getJmsMessage();
    }

    /**
     * Return the underlying JMS Out message
     *
     * @return the JMS out message
     */
    public Message getOutMessage() {
        return getOut().getJmsMessage();
    }

    /**
     * Return the underlying JMS Fault message
     *
     * @return the JMS fault message
     */
    public Message getFaultMessage() {
        return getOut().getJmsMessage();
    }


    @Override
    protected JmsMessage createInMessage() {
        return new JmsMessage();
    }

    @Override
    protected JmsMessage createOutMessage() {
        return new JmsMessage();
    }

    @Override
    protected org.apache.camel.Message createFaultMessage() {
        return new JmsMessage();
    }
}
