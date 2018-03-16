package org.apache.tools.ant.taskdefs.optional.sos;

import org.apache.tools.ant.types.Commandline;

/**
 * Retrieves and locks files in Visual SourceSafe via a SourceOffSite server.
 *
 * @ant.task name="soscheckout" category="scm"
 */
public class SOSCheckout extends SOS {

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
     * Build the command line <br>
     *
     * CheckOutFile required parameters: -server -name -password -database -project -file<br>
     * CheckOutFile optional parameters: -workdir -verbose -nocache -nocompression -soshome<br>
     *
     * CheckOutProject required parameters: -server -name -password -database -project<br>
     * CheckOutProject optional parameters:-workdir -recursive -verbose -nocache
     * -nocompression -soshome<br>
     *
     * @return    Commandline the generated command to be executed
     */
    protected Commandline buildCmdLine() {
        commandLine = new Commandline();

        if (getFilename() != null) {
            commandLine.createArgument().setValue(SOSCmd.FLAG_COMMAND);
            commandLine.createArgument().setValue(SOSCmd.COMMAND_CHECKOUT_FILE);
            commandLine.createArgument().setValue(SOSCmd.FLAG_FILE);
            commandLine.createArgument().setValue(getFilename());
        } else {
            commandLine.createArgument().setValue(SOSCmd.FLAG_COMMAND);
            commandLine.createArgument().setValue(SOSCmd.COMMAND_CHECKOUT_PROJECT);
            commandLine.createArgument().setValue(getRecursive());
        }

        getRequiredAttributes();
        getOptionalAttributes();

        return commandLine;
    }
}
