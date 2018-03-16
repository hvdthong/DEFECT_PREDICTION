package org.apache.camel.component.xmpp;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

/**
 * @version $Revision: 730441 $
 */
public class XmppPrivateChatProducer extends DefaultProducer {
    private static final transient Log LOG = LogFactory.getLog(XmppPrivateChatProducer.class);
    private final XmppEndpoint endpoint;
    private final String participant;


    public XmppPrivateChatProducer(XmppEndpoint endpoint, String participant) {
        super(endpoint);
        this.endpoint = endpoint;
        this.participant = participant;
        if (participant == null) {
            throw new IllegalArgumentException("No participant property specified");
        }
    }

    public void process(Exchange exchange) {
        String threadId = exchange.getExchangeId();

        try {
            ChatManager chatManager = endpoint.getConnection().getChatManager();
            Chat chat = chatManager.getThreadChat(threadId);

            if (chat == null) {
                chat = chatManager.createChat(getParticipant(), threadId, new MessageListener() {
                    public void processMessage(Chat chat, Message message) {
                    }
                });
            }

            Message message = new Message();
            message.setTo(participant);
            message.setThread(threadId);
            message.setType(Message.Type.normal);

            endpoint.getBinding().populateXmppMessage(message, exchange);
            if (LOG.isDebugEnabled()) {
                LOG.debug(">>>> message: " + message.getBody());
            }

            chat.sendMessage(message);
        } catch (XMPPException e) {
            throw new RuntimeXmppException(e);
        }
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
    }



    public String getParticipant() {
        return participant;
    }
}
