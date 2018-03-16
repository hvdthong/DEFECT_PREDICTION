package org.apache.camel.management;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.processor.DelegateProcessor;
import org.apache.camel.util.AsyncProcessorHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JMX enabled processor that uses the {@link Counter} for instrumenting
 * processing of exchanges.
 *
 * @version $Revision: 711235 $
 */
public class InstrumentationProcessor extends DelegateProcessor implements AsyncProcessor {

    private static final transient Log LOG = LogFactory.getLog(InstrumentationProcessor.class);
    private PerformanceCounter counter;

    public InstrumentationProcessor(PerformanceCounter counter) {
        this.counter = counter;
    }

    public InstrumentationProcessor() {
    }
    
    @Override
    public String toString() {
        return "Instrumentation(" + processor + ")";
    }

    public void setCounter(PerformanceCounter counter) {
        this.counter = counter;
    }

    public void process(Exchange exchange) throws Exception {
        AsyncProcessorHelper.process(this, exchange);
    }

    public boolean process(final Exchange exchange, final AsyncCallback callback) {
        if (processor == null) {
            callback.done(true);
            return true;
        }

        final long startTime = System.nanoTime();

        if (processor instanceof AsyncProcessor) {
            return ((AsyncProcessor)processor).process(exchange, new AsyncCallback() {
                public void done(boolean doneSynchronously) {
                    if (counter != null) {
                        recordTime(exchange, (System.nanoTime() - startTime) / 1000000.0);
                    }
                    callback.done(doneSynchronously);
                }
            });
        }

        try {
            processor.process(exchange);
        } catch (Throwable e) {
            exchange.setException(e);
        }

        if (counter != null) {
            recordTime(exchange, (System.nanoTime() - startTime) / 1000000.0);
        }
        callback.done(true);
        return true;
    }

    protected void recordTime(Exchange exchange, double duration) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Recording duration: " + duration + " millis for exchange: " + exchange);
        }

        if (!exchange.isFailed() && exchange.getException() == null) {
            counter.completedExchange(duration);
        } else {
            counter.failedExchange();
        }
    }

}
