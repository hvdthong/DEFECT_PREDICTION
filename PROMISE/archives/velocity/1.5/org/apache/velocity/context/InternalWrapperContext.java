public interface InternalWrapperContext
{
    /**
     * Returns the wrapped user context.
     * @return The wrapped user context.
     */
    Context getInternalUserContext();

    /**
     * Returns the base full context impl.
     * @return The base full context impl.
     *
     */
    InternalContextAdapter getBaseContext();

    /**
     * Allows callers to explicitly put objects in the local context.
     * Objects added to the context through this method always end up
     * in the top-level context of possible wrapped contexts.
     *
     *  @param key name of item to set.
     *  @param value object to set to key.
     *  @return old stored object
     */
    Object localPut(final String key, final Object value);
}
