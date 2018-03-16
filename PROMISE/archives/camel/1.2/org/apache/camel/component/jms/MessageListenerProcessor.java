package org.apache.camel.component.jms;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeCamelException;

/**
 * Represents a JMS {@link MessageListener} which can be used directly with any
 * JMS template or derived from to create an MDB for processing messages using a
 * {@link Processor}
 * 
 * @version $Revision:520964 $
 */
public class MessageListenerProcessor implements MessageListener {
    private final JmsEndpoint endpoint;
    private final Processor processor;

    public MessageListenerProcessor(JmsEndpoint endpoint, Processor processor) {
        this.endpoint = endpoint;
        this.processor = processor;
    }

    public void onMessage(Message message) {
        try {
            Exchange exchange = endpoint.createExchange(message);
            processor.process(exchange);
        } catch (Exception e) {
            throw new RuntimeCamelException(e);
        }
    }
}
