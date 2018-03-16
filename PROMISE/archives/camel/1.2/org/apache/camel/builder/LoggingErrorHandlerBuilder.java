package org.apache.camel.builder;

import org.apache.camel.Processor;
import org.apache.camel.processor.Logger;
import org.apache.camel.processor.LoggingErrorHandler;
import org.apache.camel.processor.LoggingLevel;
import org.apache.camel.processor.ErrorHandlerSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Uses the {@link Logger} as an error handler
 *
 * @version $Revision: 564295 $
 */
public class LoggingErrorHandlerBuilder extends ErrorHandlerBuilderSupport {
    private Log log = LogFactory.getLog(Logger.class);
    private LoggingLevel level = LoggingLevel.INFO;

    public LoggingErrorHandlerBuilder() {
    }

    public LoggingErrorHandlerBuilder(Log log) {
        this.log = log;
    }

    public LoggingErrorHandlerBuilder(Log log, LoggingLevel level) {
        this.log = log;
        this.level = level;
    }

    public ErrorHandlerBuilder copy() {
        LoggingErrorHandlerBuilder answer = new LoggingErrorHandlerBuilder();
        answer.setLog(getLog());
        answer.setLevel(getLevel());
        return answer;
    }

    public Processor createErrorHandler(Processor processor) {
        LoggingErrorHandler handler = new LoggingErrorHandler(processor, log, level);
        configure(handler);
        return handler;
    }

    public LoggingLevel getLevel() {
        return level;
    }

    public void setLevel(LoggingLevel level) {
        this.level = level;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }
}
