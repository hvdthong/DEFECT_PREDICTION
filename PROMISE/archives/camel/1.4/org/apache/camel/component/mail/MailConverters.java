package org.apache.camel.component.mail;

import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;

import org.apache.camel.Converter;

/**
 * JavaMail specific converters.
 *
 * @version $Revision: 652253 $
 */
@Converter
public class MailConverters {

    /**
     * Converts the given JavaMail message to a String body.
     * Can return null.
     */
    @Converter
    public String toString(Message message) throws MessagingException, IOException {
        Object content = message.getContent();
        if (content instanceof MimeMultipart) {
            MimeMultipart multipart = (MimeMultipart) content;
            if (multipart.getCount() > 0) {
                BodyPart part = multipart.getBodyPart(0);
                content = part.getContent();
            }
        }
        if (content != null) {
            return content.toString();
        }
        return null;
    }

    /**
     * Converts the given JavaMail multipart to a String body, where the contenttype of the multipart
     * must be text based (ie start with text). Can return null.
     */
    @Converter
    public static String toString(Multipart multipart) throws MessagingException, IOException {
        int size = multipart.getCount();
        for (int i = 0; i < size; i++) {
            BodyPart part = multipart.getBodyPart(i);
            if (part.getContentType().startsWith("text")) {
                return part.getContent().toString();
            }
        }
        return null;
    }
}
