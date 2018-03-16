package org.apache.tools.ant.taskdefs.optional.extension;

import java.util.ArrayList;
import java.util.Arrays;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.FileSet;

/**
 * The Extension set lists a set of "Optional Packages" /
 * "Extensions".
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision: 274041 $ $Date: 2003-02-10 22:25:38 +0800 (周一, 2003-02-10) $
 * @ant.data-type name="extension-set"
 */
public class ExtensionSet
    extends DataType
{
    /**
     * ExtensionAdapter objects representing extensions.
     */
    private final ArrayList m_extensions = new ArrayList();

    /**
     * Filesets specifying all the extensions wanted.
     */
    private final ArrayList m_extensionsFilesets = new ArrayList();

    /**
     * Adds an extension that this library requires.
     *
     * @param extensionAdapter an extension that this library requires.
     */
    public void addExtension( final ExtensionAdapter extensionAdapter )
    {
        m_extensions.add( extensionAdapter );
    }

    /**
     * Adds a set of files about which extensions data will be extracted.
     *
     * @param fileSet a set of files about which extensions data will be extracted.
     */
    public void addLibfileset( final LibFileSet fileSet )
    {
        m_extensionsFilesets.add( fileSet );
    }

    /**
     * Adds a set of files about which extensions data will be extracted.
     *
     * @param fileSet a set of files about which extensions data will be extracted.
     */
    public void addFileset( final FileSet fileSet )
    {
        m_extensionsFilesets.add( fileSet );
    }

    /**
     * Extract a set of Extension objects from the ExtensionSet.
     *
     * @throws BuildException if an error occurs
     */
    public Extension[] toExtensions( final Project project )
        throws BuildException
    {
        final ArrayList extensions = ExtensionUtil.toExtensions( m_extensions );
        ExtensionUtil.extractExtensions( project, extensions, m_extensionsFilesets );
        return (Extension[])extensions.toArray( new Extension[ extensions.size() ] );
    }

    /**
     * Makes this instance in effect a reference to another ExtensionSet
     * instance.
     *
     * <p>You must not set another attribute or nest elements inside
     * this element if you make it a reference.</p>
     *
     * @param reference the reference to which this instance is associated
     * @exception BuildException if this instance already has been configured.
     */
    public void setRefid( final Reference reference )
        throws BuildException
    {
        if( !m_extensions.isEmpty() ||
            !m_extensionsFilesets.isEmpty() )
        {
            throw tooManyAttributes();
        }
        final Object object =
            reference.getReferencedObject( getProject() );
        if( object instanceof ExtensionSet )
        {
            final ExtensionSet other = (ExtensionSet)object;
            m_extensions.addAll( other.m_extensions );
            m_extensionsFilesets.addAll( other.m_extensionsFilesets );
        }
        else
        {
            final String message =
                reference.getRefId() + " doesn\'t refer to a ExtensionSet";
            throw new BuildException( message );
        }

        super.setRefid( reference );
    }

    public String toString()
    {
        return "ExtensionSet" + Arrays.asList( toExtensions( getProject() ) );
    }
}
