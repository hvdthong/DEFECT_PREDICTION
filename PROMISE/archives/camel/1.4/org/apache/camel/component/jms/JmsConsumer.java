package org.apache.camel.component.jms;

import javax.jms.MessageListener;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.springframework.jms.listener.AbstractMessageListenerContainer;

/**
 * A {@link Consumer} which uses Spring's {@link AbstractMessageListenerContainer} implementations to consume JMS messages
 *
 * @version $Revision: 649165 $
 */
public class JmsConsumer extends DefaultConsumer<JmsExchange> {
    private final AbstractMessageListenerContainer listenerContainer;
    private EndpointMessageListener messageListener;

    public JmsConsumer(JmsEndpoint endpoint, Processor processor, AbstractMessageListenerContainer listenerContainer) {
        super(endpoint, processor);
        this.listenerContainer = listenerContainer;

        createMessageListener(endpoint, processor);
        this.listenerContainer.setMessageListener(messageListener);
    }

    public AbstractMessageListenerContainer getListenerContainer() {
        return listenerContainer;
    }

    public EndpointMessageListener getEndpointMessageListener() {
        return messageListener;
    }
    
    protected void createMessageListener(JmsEndpoint endpoint, Processor processor) {
        messageListener = new EndpointMessageListener(endpoint, processor);
        messageListener.setBinding(endpoint.getBinding());
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
