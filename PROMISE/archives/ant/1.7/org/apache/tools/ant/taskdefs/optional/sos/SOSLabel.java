package org.apache.tools.ant.taskdefs.optional.sos;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;

/**
 * Labels Visual SourceSafe files via a SourceOffSite server.
 *
 * @ant.task name="soslabel" category="scm"
 */
public class SOSLabel extends SOS {

    /**
     * The version number to label.
     *
     * @param  version  The new version value
     */
    public void setVersion(String version) {
        super.setInternalVersion(version);
    }

    /**
     * The label to apply the the files in SourceSafe.
     *
     * @param  label  The new label value
     *
     * @ant.attribute group="required"
     */
    public void setLabel(String label) {
        super.setInternalLabel(label);
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
     *  Build the command line <br>
     *  AddLabel required parameters: -server -name -password -database -project -label<br>
     *  AddLabel optional parameters: -verbose -comment<br>
     *
     * @return    Commandline the generated command to be executed
     */
    protected Commandline buildCmdLine() {
        commandLine = new Commandline();

        commandLine.createArgument().setValue(SOSCmd.FLAG_COMMAND);
        commandLine.createArgument().setValue(SOSCmd.COMMAND_LABEL);

        getRequiredAttributes();

        if (getLabel() == null) {
            throw new BuildException("label attribute must be set!", getLocation());
        }
        commandLine.createArgument().setValue(SOSCmd.FLAG_LABEL);
        commandLine.createArgument().setValue(getLabel());

        commandLine.createArgument().setValue(getVerbose());
        if (getComment() != null) {
            commandLine.createArgument().setValue(SOSCmd.FLAG_COMMENT);
            commandLine.createArgument().setValue(getComment());
        }
        return commandLine;
    }
}
