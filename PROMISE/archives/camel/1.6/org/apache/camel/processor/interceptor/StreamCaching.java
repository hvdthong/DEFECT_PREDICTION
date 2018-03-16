package org.apache.camel.processor.interceptor;

import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorType;
import org.apache.camel.spi.InterceptStrategy;
import org.apache.camel.spi.RouteContext;

/**
 * {@link InterceptStrategy} implementation to configure stream caching on a RouteContext
 */
public final class StreamCaching implements InterceptStrategy {
    
    /*
     * Hide constructor -- instances will be created through static enable() methods
     */
    private StreamCaching() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Processor wrapProcessorInInterceptors(ProcessorType processorType, Processor target) throws Exception {
        return new StreamCachingInterceptor(target);
    }
    
    /**
     * Enable stream caching for a RouteContext
     * 
     * @param context the route context
     */
    public static void enable(RouteContext context) {
        for (InterceptStrategy strategy : context.getInterceptStrategies()) {
            if (strategy instanceof StreamCaching) {
                return;
            }
        }
        context.addInterceptStrategy(new StreamCaching());
    }
}
