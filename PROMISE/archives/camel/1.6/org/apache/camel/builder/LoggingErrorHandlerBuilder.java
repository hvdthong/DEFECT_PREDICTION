package org.apache.camel.builder;

import org.apache.camel.Processor;
import org.apache.camel.model.LoggingLevel;
import org.apache.camel.processor.Logger;
import org.apache.camel.processor.LoggingErrorHandler;
import org.apache.camel.spi.RouteContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Uses the {@link Logger} as an error handler, will log at <tt>ERROR</tt> level by default.
 *
 * @version $Revision: 710133 $
 */
public class LoggingErrorHandlerBuilder extends ErrorHandlerBuilderSupport {
    private Log log = LogFactory.getLog(Logger.class);
    private LoggingLevel level = LoggingLevel.ERROR;

    public LoggingErrorHandlerBuilder() {
    }

    public LoggingErrorHandlerBuilder(final Log log) {
        this.log = log;
    }

    public LoggingErrorHandlerBuilder(final Log log, final LoggingLevel level) {
        this.log = log;
        this.level = level;
    }

    public ErrorHandlerBuilder copy() {
        LoggingErrorHandlerBuilder answer = new LoggingErrorHandlerBuilder();
        answer.setLog(getLog());
        answer.setLevel(getLevel());
        return answer;
    }

    public Processor createErrorHandler(final RouteContext routeContext, final Processor processor) {
        LoggingErrorHandler handler = new LoggingErrorHandler(processor, log, level);
        configure(handler);
        return handler;
    }

    public LoggingLevel getLevel() {
        return level;
    }

    public void setLevel(final LoggingLevel level) {
        this.level = level;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(final Log log) {
        this.log = log;
    }

    public LoggingErrorHandlerBuilder level(final LoggingLevel level) {
        this.level = level;
        return this;
    }

    public LoggingErrorHandlerBuilder log(final Log log) {
        this.log = log;
        return this;
    }

}
