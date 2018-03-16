package org.apache.camel.component.spring.integration.converter;

import java.util.Map;

import org.apache.camel.Converter;
import org.apache.camel.Endpoint;
import org.apache.camel.component.spring.integration.SpringIntegrationEndpoint;
import org.apache.camel.component.spring.integration.SpringIntegrationMessage;
import org.springframework.integration.channel.MessageChannel;
import org.springframework.integration.message.DefaultMessageHeader;
import org.springframework.integration.message.GenericMessage;

/**
 * for turning the Spring Integration types into Camel native type.
 *
 * @version $Revision: 677135 $
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
    public static org.springframework.integration.message.Message toSpringMessage(final org.apache.camel.Message camelMessage) throws Exception {
        if (camelMessage instanceof SpringIntegrationMessage) {
            SpringIntegrationMessage siMessage = (SpringIntegrationMessage)camelMessage;
            org.springframework.integration.message.Message message =  siMessage.getMessage();
            if (message != null) {
                return message;
            }
        }

        DefaultMessageHeader messageHeader = new DefaultMessageHeader();
        Map<String, Object> headers = camelMessage.getHeaders();
        for (String key : headers.keySet()) {
            Object value = headers.get(key);
            messageHeader.setAttribute(key, value);
        }
        return new GenericMessage(camelMessage.getBody(), messageHeader);
    }

    @Converter
    public static org.apache.camel.Message toCamelMessage(final org.springframework.integration.message.Message springMessage) throws Exception {
        return new SpringIntegrationMessage(springMessage);
    }



}
