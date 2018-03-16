package org.apache.camel.component.mail;

import javax.mail.internet.MimeMessage;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

/**
 * A Producer to send messages using JavaMail.
 *  
 * @version $Revision: 654680 $
 */
public class MailProducer extends DefaultProducer<MailExchange> {
    private static final transient Log LOG = LogFactory.getLog(MailProducer.class);
    private final MailEndpoint endpoint;
    private final JavaMailSender sender;

    public MailProducer(MailEndpoint endpoint, JavaMailSender sender) {
        super(endpoint);
        this.endpoint = endpoint;
        this.sender = sender;
    }

    public void process(final Exchange exchange) {
        sender.send(new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                endpoint.getBinding().populateMailMessage(endpoint, mimeMessage, exchange);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Sending MimeMessage: " + MailUtils.dumpMessage(mimeMessage));
                }
            }
        });
    }
}
