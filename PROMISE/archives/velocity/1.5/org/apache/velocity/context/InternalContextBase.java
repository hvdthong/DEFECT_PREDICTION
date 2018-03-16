import java.util.HashMap;
import java.util.Stack;

import org.apache.velocity.app.event.EventCartridge;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.util.introspection.IntrospectionCacheData;

/**
 *  class to encapsulate the 'stuff' for internal operation of velocity.
 *  We use the context as a thread-safe storage : we take advantage of the
 *  fact that it's a visitor  of sorts  to all nodes (that matter) of the
 *  AST during init() and render().
 *  Currently, it carries the template name for namespace
 *  support, as well as node-local context data introspection caching.
 *
 *  Note that this is not a public class.  It is for package access only to
 *  keep application code from accessing the internals, as AbstractContext
 *  is derived from this.
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: InternalContextBase.java 463298 2006-10-12 16:10:32Z henning $
 */
class InternalContextBase implements InternalHousekeepingContext, InternalEventContext
{
    /**
     * Version Id for serializable
     */
    private static final long serialVersionUID = -245905472770843470L;

    /**
     *  cache for node/context specific introspection information
     */
    private HashMap introspectionCache = new HashMap(33);

    /**
     *  Template name stack. The stack top contains the current template name.
     */
    private Stack templateNameStack = new Stack();

    /**
     *  EventCartridge we are to carry.  Set by application
     */
    private EventCartridge eventCartridge = null;

    /**
     *  Current resource - used for carrying encoding and other
     *  information down into the rendering process
     */
    private Resource currentResource = null;

    /**
     *  Is rendering allowed?  Defaults to true, can be changed by #stop directive.
     */
    private boolean allowRendering = true;

    /**
     *  set the current template name on top of stack
     *
     *  @param s current template name
     */
    public void pushCurrentTemplateName( String s )
    {
        templateNameStack.push(s);
    }

    /**
     *  remove the current template name from stack
     */
    public void popCurrentTemplateName()
    {
        templateNameStack.pop();
    }

    /**
     *  get the current template name
     *
     *  @return String current template name
     */
    public String getCurrentTemplateName()
    {
        if ( templateNameStack.empty() )
            return "<undef>";
        else
            return (String) templateNameStack.peek();
    }

    /**
     *  get the current template name stack
     *
     *  @return Object[] with the template name stack contents.
     */
    public Object[] getTemplateNameStack()
    {
        return templateNameStack.toArray();
    }

    /**
     *  returns an IntrospectionCache Data (@see IntrospectionCacheData)
     *  object if exists for the key
     *
     *  @param key  key to find in cache
     *  @return cache object
     */
    public IntrospectionCacheData icacheGet( Object key )
    {
        return ( IntrospectionCacheData ) introspectionCache.get( key );
    }

    /**
     *  places an IntrospectionCache Data (@see IntrospectionCacheData)
     *  element in the cache for specified key
     *
     *  @param key  key
     *  @param o  IntrospectionCacheData object to place in cache
     */
    public void icachePut( Object key, IntrospectionCacheData o )
    {
        introspectionCache.put( key, o );
    }

    /**
     * @see org.apache.velocity.context.InternalHousekeepingContext#setCurrentResource(org.apache.velocity.runtime.resource.Resource)
     */
    public void setCurrentResource( Resource r )
    {
        currentResource = r;
    }

    /**
     * @see org.apache.velocity.context.InternalHousekeepingContext#getCurrentResource()
     */
    public Resource getCurrentResource()
    {
        return currentResource;
    }


     /**
     * @see org.apache.velocity.context.InternalHousekeepingContext#getAllowRendering()
     */
    public boolean getAllowRendering()
     {
        return allowRendering;
     }

    /**
     * @see org.apache.velocity.context.InternalHousekeepingContext#setAllowRendering(boolean)
     */
    public void setAllowRendering(boolean v)
    {
        allowRendering = v;
    }


    /**
     * @see org.apache.velocity.context.InternalEventContext#attachEventCartridge(org.apache.velocity.app.event.EventCartridge)
     */
    public EventCartridge attachEventCartridge( EventCartridge ec )
    {
        EventCartridge temp = eventCartridge;

        eventCartridge = ec;

        return temp;
    }

    /**
     * @see org.apache.velocity.context.InternalEventContext#getEventCartridge()
     */
    public EventCartridge getEventCartridge()
    {
        return eventCartridge;
    }
}

