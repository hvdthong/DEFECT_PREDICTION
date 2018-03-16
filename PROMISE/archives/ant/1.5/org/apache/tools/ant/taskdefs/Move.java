package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FilterSetCollection;
import org.apache.tools.ant.types.FilterSet;
import org.apache.tools.ant.types.FileSet;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Moves a file or directory to a new file or directory.
 * By default, the
 * destination file is overwritten if it already exists.
 * When <i>overwrite</i> is
 * turned off, then files are only moved if the source file is
 * newer than the destination file, or when the destination file does
 * not exist.
 *
 * <p>Source files and directories are only deleted when the file or
 * directory has been copied to the destination successfully.  Filtering
 * also works.</p>
 *
 * <p>This implementation is based on Arnout Kuiper's initial design
 * document, the following mailing list discussions, and the
 * copyfile/copydir tasks.</p>
 *
 * @author Glenn McAllister 
 *         <a href="mailto:glennm@ca.ibm.com">glennm@ca.ibm.com</a>
 * @author Magesh Umasankar
 * @version $Revision: 274041 $
 *
 * @since Ant 1.2
 *
 * @ant.task category="filesystem"
 */
public class Move extends Copy {

    public Move() {
        super();
        forceOverwrite = true;
    }


    protected void doFileOperations() {
        if (completeDirMap.size() > 0) {
            Enumeration e = completeDirMap.keys();
            while (e.hasMoreElements()) {
                File fromDir = (File) e.nextElement();
                File toDir = (File) completeDirMap.get(fromDir);
                try {
                    log("Attempting to rename dir: " + fromDir +
                        " to " + toDir, verbosity);
                    renameFile(fromDir, toDir, filtering, forceOverwrite);
                } catch (IOException ioe) {
                    String msg = "Failed to rename dir " + fromDir
                        + " to " + toDir
                        + " due to " + ioe.getMessage();
                    throw new BuildException(msg, ioe, location);
                }
            }
        }
            log("Moving " + fileCopyMap.size() + " files to " +
                destDir.getAbsolutePath());

            Enumeration e = fileCopyMap.keys();
            while (e.hasMoreElements()) {
                String fromFile = (String) e.nextElement();
                String toFile = (String) fileCopyMap.get(fromFile);

                if (fromFile.equals(toFile)) {
                    log("Skipping self-move of " + fromFile, verbosity);
                    continue;
                }

                boolean moved = false;
                File f = new File(fromFile);

                    File d = new File(toFile);

                    try {
                        log("Attempting to rename: " + fromFile +
                            " to " + toFile, verbosity);
                        moved = renameFile(f, d, filtering, forceOverwrite);
                    } catch (IOException ioe) {
                        String msg = "Failed to rename " + fromFile
                            + " to " + toFile
                            + " due to " + ioe.getMessage();
                        throw new BuildException(msg, ioe, location);
                    }

                    if (!moved) {
                        try {
                            log("Moving " + fromFile + " to " + toFile, 
                                verbosity);

                            FilterSetCollection executionFilters = 
                                new FilterSetCollection();
                            if (filtering) {
                                executionFilters
                                    .addFilterSet(project.getGlobalFilterSet());
                            }
                            for (Enumeration filterEnum = 
                                     getFilterSets().elements(); 
                                 filterEnum.hasMoreElements();) {
                                executionFilters
                                    .addFilterSet((FilterSet) filterEnum
                                                  .nextElement());
                            }
                            getFileUtils().copyFile(f, d, executionFilters, 
                                                    getFilterChains(), 
                                                    forceOverwrite,
                                                    getPreserveLastModified(),
                                                    getEncoding(), project);

                            f = new File(fromFile);
                            if (!f.delete()) {
                                throw new BuildException("Unable to delete "
                                                         + "file "
                                                         + f.getAbsolutePath());
                            }
                        } catch (IOException ioe) {
                            String msg = "Failed to copy " + fromFile + " to "
                                + toFile
                                + " due to " + ioe.getMessage();
                            throw new BuildException(msg, ioe, location);
                        }
                    }
                }
            }
        }

        if (includeEmpty) {
            Enumeration e = dirCopyMap.elements();
            int count = 0;
            while (e.hasMoreElements()) {
                File d = new File((String) e.nextElement());
                if (!d.exists()) {
                    if (!d.mkdirs()) {
                        log("Unable to create directory " 
                            + d.getAbsolutePath(), Project.MSG_ERR);
                    } else {
                        count++;
                    }
                }
            }

            if (count > 0) {
                log("Moved " + count + " empty directories to " 
                    + destDir.getAbsolutePath());
            }
        }

        if (filesets.size() > 0) {
            Enumeration e = filesets.elements();
            while (e.hasMoreElements()) {
                FileSet fs = (FileSet) e.nextElement();
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
     * @return true if a deletion can go ahead
     */
    protected boolean okToDelete(File d) {
        String[] list = d.list();
        if (list == null) {
            return false;

        for (int i = 0; i < list.length; i++) {
            String s = list[i];
            File f = new File(d, s);
            if (f.isDirectory()) {
                if (!okToDelete(f)) {
                    return false;
                }
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
        if (list == null) {
            return;

        for (int i = 0; i < list.length; i++) {
            String s = list[i];
            File f = new File(d, s);
            if (f.isDirectory()) {
                deleteDir(f);
            } else {
                throw new BuildException("UNEXPECTED ERROR - The file " 
                                         + f.getAbsolutePath() 
                                         + " should not exist!");
            }
        }
        log("Deleting directory " + d.getAbsolutePath(), verbosity);
        if (!d.delete()) {
            throw new BuildException("Unable to delete directory " 
                                     + d.getAbsolutePath());
        }
    }

    /**
     * Attempts to rename a file from a source to a destination.
     * If overwrite is set to true, this method overwrites existing file
     * even if the destination file is newer.  Otherwise, the source file is
     * renamed only if the destination file is older than it.
     * Method then checks if token filtering is used.  If it is, this method
     * returns false assuming it is the responsibility to the copyFile method.
     *
     * @throws IOException
     */
    protected boolean renameFile(File sourceFile, File destFile,
                                 boolean filtering, boolean overwrite)
        throws IOException, BuildException {

        boolean renamed = true;
        if ((getFilterSets() != null && getFilterSets().size() > 0) ||
            (getFilterChains() != null && getFilterChains().size() > 0)) {
            renamed = false;
        } else {
            if (!filtering) {
                String parentPath = destFile.getParent();
                if (parentPath != null) {
                    File parent = new File(parentPath);
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                }

                if (destFile.exists() && destFile.isFile()) {
                    if (!destFile.delete()) {
                        throw new BuildException("Unable to remove existing "
                                                 + "file " + destFile);
                    }
                }
                renamed = sourceFile.renameTo(destFile);
            } else {
                renamed = false;
            }
        }
        return renamed;
    }
}
