package org.apache.tools.ant.types.selectors;

/**
 * An interface used to describe the actions required by any type of
 * directory scanner that supports Selecters.
 *
 * @author <a href="mailto:bruce@callenish.com">Bruce Atherton</a>
 * @since 1.5
 */
public interface SelectorScanner {
    /**
     * Sets the selectors the scanner should use.
     *
     * @param selectors the list of selectors
     */
    void setSelectors(FileSelector[] selectors);

    /**
     * Directories which were selected out of a scan.
     *
     * @param selectors list selector objects
     */
    public String[] getDeselectedDirectories();

    /**
     * Files which were selected out of a scan.
     *
     * @param selectors list selector objects
     */
    public String[] getDeselectedFiles();


}
