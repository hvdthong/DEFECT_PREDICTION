package org.apache.camel.component.mail;

import org.apache.camel.Converter;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;

/**
 * @version $Revision: 1.1 $
 */
@Converter
public class MailConverters {
    /**
     * Converts the given JavaMail message to a String body
     *
     * @param message the message
     * @return the String content
     * @throws MessagingException
     * @throws IOException
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

    @Converter
    public static String toString(Multipart multipart) throws MessagingException, IOException {
        for (int i = 0, size = multipart.getCount(); i < size; i++) {
            BodyPart part = multipart.getBodyPart(i);
            if (part.getContentType().startsWith("text")) {
                return part.getContent().toString();
            }
        }
        return null;
    }
}
