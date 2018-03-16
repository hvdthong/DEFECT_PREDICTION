package org.apache.camel.component.jms;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * @version $Revision: 540547 $
 */
public class JmsProducer extends DefaultProducer {
    private static final transient Log log = LogFactory.getLog(JmsProducer.class);
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
                if (log.isDebugEnabled()) {
                    log.debug(endpoint + " sending JMS message: " + message);
                }
                return message;
            }
        });
    }

    public JmsOperations getTemplate() {
        return template;
    }
}
