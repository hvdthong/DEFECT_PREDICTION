package org.apache.tools.ant.taskdefs.optional.sos;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;

/**
 * A base class for creating tasks for executing commands on SourceOffSite.
 *
 *  These tasks were inspired by the VSS tasks
 *
 */

public abstract class SOS extends Task implements SOSCmd {

    private String sosCmdDir = null;
    private String sosUsername = null;
    private String sosPassword = "";
    private String projectPath = null;
    private String vssServerPath = null;
    private String sosServerPath = null;
    private String sosHome = null;
    private String localPath = null;
    private String version = null;
    private String label = null;
    private String comment = null;
    private String filename = null;

    private boolean noCompress = false;
    private boolean noCache = false;
    private boolean recursive = false;
    private boolean verbose = false;

    /** Commandline to be executed */
    protected Commandline commandLine;

    /**
     * Flag to disable the cache when set.
     * Required if SOSHOME is set as an environment variable.
     * Defaults to false.
     *
     * @param  nocache  True to disable caching.
     */
    public final void setNoCache(boolean nocache) {
        noCache = nocache;
    }

    /**
     * Flag to disable compression when set. Defaults to false.
     *
     * @param  nocompress  True to disable compression.
     */
    public final void setNoCompress(boolean nocompress) {
        noCompress = nocompress;
    }

    /**
     * The directory where soscmd(.exe) is located.
     * soscmd must be on the path if omitted.
     *
     * @param  dir  The new sosCmd value
     */
    public final void setSosCmd(String dir) {
        sosCmdDir = Project.translatePath(dir);
    }

    /**
     * The SourceSafe username.
     *
     * @param  username  The new username value
     *
     * @ant.attribute group="required"
     */
    public final void setUsername(String username) {
        sosUsername = username;
    }

    /**
     * The SourceSafe password.
     *
     * @param  password  The new password value
     */
    public final void setPassword(String password) {
        sosPassword = password;
    }

    /**
     * The SourceSafe project path.
     *
     * @param  projectpath  The new projectpath value
     *
     * @ant.attribute group="required"
     */
    public final void setProjectPath(String projectpath) {
        if (projectpath.startsWith(SOSCmd.PROJECT_PREFIX)) {
            projectPath = projectpath;
        } else {
            projectPath = SOSCmd.PROJECT_PREFIX + projectpath;
        }
    }

    /**
     * The path to the location of the ss.ini file.
     *
     * @param  vssServerPath  The new vssServerPath value
     *
     * @ant.attribute group="required"
     */
    public final void setVssServerPath(String vssServerPath) {
        this.vssServerPath = vssServerPath;
    }

    /**
     * Path to the SourceOffSite home directory.
     *
     * @param  sosHome  The new sosHome value
     */
    public final void setSosHome(String sosHome) {
        this.sosHome = sosHome;
    }

    /**
     * The address and port of SourceOffSite Server,
     * for example 192.168.0.1:8888.
     *
     * @param  sosServerPath  The new sosServerPath value
     *
     * @ant.attribute group="required"
     */
    public final void setSosServerPath(String sosServerPath) {
        this.sosServerPath = sosServerPath;
    }

    /**
     * Override the working directory and get to the specified path.
     *
     * @param  path  The new localPath value
     */
    public final void setLocalPath(Path path) {
        localPath = path.toString();
    }

    /**
     * Enable verbose output. Defaults to false.
     *
     * @param  verbose  True for verbose output.
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }


    protected void setInternalFilename(String file) {
        filename = file;
    }

    protected void setInternalRecursive(boolean recurse) {
        recursive = recurse;
    }

    protected void setInternalComment(String text) {
        comment = text;
    }

    protected void setInternalLabel(String text) {
        label = text;
    }

    protected void setInternalVersion(String text) {
        version = text;
    }

    /**
     * Get the executable to run. Add the path if it was specifed in the build file
     *
     * @return the executable to run
     */
    protected String getSosCommand() {
        if (sosCmdDir == null) {
            return COMMAND_SOS_EXE;
        } else {
            return sosCmdDir + File.separator + COMMAND_SOS_EXE;
        }
    }

    /**
     * Get the comment
     * @return if it was set, null if not
     */
    protected String getComment() {
        return comment;
    }

    /**
     * Get the version
     * @return if it was set, null if not
     */
    protected String getVersion() {
        return version;
    }

    /**
     * Get the label
     * @return if it was set, null if not
     */
    protected String getLabel() {
        return label;
    }

    /**
     * Get the username
     * @return if it was set, null if not
     */
    protected String getUsername() {
        return sosUsername;
    }

    /**
     * Get the password
     * @return empty string if it wans't set
     */
    protected String getPassword() {
        return sosPassword;
    }

    /**
     * Get the project path
     * @return if it was set, null if not
     */
    protected String getProjectPath() {
        return projectPath;
    }

    /**
     * Get the VSS server path
     * @return if it was set, null if not
     */
    protected String getVssServerPath() {
        return vssServerPath;
    }

