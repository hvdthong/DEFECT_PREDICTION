package org.apache.tools.ant.taskdefs.email;

import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.DateUtils;

/**
 * Base class for the various emailing implementations.
 *
 * @author roxspring@yahoo.com Rob Oxspring
 * @since Ant 1.5
 */
abstract class Mailer {
    protected String host = null;
    protected int port = -1;
    protected Message message;
    protected EmailAddress from;
    protected Vector toList = null;
    protected Vector ccList = null;
    protected Vector bccList = null;
    protected Vector files = null;
    protected String subject = null;
    protected Task task;
    protected boolean includeFileNames = false;

    /**
     * Sets the mail server
     *
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }


    /**
     * Sets the smtp port
     *
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }


    /**
     * Sets the message
     *
     * @param m
     */
    public void setMessage(Message m) {
        this.message = m;
    }


    /**
     * Sets the address to send from
     *
     * @param from
     */
    public void setFrom(EmailAddress from) {
        this.from = from;
    }


    /**
     * Set the to addresses
     *
     * @param list
     */
    public void setToList(Vector list) {
        this.toList = list;
    }


    /**
     * Sets the cc addresses
     *
     * @param list
     */
    public void setCcList(Vector list) {
        this.ccList = list;
    }


    /**
     * Sets the bcc addresses
     *
     * @param list
     */
    public void setBccList(Vector list) {
        this.bccList = list;
    }


    /**
     * Sets the files to attach
     *
     * @param files
     */
    public void setFiles(Vector files) {
        this.files = files;
    }


    /**
     * Sets the subject
     *
     * @param subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }


    /**
     * Sets the owning task
     *
     * @param task
     */
    public void setTask(Task task) {
        this.task = task;
    }


    /**
     * Indicates whether filenames should be listed in the body
     *
     * @param b
     */
    public void setIncludeFileNames(boolean b) {
        this.includeFileNames = b;
    }


    /**
     * This method should send the email
     *
     * @throws BuildException
     */
    public abstract void send()
         throws BuildException;

    /**
     * Returns the current Date in a format suitable for a SMTP date
     * header.
     *
     * @since Ant 1.5
     */
    protected final String getDate() {
        return DateUtils.getDateForHeader();
    }
}

