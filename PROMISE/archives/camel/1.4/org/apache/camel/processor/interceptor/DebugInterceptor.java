package org.apache.camel.processor.interceptor;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorType;
import org.apache.camel.processor.DelegateProcessor;

/**
 * An interceptor for debugging and tracing routes
 *
 * @version $Revision: 664245 $
 */
public class DebugInterceptor extends DelegateProcessor {
    private final ProcessorType node;
    private final List<Exchange> exchanges;
    private final List<ExceptionEvent> exceptions;
    private Predicate traceFilter;
    private Breakpoint breakpoint = new Breakpoint();
    private boolean traceExceptions = true;

    public DebugInterceptor(ProcessorType node, Processor target, List<Exchange> exchanges, List<ExceptionEvent> exceptions) {
        super(target);
        this.node = node;
        this.exchanges = exchanges;
        this.exceptions = exceptions;
    }

    @Override
    public String toString() {
        return "DebugInterceptor[" + node + "]";
    }

    public void process(Exchange exchange) throws Exception {
        checkForBreakpoint(exchange);
        addTraceExchange(exchange);
        try {
            super.proceed(exchange);
        } catch (Exception e) {
            onException(exchange, e);
            throw e;
        } catch (Error e) {
            onException(exchange, e);
            throw e;
        }
    }

    public ProcessorType getNode() {
        return node;
    }

    public List<Exchange> getExchanges() {
        return exchanges;
    }

    public List<ExceptionEvent> getExceptions() {
        return exceptions;
    }

    public Breakpoint getBreakpoint() {
        return breakpoint;
    }

    public Predicate getTraceFilter() {
        return traceFilter;
    }

    public void setTraceFilter(Predicate traceFilter) {
        this.traceFilter = traceFilter;
    }

    public boolean isTraceExceptions() {
        return traceExceptions;
    }

    public void setTraceExceptions(boolean traceExceptions) {
        this.traceExceptions = traceExceptions;
    }

    /**
     * Stategy method to wait for a breakpoint if one is set
     */
    protected void checkForBreakpoint(Exchange exchange) {
        breakpoint.waitForBreakpoint(exchange);
    }

    /**
     * Fired when an exception is thrown when processing the underlying processor
     */
    protected void onException(Exchange exchange, Throwable e) {
        if (shouldTraceExceptionEvents(exchange, e))  {
            exceptions.add(new ExceptionEvent(this, exchange, e));
        }

    }

    private boolean shouldTraceExceptionEvents(Exchange exchange, Throwable e) {
        return isTraceExceptions();
    }

    /**
     * Strategy method to store the exchange in a trace log if it is enabled
     */
    protected void addTraceExchange(Exchange exchange) {
        if (shouldTraceExchange(exchange)) {
            exchanges.add(copyExchange(exchange));
        }
    }

    protected Exchange copyExchange(Exchange previousExchange) {
        Exchange answer = previousExchange.newInstance();
        answer.getProperties().putAll(previousExchange.getProperties());
        answer.getIn().copyFrom(previousExchange.getIn());

        Message previousOut = previousExchange.getOut(false);
        if (previousOut != null) {
            answer.getOut().copyFrom(previousOut);
        }
        return answer;
    }

    /**
     * Returns true if the given exchange should be logged in the trace list
     */
    protected boolean shouldTraceExchange(Exchange exchange) {
        return traceFilter == null || traceFilter.matches(exchange);
    }
}
