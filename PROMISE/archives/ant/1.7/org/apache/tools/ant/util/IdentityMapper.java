package org.apache.tools.ant.util;

/**
 * Implementation of FileNameMapper that always returns the source file name.
 *
 * <p>This is the default FileNameMapper for the copy and move
 * tasks.</p>
 *
 */
public class IdentityMapper implements FileNameMapper {

    /**
     * Ignored.
     * @param from ignored.
     */
    public void setFrom(String from) {
    }

    /**
     * Ignored.
     * @param to ignored.
     */
    public void setTo(String to) {
    }

    /**
     * Returns an one-element array containing the source file name.
     * @param sourceFileName the name to map.
     * @return the source filename in a one-element array.
     */
    public String[] mapFileName(String sourceFileName) {
        return new String[] {sourceFileName};
    }
}
