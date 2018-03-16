package org.apache.ivy.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * A {@link MessageLogger} implementation delegating the work to the current top logger on a stack.
 * <p>
 * When the logger stack is empty, it delegates the work to a default logger, which by default is
 * the {@link Message#getDefaultLogger()}.
 * </p>
 * <p>
 * {@link #pushLogger(MessageLogger)} should be called to delegate to a new logger, and
 * {@link #popLogger()} should be called when the context of this logger is finished.
 * </p>
 */
public class MessageLoggerEngine implements MessageLogger {
    private final Stack/*<MessageLogger>*/ loggerStack = new Stack();
    
    private MessageLogger defaultLogger = null;

    private List problems = new ArrayList();

    private List warns = new ArrayList();

    private List errors = new ArrayList();
    
    public MessageLoggerEngine() {
    }

    /**
     * Sets the logger used when the stack is empty.
     * 
     * @param defaultLogger the logger to use when the stack is empty.
     */
    public void setDefaultLogger(MessageLogger defaultLogger) {
        this.defaultLogger = defaultLogger;
    }



    /**
     * Push a logger on the stack.
     * 
     * @param logger
     *            the logger to push. Must not be <code>null</code>.
     */
    public void pushLogger(MessageLogger logger) {
        Checks.checkNotNull(logger, "logger");
        loggerStack.push(logger);
    }
    
    /**
     * Pops a logger from the logger stack.
     * <p>
     * Does nothing if the logger stack is empty
     * </p>
     */
    public void popLogger() {
        if (!loggerStack.isEmpty()) {
            loggerStack.pop();
        }
    }

    /**
     * Returns the current logger, or the default one if there is no logger in the stack
     * @return the current logger, or the default one if there is no logger in the stack
     */
    private MessageLogger peekLogger() {
        if (loggerStack.isEmpty()) {
            return getDefaultLogger();
        }
        return (MessageLogger) loggerStack.peek();
    }

    private MessageLogger getDefaultLogger() {
        return defaultLogger == null ? Message.getDefaultLogger() : defaultLogger;
    }

    public void warn(String msg) {
        peekLogger().warn(msg);
        problems.add("WARN:  " + msg);
        warns.add(msg);
    }
    
    public void error(String msg) {
        peekLogger().error(msg);
        problems.add("\tERROR: " + msg);
        errors.add(msg);
    }

    public List getErrors() {
        return errors;
    }

    public List getProblems() {
        return problems;
    }

    public List getWarns() {
        return warns;
    }

    public void sumupProblems() {
        MessageLoggerHelper.sumupProblems(this);
        clearProblems();
    }
    
    public void clearProblems() {
        getDefaultLogger().clearProblems();
        for (Iterator iter = loggerStack.iterator(); iter.hasNext();) {
            MessageLogger l = (MessageLogger) iter.next();
            l.clearProblems();
        }
        problems.clear();
        errors.clear();
        warns.clear();
    }

    public void setShowProgress(boolean progress) {
        getDefaultLogger().setShowProgress(progress);
        for (Iterator iter = loggerStack.iterator(); iter.hasNext();) {
            MessageLogger l = (MessageLogger) iter.next();
            l.setShowProgress(progress);
        }
    }
    
    public boolean isShowProgress() {
        return getDefaultLogger().isShowProgress();
    }

    
    public void debug(String msg) {
        peekLogger().debug(msg);
    }
    
    public void deprecated(String msg) {
        peekLogger().deprecated(msg);
    }

    public void endProgress() {
        peekLogger().endProgress();
    }

    public void endProgress(String msg) {
        peekLogger().endProgress(msg);
    }

    public void info(String msg) {
        peekLogger().info(msg);
    }

    public void rawinfo(String msg) {
        peekLogger().rawinfo(msg);
    }

    public void log(String msg, int level) {
        peekLogger().log(msg, level);
    }

    public void progress() {
        peekLogger().progress();
    }

    public void rawlog(String msg, int level) {
        peekLogger().rawlog(msg, level);
    }

    public void verbose(String msg) {
        peekLogger().verbose(msg);
    }

    
}
