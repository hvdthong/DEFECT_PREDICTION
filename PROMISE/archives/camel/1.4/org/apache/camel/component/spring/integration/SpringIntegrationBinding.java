package org.apache.camel.component.spring.integration;

import org.apache.camel.Exchange;
import org.springframework.integration.message.GenericMessage;


/**
 * The helper class for Mapping between the Spring Integration message and
 * the Camel Message
 * @version $Revision: 652240 $
 */
public final class SpringIntegrationBinding {

    private SpringIntegrationBinding() {
    }

    @SuppressWarnings("unchecked")
    public static org.springframework.integration.message.Message createSpringIntegrationMessage(Exchange exchange) {
        org.apache.camel.Message message = exchange.getIn();
        GenericMessage siMessage = new GenericMessage(message.getBody());
        return siMessage;
    }

    @SuppressWarnings("unchecked")
    public static org.springframework.integration.message.Message storeToSpringIntegrationMessage(org.apache.camel.Message message) {
        GenericMessage siMessage = new GenericMessage(message.getBody());
        return siMessage;
    }

    public static void storeToCamelMessage(org.springframework.integration.message.Message siMessage, org.apache.camel.Message cMessage) {
        cMessage.setBody(siMessage.getPayload());
    }

}
