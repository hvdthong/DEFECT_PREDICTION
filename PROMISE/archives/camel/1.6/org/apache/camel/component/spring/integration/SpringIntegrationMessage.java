package org.apache.camel.component.spring.integration;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.impl.DefaultMessage;
import org.springframework.integration.core.MessageHeaders;

/**
 * The Message {@link DefaultMessage} implementation
 * for accessing the SpringIntegrationMessage
 *
 * @version $Revision: 711528 $
 */
public class SpringIntegrationMessage extends DefaultMessage {
    private org.springframework.integration.core.Message siMessage;

    public SpringIntegrationMessage(org.springframework.integration.core.Message message) {
        siMessage = message;
    }

    public SpringIntegrationMessage() {

    }

    public void setMessage(org.springframework.integration.core.Message message) {
        siMessage = message;
    }

    public org.springframework.integration.core.Message getMessage() {
        return siMessage;
    }

    @Override
    public void copyFrom(org.apache.camel.Message that) {
        setMessageId(that.getMessageId());
        setBody(that.getBody());
        getHeaders().putAll(that.getHeaders());
        if (that instanceof SpringIntegrationMessage) {
            SpringIntegrationMessage orig = (SpringIntegrationMessage) that;
            setMessage(orig.getMessage());
        }
    }

    @Override
    public String toString() {
        if (siMessage != null) {
            return "SpringIntegrationMessage: " + siMessage;
        } else {
            return "SpringIntegrationMessage: " + getBody();
        }
    }

    @Override
    public SpringIntegrationExchange getExchange() {
        return (SpringIntegrationExchange)super.getExchange();
    }


    public Object getHeader(String name) {
        if (siMessage != null) {
            return siMessage.getHeaders().get(name);
        } else {
            return super.getHeader(name);
        }
    }

    @Override
    public Map<String, Object> getHeaders() {
        if (siMessage != null) {
            return siMessage.getHeaders();
        } else {
            return super.getHeaders();
        }
    }

    @Override
    public SpringIntegrationMessage newInstance() {
        return new SpringIntegrationMessage();
    }

    @Override
    protected Object createBody() {
        return siMessage.getPayload();
    }

}
