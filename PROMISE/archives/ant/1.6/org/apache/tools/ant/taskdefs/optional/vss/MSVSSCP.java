package org.apache.tools.ant.taskdefs.optional.vss;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;

/**
 * Performs CP (Change Project) commands to Microsoft Visual SourceSafe.
 * <p>This task is typically used before a VssAdd in order to set the target project</p>
 *
 * @ant.task name="vsscp" category="scm"
 */
public class MSVSSCP extends MSVSS {

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
        commandLine.createArgument().setValue(COMMAND_CP);

        commandLine.createArgument().setValue(getVsspath());
        commandLine.createArgument().setValue(getAutoresponse());
        commandLine.createArgument().setValue(getLogin());

        return commandLine;
    }

    /**
     * Autoresponce behaviour. Valid options are Y and N.
     *
     * @param response The auto response value.
     */
    public void setAutoresponse(String response) {
        super.setInternalAutoResponse(response);
    }
}
