package org.apache.camel.builder;

import java.util.List;

import org.apache.camel.Processor;
import org.apache.camel.model.ExceptionType;
import org.apache.camel.spi.RouteContext;
import org.apache.camel.util.ObjectHelper;

/**
 * Represents a proxy to an error handler builder which is resolved by named reference
 *
 * @version $Revision: 674383 $
 */
public class ErrorHandlerBuilderRef extends ErrorHandlerBuilderSupport {
    private final String ref;
    private ErrorHandlerBuilder handler;

    public ErrorHandlerBuilderRef(String ref) {
        this.ref = ref;
    }

    public ErrorHandlerBuilder copy() {
        return new ErrorHandlerBuilderRef(ref);
    }

    @Override
    public void addErrorHandlers(ExceptionType exception) {
        if (handler != null) {
            handler.addErrorHandlers(exception);
        }
        super.addErrorHandlers(exception);
    }

    public Processor createErrorHandler(RouteContext routeContext, Processor processor) throws Exception {
        if (handler == null) {
            handler = routeContext.lookup(ref, ErrorHandlerBuilder.class);
            ObjectHelper.notNull(handler, "error handler '" + ref + "'");
            List<ExceptionType> list = getExceptions();
            for (ExceptionType exceptionType : list) {
                handler.addErrorHandlers(exceptionType);
            }
        }
        return handler.createErrorHandler(routeContext, processor);
    }
}
