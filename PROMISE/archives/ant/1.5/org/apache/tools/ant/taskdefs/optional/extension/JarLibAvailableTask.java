package org.apache.tools.ant.taskdefs.optional.extension;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;
import java.util.jar.Manifest;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Checks whether an extension is present in a fileset or an extensionSet.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @ant.task name="jarlib-available"
 */
public class JarLibAvailableTask
    extends Task
{
    /**
     * The library to display information about.
     */
    private File m_file;

    /**
     * Filesets specifying all the librarys
     * to display information about.
     */
    private final Vector m_extensionSets = new Vector();

    /**
     * The name of the property to set if extension is available.
     */
    private String m_property;

    /**
     * The extension that is required.
     */
    private ExtensionAdapter m_extension;

    /**
     * The name of property to set if extensions are available.
     *
     * @param property The name of property to set if extensions is available.
     */
    public void setProperty( final String property )
    {
        m_property = property;
    }

    /**
     * The JAR library to check.
     *
     * @param file The jar library to check.
     */
    public void setFile( final File file )
    {
        m_file = file;
    }

    /**
     * Set the Extension looking for.
     *
     * @param extension Set the Extension looking for.
     */
    public void addConfiguredExtension( final ExtensionAdapter extension )
    {
        if( null != m_extension )
        {
            final String message = "Can not specify extension to " +
                "search for multiple times.";
            throw new BuildException( message );
        }
        m_extension = extension;
    }

    /**
     * Adds a set of extensions to search in.
     *
     * @param extensionSet a set of extensions to search in.
     */
    public void addConfiguredExtensionSet( final ExtensionSet extensionSet )
    {
        m_extensionSets.addElement( extensionSet );
    }

    public void execute()
        throws BuildException
    {
        validate();

        final Extension test = m_extension.toExtension();

        if( !m_extensionSets.isEmpty() )
        {
            final Iterator iterator = m_extensionSets.iterator();
            while( iterator.hasNext() )
            {
                final ExtensionSet extensionSet = (ExtensionSet)iterator.next();
                final Extension[] extensions =
                    extensionSet.toExtensions( getProject() );
                for( int i = 0; i < extensions.length; i++ )
                {
                    final Extension extension = extensions[ i ];
                    if( extension.isCompatibleWith( test ) )
                    {
                        getProject().setNewProperty( m_property, "true" );
                    }
                }
            }
        }
        else
        {
            final Manifest manifest = ExtensionUtil.getManifest( m_file );
            final Extension[] extensions = Extension.getAvailable( manifest );
            for( int i = 0; i < extensions.length; i++ )
            {
                final Extension extension = extensions[ i ];
                if( extension.isCompatibleWith( test ) )
                {
                    getProject().setNewProperty( m_property, "true" );
                }
            }
        }
    }

    /**
     * Validate the tasks parameters.
     *
     * @throws BuildException if invalid parameters found
     */
    private void validate()
        throws BuildException
    {
        if( null == m_extension )
        {
            final String message = "Extension element must be specified.";
            throw new BuildException( message );
        }

        if( null == m_file && m_extensionSets.isEmpty() )
        {
            final String message = "File attribute not specified.";
            throw new BuildException( message );
        }
        if( null != m_file && !m_file.exists() )
        {
            final String message = "File '" + m_file + "' does not exist.";
            throw new BuildException( message );
        }
        if( null != m_file && !m_file.isFile() )
        {
            final String message = "\'" + m_file + "\' is not a file.";
            throw new BuildException( message );
        }
    }
}
