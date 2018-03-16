package org.apache.camel.component.mail;

import javax.mail.Folder;
import javax.mail.Message;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.ScheduledPollEndpoint;

import org.springframework.mail.javamail.JavaMailSender;

/**
 * @version $Revision:520964 $
 */
public class MailEndpoint extends ScheduledPollEndpoint<MailExchange> {
    private MailBinding binding;
    private MailConfiguration configuration;

    public MailEndpoint(String uri, MailComponent component, MailConfiguration configuration) {
        super(uri, component);
        this.configuration = configuration;
    }

    public Producer<MailExchange> createProducer() throws Exception {
        JavaMailSender sender = configuration.createJavaMailConnection(this);
        return createProducer(sender);
    }

    /**
     * Creates a producer using the given sender
     */
    public Producer<MailExchange> createProducer(JavaMailSender sender) throws Exception {
        return new MailProducer(this, sender);
    }

    public Consumer<MailExchange> createConsumer(Processor processor) throws Exception {
        JavaMailConnection connection = configuration.createJavaMailConnection(this);
        String protocol = getConfiguration().getProtocol();
        if (protocol.equals("smtp")) {
            protocol = "pop3";
        }
        String folderName = getConfiguration().getFolderName();
        Folder folder = connection.getFolder(protocol, folderName);
        if (folder == null) {
            throw new IllegalArgumentException("No folder for protocol: " + protocol + " and name: " + folderName);
        }
        return createConsumer(processor, folder);
    }

    /**
     * Creates a consumer using the given processor and transport
     * 
     * @param processor the processor to use to process the messages
     * @param folder the JavaMail Folder to use for inbound messages
     * @return a newly created consumer
     * @throws Exception if the consumer cannot be created
     */
    public Consumer<MailExchange> createConsumer(Processor processor, Folder folder) throws Exception {
        MailConsumer answer = new MailConsumer(this, processor, folder);
        configureConsumer(answer);
        return answer;
    }

    @Override
    public MailExchange createExchange(ExchangePattern pattern) {
        return new MailExchange(getContext(), pattern, getBinding());
    }

    public MailExchange createExchange(Message message) {
        return new MailExchange(getContext(), getExchangePattern(), getBinding(), message);
    }

    public MailBinding getBinding() {
        if (binding == null) {
            binding = new MailBinding();
        }
        return binding;
    }

    /**
     * Sets the binding used to convert from a Camel message to and from a Mail
     * message
     * 
     * @param binding the binding to use
     */
    public void setBinding(MailBinding binding) {
        this.binding = binding;
    }

    public boolean isSingleton() {
        return false;
    }

    public MailConfiguration getConfiguration() {
        return configuration;
    }
}
