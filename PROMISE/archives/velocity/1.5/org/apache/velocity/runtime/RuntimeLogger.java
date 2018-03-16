public interface RuntimeLogger
{
    /**
     * @deprecated Use Log.warn(Object).
     * @see org.apache.velocity.runtime.log.Log#warn(Object)
     * @param message The message to log.
     */
    public void warn(Object message);

    /**
     * @deprecated Use Log.info(Object)
     * @see org.apache.velocity.runtime.log.Log#info(Object)
     * @param message The message to log.
     */
    public  void info(Object message);

    /**
     * @deprecated Use Log.error(Object)
     * @see org.apache.velocity.runtime.log.Log#error(Object)
     * @param message The message to log.
     */
    public void error(Object message);

    /**
     * @deprecated Use Log.debug(Object)
     * @see org.apache.velocity.runtime.log.Log#debug(Object)
     * @param message The message to log.
     */
    public void debug(Object message);
}
