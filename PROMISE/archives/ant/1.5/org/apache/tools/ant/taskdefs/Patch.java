package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;
import java.io.File;
import java.io.IOException;

/**
 * Patches a file by applying a 'diff' file to it; requires "patch" to be
 * on the execution path.
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 *
 * @since Ant 1.1
 *
 * @ant.task category="utility"
 */
public class Patch extends Task {

    private File originalFile;
    private File directory;
    private boolean havePatchfile = false;
    private Commandline cmd = new Commandline();

    /**
     * The file to patch; optional if it can be inferred from
     * the diff file
     */
    public void setOriginalfile(File file) {
        originalFile = file;
    }

    /**
     * The file containing the diff output; required.
     */
    public void setPatchfile(File file) {
        if (!file.exists()) {
            throw new BuildException("patchfile " + file + " doesn\'t exist", 
                                     location);
        }
        cmd.createArgument().setValue("-i");
        cmd.createArgument().setFile(file);
        havePatchfile = true;
    }

    /**
     * flag to create backups; optional, default=false
     */
    public void setBackups(boolean backups) {
        if (backups) {
            cmd.createArgument().setValue("-b");
        }
    }

    /**
     * flag to ignore whitespace differences; default=false
     */
    public void setIgnorewhitespace(boolean ignore) {
        if (ignore) {
            cmd.createArgument().setValue("-l");
        }
    }

    /**
     * Strip the smallest prefix containing <i>num</i> leading slashes
     * from filenames.
     *
     * <p>patch's <i>-p</i> option.
     * @param num number of lines to strip
     */
    public void setStrip(int num) throws BuildException {
        if (num < 0) {
            throw new BuildException("strip has to be >= 0", location);
        }
        cmd.createArgument().setValue("-p" + num);
    }

    /**
     * Work silently unless an error occurs; optional, default=false
     */
    public void setQuiet(boolean q) {
        if (q) {
            cmd.createArgument().setValue("-s");
        }
    }

    /**
     * Assume patch was created with old and new files swapped; optional,
     * default=false
     */
    public void setReverse(boolean r) {
        if (r) {
            cmd.createArgument().setValue("-R");
        }
    }

    /**
     * The directory to run the patch command in, defaults to the
     * project's base directory.
     *
     * @since Ant 1.5
     */
    public void setDir(File directory) throws BuildException {
        this.directory = directory;
    }

    /**
     * execute patch
     * @throws BuildException when it all goes a bit pear shaped
     */
    public void execute() throws BuildException {
        if (!havePatchfile) {
            throw new BuildException("patchfile argument is required", 
                                     location);
        } 
        Commandline toExecute = (Commandline) cmd.clone();
        toExecute.setExecutable("patch");

        if (originalFile != null) {
            toExecute.createArgument().setFile(originalFile);
        }

        Execute exe = new Execute(new LogStreamHandler(this, Project.MSG_INFO,
                                                       Project.MSG_WARN), 
                                  null);
        exe.setCommandline(toExecute.getCommandline());

        if (directory != null) {
            if (directory.exists() && directory.isDirectory()) {
                exe.setWorkingDirectory(directory);
            } else if (!directory.isDirectory()) {
                throw new BuildException(directory + " is not a directory.",
                                         location);
            } else {
                throw new BuildException("directory " + directory
                                         + " doesn\'t exist", location);
            }
        } else {
            exe.setWorkingDirectory(getProject().getBaseDir());
        }

        log(toExecute.describeCommand(), Project.MSG_VERBOSE);
        try {
            exe.execute();
        } catch (IOException e) {
            throw new BuildException(e, location);
        }
    }

