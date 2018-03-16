package org.apache.camel.component.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.MessageCreator;

/**
 * @version $Revision: 563665 $
 */
public class JmsProducer extends DefaultProducer {
    private static final transient Log LOG = LogFactory.getLog(JmsProducer.class);
    private final JmsEndpoint endpoint;
    private final JmsOperations template;

    public JmsProducer(JmsEndpoint endpoint, JmsOperations template) {
        super(endpoint);
        this.endpoint = endpoint;
        this.template = template;
    }

    public void process(final Exchange exchange) {
        template.send(endpoint.getDestination(), new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                Message message = endpoint.getBinding().makeJmsMessage(exchange, session);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(endpoint + " sending JMS message: " + message);
                }
                return message;
            }
        });
    }

    public JmsOperations getTemplate() {
        return template;
    }
}
