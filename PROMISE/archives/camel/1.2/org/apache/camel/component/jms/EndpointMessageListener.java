package org.apache.camel.component.jms;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeCamelException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A JMS {@link MessageListener} which can be used to delegate processing to a
 * Camel endpoint.
 * 
 * @version $Revision: 572899 $
 */
public class EndpointMessageListener<E extends Exchange> implements MessageListener {
    private static final transient Log LOG = LogFactory.getLog(EndpointMessageListener.class);
    private JmsEndpoint endpoint;
    private Processor processor;
    private JmsBinding binding;
    private boolean eagerLoadingOfProperties;

    public EndpointMessageListener(JmsEndpoint endpoint, Processor processor) {
        this.endpoint = endpoint;
        this.processor = processor;
    }

    public void onMessage(Message message) {
        try {

            if (LOG.isDebugEnabled()) {
                LOG.debug(endpoint + " receiving JMS message: " + message);
            }
            JmsExchange exchange = createExchange(message);
            if (eagerLoadingOfProperties) {
                exchange.getIn().getHeaders();
            }
            processor.process(exchange);

        } catch (Exception e) {
            throw new RuntimeCamelException(e);
        }
    }

    public JmsExchange createExchange(Message message) {
        return new JmsExchange(endpoint.getContext(), endpoint.getExchangePattern(), getBinding(), message);
    }

    public JmsBinding getBinding() {
        if (binding == null) {
            binding = new JmsBinding();
        }
        return binding;
    }

    /**
     * Sets the binding used to convert from a Camel message to and from a JMS
     * message
     * 
     * @param binding the binding to use
     */
    public void setBinding(JmsBinding binding) {
        this.binding = binding;
    }

    public boolean isEagerLoadingOfProperties() {
        return eagerLoadingOfProperties;
    }

    public void setEagerLoadingOfProperties(boolean eagerLoadingOfProperties) {
        this.eagerLoadingOfProperties = eagerLoadingOfProperties;
    }
}
