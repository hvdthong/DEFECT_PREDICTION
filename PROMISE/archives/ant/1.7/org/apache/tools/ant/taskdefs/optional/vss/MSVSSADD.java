package org.apache.tools.ant.taskdefs.optional.vss;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

/**
 * Performs Add commands to Microsoft Visual SourceSafe.
 *
 * @ant.task name="vssadd" category="scm"
 */
public class MSVSSADD extends MSVSS {

    private String localPath = null;

    /**
     * Builds a command line to execute ss.
     * @return     The constructed commandline.
     */
    protected Commandline buildCmdLine() {
        Commandline commandLine = new Commandline();

        if (getLocalpath() == null) {
            String msg = "localPath attribute must be set!";
            throw new BuildException(msg, getLocation());
        }

        commandLine.setExecutable(getSSCommand());
        commandLine.createArgument().setValue(COMMAND_ADD);

        commandLine.createArgument().setValue(getLocalpath());
        commandLine.createArgument().setValue(getAutoresponse());
        commandLine.createArgument().setValue(getRecursive());
        commandLine.createArgument().setValue(getWritable());
        commandLine.createArgument().setValue(getLogin());
        commandLine.createArgument().setValue(getComment());

        return commandLine;
    }

    /**
     * Returns the local path without the flag.; required
     * @todo See why this returns the local path without the flag.
     * @return The local path value.
     */
    protected String getLocalpath() {
        return localPath;
    }

    /**
     * Add files recursively. Defaults to false.
     *
     * @param recursive  The boolean value for recursive.
     */
    public void setRecursive(boolean recursive) {
        super.setInternalRecursive(recursive);
    }

    /**
     * Unset the READ-ONLY flag on local copies of files added to VSS. Defaults to false.
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
     * Comment to apply to files added to SourceSafe.
     *
     * @param comment The comment to apply in SourceSafe
     */
    public void setComment(String comment) {
        super.setInternalComment(comment);
    }

    /**
     * Override the project working directory.
     *
     * @param   localPath   The path on disk.
     */
    public void setLocalpath(Path localPath) {
        this.localPath = localPath.toString();
    }
}
