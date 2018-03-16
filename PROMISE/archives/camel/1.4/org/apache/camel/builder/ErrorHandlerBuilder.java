package org.apache.camel.builder;

import org.apache.camel.Processor;
import org.apache.camel.model.ExceptionType;
import org.apache.camel.spi.RouteContext;

/**
 *
 * @version $Revision: 674383 $
 */
public interface ErrorHandlerBuilder {

    /**
     * Creates a copy of this builder
     */
    ErrorHandlerBuilder copy();

    /**
     * Creates the error handler interceptor
     */
    Processor createErrorHandler(RouteContext routeContext, Processor processor) throws Exception;

    /**
     * Adds error handler for the given exception type
     * @param exception  the exception to handle
     */
    void addErrorHandlers(ExceptionType exception);

}
