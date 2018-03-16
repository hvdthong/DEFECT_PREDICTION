package org.apache.commons.logging.impl;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogRecord;
import java.util.StringTokenizer;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.logging.Log;

/**
 * Implementation of the <code>org.apache.commons.logging.Log</code>
 * interface that wraps the standard JDK logging mechanisms that are
 * available in SourceForge's Lumberjack for JDKs prior to 1.4.
 *
 * @version $Id: Jdk13LumberjackLogger.java 1432663 2013-01-13 17:24:18Z tn $
 * @since 1.1
 */
public class Jdk13LumberjackLogger implements Log, Serializable {

    /** Serializable version identifier. */
    private static final long serialVersionUID = -8649807923527610591L;


    /**
     * The underlying Logger implementation we are using.
     */
    protected transient Logger logger = null;
    protected String name = null;
    private String sourceClassName = "unknown";
    private String sourceMethodName = "unknown";
    private boolean classAndMethodFound = false;

    /**
     * This member variable simply ensures that any attempt to initialise
     * this class in a pre-1.4 JVM will result in an ExceptionInInitializerError.
     * It must not be private, as an optimising compiler could detect that it
     * is not used and optimise it away.
     */
    protected static final Level dummyLevel = Level.FINE;


    /**
     * Construct a named instance of this Logger.
     *
     * @param name Name of the logger to be constructed
     */
    public Jdk13LumberjackLogger(String name) {
        this.name = name;
        logger = getLogger();
    }


    private void log( Level level, String msg, Throwable ex ) {
        if( getLogger().isLoggable(level) ) {
            LogRecord record = new LogRecord(level, msg);
            if( !classAndMethodFound ) {
                getClassAndMethod();
            }
            record.setSourceClassName(sourceClassName);
            record.setSourceMethodName(sourceMethodName);
            if( ex != null ) {
                record.setThrown(ex);
            }
            getLogger().log(record);
        }
    }

    /**
     * Gets the class and method by looking at the stack trace for the
     * first entry that is not this class.
     */
    private void getClassAndMethod() {
        try {
            Throwable throwable = new Throwable();
            throwable.fillInStackTrace();
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter( stringWriter );
            throwable.printStackTrace( printWriter );
            String traceString = stringWriter.getBuffer().toString();
            StringTokenizer tokenizer =
                new StringTokenizer( traceString, "\n" );
            tokenizer.nextToken();
            String line = tokenizer.nextToken();
            while ( line.indexOf( this.getClass().getName() )  == -1 ) {
                line = tokenizer.nextToken();
            }
            while ( line.indexOf( this.getClass().getName() ) >= 0 ) {
                line = tokenizer.nextToken();
            }
            int start = line.indexOf( "at " ) + 3;
            int end = line.indexOf( '(' );
            String temp = line.substring( start, end );
            int lastPeriod = temp.lastIndexOf( '.' );
            sourceClassName = temp.substring( 0, lastPeriod );
            sourceMethodName = temp.substring( lastPeriod + 1 );
        } catch ( Exception ex ) {
        }
        classAndMethodFound = true;
    }

    /**
     * Logs a message with <code>java.util.logging.Level.FINE</code>.
     *
     * @param message to log
     * @see org.apache.commons.logging.Log#debug(Object)
     */
    public void debug(Object message) {
        log(Level.FINE, String.valueOf(message), null);
    }

    /**
     * Logs a message with <code>java.util.logging.Level.FINE</code>.
     *
     * @param message to log
     * @param exception log this cause
     * @see org.apache.commons.logging.Log#debug(Object, Throwable)
     */
    public void debug(Object message, Throwable exception) {
        log(Level.FINE, String.valueOf(message), exception);
    }

    /**
     * Logs a message with <code>java.util.logging.Level.SEVERE</code>.
     *
     * @param message to log
     * @see org.apache.commons.logging.Log#error(Object)
     */
    public void error(Object message) {
        log(Level.SEVERE, String.valueOf(message), null);
    }

