package org.apache.camel.component.mail;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * An extension of Spring's {@link JavaMailSenderImpl} to provide helper methods
 * for listening for new mail
 * 
 * @version $Revision: 1.1 $
 */
public class JavaMailConnection extends JavaMailSenderImpl {

    public Folder getFolder(String protocol, String folderName) {
        try {
            Store store = getSession().getStore(protocol);
            store.connect(getHost(), getPort(), getUsername(), getPassword());
            return store.getFolder(folderName);
        } catch (MessagingException e) {
            throw new MailSendException("Mail server connection failed", e);
        }
    }
}
