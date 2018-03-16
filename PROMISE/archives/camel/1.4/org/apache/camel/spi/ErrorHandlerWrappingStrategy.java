package org.apache.camel.spi;

import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorType;

/**
 * The purpose of this interface is to allow an implementation to
 * provide custom logic to wrap a processor with error handler
 * @version $Revision: 673837 $
 */
public interface ErrorHandlerWrappingStrategy {

    /**
     * This method is invoked by
     * {@link ProcessorType#wrapProcessor(RouteContext, Processor)
     * to give the implementor an opportunity to wrap the target processor
     * in a route.
     *
     * @param processorType the object that invokes this method
     * @param target the processor to be wrapped
     * @return processor wrapped with an interceptor or not wrapped
     * @throws Exception
     */
    Processor wrapProcessorInErrorHandler(RouteContext routeContext, ProcessorType processorType,
                                          Processor target) throws Exception;

}
