package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.Commandline;
import java.io.File;
import java.io.IOException;

/**
 * Task as a layer on top of patch. Patch applies a diff file to an original.
 *
 * @author <a href="mailto:stefan.bodewig@megabit.net">Stefan Bodewig</a>
 */
public class Patch extends Task {

    private File originalFile;
    private boolean havePatchfile = false;
    private Commandline cmd = new Commandline();

    /**
     * The file to patch.
     */
    public void setOriginalfile(File file) {
        originalFile = file;
    }

    /**
     * The file containing the diff output.
     */
    public void setPatchfile(File file) {
        if (!file.exists()) {
            throw new BuildException("patchfile "+file+" doesn\'t exist", 
                                     location);
        }
        cmd.createArgument().setValue("-i");
        cmd.createArgument().setFile(file);
        havePatchfile = true;
    }

    /**
     * Shall patch write backups.
     */
    public void setBackups(boolean backups) {
        if (backups) {
            cmd.createArgument().setValue("-b");
        }
    }

    /**
     * Ignore whitespace differences.
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
     */
    public void setStrip(int num) throws BuildException {
        if (num < 0) {
            throw new BuildException("strip has to be >= 0", location);
        }
        cmd.createArgument().setValue("-p"+num);
    }

    /**
     * Work silently unless an error occurs.
     */
    public void setQuiet(boolean q) {
        if (q) {
            cmd.createArgument().setValue("-s");
        }
    }

    /**
     * Assume patch was created with old and new files swapped.
     */
    public void setReverse(boolean r) {
        if (r) {
            cmd.createArgument().setValue("-R");
        }
    }

    public void execute() throws BuildException {
        if (!havePatchfile) {
            throw new BuildException("patchfile argument is required", 
                                     location);
        } 
        
        Commandline toExecute = (Commandline)cmd.clone();
        toExecute.setExecutable("patch");

        if (originalFile != null) {
            toExecute.createArgument().setFile(originalFile);
        }

        Execute exe = new Execute(new LogStreamHandler(this, Project.MSG_INFO,
                                                       Project.MSG_WARN), 
                                  null);
        exe.setCommandline(toExecute.getCommandline());
        try {
            exe.execute();
        } catch (IOException e) {
            throw new BuildException(e, location);
        }
    }

