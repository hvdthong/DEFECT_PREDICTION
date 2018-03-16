package org.apache.tools.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.Project;

/**
 * Creates a given directory.
 * Creates a directory and any non-existent parent directories, when
 * necessary
 *
 * @since Ant 1.1
 *
 * @ant.task category="filesystem"
 */

public class Mkdir extends Task {

    private static final int MKDIR_RETRY_SLEEP_MILLIS = 10;
    /**
     * our little directory
     */
    private File dir;

    /**
     * create the directory and all parents
     * @throws BuildException if dir is somehow invalid, or creation failed.
     */
    public void execute() throws BuildException {
        if (dir == null) {
            throw new BuildException("dir attribute is required", getLocation());
        }

        if (dir.isFile()) {
            throw new BuildException("Unable to create directory as a file "
                                     + "already exists with that name: "
                                     + dir.getAbsolutePath());
        }

        if (!dir.exists()) {
            boolean result = mkdirs(dir);
            if (!result) {
                String msg = "Directory " + dir.getAbsolutePath()
                    + " creation was not successful for an unknown reason";
                throw new BuildException(msg, getLocation());
            }
            log("Created dir: " + dir.getAbsolutePath());
        } else {
            log("Skipping " + dir.getAbsolutePath()
                + " because it already exists.", Project.MSG_VERBOSE);
        }
    }

    /**
     * the directory to create; required.
     *
     * @param dir the directory to be made.
     */
    public void setDir(File dir) {
        this.dir = dir;
    }
    /**
     * Attempt to fix possible race condition when creating
     * directories on WinXP. If the mkdirs does not work,
     * wait a little and try again.
     */
    private boolean mkdirs(File f) {
        if (!f.mkdirs()) {
            try {
                Thread.sleep(MKDIR_RETRY_SLEEP_MILLIS);
                return f.mkdirs();
            } catch (InterruptedException ex) {
                return f.mkdirs();
            }
        }
        return true;
    }
}

