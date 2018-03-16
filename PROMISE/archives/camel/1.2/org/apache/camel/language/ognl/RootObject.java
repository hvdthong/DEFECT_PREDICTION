package org.apache.camel.language.ognl;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;

/**
 * @version $Revision: $
 */
public class RootObject {
    private final Exchange exchange;

    public RootObject(Exchange exchange) {
        this.exchange = exchange;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public CamelContext getContext() {
        return exchange.getContext();
    }

    public Throwable getException() {
        return exchange.getException();
    }

    public String getExchangeId() {
        return exchange.getExchangeId();
    }

    public Message getFault() {
        return exchange.getFault();
    }

    public Message getRequest() {
        return exchange.getIn();
    }
    public Message getIn() {
        return exchange.getIn();
    }

    public Message getOut() {
        return exchange.getOut();
    }

    public Message getResponse() {
        return exchange.getOut();
    }

    public Map<String, Object> getProperties() {
        return exchange.getProperties();
    }

    public Object getProperty(String name) {
        return exchange.getProperty(name);
    }

    public <T> T getProperty(String name, Class<T> type) {
        return exchange.getProperty(name, type);
    }
}
