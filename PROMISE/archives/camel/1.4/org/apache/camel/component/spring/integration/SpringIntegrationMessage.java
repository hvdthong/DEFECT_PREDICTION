package org.apache.camel.component.spring.integration;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.impl.DefaultMessage;
import org.springframework.integration.message.MessageHeader;

/**
 * The Message {@link DefaultMessage} implementation
 * for accessing the SpringIntegrationMessage
 *
 * @version $Revision: 652240 $
 */
public class SpringIntegrationMessage extends DefaultMessage {
    private org.springframework.integration.message.Message siMessage;

    public SpringIntegrationMessage(org.springframework.integration.message.Message message) {
        siMessage = message;
    }

    public SpringIntegrationMessage() {

    }

    public void setMessage(org.springframework.integration.message.Message message) {
        siMessage = message;
    }

    public org.springframework.integration.message.Message getMessage() {
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
            return siMessage.getHeader().getAttribute(name);
        } else {
            return super.getHeader(name);
        }
    }

    @Override
    public void setHeader(String name, Object value) {
        if (siMessage != null) {
            siMessage.getHeader().setAttribute(name, value);
        } else {
            super.setHeader(name, value);
        }
    }

    @Override
    public Map<String, Object> getHeaders() {
        if (siMessage != null) {
            Map<String, Object> answer = new HashMap<String, Object>();
            MessageHeader header = siMessage.getHeader();
            for (String name : header.getAttributeNames()) {
                answer.put(name, header.getAttribute(name));
            }
            return answer;
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
