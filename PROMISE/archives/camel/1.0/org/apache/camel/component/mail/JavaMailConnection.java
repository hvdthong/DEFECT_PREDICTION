package org.apache.camel.component.mail;

import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.Folder;
import javax.mail.Store;

/**
 * An extension of Spring's {@link JavaMailSenderImpl} to provide helper methods for listening for new mail
 *
 * @version $Revision: 1.1 $
 */
public class JavaMailConnection extends JavaMailSenderImpl {

    public Folder getFolder(String protocol, String folderName) {
        try {
            Store store = getSession().getStore(protocol);
            store.connect(getHost(), getPort(), getUsername(), getPassword());
            return store.getFolder(folderName);
        }
        catch (MessagingException e) {
            throw new MailSendException("Mail server connection failed", e);
        }
    }
}
