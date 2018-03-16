package org.apache.tools.ant.taskdefs.optional.extension;

import org.apache.tools.ant.types.FileSet;

/**
 * LibFileSet represents a fileset containing libraries.
 * Asociated with the libraries is data pertaining to
 * how they are to be handled when building manifests.
 *
 */
public class LibFileSet
    extends FileSet {
    /**
     * Flag indicating whether should include the
     * "Implementation-URL" attribute in manifest.
     * Defaults to false.
     */
    private boolean includeURL;

    /**
     * Flag indicating whether should include the
     * "Implementation-*" attributes in manifest.
     * Defaults to false.
     */
    private boolean includeImpl;

    /**
     * String that is the base URL for the librarys
     * when constructing the "Implementation-URL"
     * attribute. For instance setting the base to
     * including the library "excalibur-cli-1.0.jar" in the
     * fileset will result in the "Implementation-URL" attribute
     *
     * Note this is only used if the library does not define
     * "Implementation-URL" itself.
     *
     * Note that this also implies includeURL=true
     */
    private String urlBase;

    /**
     * Flag indicating whether should include the
     * "Implementation-URL" attribute in manifest.
     * Defaults to false.
     *
     * @param includeURL the flag
     */
    public void setIncludeUrl(boolean includeURL) {
        this.includeURL = includeURL;
    }

    /**
     * Flag indicating whether should include the
     * "Implementation-*" attributes in manifest.
     * Defaults to false.
     *
     * @param includeImpl the flag
     */
    public void setIncludeImpl(boolean includeImpl) {
        this.includeImpl = includeImpl;
    }

    /**
     * Set the url base for fileset.
     *
     * @param urlBase the base url
     */
    public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }

    /**
     * Get the includeURL flag.
     *
     * @return the includeURL flag.
     */
    boolean isIncludeURL() {
        return includeURL;
    }

    /**
     * Get the includeImpl flag.
     *
     * @return the includeImpl flag.
     */
    boolean isIncludeImpl() {
        return includeImpl;
    }

    /**
     * Get the urlbase.
     *
     * @return the urlbase.
     */
    String getUrlBase() {
        return urlBase;
    }
}
