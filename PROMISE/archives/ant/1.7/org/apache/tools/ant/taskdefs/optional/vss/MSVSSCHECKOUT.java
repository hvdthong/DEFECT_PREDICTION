package org.apache.tools.ant.taskdefs.optional.vss;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

/**
 * Performs CheckOut commands to Microsoft Visual SourceSafe.
 *
 * @ant.task name="vsscheckout" category="scm"
 * @ant.attribute.group name="vdl" description="Only one of version, date or label"
 */
public class MSVSSCHECKOUT extends MSVSS {

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
        commandLine.createArgument().setValue(COMMAND_CHECKOUT);

        commandLine.createArgument().setValue(getVsspath());
        commandLine.createArgument().setValue(getLocalpath());
        commandLine.createArgument().setValue(getAutoresponse());
        commandLine.createArgument().setValue(getRecursive());
        commandLine.createArgument().setValue(getVersionDateLabel());
        commandLine.createArgument().setValue(getLogin());
        commandLine.createArgument().setValue(getFileTimeStamp());
        commandLine.createArgument().setValue(getWritableFiles());
        commandLine.createArgument().setValue(getGetLocalCopy());

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
     * Check-out files recursively. Defaults to false.
     *
     * @param recursive  The boolean value for recursive.
     */
    public void setRecursive(boolean recursive) {
        super.setInternalRecursive(recursive);
    }

    /**
     * Version to check-out.
     *
     * @param  version The version to check-out.
     *
     * @ant.attribute group="vdl"
     */
    public void setVersion(String version) {
        super.setInternalVersion(version);
    }

    /**
     * Date to check-out.
     *
     * @param  date The date to check-out.
     *
     * @ant.attribute group="vdl"
     */
    public void setDate(String date) {
        super.setInternalDate(date);
    }

    /**
     * Label to check-out.
     *
     * @param  label The label to check-out.
     *
     * @ant.attribute group="vdl"
     */
    public void setLabel(String label) {
        super.setInternalLabel(label);
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
     * Date and time stamp given to the local copy. Defaults to <code>current</code>.
     *
     * @param timestamp     The file time stamping behaviour.
     */
    public void setFileTimeStamp(CurrentModUpdated timestamp) {
        super.setInternalFileTimeStamp(timestamp);
    }

    /**
     * Action taken when local files are writable. Defaults to <code>fail</code>.
     * <p>
     * Due to ss.exe returning with an exit code of '100' for both errors and when
     * a file has been skipped, <code>failonerror</code> is set to false when using
     * the <code>skip</code> option.
     * </p>
     *
     * @param files     The writable files behaviour
     */
    public void setWritableFiles(WritableFiles files) {
        super.setInternalWritableFiles(files);
    }

    /**
     * Retrieve a local copy during a checkout. Defaults to true.
     *
     * @param get   The get local copy behaviour
     */
    public void setGetLocalCopy(boolean get) {
        super.setInternalGetLocalCopy(get);
    }
}
