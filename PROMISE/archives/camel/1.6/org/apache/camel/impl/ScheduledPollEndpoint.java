package org.apache.camel.impl;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.ResolveEndpointFailedException;
import org.apache.camel.util.IntrospectionSupport;

/**
 * A base class for {@link Endpoint} which creates a {@link ScheduledPollConsumer}
 *
 * @version $Revision: 688951 $
 */
public abstract class ScheduledPollEndpoint<E extends Exchange> extends DefaultEndpoint<E> {
    private Map consumerProperties;

    protected ScheduledPollEndpoint(String endpointUri, Component component) {
        super(endpointUri, component);
    }

    protected ScheduledPollEndpoint(String endpointUri, CamelContext context) {
        super(endpointUri, context);
    }

    protected ScheduledPollEndpoint(String endpointUri) {
        super(endpointUri);
    }

    protected ScheduledPollEndpoint() {
    }

    public Map getConsumerProperties() {
        return consumerProperties;
    }

    public void setConsumerProperties(Map consumerProperties) {
        this.consumerProperties = consumerProperties;
    }

    protected void configureConsumer(Consumer<E> consumer) throws Exception {
        if (consumerProperties != null) {
            IntrospectionSupport.setProperties(getCamelContext().getTypeConverter(), consumer, consumerProperties);
            if (!this.isLenientProperties() && consumerProperties.size() > 0) {
                throw new ResolveEndpointFailedException(this.getEndpointUri(), "There are " + consumerProperties.size()
                    + " parameters that couldn't be set on the endpoint consumer."
                    + " Check the uri if the parameters are spelt correctly and that they are properties of the endpoint."
                    + " Unknown consumer parameters=[" + consumerProperties + "]");
            }
        }
    }

    public void configureProperties(Map options) {
        Map consumerProperties = IntrospectionSupport.extractProperties(options, "consumer.");
        if (consumerProperties != null) {
            setConsumerProperties(consumerProperties);
        }
    }

}
