package org.apache.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ServiceSupport;
import org.apache.camel.util.ServiceHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An {@link ErrorHandler} which uses commons-logging to dump the error
 * 
 * @version $Revision: 564295 $
 */
public class LoggingErrorHandler extends ErrorHandlerSupport {
    private Processor output;
    private Log log;
    private LoggingLevel level;

    public LoggingErrorHandler(Processor output) {
        this(output, LogFactory.getLog(LoggingErrorHandler.class), LoggingLevel.INFO);
    }

    public LoggingErrorHandler(Processor output, Log log, LoggingLevel level) {
        this.output = output;
        this.log = log;
        this.level = level;
    }

    @Override
    public String toString() {
        return "LoggingErrorHandler[" + output + "]";
    }

    public void process(Exchange exchange) throws Exception {
        try {
            output.process(exchange);
        } catch (Throwable e) {
            if (!customProcessorForException(exchange, e)) {
                logError(exchange, e);
            }
        }
    }


    /**
     * Returns the output processor
     */
    public Processor getOutput() {
        return output;
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

    protected void logError(Exchange exchange, Throwable e) {
        switch (level) {
        case DEBUG:
            if (log.isDebugEnabled()) {
                log.debug(logMessage(exchange, e), e);
            }
            break;
        case ERROR:
            if (log.isErrorEnabled()) {
                log.error(logMessage(exchange, e), e);
            }
            break;
        case FATAL:
            if (log.isFatalEnabled()) {
                log.fatal(logMessage(exchange, e), e);
            }
            break;
        case INFO:
            if (log.isInfoEnabled()) {
                log.debug(logMessage(exchange, e), e);
            }
            break;
        case TRACE:
            if (log.isTraceEnabled()) {
                log.trace(logMessage(exchange, e), e);
            }
            break;
        case WARN:
            if (log.isWarnEnabled()) {
                log.warn(logMessage(exchange, e), e);
            }
            break;
        default:
            log.error("Unknown level: " + level + " when trying to log exchange: " + logMessage(exchange, e),
                      e);
        }
    }

    protected Object logMessage(Exchange exchange, Throwable e) {
        return e + " while processing exchange: " + exchange;
    }

    protected void doStart() throws Exception {
        ServiceHelper.startServices(output);
    }

    protected void doStop() throws Exception {
        ServiceHelper.stopServices(output);
    }
}
