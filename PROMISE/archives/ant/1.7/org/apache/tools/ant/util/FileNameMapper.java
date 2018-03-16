package org.apache.tools.ant.util;

/**
 * Interface to be used by SourceFileScanner.
 *
 * <p>Used to find the name of the target file(s) corresponding to a
 * source file.</p>
 *
 * <p>The rule by which the file names are transformed is specified
 * via the setFrom and setTo methods. The exact meaning of these is
 * implementation dependent.</p>
 *
 */
public interface FileNameMapper {

    /**
     * Sets the from part of the transformation rule.
     * @param from a string.
     */
    void setFrom(String from);

    /**
     * Sets the to part of the transformation rule.
     * @param to a string.
     */
    void setTo(String to);

    /**
     * Returns an array containing the target filename(s) for the
     * given source file.
     *
     * <p>if the given rule doesn't apply to the source file,
     * implementation must return null. SourceFileScanner will then
     * omit the source file in question.</p>
     *
     * @param sourceFileName the name of the source file relative to
     *                       some given basedirectory.
     * @return an array of strings if the ruld applies to the source file, or
     *         null if it does not.
     */
    String[] mapFileName(String sourceFileName);
}
