package org.apache.camel.management;

import java.util.Map;

import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorType;
import org.apache.camel.spi.ErrorHandlerWrappingStrategy;
import org.apache.camel.spi.RouteContext;

/**
 * @version $Revision: 673837 $
 */
public class InstrumentationErrorHandlerWrappingStrategy implements
        ErrorHandlerWrappingStrategy {

    private Map<ProcessorType, PerformanceCounter> counterMap;

    public InstrumentationErrorHandlerWrappingStrategy(
            Map<ProcessorType, PerformanceCounter> counterMap) {
        this.counterMap = counterMap;
    }

    public Processor wrapProcessorInErrorHandler(RouteContext routeContext, ProcessorType processorType,
                                                 Processor target) throws Exception {

        if (counterMap.containsKey(processorType)) {
            return processorType.getErrorHandlerBuilder().createErrorHandler(routeContext, target);
        }

        return target;
    }

}
