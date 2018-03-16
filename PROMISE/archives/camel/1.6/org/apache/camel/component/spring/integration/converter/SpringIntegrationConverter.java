package org.apache.camel.component.spring.integration.converter;

import org.apache.camel.Converter;
import org.apache.camel.Endpoint;
import org.apache.camel.component.spring.integration.SpringIntegrationEndpoint;
import org.apache.camel.component.spring.integration.SpringIntegrationMessage;
import org.springframework.integration.core.MessageChannel;
import org.springframework.integration.core.MessageHeaders;
import org.springframework.integration.message.GenericMessage;

/**
 * for turning the Spring Integration types into Camel native type.
 *
 * @version $Revision: 711528 $
 */
@Converter
public final class SpringIntegrationConverter {

    private SpringIntegrationConverter() {
    }

    /**
     * @param Spring Integration MessageChannel
     * @return an Camel Endpoint
     * @throws Exception
     */
    @Converter
    public static Endpoint toEndpoint(final MessageChannel channel) throws Exception {
        if (channel == null) {
            throw new IllegalArgumentException("The MessageChannel is null");
        }
        Endpoint answer = new SpringIntegrationEndpoint("URL", channel, null);
        return answer;
    }


    @SuppressWarnings("unchecked")
    @Converter
    public static org.springframework.integration.core.Message toSpringMessage(final org.apache.camel.Message camelMessage) throws Exception {
        if (camelMessage instanceof SpringIntegrationMessage) {
            SpringIntegrationMessage siMessage = (SpringIntegrationMessage)camelMessage;
            org.springframework.integration.core.Message message =  siMessage.getMessage();
            if (message != null) {
                return message;
            }
        }

        MessageHeaders messageHeaders = new MessageHeaders(camelMessage.getHeaders());
        return new GenericMessage(camelMessage.getBody(), messageHeaders);
    }

    @Converter
    public static org.apache.camel.Message toCamelMessage(final org.springframework.integration.core.Message springMessage) throws Exception {
        return new SpringIntegrationMessage(springMessage);
    }



}
