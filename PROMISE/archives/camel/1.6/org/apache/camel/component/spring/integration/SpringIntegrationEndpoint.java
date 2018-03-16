package org.apache.camel.component.spring.integration;

import org.apache.camel.CamelContext;
import org.apache.camel.Consumer;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.impl.ScheduledPollEndpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.integration.core.MessageChannel;

/**
 *
 * @version $Revision: 711528 $
 */
public class SpringIntegrationEndpoint extends ScheduledPollEndpoint<SpringIntegrationExchange> {
    private static final Log LOG = LogFactory.getLog(SpringIntegrationEndpoint.class);
    private String inputChannel;
    private String outputChannel;
    private String defaultChannel;
    private MessageChannel messageChannel;
    private boolean inOut;

    public SpringIntegrationEndpoint(String uri, String channel, SpringIntegrationComponent component) {
        super(uri, component);
        defaultChannel = channel;
    }

    public SpringIntegrationEndpoint(String uri, MessageChannel channel, CamelContext context) {
        super(uri, context);
        messageChannel = channel;
    }

    public SpringIntegrationEndpoint(String endpointUri, MessageChannel messageChannel) {
        super(endpointUri);
        this.messageChannel = messageChannel;
    }

    public Producer<SpringIntegrationExchange> createProducer() throws Exception {
        return new SpringIntegrationProducer(this);
    }

    public Consumer<SpringIntegrationExchange> createConsumer(Processor processor) throws Exception {
        return new SpringIntegrationConsumer(this, processor);
    }

    public SpringIntegrationExchange createExchange() {
        return createExchange(getExchangePattern());
    }

    public SpringIntegrationExchange createExchange(ExchangePattern pattern) {
        return new SpringIntegrationExchange(getCamelContext(), pattern);
    }

    public void setInputChannel(String input) {
        inputChannel = input;
    }

    public String getInputChannel() {
        return inputChannel;
    }

    public void setOutputChannel(String output) {
        outputChannel = output;
    }

    public String getOutputChannel() {
        return outputChannel;
    }

    public String getDefaultChannel() {
        return defaultChannel;
    }

    public MessageChannel getMessageChannel() {
        return messageChannel;
    }

    public boolean isSingleton() {
        return false;
    }

    public void setInOut(boolean inOut) {
        this.inOut = inOut;
    }

    public boolean isInOut() {
        return this.inOut;
    }

}
