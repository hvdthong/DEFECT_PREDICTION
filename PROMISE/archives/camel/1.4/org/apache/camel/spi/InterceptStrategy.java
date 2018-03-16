package org.apache.camel.spi;

import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorType;

 /**
  * The purpose of this interface is to allow an implementation to wrap
  * processors in a route with interceptors.  For example, a possible
  * usecase is to gather performance statistics at the processor's level.
  *
  * @version $Revision: 657300 $
  */
public interface InterceptStrategy {

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
    Processor wrapProcessorInInterceptors(ProcessorType processorType,
            Processor target) throws Exception;
}
