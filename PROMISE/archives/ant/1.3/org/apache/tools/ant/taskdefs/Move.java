package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;

import java.io.*;
import java.util.*;

/**
 * Moves a file or directory to a new file or directory.  By default,
 * the destination is overwriten when existing.  When overwrite is
 * turned off, then files are only moved if the source file is 
 * newer than the destination file, or when the destination file does
 * not exist.</p>
 *
 * <p>Source files and directories are only deleted when the file or 
 * directory has been copied to the destination successfully.  Filtering
 * also works.</p>
 *
 * <p>This implementation is based on Arnout Kuiper's initial design
 * document, the following mailing list discussions, and the 
 * copyfile/copydir tasks.</p>
 *
 * @author Glenn McAllister <a href="mailto:glennm@ca.ibm.com">glennm@ca.ibm.com</a>
 */
public class Move extends Copy {

    public Move() {
        super();
        forceOverwrite = true;
    }


    protected void doFileOperations() {
            log("Moving " + fileCopyMap.size() + " files to " + 
                destDir.getAbsolutePath() );

            Enumeration e = fileCopyMap.keys();
            while (e.hasMoreElements()) {
                String fromFile = (String) e.nextElement();
                String toFile = (String) fileCopyMap.get(fromFile);

                try {
                    log("Moving " + fromFile + " to " + toFile, verbosity);
                    project.copyFile(fromFile, toFile, filtering, forceOverwrite);
                    File f = new File(fromFile);
                    if (!f.delete()) {
                        throw new BuildException("Unable to delete file " + f.getAbsolutePath());
                    }
                } catch (IOException ioe) {
                    String msg = "Failed to copy " + fromFile + " to " + toFile
                        + " due to " + ioe.getMessage();
                    throw new BuildException(msg, ioe, location);
                }
            }
        }

        if (includeEmpty) {
            Enumeration e = dirCopyMap.elements();
            int count = 0;
            while (e.hasMoreElements()) {
                File d = new File((String)e.nextElement());
                if (!d.exists()) {
                    if (!d.mkdirs()) {
                        log("Unable to create directory " + d.getAbsolutePath(), Project.MSG_ERR);
                    } else {
                        count++;
                    }
                }
            }

            if (count > 0) {
                log("Moved " + count + " empty directories to " + destDir.getAbsolutePath());
            }
        }

        if (filesets.size() > 0) {
            Enumeration e = filesets.elements();
            while (e.hasMoreElements()) {
                FileSet fs = (FileSet)e.nextElement();
                File dir = fs.getDir(project);

                if (okToDelete(dir)) {
                    deleteDir(dir);
                }
            }
        }
    }

    /**
     * Its only ok to delete a directory tree if there are 
     * no files in it.
     */
    protected boolean okToDelete(File d) {
        String[] list = d.list();

        for (int i = 0; i < list.length; i++) {
            String s = list[i];
            File f = new File(d, s);
            if (f.isDirectory()) {
                if (!okToDelete(f)) return false;
            } else {
            }
        }

        return true;
    }

    /**
     * Go and delete the directory tree.
     */
    protected void deleteDir(File d) {
        String[] list = d.list();

        for (int i = 0; i < list.length; i++) {
            String s = list[i];
            File f = new File(d, s);
            if (f.isDirectory()) {
                deleteDir(f);
            } else {
                throw new BuildException("UNEXPECTED ERROR - The file " + f.getAbsolutePath() + " should not exist!");
            }
        }
        log("Deleting directory " + d.getAbsolutePath(), verbosity);
        if (!d.delete()) {
           throw new BuildException("Unable to delete directory " + d.getAbsolutePath());
       }
    }

}
