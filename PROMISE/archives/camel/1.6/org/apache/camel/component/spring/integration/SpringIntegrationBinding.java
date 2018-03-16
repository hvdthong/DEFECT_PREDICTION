package org.apache.camel.component.spring.integration;

import java.util.Map;

import org.apache.camel.Exchange;
import org.springframework.integration.message.GenericMessage;


/**
 * The helper class for Mapping between the Spring Integration message and
 * the Camel Message
 * @version $Revision: 711528 $
 */
public final class SpringIntegrationBinding {

    private SpringIntegrationBinding() {
    }

    public static org.springframework.integration.core.Message createSpringIntegrationMessage(Exchange exchange) {
        return createSpringIntegrationMessage(exchange, null);
    }

    @SuppressWarnings("unchecked")
    public static org.springframework.integration.core.Message createSpringIntegrationMessage(Exchange exchange, Map<String, Object> headers) {
        org.apache.camel.Message message = exchange.getIn();
        GenericMessage siMessage = new GenericMessage(message.getBody(), headers);
        return siMessage;
    }

    @SuppressWarnings("unchecked")
    public static org.springframework.integration.core.Message storeToSpringIntegrationMessage(org.apache.camel.Message message) {
        GenericMessage siMessage = new GenericMessage(message.getBody());
        return siMessage;
    }

    public static void storeToCamelMessage(org.springframework.integration.core.Message siMessage, org.apache.camel.Message cMessage) {
        cMessage.setBody(siMessage.getPayload());
        cMessage.setHeaders(siMessage.getHeaders());
    }

}
