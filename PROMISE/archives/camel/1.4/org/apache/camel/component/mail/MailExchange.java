package org.apache.camel.component.mail;

import javax.mail.Message;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultExchange;

/**
 * Represents an {@link Exchange} for working with Mail
 *
 * @version $Revision:520964 $
 */
public class MailExchange extends DefaultExchange {
    private MailBinding binding;

    public MailExchange(CamelContext context, ExchangePattern pattern, MailBinding binding) {
        super(context, pattern);
        this.binding = binding;
    }

    public MailExchange(CamelContext context, ExchangePattern pattern, MailBinding binding, Message message) {
        this(context, pattern, binding);
        setIn(new MailMessage(message));
    }

    public MailExchange(DefaultExchange parent, MailBinding binding) {
        super(parent);
        this.binding = binding;
    }

    @Override
    public MailMessage getIn() {
        return (MailMessage) super.getIn();
    }

    @Override
    public MailMessage getOut() {
        return (MailMessage) super.getOut();
    }

    @Override
    public MailMessage getOut(boolean lazyCreate) {
        return (MailMessage) super.getOut(lazyCreate);
    }

    @Override
    public MailMessage getFault() {
        return (MailMessage) super.getFault();
    }

    public MailBinding getBinding() {
        return binding;
    }

    @Override
    public Exchange newInstance() {
        return new MailExchange(this, binding);
    }



    @Override
    protected MailMessage createInMessage() {
        return new MailMessage();
    }

    @Override
    protected MailMessage createOutMessage() {
        return new MailMessage();
    }
}
