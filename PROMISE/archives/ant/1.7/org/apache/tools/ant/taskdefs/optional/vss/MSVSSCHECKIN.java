package org.apache.tools.ant.taskdefs.optional.vss;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

/**
 * Performs CheckIn commands to Microsoft Visual SourceSafe.
 *
 * @ant.task name="vsscheckin" category="scm"
 */
public class MSVSSCHECKIN extends MSVSS {

    /**
     * Builds a command line to execute ss.
     * @return     The constructed commandline.
     */
    protected Commandline buildCmdLine() {
        Commandline commandLine = new Commandline();

        if (getVsspath() == null) {
            String msg = "vsspath attribute must be set!";
            throw new BuildException(msg, getLocation());
        }

        commandLine.setExecutable(getSSCommand());
        commandLine.createArgument().setValue(COMMAND_CHECKIN);

        commandLine.createArgument().setValue(getVsspath());
        commandLine.createArgument().setValue(getLocalpath());
        commandLine.createArgument().setValue(getAutoresponse());
        commandLine.createArgument().setValue(getRecursive());
        commandLine.createArgument().setValue(getWritable());
        commandLine.createArgument().setValue(getLogin());
        commandLine.createArgument().setValue(getComment());

        return commandLine;
    }

    /**
     * Override the project working directory.
     *
     * @param   localPath   The path on disk.
     */
    public void setLocalpath(Path localPath) {
        super.setInternalLocalPath(localPath.toString());
    }

    /**
     * Check-in files recursively. Defaults to false.
     *
     * @param recursive  The boolean value for recursive.
     */
    public void setRecursive(boolean recursive) {
        super.setInternalRecursive(recursive);
    }

    /**
     * Unset the READ-ONLY flag on local copies of files checked-in to VSS.
     * Defaults to false.
     *
     * @param   writable The boolean value for writable.
     */
    public final void setWritable(boolean writable) {
        super.setInternalWritable(writable);
    }

    /**
     * Autoresponce behaviour. Valid options are Y and N.
     *
     * @param response The auto response value.
     */
    public void setAutoresponse(String response) {
        super.setInternalAutoResponse(response);
    }

    /**
     * Comment to apply to files checked-in to SourceSafe.
     *
     * @param comment The comment to apply in SourceSafe
     */
    public void setComment(String comment) {
        super.setInternalComment(comment);
    }
}
