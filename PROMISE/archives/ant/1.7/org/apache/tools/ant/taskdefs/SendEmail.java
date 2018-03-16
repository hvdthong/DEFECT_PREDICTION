package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.taskdefs.email.EmailTask;

/**
 * A task to send SMTP email.
 * This task can send mail using either plain
 * text, UU encoding or Mime format mail depending on what is available.
 * Attachments may be sent using nested FileSet
 * elements.
 *
 * @since Ant 1.2
 *
 * @ant.task name="mail" category="network"
 */
public class SendEmail extends EmailTask {
    /**
     * Sets the mailport parameter of this build task.
     * @param value mail port name.
     *
     * @deprecated since 1.5.x.
     *             Use {@link #setMailport(int)} instead.
     */
    public void setMailport(Integer value) {
        setMailport(value.intValue());
    }
}
