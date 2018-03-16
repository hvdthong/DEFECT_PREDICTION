package org.apache.tools.ant.taskdefs.optional.clearcase;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.types.Commandline;

/**
 * Performs ClearCase UnCheckout command.
 *
 * <p>
 * The following attributes are interpreted:
 * <table border="1">
 *   <tr>
 *     <th>Attribute</th>
 *     <th>Values</th>
 *     <th>Required</th>
 *   </tr>
 *   <tr>
 *      <td>viewpath</td>
 *      <td>Path to the ClearCase view file or directory that the command will operate on</td>
 *      <td>No</td>
 *   <tr>
 *   <tr>
 *      <td>keepcopy</td>
 *      <td>Specifies whether to keep a copy of the file with a .keep extension or not</td>
 *      <td>No</td>
 *   <tr>
 *   <tr>
 *      <td>failonerr</td>
 *      <td>Throw an exception if the command fails. Default is true</td>
 *      <td>No</td>
 *   <tr>
 * </table>
 *
 */
public class CCUnCheckout extends ClearCase {
    private boolean mKeep = false;

    /**
     * Executes the task.
     * <p>
     * Builds a command line to execute cleartool and then calls Exec's run method
     * to execute the command line.
     * @throws BuildException if the command fails and failonerr is set to true
     */
    public void execute() throws BuildException {
        Commandline commandLine = new Commandline();
        Project aProj = getProject();
        int result = 0;

        if (getViewPath() == null) {
            setViewPath(aProj.getBaseDir().getPath());
        }

        commandLine.setExecutable(getClearToolCommand());
        commandLine.createArgument().setValue(COMMAND_UNCHECKOUT);

        checkOptions(commandLine);

        if (!getFailOnErr()) {
            getProject().log("Ignoring any errors that occur for: "
                    + getViewPathBasename(), Project.MSG_VERBOSE);
        }
        result = run(commandLine);
        if (Execute.isFailure(result) && getFailOnErr()) {
            String msg = "Failed executing: " + commandLine.toString();
            throw new BuildException(msg, getLocation());
        }
    }


    /**
     * Check the command line options.
     */
    private void checkOptions(Commandline cmd) {
        if (getKeepCopy()) {
            cmd.createArgument().setValue(FLAG_KEEPCOPY);
        } else {
            cmd.createArgument().setValue(FLAG_RM);
        }

        cmd.createArgument().setValue(getViewPath());
    }

    /**
     * If true, keep a copy of the file with a .keep extension.
     *
     * @param keep the status to set the flag to
     */
    public void setKeepCopy(boolean keep) {
        mKeep = keep;
    }

    /**
     * Get keepcopy flag status
     *
     * @return boolean containing status of keep flag
     */
    public boolean getKeepCopy() {
        return mKeep;
    }


    /**
     *  -keep flag -- keep a copy of the file with .keep extension
     */
    public static final String FLAG_KEEPCOPY = "-keep";
    /**
     *  -rm flag -- remove the copy of the file
     */
    public static final String FLAG_RM = "-rm";

}