    /**
     * Get the SOS home directory
     * @return if it was set, null if not
     */
    protected String getSosHome() {
        return sosHome;
    }

    /**
     * Get the SOS serve path
     * @return if it was set, null if not
     */
    protected String getSosServerPath() {
        return sosServerPath;
    }

    /**
     * Get the filename to be acted upon
     * @return if it was set, null if not
     */
    protected String getFilename() {
        return filename;
    }

    /**
     * Get the NoCompress flag
     *
     * @return the 'nocompress' Flag if the attribute was 'true',
     *         otherwise an empty string
     */
    protected String getNoCompress() {
        return noCompress ? FLAG_NO_COMPRESSION : "";
    }

    /**
     * Get the NoCache flag
     *
     * @return the 'nocache' Flag if the attribute was 'true', otherwise an empty string
     */
    protected String getNoCache() {
        return noCache ? FLAG_NO_CACHE : "";
    }

    /**
     * Get the 'verbose' Flag
     *
     * @return the 'verbose' Flag if the attribute was 'true', otherwise an empty string
     */
    protected String getVerbose() {
        return verbose ? FLAG_VERBOSE : "";
    }

    /**
     * Get the 'recursive' Flag
     *
     * @return the 'recursive' Flag if the attribute was 'true', otherwise an empty string
     */
    protected String getRecursive() {
        return recursive ? FLAG_RECURSION : "";
    }

    /**
     * Builds and returns the working directory.
     * <p>
     * The localpath is created if it didn't exist
     *
     * @return the absolute path of the working directory
     */
    protected String getLocalPath() {
        if (localPath == null) {
            return getProject().getBaseDir().getAbsolutePath();
        } else {
            File dir = getProject().resolveFile(localPath);
            if (!dir.exists()) {
                boolean done = dir.mkdirs();
                if (!done) {
                    String msg = "Directory " + localPath + " creation was not "
                        + "successful for an unknown reason";
                    throw new BuildException(msg, getLocation());
                }
                getProject().log("Created dir: " + dir.getAbsolutePath());
            }
            return dir.getAbsolutePath();
        }
    }

    /**
     * Subclasses implement the logic required to construct the command line.
     *
     * @return   The command line to execute.
     */
    abstract Commandline buildCmdLine();


    /**
     * Execute the created command line.
     *
     * @throws BuildException
     */
    public void execute()
        throws BuildException {
        int result = 0;
        buildCmdLine();
        result = run(commandLine);
            String msg = "Failed executing: " + commandLine.toString();
            throw new BuildException(msg, getLocation());
        }
    }

    /**
     * Execute the created command line.
     *
     * @param  cmd              The command line to run.
     * @return                  int the exit code.
     * @throws  BuildException
     */
    protected int run(Commandline cmd) {
        try {
            Execute exe = new Execute(new LogStreamHandler(this,
                    Project.MSG_INFO,
                    Project.MSG_WARN));

            exe.setAntRun(getProject());
            exe.setWorkingDirectory(getProject().getBaseDir());
            exe.setCommandline(cmd.getCommandline());
            return exe.execute();
        } catch (java.io.IOException e) {
            throw new BuildException(e, getLocation());
        }
    }

    /** Sets the executable and add the required attributes to the command line. */
    protected void getRequiredAttributes() {
        commandLine.setExecutable(getSosCommand());
        if (getSosServerPath() == null) {
            throw new BuildException("sosserverpath attribute must be set!", getLocation());
        }
        commandLine.createArgument().setValue(FLAG_SOS_SERVER);
        commandLine.createArgument().setValue(getSosServerPath());
        if (getUsername() == null) {
            throw new BuildException("username attribute must be set!", getLocation());
        }
        commandLine.createArgument().setValue(FLAG_USERNAME);
        commandLine.createArgument().setValue(getUsername());
        commandLine.createArgument().setValue(FLAG_PASSWORD);
        commandLine.createArgument().setValue(getPassword());
        if (getVssServerPath() == null) {
            throw new BuildException("vssserverpath attribute must be set!", getLocation());
        }
        commandLine.createArgument().setValue(FLAG_VSS_SERVER);
        commandLine.createArgument().setValue(getVssServerPath());
        if (getProjectPath() == null) {
            throw new BuildException("projectpath attribute must be set!", getLocation());
        }
        commandLine.createArgument().setValue(FLAG_PROJECT);
        commandLine.createArgument().setValue(getProjectPath());
    }

    /** Adds the optional attributes to the command line. */
    protected void getOptionalAttributes() {
        commandLine.createArgument().setValue(getVerbose());
        commandLine.createArgument().setValue(getNoCompress());
        if (getSosHome() == null) {
            commandLine.createArgument().setValue(getNoCache());
        } else {
            commandLine.createArgument().setValue(FLAG_SOS_HOME);
            commandLine.createArgument().setValue(getSosHome());
        }
        if (getLocalPath() != null) {
            commandLine.createArgument().setValue(FLAG_WORKING_DIR);
            commandLine.createArgument().setValue(getLocalPath());
        }
    }
}