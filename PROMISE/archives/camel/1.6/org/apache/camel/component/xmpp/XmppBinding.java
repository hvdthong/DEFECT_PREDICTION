package org.apache.camel.component.xmpp;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultHeaderFilterStrategy;
import org.apache.camel.spi.HeaderFilterStrategy;
import org.jivesoftware.smack.packet.Message;

/**
 * A Strategy used to convert between a Camel {@link XmppExchange} and {@link XmppMessage} to and from a
 * XMPP {@link Message}
 *
 * @version $Revision: 684214 $
 */
public class XmppBinding {

    private HeaderFilterStrategy headerFilterStrategy;

    public XmppBinding() {
        this.headerFilterStrategy = new DefaultHeaderFilterStrategy();
    }

    public XmppBinding(HeaderFilterStrategy headerFilterStrategy) {
        this.headerFilterStrategy = headerFilterStrategy;
    }

    /**
     * Populates the given XMPP message from the inbound exchange
     */
    public void populateXmppMessage(Message message, Exchange exchange) {
        message.setBody(exchange.getIn().getBody(String.class));

        Set<Map.Entry<String, Object>> entries = exchange.getIn().getHeaders().entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            String name = entry.getKey();
            Object value = entry.getValue();
            if (headerFilterStrategy != null
                    && !headerFilterStrategy.applyFilterToCamelHeaders(name, value)) {
                message.setProperty(name, value);
            }
        }
        String id = exchange.getExchangeId();
        if (id != null) {
            message.setProperty("exchangeId", id);
        }
    }

    /**
     * Extracts the body from the XMPP message
     */
    public Object extractBodyFromXmpp(XmppExchange exchange, Message message) {
        return message.getBody();
    }

    public Map<String, Object> extractHeadersFromXmpp(Message xmppMessage) {
        Map<String, Object> answer = new HashMap<String, Object>();

        for (String name : xmppMessage.getPropertyNames()) {
            Object value = xmppMessage.getProperty(name);

            if (headerFilterStrategy != null
                    && !headerFilterStrategy.applyFilterToExternalHeaders(name, value)) {
                answer.put(name, value);
            }
        }
        return answer;
    }
}
