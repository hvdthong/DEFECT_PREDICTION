package org.apache.ivy.util;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract base class to ease {@link MessageLogger} implementation.
 */
public abstract class AbstractMessageLogger implements MessageLogger {
    private List problems = new ArrayList();

    private List warns = new ArrayList();

    private List errors = new ArrayList();

    private boolean showProgress = true;
    
    /* (non-Javadoc)
     * @see org.apache.ivy.util.MessageLogger#debug(java.lang.String)
     */
    public void debug(String msg) {
        log(msg, Message.MSG_DEBUG);
    }

    /* (non-Javadoc)
     * @see org.apache.ivy.util.MessageLogger#verbose(java.lang.String)
     */
    public void verbose(String msg) {
        log(msg, Message.MSG_VERBOSE);
    }
    
    /* (non-Javadoc)
     * @see org.apache.ivy.util.MessageLogger#deprecated(java.lang.String)
     */
    public void deprecated(String msg) {
        log("DEPRECATED: " + msg, Message.MSG_WARN);
    }


    /* (non-Javadoc)
     * @see org.apache.ivy.util.MessageLogger#info(java.lang.String)
     */
    public void info(String msg) {
        log(msg, Message.MSG_INFO);
    }
    
    /* (non-Javadoc)
     * @see org.apache.ivy.util.MessageLogger#info(java.lang.String)
     */
    public void rawinfo(String msg) {
        rawlog(msg, Message.MSG_INFO);
    }

    /* (non-Javadoc)
     * @see org.apache.ivy.util.MessageLogger#warn(java.lang.String)
     */
    public void warn(String msg) {
        log("WARN: " + msg, Message.MSG_VERBOSE);
        problems.add("WARN:  " + msg);
        getWarns().add(msg);
    }

    /* (non-Javadoc)
     * @see org.apache.ivy.util.MessageLogger#error(java.lang.String)
     */
    public void error(String msg) {
        log("ERROR: " + msg, Message.MSG_VERBOSE);
        problems.add("\tERROR: " + msg);
        getErrors().add(msg);
    }

    /* (non-Javadoc)
     * @see org.apache.ivy.util.MessageLogger#getProblems()
     */
    public List getProblems() {
        return problems;
    }

    /* (non-Javadoc)
     * @see org.apache.ivy.util.MessageLogger#sumupProblems()
     */
    public void sumupProblems() {
        MessageLoggerHelper.sumupProblems(this);
        clearProblems();
    }

    public void clearProblems() {
        problems.clear();
        warns.clear();
        errors.clear();
    }

    public List getErrors() {
        return errors;
    }

    public List getWarns() {
        return warns;
    }

    /* (non-Javadoc)
     * @see org.apache.ivy.util.MessageLogger#progress()
     */
    public void progress() {
        if (showProgress) {
            doProgress();
        }
    }


    /* (non-Javadoc)
     * @see org.apache.ivy.util.MessageLogger#endProgress()
     */
    public void endProgress() {
        endProgress("");
    }

    /* (non-Javadoc)
     * @see org.apache.ivy.util.MessageLogger#endProgress(java.lang.String)
     */
    public void endProgress(String msg) {
        if (showProgress) {
            doEndProgress(msg);
        }
    }


    /* (non-Javadoc)
     * @see org.apache.ivy.util.MessageLogger#isShowProgress()
     */
    public boolean isShowProgress() {
        return showProgress;
    }

    /* (non-Javadoc)
     * @see org.apache.ivy.util.MessageLogger#setShowProgress(boolean)
     */
    public void setShowProgress(boolean progress) {
        showProgress = progress;
    }

    /**
     * Indicates a progression for a long running task
     */
    protected abstract void doProgress();
    /**
     * Indicates the end of a long running task
     * @param msg the message associated with long running task end.
     */
    protected abstract void doEndProgress(String msg);

}
