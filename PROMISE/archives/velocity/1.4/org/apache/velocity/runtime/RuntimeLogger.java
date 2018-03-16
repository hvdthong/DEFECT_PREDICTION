public interface RuntimeLogger
{
    /**
     * Log a warning message.
     *
     * @param Object message to log
     */
    public void warn(Object message);

    /**
     * Log an info message.
     *
     * @param Object message to log
     */
    public  void info(Object message);

    /**
     * Log an error message.
     *
     * @param Object message to log
     */
    public void error(Object message);

    /**
     * Log a debug message.
     *
     * @param Object message to log
     */
    public void debug(Object message);
}