    /**
     * Logs a message with <code>java.util.logging.Level.SEVERE</code>.
     *
     * @param message to log
     * @param exception log this cause
     * @see org.apache.commons.logging.Log#error(Object, Throwable)
     */
    public void error(Object message, Throwable exception) {
        log(Level.SEVERE, String.valueOf(message), exception);
    }

    /**
     * Logs a message with <code>java.util.logging.Level.SEVERE</code>.
     *
     * @param message to log
     * @see org.apache.commons.logging.Log#fatal(Object)
     */
    public void fatal(Object message) {
        log(Level.SEVERE, String.valueOf(message), null);
    }

    /**
     * Logs a message with <code>java.util.logging.Level.SEVERE</code>.
     *
     * @param message to log
     * @param exception log this cause
     * @see org.apache.commons.logging.Log#fatal(Object, Throwable)
     */
    public void fatal(Object message, Throwable exception) {
        log(Level.SEVERE, String.valueOf(message), exception);
    }

    /**
     * Return the native Logger instance we are using.
     */
    public Logger getLogger() {
        if (logger == null) {
            logger = Logger.getLogger(name);
        }
        return logger;
    }

    /**
     * Logs a message with <code>java.util.logging.Level.INFO</code>.
     *
     * @param message to log
     * @see org.apache.commons.logging.Log#info(Object)
     */
    public void info(Object message) {
        log(Level.INFO, String.valueOf(message), null);
    }

    /**
     * Logs a message with <code>java.util.logging.Level.INFO</code>.
     *
     * @param message to log
     * @param exception log this cause
     * @see org.apache.commons.logging.Log#info(Object, Throwable)
     */
    public void info(Object message, Throwable exception) {
        log(Level.INFO, String.valueOf(message), exception);
    }

    /**
     * Is debug logging currently enabled?
     */
    public boolean isDebugEnabled() {
        return getLogger().isLoggable(Level.FINE);
    }

    /**
     * Is error logging currently enabled?
     */
    public boolean isErrorEnabled() {
        return getLogger().isLoggable(Level.SEVERE);
    }

    /**
     * Is fatal logging currently enabled?
     */
    public boolean isFatalEnabled() {
        return getLogger().isLoggable(Level.SEVERE);
    }

    /**
     * Is info logging currently enabled?
     */
    public boolean isInfoEnabled() {
        return getLogger().isLoggable(Level.INFO);
    }

    /**
     * Is trace logging currently enabled?
     */
    public boolean isTraceEnabled() {
        return getLogger().isLoggable(Level.FINEST);
    }

    /**
     * Is warn logging currently enabled?
     */
    public boolean isWarnEnabled() {
        return getLogger().isLoggable(Level.WARNING);
    }

    /**
     * Logs a message with <code>java.util.logging.Level.FINEST</code>.
     *
     * @param message to log
     * @see org.apache.commons.logging.Log#trace(Object)
     */
    public void trace(Object message) {
        log(Level.FINEST, String.valueOf(message), null);
    }

    /**
     * Logs a message with <code>java.util.logging.Level.FINEST</code>.
     *
     * @param message to log
     * @param exception log this cause
     * @see org.apache.commons.logging.Log#trace(Object, Throwable)
     */
    public void trace(Object message, Throwable exception) {
        log(Level.FINEST, String.valueOf(message), exception);
    }

    /**
     * Logs a message with <code>java.util.logging.Level.WARNING</code>.
     *
     * @param message to log
     * @see org.apache.commons.logging.Log#warn(Object)
     */
    public void warn(Object message) {
        log(Level.WARNING, String.valueOf(message), null);
    }

    /**
     * Logs a message with <code>java.util.logging.Level.WARNING</code>.
     *
     * @param message to log
     * @param exception log this cause
     * @see org.apache.commons.logging.Log#warn(Object, Throwable)
     */
    public void warn(Object message, Throwable exception) {
        log(Level.WARNING, String.valueOf(message), exception);
    }
}
