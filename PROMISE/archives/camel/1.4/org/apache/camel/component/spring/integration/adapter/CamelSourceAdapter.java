package org.apache.camel.component.spring.integration.adapter;

import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.spring.integration.SpringIntegrationBinding;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.ConfigurationException;
import org.springframework.integration.bus.MessageBus;
import org.springframework.integration.bus.MessageBusAware;
import org.springframework.integration.channel.MessageChannel;
import org.springframework.integration.gateway.RequestReplyTemplate;
import org.springframework.integration.message.Message;

/**
 * A CamelContext will be injected into CameSourceAdapter which will
 * let Spring Integration channel talk to the CamelContext certain endpoint
 *
 * @author Willem Jiang
 *
 * @version $Revision: 660882 $
 */
public class CamelSourceAdapter extends AbstractCamelAdapter implements InitializingBean, MessageBusAware {
    protected final Object lifecycleMonitor = new Object();
    private final Log logger = LogFactory.getLog(this.getClass());
    private Consumer consumer;
    private Endpoint camelEndpoint;
    private MessageChannel requestChannel;
    private RequestReplyTemplate requestReplyTemplate = new RequestReplyTemplate();

    private volatile boolean initialized;

    public void setRequestChannel(MessageChannel channel) {
        requestChannel = channel;
        requestReplyTemplate.setRequestChannel(requestChannel);
    }

    public MessageChannel getChannel() {
        return requestChannel;
    }

    public void setReplyChannel(MessageChannel channel) {
        requestReplyTemplate.setReplyChannel(channel);
    }

    public void setRequestTimeout(long requestTimeout) {
        this.requestReplyTemplate.setRequestTimeout(requestTimeout);
    }

    public void setReplyTimeout(long replyTimeout) {
        this.requestReplyTemplate.setReplyTimeout(replyTimeout);
    }

    private void incoming(Exchange exchange) {
        org.springframework.integration.message.Message request =
            SpringIntegrationBinding.createSpringIntegrationMessage(exchange);

        org.springframework.integration.message.Message response = handle(request);
        if (response != null) {
            SpringIntegrationBinding.storeToCamelMessage(response, exchange.getOut());
        }
    }

    protected class ConsumerProcessor implements Processor {
        public void process(Exchange exchange) {
            try {
                incoming(exchange);
            } catch (Throwable ex) {
                ex.printStackTrace();
                logger.warn("Failed to process incoming message : " + ex);
            }
        }

    }

    public final void afterPropertiesSet() throws Exception {
        synchronized (this.lifecycleMonitor) {
            if (this.initialized) {
                return;
            }
        }
        this.initialize();
        this.initialized = true;
    }

    protected void initialize() throws Exception {
        camelEndpoint = getCamelContext().getEndpoint(getCamelEndpointUri());
        consumer = camelEndpoint.createConsumer(new ConsumerProcessor());
        consumer.start();
    }

    public final Message<?> handle(Message<?> message) {
        if (!this.initialized) {
            try {
                this.afterPropertiesSet();
            } catch (Exception e) {
                throw new ConfigurationException("unable to initialize " + this.getClass().getName(), e);
            }
        }
        if (!isExpectReply()) {
            boolean sent = this.requestReplyTemplate.send(message);
            if (!sent && logger.isWarnEnabled()) {
                logger.warn("failed to send message to channel within timeout");
            }
            return null;
        }
        return this.requestReplyTemplate.request(message);
    }

    public void setMessageBus(MessageBus bus) {
        requestReplyTemplate.setMessageBus(bus);
    }



}
