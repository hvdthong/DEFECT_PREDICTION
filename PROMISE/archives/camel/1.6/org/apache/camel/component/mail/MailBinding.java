package org.apache.camel.component.mail;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.camel.Exchange;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.converter.ObjectConverter;
import org.apache.camel.impl.DefaultHeaderFilterStrategy;
import org.apache.camel.spi.HeaderFilterStrategy;
import org.apache.camel.util.CollectionHelper;

/**
 * A Strategy used to convert between a Camel {@link Exchange} and {@link Message} to and
 * from a Mail {@link MimeMessage}
 *
 * @version $Revision: 687515 $
 */
public class MailBinding {

    private HeaderFilterStrategy headerFilterStrategy;

    public MailBinding() {
        headerFilterStrategy = new DefaultHeaderFilterStrategy();
    }

    public MailBinding(HeaderFilterStrategy headerFilterStrategy) {
        this.headerFilterStrategy = headerFilterStrategy;
    }

    public void populateMailMessage(MailEndpoint endpoint, MimeMessage mimeMessage, Exchange exchange)
        throws MessagingException, IOException {

        if (hasRecipientHeaders(exchange.getIn())) {
            setRecipientFromCamelMessage(mimeMessage, exchange, exchange.getIn());
        } else {
            setRecipientFromEndpointConfiguration(mimeMessage, endpoint);
        }

        if (mimeMessage.getAllRecipients() == null) {
            throw new IllegalArgumentException("The mail message does not have any recipients set.");
        }

        appendHeadersFromCamelMessage(mimeMessage, exchange, exchange.getIn());

        if (empty(mimeMessage.getFrom())) {
            String from = endpoint.getConfiguration().getFrom();
            mimeMessage.setFrom(new InternetAddress(from));
        }

        if (exchange.getIn().hasAttachments()) {
            appendAttachmentsFromCamel(mimeMessage, exchange.getIn(), endpoint.getConfiguration());
        } else {
            if ("text/html".equals(endpoint.getConfiguration().getContentType())) {
                DataSource ds = new ByteArrayDataSource(exchange.getIn().getBody(String.class), "text/html");
                mimeMessage.setDataHandler(new DataHandler(ds));
            } else {
                mimeMessage.setText(exchange.getIn().getBody(String.class));
            }
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
    protected void appendHeadersFromCamelMessage(MimeMessage mimeMessage, Exchange exchange,
                                                 org.apache.camel.Message camelMessage)
        throws MessagingException {

        for (Map.Entry<String, Object> entry : camelMessage.getHeaders().entrySet()) {
            String headerName = entry.getKey();
            Object headerValue = entry.getValue();
            if (headerValue != null) {
                if (headerFilterStrategy != null
                        && !headerFilterStrategy.applyFilterToCamelHeaders(headerName, headerValue)) {

                    if (isRecipientHeader(headerName)) {
                        continue;
                    }

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

    private void setRecipientFromCamelMessage(MimeMessage mimeMessage, Exchange exchange,
                                                org.apache.camel.Message camelMessage)
        throws MessagingException {

        for (Map.Entry<String, Object> entry : camelMessage.getHeaders().entrySet()) {
            String headerName = entry.getKey();
            Object headerValue = entry.getValue();
            if (headerValue != null && isRecipientHeader(headerName)) {
                if (ObjectConverter.isCollection(headerValue)) {
                    Iterator iter = ObjectConverter.iterator(headerValue);
                    while (iter.hasNext()) {
                        Object recipient = iter.next();
                        appendRecipientToMimeMessage(mimeMessage, headerName, asString(exchange, recipient));
                    }
                } else {
                    appendRecipientToMimeMessage(mimeMessage, headerName, asString(exchange, headerValue));
                }
            }
        }
    }

    /**
     * Appends the Mail headers from the endpoint configuraiton.
     */
    protected void setRecipientFromEndpointConfiguration(MimeMessage mimeMessage, MailEndpoint endpoint)
        throws MessagingException {

        Map<Message.RecipientType, String> recipients = endpoint.getConfiguration().getRecipients();
        if (recipients.containsKey(Message.RecipientType.TO)) {
            appendRecipientToMimeMessage(mimeMessage, Message.RecipientType.TO.toString(), recipients.get(Message.RecipientType.TO));
        }
        if (recipients.containsKey(Message.RecipientType.CC)) {
            appendRecipientToMimeMessage(mimeMessage, Message.RecipientType.CC.toString(), recipients.get(Message.RecipientType.CC));
        }
        if (recipients.containsKey(Message.RecipientType.BCC)) {
            appendRecipientToMimeMessage(mimeMessage, Message.RecipientType.BCC.toString(), recipients.get(Message.RecipientType.BCC));
        }

        String destination = endpoint.getConfiguration().getDestination();
        if (destination != null && mimeMessage.getRecipients(Message.RecipientType.TO) == null) {
            appendRecipientToMimeMessage(mimeMessage, Message.RecipientType.TO.toString(), destination);
        }
    }

    /**
     * Appends the Mail attachments from the Camel {@link MailMessage}
     */
    protected void appendAttachmentsFromCamel(MimeMessage mimeMessage, org.apache.camel.Message camelMessage,
                                              MailConfiguration configuration)
        throws MessagingException {

        MimeMultipart multipart = new MimeMultipart();

        multipart.setSubType("mixed");
        MimeBodyPart textBodyPart = new MimeBodyPart();
        textBodyPart.setContent(camelMessage.getBody(String.class), configuration.getContentType());
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
     * Strategy to allow filtering of attachments which are put on the Mail message
     */
    protected boolean shouldOutputAttachment(org.apache.camel.Message camelMessage, String attachmentFilename, DataHandler handler) {
        return true;
    }

    protected Map<String, Object> extractHeadersFromMail(Message mailMessage) throws MessagingException {
        Map<String, Object> answer = new HashMap<String, Object>();
        Enumeration names = mailMessage.getAllHeaders();

        while (names.hasMoreElements()) {
            Header header = (Header)names.nextElement();
            String[] value = mailMessage.getHeader(header.getName());
            if (headerFilterStrategy != null
                    && !headerFilterStrategy.applyFilterToExternalHeaders(header.getName(), value)) {
                if (value.length == 1) {
                    CollectionHelper.appendValue(answer, header.getName().toLowerCase(), value[0]);
                } else {
                    CollectionHelper.appendValue(answer, header.getName().toLowerCase(), value);
                }
            }
        }

        return answer;
    }

    private static void appendRecipientToMimeMessage(MimeMessage mimeMessage, String type, String recipient)
        throws MessagingException {

        String[] lines = recipient.split("[,|;]");
        for (String line : lines) {
            line = line.trim();
            mimeMessage.addRecipients(asRecipientType(type), line);
        }
    }

    /**
     * Does the given camel message contain any To, CC or BCC header names?
     */
    private static boolean hasRecipientHeaders(org.apache.camel.Message camelMessage) {
        for (String key : camelMessage.getHeaders().keySet()) {
            if (isRecipientHeader(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Is the given key a mime message recipient header (To, CC or BCC)
     */
    private static boolean isRecipientHeader(String key) {
        if (Message.RecipientType.TO.toString().equalsIgnoreCase(key)) {
            return true;
        } else if (Message.RecipientType.CC.toString().equalsIgnoreCase(key)) {
            return true;
        } else if (Message.RecipientType.BCC.toString().equalsIgnoreCase(key)) {
            return true;
        }
        return false;
    }

    /**
     * Returns the RecipientType object.
     */
    private static Message.RecipientType asRecipientType(String type) {
        if (Message.RecipientType.TO.toString().equalsIgnoreCase(type)) {
            return Message.RecipientType.TO;
        } else if (Message.RecipientType.CC.toString().equalsIgnoreCase(type)) {
            return Message.RecipientType.CC;
        } else if (Message.RecipientType.BCC.toString().equalsIgnoreCase(type)) {
            return Message.RecipientType.BCC;
        }
        throw new IllegalArgumentException("Unknown recipient type: " + type);
    }


    private static boolean empty(Address[] addresses) {
        return addresses == null || addresses.length == 0;
    }

    private static String asString(Exchange exchange, Object value) {
        return exchange.getContext().getTypeConverter().convertTo(String.class, exchange, value);
    }

}
