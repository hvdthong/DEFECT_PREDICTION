package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.Project;

import java.io.File;

/**
 * Renames a file.
 *
 * @author haas@softwired.ch
 *
 * @deprecated The rename task is deprecated since Ant 1.2.  Use move instead.
 * @since Ant 1.1
 */
public class Rename extends Task {

    private File src;
    private File dest;
    private boolean replace = true;


    /**
     * Sets the file to be renamed.
     * @param src the file to rename
     */
    public void setSrc(File src) {
        this.src = src;
    }

    /**
     * Sets the new name of the file.
     * @param dest the new name of the file.
     */
    public void setDest(File dest) {
        this.dest = dest;
    }

    /**
     * Sets whether an existing file should be replaced.
     * @param replace <code>on</code>, if an existing file should be replaced.
     */
    public void setReplace(String replace) {
        this.replace = Project.toBoolean(replace);
    }


    /**
     * Renames the file <code>src</code> to <code>dest</code>
     * @exception org.apache.tools.ant.BuildException The exception is
     * thrown, if the rename operation fails.
     */
    public void execute() throws BuildException {
        log("DEPRECATED - The rename task is deprecated.  Use move instead.");

        if (dest == null) {
            throw new BuildException("dest attribute is required", location);
        }

        if (src == null) {
            throw new BuildException("src attribute is required", location);
        }

        if (replace && dest.exists()) {
            if (!dest.delete()) {
                throw new BuildException("Unable to remove existing file " +
                      dest);
            }
        }
        if (!src.renameTo(dest)) {
            throw new BuildException("Unable to rename " + src + " to " +
                  dest);
        }
    }
}
