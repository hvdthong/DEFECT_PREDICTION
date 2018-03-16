package org.apache.tools.ant.util;

/**
 * Implementation of FileNameMapper that always returns the source
 * file name without any leading directory information.
 *
 * <p>This is the default FileNameMapper for the copy and move
 * tasks if the flatten attribute has been set.</p>
 *
 */
public class FlatFileNameMapper implements FileNameMapper {

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
     * Returns an one-element array containing the source file name
     * without any leading directory information.
     * @param sourceFileName the name to map.
     * @return the file name in a one-element array.
     */
    public String[] mapFileName(String sourceFileName) {
        return new String[] {new java.io.File(sourceFileName).getName()};
    }
}
