package org.apache.camel.component.irc;

public class IrcBinding {
    public Object extractBodyFromIrc(IrcExchange exchange, IrcMessage message) {
        String type = message.getMessageType();
        String text = message.getMessage();
        if (text != null) {
            return text;
        } else {
            return type;
        }
    }
}
