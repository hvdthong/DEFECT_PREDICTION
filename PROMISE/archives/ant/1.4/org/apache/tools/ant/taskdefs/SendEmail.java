 
package org.apache.tools.ant.taskdefs;

import java.io.*;
import java.util.*;
import org.apache.tools.mail.MailMessage;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;

/**
 * A task to send SMTP email.
 * <p>
 * <table border="1" cellpadding="3" cellspacing="0">
 * <tr bgcolor="#CCCCFF">
 * <th>Attribute</th>
 * <th>Description</th>
 * <th>Required</th>
 * </tr>
 * <tr>
 * <td>from</td>
 * <td>Email address of sender.</td>
 * <td>Yes</td>
 * </tr>
 * <tr>
 * <td>mailhost</td>
 * <td>Host name of the mail server.</td>
 * <td>No, default to &quot;localhost&quot;</td>
 * </tr>
 * <tr>
 * <td>toList</td>
 * <td>Comma-separated list of recipients.</td>
 * <td>Yes</td>
 * </tr>
 * <tr>
 * <td>subject</td>
 * <td>Email subject line.</td>
 * <td>No</td>
 * </tr>
 * <tr>
 * <td>files</td>
 * <td>Filename(s) of text to send in the body of the email. Multiple files are
 *     comma-separated.</td>
 * <td rowspan="2">One of these two attributes</td>
 * </tr>
 * <tr>
 * <td>message</td>
 * <td>Message to send inthe body of the email.</td>
 * </tr>
 * </table>
 * <p>
 *
 * @author glenn_twiggs@bmc.com
 */
public class SendEmail extends Task {
    private String from;
    private String mailhost = "localhost";
    private String message;
    private String toList;
    private String subject;
    private Vector files = new Vector();
  
    /** Creates new SendEmail */
    public SendEmail() {
    }

    /**
     * Sets the toList parameter of this build task.
     *
     * @param toList Comma-separated list of email recipient addreses.
     */
    public void setToList(String toList) {
        this.toList = toList;
    }
  
    /**
     * Sets the from parameter of this build task.
     *
     * @param from Email address of sender.
     */
    public void setFrom(String from) {
        this.from = from;
    }
  
    /**
     * Sets the mailhost parameter of this build task.
     *
     * @param mailhost Mail host name.
     */
    public void setMailhost(String mailhost) {
        this.mailhost = mailhost;
    }
  
    /**
     * Sets the message parameter of this build task.
     *
     * @param message Message body of this email.
     */
    public void setMessage(String message) {
        this.message = message;
    }
  
    /**
     * Sets the subject parameter of this build task.
     *
     * @param subject Subject of this email.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Sets the file parameter of this build task.
     *
     * @param filenames Filenames to include as the message body of this email.
     */
    public void setFiles(String filenames) {
        StringTokenizer t = new StringTokenizer(filenames, ", ");
    
        while (t.hasMoreTokens()) {
            files.addElement(project.resolveFile(t.nextToken()));
        }
    }

    /**
     * Executes this build task.
     *
     * throws org.apache.tools.ant.BuildException if there is an error during task
     *        execution.
     */
    public void execute() {
        try {
            MailMessage mailMessage = new MailMessage(mailhost);

            if (from != null) {
                mailMessage.from(from);
            } else {
                throw new BuildException("Attribute \"from\" is required.");
            }

            if (toList != null) {
                StringTokenizer t = new StringTokenizer(toList, ", ", false);

                while (t.hasMoreTokens()) {
                    mailMessage.to(t.nextToken());
                }
            } else {
                throw new BuildException("Attribute \"toList\" is required.");
            }

            if (subject != null) {
                mailMessage.setSubject(subject);
            }

            if (!files.isEmpty()) {
                PrintStream out = mailMessage.getPrintStream();

                for (Enumeration e = files.elements(); e.hasMoreElements(); ) {
                    File file = (File)e.nextElement();

                    if (file.exists() && file.canRead()) {
                        int bufsize = 1024;
                        int length;
                        byte[] buf = new byte[bufsize];

                        BufferedInputStream in = null;
                        try {
                            in = new BufferedInputStream(
                                new FileInputStream(file), bufsize);
    
                            while ((length = in.read(buf, 0, bufsize)) != -1) {
                                out.write(buf, 0, length);
                            }
                        } finally {
                            if (in != null) {
                                in.close();
                            }
                        }

                    } else {
                        throw new BuildException("File \"" + file.getName()
                            + "\" does not exist or is not readable.");
                    }
                }
            } else if (message != null) {
                PrintStream out = mailMessage.getPrintStream();
                out.print(message);
            } else {
                throw new BuildException("Attribute \"file\" or \"message\" is required.");
            }

            log("Sending email");
            mailMessage.sendAndClose();
        } catch (IOException ioe) {
            throw new BuildException("IO error sending mail: " + ioe.getMessage());
        }
    }

}
