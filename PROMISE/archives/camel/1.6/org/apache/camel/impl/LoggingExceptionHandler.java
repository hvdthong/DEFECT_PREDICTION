package org.apache.camel.impl;

import org.apache.camel.model.LoggingLevel;
import org.apache.camel.processor.Logger;
import org.apache.camel.spi.ExceptionHandler;
import org.apache.commons.logging.LogFactory;

/**
 * A default implementation of {@link ExceptionHandler} which uses a {@link Logger} to
 * log to an arbitrary {@link org.apache.commons.logging.Log Log} with some {@link LoggingLevel}
 *
 * @version $Revision: 710133 $
 */
public class LoggingExceptionHandler implements ExceptionHandler {
    private final Logger logger;

    public LoggingExceptionHandler(Class ownerType) {
        this(new Logger(LogFactory.getLog(ownerType), LoggingLevel.ERROR));
    }

    public LoggingExceptionHandler(Logger logger) {
        this.logger = logger;
    }

    public void handleException(Throwable exception) {
        logger.log(exception.getMessage(), exception);
    }
}
