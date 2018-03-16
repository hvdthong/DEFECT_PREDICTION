package org.apache.tools.ant.taskdefs.optional.perforce;

import org.apache.tools.ant.BuildException;

/**
 * Revert Perforce open files or files in a changelist
 *
 *
 * @ant.task category="scm"
 */
public class P4Revert extends P4Base {

    private String revertChange = null;
    private boolean onlyUnchanged = false;

    /**
     * The changelist to revert; optional.
     * @param revertChange : the change list to revert
     * @throws BuildException if the change list is null or empty string
     */
    public void setChange(String revertChange) throws BuildException {
        if (revertChange == null && !revertChange.equals("")) {
            throw new BuildException("P4Revert: change cannot be null or empty");
        }

        this.revertChange = revertChange;

    }

    /**
     * flag to revert only unchanged files (p4 revert -a); optional, default false.
     * @param onlyUnchanged if set to true revert only unchanged files
     */
    public void setRevertOnlyUnchanged(boolean onlyUnchanged) {
        this.onlyUnchanged = onlyUnchanged;
    }

    /**
     * do the work
     * @throws BuildException if an error occurs during the execution of the Perforce command
     * and failonError is set to true
     */
    public void execute() throws BuildException {

        /* Here we can either revert any unchanged files in a changelist
         * or
         * any files regardless of whether they have been changed or not
         *
         *
         * The whole process also accepts a p4 filespec
         */
        String p4cmd = "-s revert";
        if (onlyUnchanged) {
            p4cmd += " -a";
        }

        if (revertChange != null) {
            p4cmd += " -c " + revertChange;
        }

        execP4Command(p4cmd + " " + P4View, new SimpleP4OutputHandler(this));
    }
}
