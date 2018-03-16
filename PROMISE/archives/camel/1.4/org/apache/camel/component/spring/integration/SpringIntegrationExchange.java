package org.apache.camel.component.spring.integration;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultExchange;

/**
 * An {@link Exchange} for working with Spring Integration endpoints which exposes the underlying
 * Spring messages via {@link #getInMessage()} and {@link #getOutMessage()}
 *
 * @version $Revision: 652240 $
 */
public class SpringIntegrationExchange  extends DefaultExchange {

    public SpringIntegrationExchange(CamelContext context) {
        super(context);
    }

    public SpringIntegrationExchange(CamelContext context, ExchangePattern pattern) {
        super(context, pattern);
    }

    @Override
    public Exchange newInstance() {
        return new SpringIntegrationExchange(this.getContext());
    }

    @Override
    public SpringIntegrationMessage getIn() {
        return (SpringIntegrationMessage) super.getIn();
    }

    @Override
    public SpringIntegrationMessage getOut() {
        return (SpringIntegrationMessage) super.getOut();
    }

    @Override
    public SpringIntegrationMessage getOut(boolean lazyCreate) {
        return (SpringIntegrationMessage) super.getOut(lazyCreate);
    }

    @Override
    public SpringIntegrationMessage getFault() {
        return (SpringIntegrationMessage) super.getFault();
    }

    @Override
    protected Message createFaultMessage() {
        return new SpringIntegrationMessage();
    }

    @Override
    protected Message createInMessage() {
        return new SpringIntegrationMessage();
    }

    @Override
    protected Message createOutMessage() {
        return new SpringIntegrationMessage();
    }

}
