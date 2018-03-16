package org.apache.tools.ant.util;

/**
 * Implementation of FileNameMapper that always returns the source file name.
 *
 * <p>This is the default FileNameMapper for the copy and move
 * tasks.</p>
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public class IdentityMapper implements FileNameMapper {

    /**
     * Ignored.
     */
    public void setFrom(String from) {}

    /**
     * Ignored.
     */
    public void setTo(String to) {}

    /**
     * Returns an one-element array containing the source file name.
     */
    public String[] mapFileName(String sourceFileName) {
        return new String[] {sourceFileName};
    }
}
