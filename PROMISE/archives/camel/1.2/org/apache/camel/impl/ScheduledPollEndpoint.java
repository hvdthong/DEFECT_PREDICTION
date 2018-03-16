package org.apache.camel.impl;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.CamelContext;
import org.apache.camel.util.IntrospectionSupport;

import java.util.Map;

/**
 * A base class for {@link Endpoint} which creates a {@link ScheduledPollConsumer}
 *
 * @version $Revision: 1.1 $
 */
public abstract class ScheduledPollEndpoint<E extends Exchange> extends DefaultEndpoint<E> {
    private Map consumerProperties;

    protected ScheduledPollEndpoint(String endpointUri, Component component) {
        super(endpointUri, component);
    }

    protected ScheduledPollEndpoint(String endpointUri, CamelContext context) {
        super(endpointUri, context);
    }

    public Map getConsumerProperties() {
        return consumerProperties;
    }

    public void setConsumerProperties(Map consumerProperties) {
        this.consumerProperties = consumerProperties;
    }

    protected void configureConsumer(Consumer<E> consumer) throws Exception {
        if (consumerProperties != null) {
            IntrospectionSupport.setProperties(getContext().getTypeConverter(), consumer, consumerProperties);
        }
    }

    public void configureProperties(Map options) {
        Map consumerProperties = IntrospectionSupport.extractProperties(options, "consumer.");
        if (consumerProperties != null) {
            setConsumerProperties(consumerProperties);
        }
    }

}
