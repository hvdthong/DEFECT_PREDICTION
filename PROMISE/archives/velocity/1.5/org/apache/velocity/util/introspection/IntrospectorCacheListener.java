public interface IntrospectorCacheListener
{
    /**
     * Gets called when the Cache is cleared.
     */
    void triggerClear();

    /**
     * Gets called when a ClassMap is requested from the Cache.
     *
     * @param c The class object to look up.
     * @param classMap The Class map to return. Might be null.
     */
    void triggerGet(Class c, ClassMap classMap);

    /**
     * Gets called when a ClassMap is put into the Cache.
     *
     * @param c The class object to look up.
     * @param classMap The Class map stored in the Cache. Is never null.
     */
    void triggerPut(Class c, ClassMap classMap);
}


