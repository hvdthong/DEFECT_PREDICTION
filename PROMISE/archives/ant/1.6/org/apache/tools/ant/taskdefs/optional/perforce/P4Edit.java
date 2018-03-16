package org.apache.tools.ant.taskdefs.optional.perforce;

import org.apache.tools.ant.BuildException;

/**
 * Open file(s) for edit.
 * P4Change should be used to obtain a new changelist for P4Edit as,
 * although P4Edit can open files to the default change,
 * P4Submit cannot yet submit to it.
 * Example Usage:<br>
 *
 *
 * @todo Should call reopen if file is already open in one of our changelists perhaps?
 *
 * @ant.task category="scm"
 */

public class P4Edit extends P4Base {

    /**
     * number of the change list to work on
     */
    public String change = null;

    /**
     * An existing changelist number to assign files to; optional
     * but strongly recommended.
     * @param change the change list number
     */
    public void setChange(String change) {
        this.change = change;
    }

    /**
     * Run the p4 edit command
     * @throws BuildException if there is no view specified
     */
    public void execute() throws BuildException {
        if (change != null) {
            P4CmdOpts = "-c " + change;
        }
        if (P4View == null) {
            throw new BuildException("No view specified to edit");
        }
        execP4Command("-s edit " + P4CmdOpts + " " + P4View, new SimpleP4OutputHandler(this));
    }
}
