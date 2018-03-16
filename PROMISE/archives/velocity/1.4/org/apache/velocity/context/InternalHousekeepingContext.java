import org.apache.velocity.util.introspection.IntrospectionCacheData;

import org.apache.velocity.runtime.resource.Resource;

/**
 *  interface to encapsulate the 'stuff' for internal operation of velocity.  
 *  We use the context as a thread-safe storage : we take advantage of the
 *  fact that it's a visitor  of sorts  to all nodes (that matter) of the 
 *  AST during init() and render().
 *
 *  Currently, it carries the template name for namespace
 *  support, as well as node-local context data introspection caching.
 *
 *  @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 *  @author <a href="mailto:Christoph.Reck@dlr.de">Christoph Reck</a>
 *  @version $Id: InternalHousekeepingContext.java 75955 2004-03-03 23:23:08Z geirm $
 */
interface InternalHousekeepingContext
{
    /**
     *  set the current template name on top of stack
     *
     *  @param s current template name
     */
    void pushCurrentTemplateName( String s );

    /**
     *  remove the current template name from stack
     */
    void popCurrentTemplateName();
    
    /**
     *  get the current template name
     *
     *  @return String current template name
     */
    String getCurrentTemplateName();

    /**
     *  Returns the template name stack in form of an array.
     *
     *  @return Object[] with the template name stack contents.
     */
    Object[] getTemplateNameStack();

    /**
     *  returns an IntrospectionCache Data (@see IntrospectionCacheData)
     *  object if exists for the key
     *
     *  @param key  key to find in cache
     *  @return cache object
     */
    IntrospectionCacheData icacheGet( Object key );
    
    /**
     *  places an IntrospectionCache Data (@see IntrospectionCacheData)
     *  element in the cache for specified key
     *
     *  @param key  key 
     *  @param o  IntrospectionCacheData object to place in cache
     */
    void icachePut( Object key, IntrospectionCacheData o );

    /**
     *  temporary fix to enable #include() to figure out
     *  current encoding.
     *
     */
    Resource getCurrentResource();
    void setCurrentResource( Resource r );
    
    
}
