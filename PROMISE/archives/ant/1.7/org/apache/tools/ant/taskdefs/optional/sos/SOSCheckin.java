package org.apache.tools.ant.taskdefs.optional.sos;

import org.apache.tools.ant.types.Commandline;

/**
 * Commits and unlocks files in Visual SourceSafe via a SourceOffSite server.
 *
 * @ant.task name="soscheckin" category="scm"
 */
public class SOSCheckin extends SOS {

    /**
     * The filename to act upon.
     * If no file is specified then the task
     * acts upon the project.
     *
     * @param  filename  The new file value
     */
    public final void setFile(String filename) {
        super.setInternalFilename(filename);
    }

    /**
     * Flag to recursively apply the action. Defaults to false.
     *
     * @param  recursive  True for recursive operation.
     */
    public void setRecursive(boolean recursive) {
        super.setInternalRecursive(recursive);
    }

    /**
     * The comment to apply to all files being labelled.
     *
     * @param  comment  The new comment value
     */
    public void setComment(String comment) {
        super.setInternalComment(comment);
    }

    /**
     * Build the command line. <p>
     *
     * CheckInFile required parameters: -server -name -password -database -project
     *  -file<br>
     * CheckInFile optional parameters: -workdir -log -verbose -nocache -nocompression
     *  -soshome<br>
     * CheckInProject required parameters: -server -name -password -database
     *  -project<br>
     * CheckInProject optional parameters: workdir -recursive -log -verbose
     *  -nocache -nocompression -soshome<br>
     *
     * @return    Commandline the generated command to be executed
     */
    protected Commandline buildCmdLine() {
        commandLine = new Commandline();

        if (getFilename() != null) {
            commandLine.createArgument().setValue(SOSCmd.FLAG_COMMAND);
            commandLine.createArgument().setValue(SOSCmd.COMMAND_CHECKIN_FILE);
            commandLine.createArgument().setValue(SOSCmd.FLAG_FILE);
            commandLine.createArgument().setValue(getFilename());
        } else {
            commandLine.createArgument().setValue(SOSCmd.FLAG_COMMAND);
            commandLine.createArgument().setValue(SOSCmd.COMMAND_CHECKIN_PROJECT);
            commandLine.createArgument().setValue(getRecursive());
        }

        getRequiredAttributes();
        getOptionalAttributes();

        if (getComment() != null) {
            commandLine.createArgument().setValue(SOSCmd.FLAG_COMMENT);
            commandLine.createArgument().setValue(getComment());
        }
        return commandLine;
    }
}
