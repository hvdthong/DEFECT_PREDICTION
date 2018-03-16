package org.apache.tools.ant.taskdefs.optional.perforce;

import org.apache.tools.ant.BuildException;


/** Lists Perforce files currently on client.
 *
 * P4Have simply dumps the current file version info into
 * the Ant log (or stdout).
 *
 * @ant.task category="scm"
 */
public class P4Have extends P4Base {

    /**
     * Execute the Perforce <code>have</code> command.
     *
     * @throws BuildException if the command cannot be executed.
     */
    public void execute() throws BuildException {
        execP4Command("have " + P4CmdOpts + " " + P4View, new SimpleP4OutputHandler(this));
    }
}
