package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;

/**
 * Copies a file.
 *
 * @author duncan@x180.com
 *
 * @since Ant 1.1
 *
 * @deprecated The copyfile task is deprecated since Ant 1.2.  Use
 * copy instead.
 */

public class Copyfile extends Task {

    private File srcFile;
    private File destFile;
    private boolean filtering = false;
    private boolean forceOverwrite = false;
 
    public void setSrc(File src) {
        srcFile = src;
    }

    public void setForceoverwrite(boolean force) {
        forceOverwrite = force;
    }

    public void setDest(File dest) {
        destFile = dest;
    }

    public void setFiltering(String filter) {
        filtering = Project.toBoolean(filter);
    }

    public void execute() throws BuildException {
        log("DEPRECATED - The copyfile task is deprecated.  Use copy instead.");

        if (srcFile == null) {
            throw new BuildException("The src attribute must be present.", 
                                     location);
        }
        
        if (!srcFile.exists()) {
            throw new BuildException("src " + srcFile.toString()
                                     + " does not exist.", location);
        }

        if (destFile == null) {
            throw new BuildException("The dest attribute must be present.", 
                                     location);
        }

        if (srcFile.equals(destFile)) {
            log("Warning: src == dest", Project.MSG_WARN);
        }

        if (forceOverwrite 
            || srcFile.lastModified() > destFile.lastModified()) {
            try {
                project.copyFile(srcFile, destFile, filtering, forceOverwrite);
            } catch (IOException ioe) {
                String msg = "Error copying file: " + srcFile.getAbsolutePath()
                    + " due to " + ioe.getMessage();
                throw new BuildException(msg);
            }
        }
    }
}
