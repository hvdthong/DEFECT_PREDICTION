package org.apache.camel.component.xmpp;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 * @version $Revision: 676826 $
 */
public class XmppGroupChatProducer extends DefaultProducer {
    private static final transient Log LOG = LogFactory.getLog(XmppGroupChatProducer.class);
    private final XmppEndpoint endpoint;
    private final String room;
    private MultiUserChat chat;

    public XmppGroupChatProducer(XmppEndpoint endpoint, String room) {
        super(endpoint);
        this.endpoint = endpoint;
        this.room = room;
        if (room == null) {
            throw new IllegalArgumentException("No room property specified");
        }
    }

    public void process(Exchange exchange) {
        Message message = chat.createMessage();
        message.setTo(room);
        message.setFrom(endpoint.getUser());

        endpoint.getBinding().populateXmppMessage(message, exchange);
        if (LOG.isDebugEnabled()) {
            LOG.debug(">>>> message: " + message.getBody());
        }
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            throw new RuntimeXmppException(e);
        }
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        if (chat == null) {
            chat = new MultiUserChat(endpoint.getConnection(), room);
            String nickname = this.endpoint.getNickname();
            chat.join(nickname != null ? nickname : this.endpoint.getUser());
        }
    }

    @Override
    protected void doStop() throws Exception {
        if (chat != null) {
            chat.leave();
            chat = null;
        }
        super.doStop();
    }

    public MultiUserChat getChat() {
        return chat;
    }

    public void setChat(MultiUserChat chat) {
        this.chat = chat;
    }

    public String getRoom() {
        return room;
    }
}
