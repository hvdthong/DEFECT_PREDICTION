package org.apache.tools.ant.taskdefs.optional.perforce;

import org.apache.tools.ant.BuildException;

/** Checkout files for deletion.
 *
 * Example Usage:<br>
 *
 * Simple re-write of P4Edit changing 'edit' to 'delete'.<br>
 *
 * @todo What to do if file is already open in one of our changelists perhaps
 * (See also {@link P4Edit P4Edit})?<br>
 *
 * @ant.task category="scm"
 */
public class P4Delete extends P4Base {

    /**
     * number of the change list to work on
     */
    public String change = null;

    /**
     * An existing changelist number for the deletion; optional
     * but strongly recommended.
     * @param change the number of a change list
     */
    public void setChange(String change) {
        this.change = change;
    }

    /**
     * executes the p4 delete task
     * @throws BuildException if there is no view specified
     */
    public void execute() throws BuildException {
        if (change != null) {
            P4CmdOpts = "-c " + change;
        }
        if (P4View == null) {
            throw new BuildException("No view specified to delete");
        }
        execP4Command("-s delete " + P4CmdOpts + " " + P4View, new SimpleP4OutputHandler(this));
    }
}
