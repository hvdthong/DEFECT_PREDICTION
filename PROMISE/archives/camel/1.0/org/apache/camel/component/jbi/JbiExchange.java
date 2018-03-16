package org.apache.camel.component.jbi;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;

import javax.jbi.messaging.MessageExchange;
import javax.jbi.messaging.NormalizedMessage;

/**
 * An {@link Exchange} working with JBI which exposes the underlying JBI features such as the
 * JBI {@link #getMessageExchange()}, {@link #getInMessage()} and {@link #getOutMessage()} 
 *
 * @version $Revision: 550760 $
 */
public class JbiExchange extends DefaultExchange {
    private final JbiBinding binding;
    private MessageExchange messageExchange;

    public JbiExchange(CamelContext context, JbiBinding binding) {
        super(context);
        this.binding = binding;
    }

    public JbiExchange(CamelContext context, JbiBinding binding, MessageExchange messageExchange) {
        super(context);
        this.binding = binding;
        this.messageExchange = messageExchange;

        setIn(new JbiMessage(messageExchange.getMessage("in")));
        setOut(new JbiMessage(messageExchange.getMessage("out")));
        setFault(new JbiMessage(messageExchange.getMessage("fault")));
    }

    @Override
    public JbiMessage getIn() {
        return (JbiMessage) super.getIn();
    }

    @Override
    public JbiMessage getOut() {
        return (JbiMessage) super.getOut();
    }

    @Override
    public JbiMessage getOut(boolean lazyCreate) {
        return (JbiMessage) super.getOut(lazyCreate);
    }

    @Override
    public JbiMessage getFault() {
        return (JbiMessage) super.getFault();
    }

    /**
     * @return the Camel <-> JBI binding
     */
    public JbiBinding getBinding() {
        return binding;
    }


    /**
     * Returns the underlying JBI message exchange for an inbound exchange
     * or null for outbound messages
     *
     * @return the inbound message exchange
     */
    public MessageExchange getMessageExchange() {
        return messageExchange;
    }

    /**
     * Returns the underlying In {@link NormalizedMessage}
     *
     * @return the In message
     */
    public NormalizedMessage getInMessage() {
        return getIn().getNormalizedMessage();
    }

    /**
     * Returns the underlying Out {@link NormalizedMessage}
     *
     * @return the Out message
     */
    public NormalizedMessage getOutMessage() {
        return getOut().getNormalizedMessage();
    }

    /**
     * Returns the underlying Fault {@link NormalizedMessage}
     *
     * @return the Fault message
     */
    public NormalizedMessage getFaultMessage() {
        return getFault().getNormalizedMessage();
    }


    @Override
    protected JbiMessage createInMessage() {
        return new JbiMessage();
    }

    @Override
    protected JbiMessage createOutMessage() {
        return new JbiMessage();
    }
}
