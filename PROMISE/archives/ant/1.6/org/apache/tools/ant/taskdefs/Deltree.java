package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 *
 *
 *
 * @since Ant 1.1
 *
 * @deprecated The deltree task is deprecated since Ant 1.2.  Use
 * delete instead.
 */

public class Deltree extends Task {

    private File dir;

    /**
     * Set the directory to be deleted
     *
     * @param dir the root of the tree to be removed.
     */
    public void setDir(File dir) {
        this.dir = dir;
    }

    /**
     * Do the work.
     *
     * @exception BuildException if the task is not configured correctly or
     * the tree cannot be removed.
     */
    public void execute() throws BuildException {
        log("DEPRECATED - The deltree task is deprecated.  "
            + "Use delete instead.");

        if (dir == null) {
            throw new BuildException("dir attribute must be set!", getLocation());
        }

        if (dir.exists()) {
            if (!dir.isDirectory()) {
                if (!dir.delete()) {
                    throw new BuildException("Unable to delete directory "
                                             + dir.getAbsolutePath(),
                                             getLocation());
                }
                return;
            }

            log("Deleting: " + dir.getAbsolutePath());

            try {
                removeDir(dir);
            } catch (IOException ioe) {
                String msg = "Unable to delete " + dir.getAbsolutePath();
                throw new BuildException(msg, getLocation());
            }
        }
    }

    private void removeDir(File dir) throws IOException {


        String[] list = dir.list();
        for (int i = 0; i < list.length; i++) {
            String s = list[i];
            File f = new File(dir, s);
            if (f.isDirectory()) {
                removeDir(f);
            } else {
                if (!f.delete()) {
                    throw new BuildException("Unable to delete file "
                                             + f.getAbsolutePath());
                }
            }
        }
        if (!dir.delete()) {
            throw new BuildException("Unable to delete directory "
                                     + dir.getAbsolutePath());
        }
    }
}

