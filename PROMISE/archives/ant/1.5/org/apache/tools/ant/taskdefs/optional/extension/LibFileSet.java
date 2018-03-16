package org.apache.tools.ant.taskdefs.optional.extension;

import org.apache.tools.ant.types.FileSet;

/**
 * LibFileSet represents a fileset containing libraries.
 * Asociated with the libraries is data pertaining to
 * how they are to be handled when building manifests.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision: 274041 $ $Date: 2003-02-10 22:25:38 +0800 (周一, 2003-02-10) $
 */
public class LibFileSet
    extends FileSet
{
    /**
     * Flag indicating whether should include the
     * "Implementation-URL" attribute in manifest.
     * Defaults to false.
     */
    private boolean m_includeURL;

    /**
     * Flag indicating whether should include the
     * "Implementation-*" attributes in manifest.
     * Defaults to false.
     */
    private boolean m_includeImpl;

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
    private String m_urlBase;

    /**
     * Flag indicating whether should include the
     * "Implementation-URL" attribute in manifest.
     * Defaults to false.
     *
     * @param includeURL the flag
     * @see #m_includeURL
     */
    public void setIncludeUrl( boolean includeURL )
    {
        m_includeURL = includeURL;
    }

    /**
     * Flag indicating whether should include the
     * "Implementation-*" attributes in manifest.
     * Defaults to false.
     *
     * @param includeImpl the flag
     * @see #m_includeImpl
     */
    public void setIncludeImpl( boolean includeImpl )
    {
        m_includeImpl = includeImpl;
    }

    /**
     * Set the url base for fileset.
     *
     * @param urlBase the base url
     * @see #m_urlBase
     */
    public void setUrlBase( String urlBase )
    {
        m_urlBase = urlBase;
    }

    /**
     * Get the includeURL flag.
     *
     * @return the includeURL flag.
     */
    boolean isIncludeURL()
    {
        return m_includeURL;
    }

    /**
     * Get the includeImpl flag.
     *
     * @return the includeImpl flag.
     */
    boolean isIncludeImpl()
    {
        return m_includeImpl;
    }

    /**
     * Get the urlbase.
     *
     * @return the urlbase.
     */
    String getUrlBase()
    {
        return m_urlBase;
    }
}
