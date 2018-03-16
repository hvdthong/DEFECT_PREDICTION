package org.apache.tools.ant.taskdefs.optional.clearcase;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

import java.io.File;

/**
 * Task to perform UnCheckout command to ClearCase.
 * <p>
 * The following attributes are interpretted:
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
 * </table>
 *
 * @author Curtis White
 */
public class CCUnCheckout extends ClearCase {
    private boolean m_Keep = false;

    /**
     * Executes the task.
     * <p>
     * Builds a command line to execute cleartool and then calls Exec's run method
     * to execute the command line.
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

        result = run(commandLine);
        if ( result != 0 ) {
            String msg = "Failed executing: " + commandLine.toString();
            throw new BuildException(msg, location);
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
     * Set keepcopy flag status
     *
     * @param keep the status to set the flag to
     */
    public void setKeepCopy(boolean keep) {
        m_Keep = keep;
    }

    /**
     * Get keepcopy flag status
     *
     * @return boolean containing status of keep flag
     */
    public boolean getKeepCopy() {
        return m_Keep;
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

