package org.apache.tools.ant.util;

/**
 * Implementation of FileNameMapper that always returns the source
 * file name without any leading directory information.
 *
 * <p>This is the default FileNameMapper for the copy and move
 * tasks if the flatten attribute has been set.</p>
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public class FlatFileNameMapper implements FileNameMapper {

    /**
     * Ignored.
     */
    public void setFrom(String from) {}

    /**
     * Ignored.
     */
    public void setTo(String to) {}

    /**
     * Returns an one-element array containing the source file name
     * without any leading directory information.
     */
    public String[] mapFileName(String sourceFileName) {
        return new String[] {new java.io.File(sourceFileName).getName()};
    }
}
