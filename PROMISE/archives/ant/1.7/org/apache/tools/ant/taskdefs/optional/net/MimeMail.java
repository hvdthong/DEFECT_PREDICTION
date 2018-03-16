package org.apache.tools.ant.taskdefs.optional.net;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.email.EmailTask;

/**
 * A task to send SMTP email; Use <tt>mail</tt> instead
 *
 * @deprecated since 1.6.x.
 *             Use {@link EmailTask} instead.
 *
 * @since Ant1.4
 */
public class MimeMail extends EmailTask {
    /**
     * Executes this build task.
     *
     * @exception BuildException On error.
     */
    public void execute()
        throws BuildException {
        log("DEPRECATED - The " + getTaskName() + " task is deprecated. "
            + "Use the mail task instead.");
        super.execute();
    }
}
