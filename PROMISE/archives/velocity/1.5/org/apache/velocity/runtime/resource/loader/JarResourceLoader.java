import java.io.InputStream;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;

import org.apache.velocity.util.StringUtils;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.commons.collections.ExtendedProperties;

/**
 * <p>
 * ResourceLoader to load templates from multiple Jar files.
 * </p>
 * <p>
 * The configuration of the JarResourceLoader is straightforward -
 * You simply add the JarResourceLoader to the configuration via
 * </p>
 * <p><pre>
 *    resource.loader = jar
 *    jar.resource.loader.class = org.apache.velocity.runtime.resource.loader.JarResourceLoader
 *    jar.resource.loader.path = list of JAR &lt;URL&gt;s
 * </pre></p>
 *
 * <p> So for example, if you had a jar file on your local filesystem, you could simply do
 *    <pre>
 *    jar.resource.loader.path = jar:file:/opt/myfiles/jar1.jar
 *    </pre>
 * </p>
 * <p> Note that jar specification for the <code>.path</code> configuration property
 * conforms to the same rules for the java.net.JarUrlConnection class.
 * </p>
 *
 * <p> For a working example, see the unit test case,
 *  org.apache.velocity.test.MultiLoaderTestCase class
 * </p>
 *
 * @author <a href="mailto:mailmur@yahoo.com">Aki Nieminen</a>
 * @author <a href="mailto:daveb@miceda-data.com">Dave Bryson</a>
 * @version $Id: JarResourceLoader.java 471259 2006-11-04 20:26:57Z henning $
 */
public class JarResourceLoader extends ResourceLoader
{
    /**
     * Maps entries to the parent JAR File
     * Key = the entry *excluding* plain directories
     * Value = the JAR URL
     */
    private Map entryDirectory = new HashMap(559);

    /**
     * Maps JAR URLs to the actual JAR
     * Key = the JAR URL
     * Value = the JAR
     */
    private Map jarfiles = new HashMap(89);

    /**
     * Called by Velocity to initialize the loader
     * @param configuration
     */
    public void init( ExtendedProperties configuration)
    {
        log.trace("JarResourceLoader : initialization starting.");

        Vector paths = configuration.getVector("path");
        StringUtils.trimStrings(paths);

        /*
         *  support the old version but deprecate with a log message
         */

        if( paths == null || paths.size() == 0)
        {
            paths = configuration.getVector("resource.path");
            StringUtils.trimStrings(paths);

            if (paths != null && paths.size() > 0)
            {
                log.info("JarResourceLoader : you are using a deprecated configuration"
                         + " property for the JarResourceLoader -> '<name>.resource.loader.resource.path'."
                         + " Please change to the conventional '<name>.resource.loader.path'.");
            }
        }

        if (paths != null)
        {
            log.debug("JarResourceLoader # of paths : " + paths.size() );

            for ( int i=0; i<paths.size(); i++ )
            {
                loadJar( (String)paths.get(i) );
            }
        }

        log.trace("JarResourceLoader : initialization complete.");
    }

    private void loadJar( String path )
    {
        if (log.isDebugEnabled())
        {
            log.debug("JarResourceLoader : trying to load \"" + path + "\"");
        }

        if ( path == null )
        {
            log.error("JarResourceLoader : can not load JAR - JAR path is null");
        }
        if ( !path.startsWith("jar:") )
        {
            log.error("JarResourceLoader : JAR path must start with jar: -> " +
                "see java.net.JarURLConnection for information");
        }
        if ( !path.endsWith("!/") )
        {
            path += "!/";
        }

        closeJar( path );

        JarHolder temp = new JarHolder( rsvc,  path );
        addEntries( temp.getEntries() );
        jarfiles.put( temp.getUrlPath(), temp );
    }

    /**
     * Closes a Jar file and set its URLConnection
     * to null.
     */
    private void closeJar( String path )
    {
        if ( jarfiles.containsKey(path) )
        {
            JarHolder theJar = (JarHolder)jarfiles.get(path);
            theJar.close();
        }
    }

    /**
     * Copy all the entries into the entryDirectory
     * It will overwrite any duplicate keys.
     */
    private void addEntries( Hashtable entries )
    {
        entryDirectory.putAll( entries );
    }

    /**
     * Get an InputStream so that the Runtime can build a
     * template with it.
     *
     * @param source name of template to get
     * @return InputStream containing the template
     * @throws ResourceNotFoundException if template not found
     *         in the file template path.
     */
    public InputStream getResourceStream( String source )
        throws ResourceNotFoundException
    {
        InputStream results = null;

        if (org.apache.commons.lang.StringUtils.isEmpty(source))
        {
            throw new ResourceNotFoundException("Need to have a resource!");
        }

        String normalizedPath = StringUtils.normalizePath( source );

        if ( normalizedPath == null || normalizedPath.length() == 0 )
        {
            String msg = "JAR resource error : argument " + normalizedPath +
                " contains .. and may be trying to access " +
                "content outside of template root.  Rejected.";

            log.error( "JarResourceLoader : " + msg );

            throw new ResourceNotFoundException ( msg );
        }

        /*
         *  if a / leads off, then just nip that :)
         */
        if ( normalizedPath.startsWith("/") )
        {
            normalizedPath = normalizedPath.substring(1);
        }

        if ( entryDirectory.containsKey( normalizedPath ) )
        {
            String jarurl  = (String)entryDirectory.get( normalizedPath );

            if ( jarfiles.containsKey( jarurl ) )
            {
                JarHolder holder = (JarHolder)jarfiles.get( jarurl );
                results =  holder.getResource( normalizedPath );
                return results;
            }
        }

        throw new ResourceNotFoundException( "JarResourceLoader Error: cannot find resource " +
          source );

    }



    /**
     * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#isSourceModified(org.apache.velocity.runtime.resource.Resource)
     */
    public boolean isSourceModified(Resource resource)
    {
        return true;
    }

    /**
     * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#getLastModified(org.apache.velocity.runtime.resource.Resource)
     */
    public long getLastModified(Resource resource)
    {
        return 0;
    }
}
