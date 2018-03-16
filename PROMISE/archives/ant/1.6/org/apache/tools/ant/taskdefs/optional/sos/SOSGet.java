package org.apache.tools.ant.taskdefs.optional.sos;

import org.apache.tools.ant.types.Commandline;

/**
 * Retrieves a read-only copy of the specified project or file
 * from Visual SourceSafe via a SourceOffSite server.
 *
 *
 * @ant.task name="sosget" category="scm"
 */
public class SOSGet extends SOS {

    /**
     * The Filename to act upon.
     * If no file is specified then the tasks
     * act upon the project.
     *
     * @param  filename  The new file value
     */
    public final void setFile(String filename) {
        super.setInternalFilename(filename);
    }

    /**
     * Flag to recursively apply the action. Defaults to false
     *
     * @param  recursive  True for recursive operation.
     */
    public void setRecursive(boolean recursive) {
        super.setInternalRecursive(recursive);
    }

    /**
     * Set the version number to get -
     * only works with SOSGet on a file.
     *
     * @param  version  The new version value
     */
    public void setVersion(String version) {
        super.setInternalVersion(version);
    }

    /**
     * The labeled version to operate on in SourceSafe.
     *
     * @param  label  The new label value
     */
    public void setLabel(String label) {
        super.setInternalLabel(label);
    }

    /**
     * Build the command line <br>
     *
     * GetFile required parameters: -server -name -password -database -project -file<br>
     * GetFile optional parameters: -workdir -revision -verbose -nocache -nocompression -soshome<br>
     *
     * GetProject required parameters: -server -name -password -database -project<br>
     * GetProject optional parameters: -label -workdir -recursive -verbose -nocache
     * -nocompression -soshome<br>
     *
     * @return    Commandline the generated command to be executed
     */
    protected Commandline buildCmdLine() {
        commandLine = new Commandline();

        if (getFilename() != null) {
            commandLine.createArgument().setValue(SOSCmd.FLAG_COMMAND);
            commandLine.createArgument().setValue(SOSCmd.COMMAND_GET_FILE);
            commandLine.createArgument().setValue(SOSCmd.FLAG_FILE);
            commandLine.createArgument().setValue(getFilename());
            if (getVersion() != null) {
                commandLine.createArgument().setValue(SOSCmd.FLAG_VERSION);
                commandLine.createArgument().setValue(getVersion());
            }
        } else {
            commandLine.createArgument().setValue(SOSCmd.FLAG_COMMAND);
            commandLine.createArgument().setValue(SOSCmd.COMMAND_GET_PROJECT);
            commandLine.createArgument().setValue(getRecursive());
            if (getLabel() != null) {
                commandLine.createArgument().setValue(SOSCmd.FLAG_LABEL);
                commandLine.createArgument().setValue(getLabel());
            }
        }

        getRequiredAttributes();
        getOptionalAttributes();

        return commandLine;
    }
}
