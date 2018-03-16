package org.apache.tools.ant.util;

/**
 * Implementation of FileNameMapper that always returns the same
 * target file name.
 *
 * <p>This is the default FileNameMapper for the archiving tasks and
 * uptodate.</p>
 *
 */
public class MergingMapper implements FileNameMapper {
    protected String[] mergedFile = null;

    /**
     * Ignored.
     */
    public void setFrom(String from) {
    }

    /**
     * Sets the name of the merged file.
     */
    public void setTo(String to) {
        mergedFile = new String[] {to};
    }

    /**
     * Returns an one-element array containing the file name set via setTo.
     */
    public String[] mapFileName(String sourceFileName) {
        return mergedFile;
    }

}
