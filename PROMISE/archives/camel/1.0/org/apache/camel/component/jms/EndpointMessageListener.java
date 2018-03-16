package org.apache.camel.component.jms;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeCamelException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A JMS {@link MessageListener} which can be used to delegate processing to a Camel endpoint.
 *
 * @version $Revision: 534145 $
 */
public class EndpointMessageListener<E extends Exchange> implements MessageListener {
    private static final transient Log log = LogFactory.getLog(EndpointMessageListener.class);
    private Endpoint<E> endpoint;
    private Processor processor;
    private JmsBinding binding;

    public EndpointMessageListener(Endpoint<E> endpoint, Processor processor) {
        this.endpoint = endpoint;
        this.processor = processor;
    }

    public void onMessage(Message message) {
        try {
			
        	if (log.isDebugEnabled()) {
			    log.debug(endpoint + " receiving JMS message: " + message);
			}
			JmsExchange exchange = createExchange(message);
			processor.process((E) exchange);
			
		} catch (Exception e) {
			throw new RuntimeCamelException(e);
		}
    }

    public JmsExchange createExchange(Message message) {
        return new JmsExchange(endpoint.getContext(), getBinding(), message);
    }

    public JmsBinding getBinding() {
        if (binding == null) {
            binding = new JmsBinding();
        }
        return binding;
    }

    /**
     * Sets the binding used to convert from a Camel message to and from a JMS message
     *
     * @param binding the binding to use
     */
    public void setBinding(JmsBinding binding) {
        this.binding = binding;
    }
}
