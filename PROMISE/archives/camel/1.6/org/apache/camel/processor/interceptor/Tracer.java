package org.apache.camel.processor.interceptor;

import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.LoggingLevel;
import org.apache.camel.model.ProcessorType;
import org.apache.camel.spi.InterceptStrategy;

/**
 * An interceptor strategy for tracing routes
 *
 * @version $Revision: 719456 $
 */
public class Tracer implements InterceptStrategy {

    private TraceFormatter formatter = new TraceFormatter();
    private boolean enabled = true;
    private String logName;
    private LoggingLevel logLevel;
    private Predicate<Exchange> traceFilter;
    private boolean traceInterceptors;
    private boolean traceExceptions = true;
    private boolean traceOutExchanges;
    
    /**
     * A helper method to return the Tracer instance for a given {@link CamelContext} if one is enabled
     *
     * @param context the camel context the tracer is connected to
     * @return the tracer or null if none can be found
     */
    public static Tracer getTracer(CamelContext context) {
        if (context instanceof DefaultCamelContext) {
            DefaultCamelContext defaultCamelContext = (DefaultCamelContext) context;
            List<InterceptStrategy> list = defaultCamelContext.getInterceptStrategies();
            for (InterceptStrategy interceptStrategy : list) {
                if (interceptStrategy instanceof Tracer) {
                    return (Tracer)interceptStrategy;
                }
            }
        }
        return null;
    }

    public Processor wrapProcessorInInterceptors(ProcessorType processorType, Processor target) throws Exception {
        String id = processorType.idOrCreate();
        return new TraceInterceptor(processorType, target, this);
    }

    public TraceFormatter getFormatter() {
        return formatter;
    }

    public void setFormatter(TraceFormatter formatter) {
        this.formatter = formatter;
    }

    public void setEnabled(boolean flag) {
        enabled = flag;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isTraceInterceptors() {
        return traceInterceptors;
    }

    /**
     * Sets whether interceptors should be traced or not
     */
    public void setTraceInterceptors(boolean traceInterceptors) {
        this.traceInterceptors = traceInterceptors;
    }

    public Predicate getTraceFilter() {
        return traceFilter;
    }

    /**
     * Sets a predicate to be used as filter when tracing
     */
    public void setTraceFilter(Predicate traceFilter) {
        this.traceFilter = traceFilter;
    }

    public LoggingLevel getLogLevel() {
        return logLevel;
    }

    /**
     * Sets the logging level to output tracing. Will use <tt>INFO</tt> level by default.
     */
    public void setLogLevel(LoggingLevel logLevel) {
        this.logLevel = logLevel;
    }

    public boolean isTraceExceptions() {
        return traceExceptions;
    }

    /**
     * Sets whether thrown exceptions should be traced
     */
    public void setTraceExceptions(boolean traceExceptions) {
        this.traceExceptions = traceExceptions;
    }

    public String getLogName() {
        return logName;
    }

    /**
     * Sets the logging name to use.
     * Will default use <tt>org.apache.camel.processor.interceptor.TraceInterceptor<tt>.
     */
    public void setLogName(String logName) {
        this.logName = logName;
    }

    /**
     * Sets whether exchanges coming out of processors should be traced
     */    
    public void setTraceOutExchanges(boolean traceOutExchanges) {
        this.traceOutExchanges = traceOutExchanges;
    }
    
    public boolean isTraceOutExchanges() {
        return traceOutExchanges;
    }
}
