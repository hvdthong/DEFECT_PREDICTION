package org.apache.tools.ant.taskdefs.email;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.tools.ant.BuildException;

/**
 * Uses the JavaMail classes to send Mime format email.
 *
 * @author roxspring@yahoo.com Rob Oxspring
 * @since Ant 1.5
 */
class MimeMailer extends Mailer {
    /** Sends the email  */
    public void send() {
        try {
            Properties props = new Properties();

            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", String.valueOf(port));

            Session sesh = Session.getDefaultInstance(props, null);

            MimeMessage msg = new MimeMessage(sesh);
            MimeMultipart attachments = new MimeMultipart();

            if (from.getName() == null) {
                msg.setFrom(new InternetAddress(from.getAddress()));
            } else {
                msg.setFrom(new InternetAddress(from.getAddress(),
                    from.getName()));
            }

            msg.setRecipients(Message.RecipientType.TO,
                internetAddresses(toList));
            msg.setRecipients(Message.RecipientType.CC,
                internetAddresses(ccList));
            msg.setRecipients(Message.RecipientType.BCC,
                internetAddresses(bccList));

            if (subject != null) {
                msg.setSubject(subject);
            }
            msg.addHeader("Date", getDate());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream out = new PrintStream(baos);

            message.print(out);
            out.close();

            MimeBodyPart textbody = new MimeBodyPart();

            textbody.setContent(baos.toString(), message.getMimeType());
            attachments.addBodyPart(textbody);

            Enumeration e = files.elements();

            while (e.hasMoreElements()) {
                File file = (File) e.nextElement();

                MimeBodyPart body;

                body = new MimeBodyPart();
                if (!file.exists() || !file.canRead()) {
                    throw new BuildException("File \"" + file.getAbsolutePath()
                         + "\" does not exist or is not "
                         + "readable.");
                }
                FileDataSource fileData = new FileDataSource(file);
                DataHandler fileDataHandler = new DataHandler(fileData);

                body.setDataHandler(fileDataHandler);
                body.setFileName(file.getName());
                attachments.addBodyPart(body);
            }

            msg.setContent(attachments);
            Transport.send(msg);
        } catch (MessagingException e) {
            throw new BuildException("Problem while sending mime mail:", e);
        } catch (IOException e) {
            throw new BuildException("Problem while sending mime mail:", e);
        }
    }


    private static InternetAddress[] internetAddresses(Vector list)
         throws AddressException, UnsupportedEncodingException {
        InternetAddress[] addrs = new InternetAddress[list.size()];

        for (int i = 0; i < list.size(); ++i) {
            EmailAddress addr = (EmailAddress) list.elementAt(i);

            if (addr.getName() == null) {
                addrs[i] = new InternetAddress(addr.getAddress());
            } else {
                addrs[i] = new InternetAddress(addr.getAddress(),
                    addr.getName());
            }
        }

        return addrs;
    }
}

