package org.apache.ivy.core;



public class LogOptions {
    /**
     * Defaults log settings. Output all usual messages during the resolve process.
     */
    public static final String LOG_DEFAULT = "default"; 
    /**
     * This log setting disable all usual messages but download ones.
     */
    public static final String LOG_DOWNLOAD_ONLY = "download-only";
    /**
     * This log setting disable all usual messages during the resolve process. 
     */
    public static final String LOG_QUIET = "quiet"; 
    
    /**
     * The log settings to use.
     * One of {@link #LOG_DEFAULT}, {@link #LOG_DOWNLOAD_ONLY}, {@link #LOG_QUIET}
     */
    private String log = LOG_DEFAULT;

    public LogOptions() {
    }

    public LogOptions(LogOptions options) {
        log = options.log;
    }

    public String getLog() {
        return log;
    }

    public LogOptions setLog(String log) {
        this.log = log;
        return this;
    }
}
