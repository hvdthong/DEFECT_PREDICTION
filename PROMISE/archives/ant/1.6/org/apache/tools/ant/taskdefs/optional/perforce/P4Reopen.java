package org.apache.tools.ant.taskdefs.optional.perforce;

import org.apache.tools.ant.BuildException;

/**
 * Reopen Perforce checkout files between changelists.
 *
 *
 * @ant.task category="scm"
 */
public class P4Reopen extends P4Base {

    private String toChange = "";

    /**
     * The changelist to move files to; required.
     * @param toChange new change list number
     * @throws BuildException if the change parameter is null or empty
     */
    public void setToChange(String toChange) throws BuildException {
        if (toChange == null && !toChange.equals("")) {
            throw new BuildException("P4Reopen: tochange cannot be null or empty");
        }

        this.toChange = toChange;
    }

    /**
     * do the work
     * @throws BuildException if P4View is null
     */
    public void execute() throws BuildException {
        if (P4View == null) {
            throw new BuildException("No view specified to reopen");
        }
        execP4Command("-s reopen -c " + toChange + " " + P4View, new SimpleP4OutputHandler(this));
    }
}
