package org.apache.camel.component.event;

import org.apache.camel.Exchange;
import org.springframework.context.ApplicationEvent;

/**
 * Represents a Spring {@link ApplicationEvent} which contains a Camel {@link Exchange}
 *
 * @version $Revision: 630591 $
 */
public class CamelEvent extends ApplicationEvent {
    private final Exchange exchange;

    public CamelEvent(EventEndpoint source, Exchange exchange) {
        super(source);
        this.exchange = exchange;
    }

    @Override
    public EventEndpoint getSource() {
        return (EventEndpoint) super.getSource();
    }

    /**
     * Returns the message exchange
     *
     * @return the camel message exchange
     */
    public Exchange getExchange() {
        return exchange;
    }
}
