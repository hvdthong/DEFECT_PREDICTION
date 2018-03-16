import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;

/**
 * Default implementation of StringResourceRepository.
 * Uses a HashMap for storage
 *
 * @author <a href="mailto:eelco.hillenius@openedge.nl">Eelco Hillenius</a>
 * @author <a href="mailto:henning@apache.org">Henning P. Schmiedehausen</a>
 * @version $Id: StringResourceRepositoryImpl.java 685724 2008-08-13 23:12:12Z nbubna $
 * @since 1.5
 */
public class StringResourceRepositoryImpl implements StringResourceRepository
{
    /**
     * mem store
     */
    protected Map resources = Collections.synchronizedMap(new HashMap());

    /**
     * Current Repository encoding.
     */
    private String encoding = StringResourceLoader.REPOSITORY_ENCODING_DEFAULT;
    
    /**
     * @see StringResourceRepository#getStringResource(java.lang.String)
     */
    public StringResource getStringResource(final String name)
    {
        return (StringResource)resources.get(name);
    }

    /**
     * @see StringResourceRepository#putStringResource(java.lang.String, java.lang.String)
     */
    public void putStringResource(final String name, final String body)
    {
        resources.put(name, new StringResource(body, getEncoding()));
    }

    /**
     * @see StringResourceRepository#putStringResource(java.lang.String, java.lang.String, java.lang.String)
     * @since 1.6
     */
    public void putStringResource(final String name, final String body, final String encoding)
    {
        resources.put(name, new StringResource(body, encoding));
    }

    /**
     * @see StringResourceRepository#removeStringResource(java.lang.String)
     */
    public void removeStringResource(final String name)
    {
        resources.remove(name);
    }

    /**
     * @see org.apache.velocity.runtime.resource.util.StringResourceRepository#getEncoding()
     */
    public String getEncoding()
    {
	    return encoding;
    }

    /**
     * @see org.apache.velocity.runtime.resource.util.StringResourceRepository#setEncoding(java.lang.String)
     */
    public void setEncoding(final String encoding)
    {
	    this.encoding = encoding;
    }
}
