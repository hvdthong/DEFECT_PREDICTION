package org.apache.camel.component.mail;

import java.util.Iterator;
import java.util.Map;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.camel.Exchange;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.converter.ObjectConverter;

/**
 * A Strategy used to convert between a Camel {@link Exchange} and {@link Message} to and
 * from a Mail {@link MimeMessage}
 *
 * @version $Revision: 653697 $
 */
public class MailBinding {

    public void populateMailMessage(MailEndpoint endpoint, MimeMessage mimeMessage, Exchange exchange)
        throws MessagingException {

        appendHeadersFromCamel(mimeMessage, exchange, exchange.getIn());

        Map<Message.RecipientType, String> recipients = endpoint.getConfiguration().getRecipients();
        if (recipients.containsKey(Message.RecipientType.TO)) {
            mimeMessage.setRecipients(Message.RecipientType.TO, recipients.get(Message.RecipientType.TO));
        }
        if (recipients.containsKey(Message.RecipientType.CC)) {
            mimeMessage.setRecipients(Message.RecipientType.CC, recipients.get(Message.RecipientType.CC));
        }
        if (recipients.containsKey(Message.RecipientType.BCC)) {
            mimeMessage.setRecipients(Message.RecipientType.BCC, recipients.get(Message.RecipientType.BCC));
        }

        if (mimeMessage.getAllRecipients() == null) {
            throw new IllegalArgumentException("The mail message does not have any recipients set.");
        }

        if (empty(mimeMessage.getFrom())) {
            String from = endpoint.getConfiguration().getFrom();
            mimeMessage.setFrom(new InternetAddress(from));
        }

        if (exchange.getIn().hasAttachments()) {
            appendAttachmentsFromCamel(mimeMessage, exchange, exchange.getIn());
        } else {
            mimeMessage.setText(exchange.getIn().getBody(String.class));
        }
    }

    /**
     * Extracts the body from the Mail message
     */
    public Object extractBodyFromMail(MailExchange exchange, Message message) {
        try {
            return message.getContent();
        } catch (Exception e) {
            throw new RuntimeCamelException("Failed to extract body due to: " + e.getMessage()
                + ". Exchange: " + exchange + ". Message: " + message, e);
        }
    }

    /**
     * Appends the Mail headers from the Camel {@link MailMessage}
     */
    protected void appendHeadersFromCamel(MimeMessage mimeMessage, Exchange exchange,
                                          org.apache.camel.Message camelMessage)
        throws MessagingException {

        for (Map.Entry<String, Object> entry : camelMessage.getHeaders().entrySet()) {
            String headerName = entry.getKey();
            Object headerValue = entry.getValue();
            if (headerValue != null) {
                if (shouldOutputHeader(camelMessage, headerName, headerValue)) {

                    if (ObjectConverter.isCollection(headerValue)) {
                        Iterator iter = ObjectConverter.iterator(headerValue);
                        while (iter.hasNext()) {
                            Object value = iter.next();
                            mimeMessage.addHeader(headerName, asString(exchange, value));
                        }
                    } else {
                        mimeMessage.setHeader(headerName, asString(exchange, headerValue));
                    }
                }
            }
        }
    }

    /**
     * Appends the Mail attachments from the Camel {@link MailMessage}
     */
    protected void appendAttachmentsFromCamel(MimeMessage mimeMessage, Exchange exchange,
                                              org.apache.camel.Message camelMessage)
        throws MessagingException {

        MimeMultipart multipart = new MimeMultipart();

        multipart.setSubType("mixed");
        MimeBodyPart textBodyPart = new MimeBodyPart();
        textBodyPart.setContent(exchange.getIn().getBody(String.class), "text/plain");
        multipart.addBodyPart(textBodyPart);

        for (Map.Entry<String, DataHandler> entry : camelMessage.getAttachments().entrySet()) {
            String attachmentFilename = entry.getKey();
            DataHandler handler = entry.getValue();
            if (handler != null) {
                if (shouldOutputAttachment(camelMessage, attachmentFilename, handler)) {
                    BodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setDataHandler(handler);
                    messageBodyPart.setFileName(attachmentFilename);
                    messageBodyPart.setDisposition(Part.ATTACHMENT);
                    multipart.addBodyPart(messageBodyPart);
                }
            }
        }

        mimeMessage.setContent(multipart);
    }

    /**
     * Strategy to allow filtering of headers which are put on the Mail message
     */
    protected boolean shouldOutputHeader(org.apache.camel.Message camelMessage, String headerName, Object headerValue) {
        return true;
    }

    /**
     * Strategy to allow filtering of attachments which are put on the Mail message
     */
    protected boolean shouldOutputAttachment(org.apache.camel.Message camelMessage, String attachmentFilename, DataHandler handler) {
        return true;
    }

    private static boolean empty(Address[] addresses) {
        return addresses == null || addresses.length == 0;
    }

    private static String asString(Exchange exchange, Object value) {
        return exchange.getContext().getTypeConverter().convertTo(String.class, value);
    }

}
