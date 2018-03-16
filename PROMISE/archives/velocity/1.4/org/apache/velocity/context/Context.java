public interface Context
{
    /**
     * Adds a name/value pair to the context.
     *
     * @param key   The name to key the provided value with.
     * @param value The corresponding value.
     */
    Object put(String key, Object value);

    /**
     * Gets the value corresponding to the provided key from the context.
     *
     * @param key The name of the desired value.
     * @return    The value corresponding to the provided key.
     */
    Object get(String key);
 
    /**
     * Indicates whether the specified key is in the context.
     *
     * @param key The key to look for.
     * @return    Whether the key is in the context.
     */
    boolean containsKey(Object key);

    /**
     * Get all the keys for the values in the context
     */
    Object[] getKeys();

    /**
     * Removes the value associated with the specified key from the context.
     *
     * @param key The name of the value to remove.
     * @return    The value that the key was mapped to, or <code>null</code> 
     *            if unmapped.
     */
    Object remove(Object key);
}
