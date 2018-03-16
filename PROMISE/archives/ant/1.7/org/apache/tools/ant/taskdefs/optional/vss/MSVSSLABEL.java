package org.apache.tools.ant.taskdefs.optional.vss;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;

/**
 * Performs Label commands to Microsoft Visual SourceSafe.
 *
 * @ant.task name="vsslabel" category="scm"
 */
public class MSVSSLABEL extends MSVSS {

    /**
     * Builds a command line to execute ss.
     * @return     The constructed commandline.
     */
    Commandline buildCmdLine() {
        Commandline commandLine = new Commandline();

        if (getVsspath() == null) {
            throw new BuildException("vsspath attribute must be set!", getLocation());
        }

        String label = getLabel();
        if (label.equals("")) {
            String msg = "label attribute must be set!";
            throw new BuildException(msg, getLocation());
        }

        commandLine.setExecutable(getSSCommand());
        commandLine.createArgument().setValue(COMMAND_LABEL);

        commandLine.createArgument().setValue(getVsspath());
        commandLine.createArgument().setValue(getComment());
        commandLine.createArgument().setValue(getAutoresponse());
        commandLine.createArgument().setValue(label);
        commandLine.createArgument().setValue(getVersion());
        commandLine.createArgument().setValue(getLogin());

        return commandLine;
    }

    /**
     * Label to apply in SourceSafe.
     *
     * @param  label The label to apply.
     *
     * @ant.attribute group="required"
     */
    public void setLabel(String label) {
        super.setInternalLabel(label);
    }

    /**
     * Version to label.
     *
     * @param  version The version to label.
     */
    public void setVersion(String version) {
        super.setInternalVersion(version);
    }

    /**
     * Comment to apply to files labeled in SourceSafe.
     *
     * @param comment The comment to apply in SourceSafe
     */
    public void setComment(String comment) {
        super.setInternalComment(comment);
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
