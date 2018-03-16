package org.apache.xml.serializer;

import java.util.Hashtable;

/**
 * This class contains utilities used by the serializer
 */
class Utils
{

    /**
     * This nested class acts as a way to lazy load the hashtable
     * in a thread safe way.
     */
    static private class CacheHolder
    {
        static final Hashtable cache;
        static {
            cache = new Hashtable();
        }
    }
    /**
     * Load the class by name.
     * 
     * This implementation, for performance reasons,
     * caches all classes loaded by name and
     * returns the cached Class object if it can previously 
     * loaded classes that were load by name.  If not previously loaded
     * an attempt is made to load with Class.forName(classname)
     * @param classname the name of the class to be loaded
     * @return the loaded class, never null. If the class could not be
     * loaded a ClassNotFound exception is thrown.
     * @throws ClassNotFoundException if the class was not loaded
     */
    static Class ClassForName(String classname) throws ClassNotFoundException
    {
        Class c;
        Object o = CacheHolder.cache.get(classname);
        if (o == null)
        {
            c = Class.forName(classname);
            
            CacheHolder.cache.put(classname, c);
        }
        else
        {
            c = (Class)o;
        }
        return c;
    }
}
