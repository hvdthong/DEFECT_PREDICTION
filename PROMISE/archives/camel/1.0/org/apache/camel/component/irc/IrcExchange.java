package org.apache.camel.component.irc;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultExchange;

public class IrcExchange extends DefaultExchange {
    private IrcBinding binding;

    public IrcExchange(CamelContext context, IrcBinding binding) {
        super(context);
        this.binding = binding;
    }

    public IrcExchange(CamelContext context, IrcBinding binding, IrcMessage inMessage) {
        this(context, binding);
        setIn(inMessage);
    }

    public IrcBinding getBinding() {
        return binding;
    }

    public void setBinding(IrcBinding binding) {
        this.binding = binding;
    }

    @Override
    public IrcMessage getIn() {
        return (IrcMessage) super.getIn();
    }

    @Override
    public IrcMessage getOut() {
        return (IrcMessage) super.getOut();
    }

    @Override
    public IrcMessage getOut(boolean lazyCreate) {
        return (IrcMessage) super.getOut(lazyCreate);
    }

    @Override
    public IrcMessage getFault() {
        return (IrcMessage) super.getFault();
    }

    @Override
    public IrcExchange newInstance() {
        return new IrcExchange(getContext(), getBinding());
    }

    @Override
    protected IrcMessage createInMessage() {
        return new IrcMessage();
    }

    @Override
    protected IrcMessage createOutMessage() {
        return new IrcMessage();
    }
}
