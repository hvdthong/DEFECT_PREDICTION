package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import java.io.File;

/**
 * Creates a given directory.
 * Creates a directory and any non-existent parent directories, when
 * necessary
 *
 * @author duncan@x180.com
 * @since Ant 1.1
 *
 * @ant.task category="filesystem"
 */

public class Mkdir extends Task {

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
            throw new BuildException("dir attribute is required", location);
        }

        if (dir.isFile()) {
            throw new BuildException("Unable to create directory as a file "
                                     + "already exists with that name: " 
                                     + dir.getAbsolutePath());
        }

        if (!dir.exists()) {
            boolean result = dir.mkdirs();
            if (!result) {
                String msg = "Directory " + dir.getAbsolutePath() 
                    + " creation was not successful for an unknown reason";
                throw new BuildException(msg, location);
            }
            log("Created dir: " + dir.getAbsolutePath());
        }
    }

    /**
     * the directory to create; required.
     * @param dir
     */
    public void setDir(File dir) {
        this.dir = dir;
    }
}

