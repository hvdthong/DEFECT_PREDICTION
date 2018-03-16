package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.log.Logger;
import org.apache.log.Hierarchy;
import org.apache.commons.logging.Log;

/**
 * Implementation of <code>org.apache.commons.logging.Log</code>
 * logging system. Configuration of <code>LogKit</code> is left to the user.
 * <p>
 * <code>LogKit</code> accepts only <code>String</code> messages.
 * Therefore, this implementation converts object messages into strings
 * by called their <code>toString()</code> method before logging them.
 *
 * @version $Id: LogKitLogger.java 1448119 2013-02-20 12:28:04Z tn $
 */
public class LogKitLogger implements Log, Serializable {

    /** Serializable version identifier. */
    private static final long serialVersionUID = 3768538055836059519L;


    /** Logging goes to this <code>LogKit</code> logger */
    protected transient volatile Logger logger = null;

    /** Name of this logger */
    protected String name = null;


    /**
     * Construct <code>LogKitLogger</code> which wraps the <code>LogKit</code>
     * logger with given name.
     *
     * @param name log name
     */
    public LogKitLogger(String name) {
        this.name = name;
        this.logger = getLogger();
    }


    /**
     * Return the underlying Logger we are using.
     */
    public Logger getLogger() {
        Logger result = logger;
        if (result == null) {
            synchronized(this) {
                result = logger;
                if (result == null) {
                    logger = result = Hierarchy.getDefaultHierarchy().getLoggerFor(name);
                }
            }
        }
        return result;
    }


    /**
     * Logs a message with <code>org.apache.log.Priority.DEBUG</code>.
     *
     * @param message to log
     * @see org.apache.commons.logging.Log#trace(Object)
    */
    public void trace(Object message) {
        debug(message);
    }

    /**
     * Logs a message with <code>org.apache.log.Priority.DEBUG</code>.
     *
     * @param message to log
     * @param t log this cause
     * @see org.apache.commons.logging.Log#trace(Object, Throwable)
     */
    public void trace(Object message, Throwable t) {
        debug(message, t);
    }

    /**
     * Logs a message with <code>org.apache.log.Priority.DEBUG</code>.
     *
     * @param message to log
     * @see org.apache.commons.logging.Log#debug(Object)
     */
    public void debug(Object message) {
        if (message != null) {
            getLogger().debug(String.valueOf(message));
        }
    }

    /**
     * Logs a message with <code>org.apache.log.Priority.DEBUG</code>.
     *
     * @param message to log
     * @param t log this cause
     * @see org.apache.commons.logging.Log#debug(Object, Throwable)
     */
    public void debug(Object message, Throwable t) {
        if (message != null) {
            getLogger().debug(String.valueOf(message), t);
        }
    }

    /**
     * Logs a message with <code>org.apache.log.Priority.INFO</code>.
     *
     * @param message to log
     * @see org.apache.commons.logging.Log#info(Object)
     */
    public void info(Object message) {
        if (message != null) {
            getLogger().info(String.valueOf(message));
        }
    }

    /**
     * Logs a message with <code>org.apache.log.Priority.INFO</code>.
     *
     * @param message to log
     * @param t log this cause
     * @see org.apache.commons.logging.Log#info(Object, Throwable)
     */
    public void info(Object message, Throwable t) {
        if (message != null) {
            getLogger().info(String.valueOf(message), t);
        }
    }

    /**
     * Logs a message with <code>org.apache.log.Priority.WARN</code>.
     *
     * @param message to log
     * @see org.apache.commons.logging.Log#warn(Object)
     */
    public void warn(Object message) {
        if (message != null) {
            getLogger().warn(String.valueOf(message));
        }
    }

    /**
     * Logs a message with <code>org.apache.log.Priority.WARN</code>.
     *
     * @param message to log
     * @param t log this cause
     * @see org.apache.commons.logging.Log#warn(Object, Throwable)
     */
    public void warn(Object message, Throwable t) {
        if (message != null) {
            getLogger().warn(String.valueOf(message), t);
        }
    }

    /**
     * Logs a message with <code>org.apache.log.Priority.ERROR</code>.
     *
     * @param message to log
     * @see org.apache.commons.logging.Log#error(Object)
     */
    public void error(Object message) {
        if (message != null) {
            getLogger().error(String.valueOf(message));
        }
    }

    /**
     * Logs a message with <code>org.apache.log.Priority.ERROR</code>.
     *
     * @param message to log
     * @param t log this cause
     * @see org.apache.commons.logging.Log#error(Object, Throwable)
     */
    public void error(Object message, Throwable t) {
        if (message != null) {
            getLogger().error(String.valueOf(message), t);
        }
    }

    /**
     * Logs a message with <code>org.apache.log.Priority.FATAL_ERROR</code>.
     *
     * @param message to log
     * @see org.apache.commons.logging.Log#fatal(Object)
     */
    public void fatal(Object message) {
        if (message != null) {
            getLogger().fatalError(String.valueOf(message));
        }
    }

    /**
     * Logs a message with <code>org.apache.log.Priority.FATAL_ERROR</code>.
     *
     * @param message to log
     * @param t log this cause
     * @see org.apache.commons.logging.Log#fatal(Object, Throwable)
     */
    public void fatal(Object message, Throwable t) {
        if (message != null) {
            getLogger().fatalError(String.valueOf(message), t);
        }
    }

    /**
     * Checks whether the <code>LogKit</code> logger will log messages of priority <code>DEBUG</code>.
     */
    public boolean isDebugEnabled() {
        return getLogger().isDebugEnabled();
    }

    /**
     * Checks whether the <code>LogKit</code> logger will log messages of priority <code>ERROR</code>.
     */
    public boolean isErrorEnabled() {
        return getLogger().isErrorEnabled();
    }

    /**
     * Checks whether the <code>LogKit</code> logger will log messages of priority <code>FATAL_ERROR</code>.
     */
    public boolean isFatalEnabled() {
        return getLogger().isFatalErrorEnabled();
    }

    /**
     * Checks whether the <code>LogKit</code> logger will log messages of priority <code>INFO</code>.
     */
    public boolean isInfoEnabled() {
        return getLogger().isInfoEnabled();
    }

    /**
     * Checks whether the <code>LogKit</code> logger will log messages of priority <code>DEBUG</code>.
     */
    public boolean isTraceEnabled() {
        return getLogger().isDebugEnabled();
    }

    /**
     * Checks whether the <code>LogKit</code> logger will log messages of priority <code>WARN</code>.
     */
    public boolean isWarnEnabled() {
        return getLogger().isWarnEnabled();
    }
}
