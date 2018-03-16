package org.apache.camel.component.jms;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.springframework.jms.listener.AbstractMessageListenerContainer;

import javax.jms.MessageListener;

/**
 * A {@link Consumer} which uses Spring's {@link AbstractMessageListenerContainer} implementations to consume JMS messages
 *
 * @version $Revision: 540547 $
 */
public class JmsConsumer extends DefaultConsumer<JmsExchange> {
    private final AbstractMessageListenerContainer listenerContainer;

    public JmsConsumer(JmsEndpoint endpoint, Processor processor, AbstractMessageListenerContainer listenerContainer) {
        super(endpoint, processor);
        this.listenerContainer = listenerContainer;

        MessageListener messageListener = createMessageListener(endpoint, processor);
        this.listenerContainer.setMessageListener(messageListener);
    }

    public AbstractMessageListenerContainer getListenerContainer() {
        return listenerContainer;
    }

    protected MessageListener createMessageListener(JmsEndpoint endpoint, Processor processor) {
        EndpointMessageListener<JmsExchange> messageListener = new EndpointMessageListener<JmsExchange>(endpoint, processor);
        messageListener.setBinding(endpoint.getBinding());
        return messageListener;
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        listenerContainer.afterPropertiesSet();
        listenerContainer.start();
    }

    @Override
    protected void doStop() throws Exception {
        listenerContainer.stop();
        listenerContainer.destroy();
        super.doStop();
    }
}
