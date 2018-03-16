package org.apache.camel.processor.interceptor;

import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorType;
import org.apache.camel.spi.InterceptStrategy;

/**
 * An interceptor strategy for tracing routes
 *
 * @version $Revision: 675876 $
 */
public class Tracer implements InterceptStrategy {

    private TraceFormatter formatter = new TraceFormatter();

    public Processor wrapProcessorInInterceptors(ProcessorType processorType, Processor target) throws Exception {
        String id = processorType.idOrCreate();
        return new TraceInterceptor(processorType, target, formatter);
    }

    public TraceFormatter getFormatter() {
        return formatter;
    }

    public void setFormatter(TraceFormatter formatter) {
        this.formatter = formatter;
    }
}
