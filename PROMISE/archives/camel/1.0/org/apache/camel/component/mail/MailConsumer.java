package org.apache.camel.component.mail;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;

/**
 * A {@link Consumer} which consumes messages from JavaMail using a {@link Transport} and dispatches them
 * to the {@link Processor}
 *
 * @version $Revision: 523430 $
 */
public class MailConsumer extends ScheduledPollConsumer<MailExchange> implements MessageCountListener {
    private static final transient Log log = LogFactory.getLog(MailConsumer.class);
    private final MailEndpoint endpoint;
    private final Folder folder;

    public MailConsumer(MailEndpoint endpoint, Processor processor, Folder folder) {
        super(endpoint, processor);
        this.endpoint = endpoint;
        this.folder = folder;
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        ensureFolderIsOpen();
        folder.addMessageCountListener(this);
    }

    @Override
    protected void doStop() throws Exception {
        folder.removeMessageCountListener(this);
        folder.close(true);
        super.doStop();
    }

    public void messagesAdded(MessageCountEvent event) {
        Message[] messages = event.getMessages();
        for (Message message : messages) {
            try {
                if (!message.getFlags().contains(Flags.Flag.DELETED)) {
                    processMessage(message);

                    flagMessageDeleted(message);
                }
            }
            catch (MessagingException e) {
                handleException(e);
            }
        }
    }

    public void messagesRemoved(MessageCountEvent event) {
        Message[] messages = event.getMessages();
        for (Message message : messages) {
            if (log.isDebugEnabled()) {
                try {
                    log.debug("Removing message: " + message.getSubject());
                }
                catch (MessagingException e) {
                    log.debug("Ignored: " + e);
                }
            }
        }
    }

    protected void poll() throws Exception {
        ensureFolderIsOpen();

        int count = folder.getMessageCount();
        if (count > 0) {
            Message[] messages = folder.getMessages();
            MessageCountEvent event = new MessageCountEvent(folder, MessageCountEvent.ADDED, true, messages);
            messagesAdded(event);
        }
        else if (count == -1) {
            throw new MessagingException("Folder: " + folder.getFullName() + " is closed");
        }

        folder.close(true);
    }

    protected void processMessage(Message message) {
        try {
            MailExchange exchange = endpoint.createExchange(message);
            getProcessor().process(exchange);
        }
        catch (Throwable e) {
            handleException(e);
        }
    }

    protected void ensureFolderIsOpen() throws MessagingException {
        if (!folder.isOpen()) {
            folder.open(Folder.READ_WRITE);
        }
    }

    protected void flagMessageDeleted(Message message) throws MessagingException {
        if (endpoint.getConfiguration().isDeleteProcessedMessages()) {
            message.setFlag(Flags.Flag.DELETED, true);
        }
        else {
            message.setFlag(Flags.Flag.SEEN, true);
        }
    }
}
