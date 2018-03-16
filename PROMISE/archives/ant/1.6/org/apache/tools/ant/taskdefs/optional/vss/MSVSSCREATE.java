package org.apache.tools.ant.taskdefs.optional.vss;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;

/**
 * Creates a new project in Microsoft Visual SourceSafe.
 *
 * @ant.task name="vsscreate" category="scm"
 */
public class MSVSSCREATE extends MSVSS {

    /**
     * Builds a command line to execute ss.
     * @return     The constructed commandline.
     */
    Commandline buildCmdLine() {
        Commandline commandLine = new Commandline();

        if (getVsspath() == null) {
            String msg = "vsspath attribute must be set!";
            throw new BuildException(msg, getLocation());
        }

        commandLine.setExecutable(getSSCommand());
        commandLine.createArgument().setValue(COMMAND_CREATE);

        commandLine.createArgument().setValue(getVsspath());
        commandLine.createArgument().setValue(getComment());
        commandLine.createArgument().setValue(getAutoresponse());
        commandLine.createArgument().setValue(getQuiet());
        commandLine.createArgument().setValue(getLogin());

        return commandLine;
    }

    /**
     * Comment to apply to the project created in SourceSafe.
     *
     * @param comment The comment to apply in SourceSafe
     */
    public void setComment(String comment) {
        super.setInternalComment(comment);
    }

    /**
     * Enable quiet mode. Defaults to false.
     *
     * @param   quiet The boolean value for quiet.
     */
    public final void setQuiet (boolean quiet) {
        super.setInternalQuiet(quiet);
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
