 package org.apache.tools.ant.taskdefs.condition;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;

/**
 * Compares two files for equality based on size and
 * content. Timestamps are not at all looked at.
 *
 * @since Ant 1.5
 */

public class FilesMatch implements Condition {

    /**
     * Helper that provides the file comparison method.
     */
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

    /**
     * files to compare
     */
    private File file1, file2;

    private boolean textfile = false;


    /**
     * Sets the File1 attribute
     *
     * @param file1 The new File1 value
     */
    public void setFile1(File file1) {
        this.file1 = file1;
    }


    /**
     * Sets the File2 attribute
     *
     * @param file2 The new File2 value
     */
    public void setFile2(File file2) {
        this.file2 = file2;
    }

    /**
     * Set whether to ignore line endings when comparing files.
     * @param textfile whether to ignore line endings.
     */
    public void setTextfile(boolean textfile) {
        this.textfile = textfile;
    }

    /**
     * comparison method of the interface
     *
     * @return true if the files are equal
     * @exception BuildException if it all went pear-shaped
     */
    public boolean eval()
        throws BuildException {

        if (file1 == null || file2 == null) {
            throw new BuildException("both file1 and file2 are required in "
                                     + "filesmatch");
        }

        boolean matches = false;
        try {
            matches = FILE_UTILS.contentEquals(file1, file2, textfile);
        } catch (IOException ioe) {
            throw new BuildException("when comparing files: "
                + ioe.getMessage(), ioe);
        }
        return matches;
    }
}

