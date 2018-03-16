package org.apache.camel.component.spring.integration;

import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.impl.ScheduledPollConsumer;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.util.ObjectHelper;
import org.springframework.integration.channel.ChannelRegistry;
import org.springframework.integration.channel.MessageChannel;
import org.springframework.integration.config.MessageBusParser;

/**
 * A consumer of exchanges for the Spring Integration
 * Please specify the inputChannel in the endpoint url for this consumer.
 * If the message pattern is inOut, the outputChannel property
 * should be set for the outgoing message.
 *
 * @version $Revision: 655516 $
 */
public class SpringIntegrationConsumer  extends ScheduledPollConsumer<SpringIntegrationExchange> {
    private SpringCamelContext context;
    private MessageChannel inputChannel;
    private MessageChannel outputChannel;
    private String inputChannelName;
    private ChannelRegistry channelRegistry;
    private SpringIntegrationEndpoint endpoint;

    public SpringIntegrationConsumer(SpringIntegrationEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
        context = (SpringCamelContext) endpoint.getCamelContext();
        if (context != null && endpoint.getMessageChannel() == null) {
            channelRegistry = (ChannelRegistry) context.getApplicationContext().getBean(MessageBusParser.MESSAGE_BUS_BEAN_NAME);
            inputChannelName = endpoint.getDefaultChannel();
            if (ObjectHelper.isNullOrBlank(inputChannelName)) {
                inputChannelName = endpoint.getInputChannel();
            }
            if (!ObjectHelper.isNullOrBlank(inputChannelName)) {
                inputChannel = (MessageChannel) channelRegistry.lookupChannel(inputChannelName);
                ObjectHelper.notNull(inputChannel, "The inputChannel with the name [" + inputChannelName + "]");
            } else {
                throw new RuntimeCamelException("Can't find the right inputChannelName, , please check your configuration.");
            }
        } else {
            if (endpoint.getMessageChannel() != null) {
                inputChannel = endpoint.getMessageChannel();
            } else {
                throw new RuntimeCamelException("Can't find the right message channel, please check your configuration.");
            }
        }
        if (endpoint.isInOut()) {
            endpoint.setExchangePattern(ExchangePattern.InOut);
        }

    }

    @Override
    protected void poll() throws Exception {
        org.springframework.integration.message.Message siInMessage = inputChannel.receive(this.getDelay());
        SpringIntegrationExchange  exchange = getEndpoint().createExchange();
        exchange.setIn(new SpringIntegrationMessage(siInMessage));
        getProcessor().process(exchange);
        if (endpoint.isInOut()) {
            Object returnAddress = siInMessage.getHeader().getReturnAddress();
            MessageChannel reply = null;

            if (returnAddress != null) {
                if (returnAddress instanceof String) {
                    reply = (MessageChannel)context.getApplicationContext().getBean((String)returnAddress);
                } else if (returnAddress instanceof MessageChannel) {
                    reply = (MessageChannel) returnAddress;
                }
            } else {
                if (outputChannel != null) {
                    reply = outputChannel;
                } else {
                    if (ObjectHelper.isNullOrBlank(endpoint.getOutputChannel())) {
                        outputChannel = (MessageChannel) channelRegistry.lookupChannel(endpoint.getOutputChannel());
                        ObjectHelper.notNull(inputChannel, "The outputChannel with the name [" + endpoint.getOutputChannel() + "]");
                        reply = outputChannel;
                    } else {
                        throw new RuntimeCamelException("Can't find the right outputChannelName");
                    }
                }
            }
            org.springframework.integration.message.Message siOutMessage =
                SpringIntegrationBinding.storeToSpringIntegrationMessage(exchange.getOut());
            reply.send(siOutMessage);
        }


    }


}
