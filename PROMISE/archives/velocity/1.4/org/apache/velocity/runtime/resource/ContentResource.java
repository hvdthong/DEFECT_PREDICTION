import java.io.StringWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * This class represent a general text resource that may have been
 * retrieved from any number of possible sources.
 *
 * Also of interest is Velocity's {@link org.apache.velocity.Template}
 * <code>Resource</code>.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: ContentResource.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class ContentResource extends Resource
{
    /** Default empty constructor */
    public ContentResource()
    {
    }
    
    /**
     * Pull in static content and store it.
     *
     * @exception ResourceNotFoundException Resource could not be
     * found.
     */
    public boolean process()
        throws ResourceNotFoundException
    {
        BufferedReader reader = null;

        try
        {
            StringWriter sw = new StringWriter();
            
            reader = new BufferedReader(
                new InputStreamReader(resourceLoader.getResourceStream(name),
                                      encoding));
            
            char buf[] = new char[1024];
            int len = 0;
            
            while ( ( len = reader.read( buf, 0, 1024 )) != -1)
                sw.write( buf, 0, len );
        
            setData(sw.toString());
           
            return true;
        }
        catch ( ResourceNotFoundException e )
        {
            throw e;
        }
        catch ( Exception e ) 
        {
            rsvc.error("Cannot process content resource : " + e.toString() );
            return false;
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (Exception ignored)
                {
                }
            }
        }
    }
}
