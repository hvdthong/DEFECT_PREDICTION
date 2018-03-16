package org.apache.camel.component.jms;

import javax.jms.Message;

import org.apache.camel.impl.PollingConsumerSupport;

import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.JmsTemplate102;

/**
 * @version $Revision: 1.1 $
 */
public class JmsPollingConsumer extends PollingConsumerSupport<JmsExchange> {
    private JmsOperations template;

    public JmsPollingConsumer(JmsEndpoint endpoint, JmsOperations template) {
        super(endpoint);
        this.template = template;
    }

    @Override
    public JmsEndpoint getEndpoint() {
        return (JmsEndpoint)super.getEndpoint();
    }

    public JmsExchange receiveNoWait() {
        return receive(0);
    }

    public JmsExchange receive() {
        return receive(-1);
    }

    public JmsExchange receive(long timeout) {
        setReceiveTimeout(timeout);
        Message message = template.receive();
        if (message != null) {
            return getEndpoint().createExchange(message);
        }
        return null;
    }

    protected void doStart() throws Exception {
    }

    protected void doStop() throws Exception {
    }

    protected void setReceiveTimeout(long timeout) {
        if (template instanceof JmsTemplate) {
            JmsTemplate jmsTemplate = (JmsTemplate)template;
            jmsTemplate.setReceiveTimeout(timeout);
        } else if (template instanceof JmsTemplate102) {
            JmsTemplate102 jmsTemplate102 = (JmsTemplate102)template;
            jmsTemplate102.setReceiveTimeout(timeout);
        } else {
            throw new IllegalArgumentException("Cannot set the receiveTimeout property on unknown JmsOperations type: " + template);
        }
    }
}
