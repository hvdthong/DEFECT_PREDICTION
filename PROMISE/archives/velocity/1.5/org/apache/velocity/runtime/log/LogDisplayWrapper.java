public class LogDisplayWrapper
        extends Log
{
    /** The prefix to record with every log message */
    private final String prefix;

    /** log messages only if true */
    private final boolean outputMessages;

    /** The Log object we wrap */
    private final Log log;

    /**
     * Create a new LogDisplayWrapper
     * @param log The Log object to wrap.
     * @param prefix The prefix to record with all messages.
     * @param outputMessages True when messages should actually get logged.
     */
    public LogDisplayWrapper(final Log log, final String prefix, final boolean outputMessages)
    {
        super(log.getLogChute());
        this.log = log;
        this.prefix = prefix;
        this.outputMessages = outputMessages;
    }

    /**
     * make sure that we always use the right LogChute Object
     */
    protected LogChute getLogChute()
    {
        return log.getLogChute();
    }

    /**
     * @see Log#log(int, Object)
     */
    protected void log(final int level, final Object message)
    {
    	log(outputMessages, level, message);
    }
    
    protected void log(final boolean doLogging, final int level, final Object message)
    {
        if (doLogging)
        {
            getLogChute().log(level, prefix + String.valueOf(message));
        }
    }

    /**
     * @see Log#log(int, Object, Throwable)
     */
    protected void log(final int level, final Object message, final Throwable t)
    {
    	log(outputMessages, level, message);
    }
    
    protected void log(final boolean doLogging, final int level, final Object message, final Throwable t)
    {
        if (doLogging)
        {
            getLogChute().log(level, prefix + String.valueOf(message), t);
        }
    }
    
    /**
     * Log a trace message.
     * @param doLogging Log only if this parameter is true.
     * @param message
     */
    public void trace(final boolean doLogging, final Object message)
    {
        log(doLogging, LogChute.TRACE_ID, message);
    }

    /**
     * Log a trace message and accompanying Throwable.
     * @param doLogging Log only if this parameter is true.
     * @param message
     * @param t
     */
    public void trace(final boolean doLogging, final Object message, final Throwable t)
    {
        log(doLogging, LogChute.TRACE_ID, message, t);
    }

    /**
     * Log a debug message.
     * @param doLogging Log only if this parameter is true.
     * @param message
     */
    public void debug(final boolean doLogging, final Object message)
    {
        log(doLogging, LogChute.DEBUG_ID, message);
    }

    /**
     * Log a debug message and accompanying Throwable.
     * @param doLogging Log only if this parameter is true.
     * @param message
     * @param t
     */
    public void debug(final boolean doLogging, final Object message, final Throwable t)
    {
        log(doLogging, LogChute.DEBUG_ID, message, t);
    }

    /**
     * Log an info message.
     * @param doLogging Log only if this parameter is true.
     * @param message
     */
    public void info(final boolean doLogging, final Object message)
    {
        log(doLogging, LogChute.INFO_ID, message);
    }

    /**
     * Log an info message and accompanying Throwable.
     * @param doLogging Log only if this parameter is true.
     * @param message
     * @param t
     */
    public void info(final boolean doLogging, final Object message, final Throwable t)
    {
        log(doLogging, LogChute.INFO_ID, message, t);
    }

    /**
     * Log a warning message.
     * @param doLogging Log only if this parameter is true.
     * @param message
     */
    public void warn(final boolean doLogging, final Object message)
    {
        log(doLogging, LogChute.WARN_ID, message);
    }

    /**
     * Log a warning message and accompanying Throwable.
     * @param doLogging Log only if this parameter is true.
     * @param message
     * @param t
     */
    public void warn(final boolean doLogging, final Object message, final Throwable t)
    {
        log(doLogging, LogChute.WARN_ID, message, t);
    }

    /**
     * Log an error message.
     * @param doLogging Log only if this parameter is true.
     * @param message
     */
    public void error(final boolean doLogging, final Object message)
    {
        log(doLogging, LogChute.ERROR_ID, message);
    }

    /**
     * Log an error message and accompanying Throwable.
     * @param doLogging Log only if this parameter is true.
     * @param message
     * @param t
     */
    public void error(final boolean doLogging, final Object message, final Throwable t)
    {
        log(doLogging, LogChute.ERROR_ID, message, t);
    }
}

