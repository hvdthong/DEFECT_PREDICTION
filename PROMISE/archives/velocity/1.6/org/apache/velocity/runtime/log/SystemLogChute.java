import java.io.PrintStream;
import org.apache.velocity.runtime.RuntimeServices;

/**
 * Logger used when no other is configured.  By default, all messages
 * will be printed to the System.err output stream.
 *
 * @author <a href="mailto:nbubna@apache.org">Nathan Bubna</a>
 * @version $Id: SystemLogChute.java 718424 2008-11-17 22:50:43Z nbubna $
 * @since 1.5
 */
public class SystemLogChute implements LogChute
{
    public static final String RUNTIME_LOG_LEVEL_KEY = 
        "runtime.log.logsystem.system.level";
    public static final String RUNTIME_LOG_SYSTEM_ERR_LEVEL_KEY = 
        "runtime.log.logsystem.system.err.level";

    private int enabled = WARN_ID;
    private int errLevel = TRACE_ID;

    public void init(RuntimeServices rs) throws Exception
    {
        String level = (String)rs.getProperty(RUNTIME_LOG_LEVEL_KEY);
        if (level != null)
        {
            setEnabledLevel(toLevel(level));
        }

        String errLevel = (String)rs.getProperty(RUNTIME_LOG_SYSTEM_ERR_LEVEL_KEY);
        if (errLevel != null)
        {
            setSystemErrLevel(toLevel(errLevel));
        }
    }

    protected int toLevel(String level) {
        if (level.equalsIgnoreCase("debug"))
        {
            return DEBUG_ID;
        }
        else if (level.equalsIgnoreCase("info"))
        {
            return INFO_ID;
        }
        else if (level.equalsIgnoreCase("warn"))
        {
            return WARN_ID;
        }
        else if (level.equalsIgnoreCase("error"))
        {
            return ERROR_ID;
        }
        else
        {
            return TRACE_ID;
        }
    }

    protected String getPrefix(int level)
    {
        switch (level)
        {
            case WARN_ID:
                return WARN_PREFIX;
            case DEBUG_ID:
                return DEBUG_PREFIX;
            case TRACE_ID:
                return TRACE_PREFIX;
            case ERROR_ID:
                return ERROR_PREFIX;
            case INFO_ID:
            default:
                return INFO_PREFIX;
        }
    }

    /**
     * Logs messages to either std.out or std.err
     * depending on their severity.
     *
     * @param level severity level
     * @param message complete error message
     */
    public void log(int level, String message)
    {
        log(level, message, null);
    }

    /**
     * Logs messages to the system console so long as the specified level
     * is equal to or greater than the level this LogChute is enabled for.
     * If the level is equal to or greater than LogChute.ERROR_ID, 
     * messages will be printed to System.err. Otherwise, they will be 
     * printed to System.out. If a java.lang.Throwable accompanies the 
     * message, it's stack trace will be printed to the same stream
     * as the message.
     *
     * @param level severity level
     * @param message complete error message
     * @param t the java.lang.Throwable
     */
    public void log(int level, String message, Throwable t)
    {
        if (!isLevelEnabled(level))
        {
            return;
        }

        String prefix = getPrefix(level);
        if (level >= this.errLevel)
        {
            write(System.err, prefix, message, t);
        }
        else
        {
            write(System.out, prefix, message, t);
        }
    }

    protected void write(PrintStream stream, String prefix, String message, Throwable t)
    {
        stream.print(prefix);
        stream.println(message);
        if (t != null)
        {
            stream.println(t.getMessage());
            t.printStackTrace(stream);
        }
    }

    /**
     * Set the minimum level at which messages will be printed.
     */
    public void setEnabledLevel(int level)
    {
        this.enabled = level;
    }

    /**
     * Returns the current minimum level at which messages will be printed.
     */
    public int getEnabledLevel()
    {
        return this.enabled;
    }

    /**
     * Set the minimum level at which messages will be printed to System.err
     * instead of System.out.
     */
    public void setSystemErrLevel(int level)
    {
        this.errLevel = level;
    }

    /**
     * Returns the current minimum level at which messages will be printed
     * to System.err instead of System.out.
     */
    public int getSystemErrLevel()
    {
        return this.errLevel;
    }

    /**
     * This will return true if the specified level
     * is equal to or higher than the level this
     * LogChute is enabled for.
     */
    public boolean isLevelEnabled(int level)
    {
        return (level >= this.enabled);
    }

}
