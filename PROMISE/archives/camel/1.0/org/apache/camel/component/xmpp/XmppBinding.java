package org.apache.camel.component.xmpp;

import org.jivesoftware.smack.packet.Message;
import org.apache.camel.Exchange;

import java.util.Map;
import java.util.Set;

/**
 * A Strategy used to convert between a Camel {@XmppExchange} and {@XmppMessage} to and from a
 * XMPP {@link Message}
 *
 * @version $Revision: 534145 $
 */
public class XmppBinding {
    /**
     * Populates the given XMPP message from the inbound exchange
     */
    public void populateXmppMessage(Message message, Exchange exchange) {
        message.setBody(exchange.getIn().getBody(String.class));

        Set<Map.Entry<String, Object>> entries = exchange.getIn().getHeaders().entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            String name = entry.getKey();
            Object value = entry.getValue();
            if (shouldOutputHeader(exchange, name, value)) {
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
     *
     * @param exchange
     * @param message
     */
    public Object extractBodyFromXmpp(XmppExchange exchange, Message message) {
        return message.getBody();
    }

    /**
     * Strategy to allow filtering of headers which are put on the XMPP message
     */
    protected boolean shouldOutputHeader(Exchange exchange, String headerName, Object headerValue) {
        return true;
    }
}
