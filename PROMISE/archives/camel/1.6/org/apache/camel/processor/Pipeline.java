package org.apache.camel.processor;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.impl.converter.AsyncProcessorTypeConverter;
import org.apache.camel.util.AsyncProcessorHelper;
import org.apache.camel.util.ExchangeHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Creates a Pipeline pattern where the output of the previous step is sent as
 * input to the next step, reusing the same message exchanges
 *
 * @version $Revision: 706857 $
 */
public class Pipeline extends MulticastProcessor implements AsyncProcessor {
    private static final transient Log LOG = LogFactory.getLog(Pipeline.class);

    public Pipeline(Collection<Processor> processors) {
        super(processors);
    }

    public static Processor newInstance(List<Processor> processors) {
        if (processors.isEmpty()) {
            return null;
        } else if (processors.size() == 1) {
            return processors.get(0);
        }
        return new Pipeline(processors);
    }

    public void process(Exchange exchange) throws Exception {
        AsyncProcessorHelper.process(this, exchange);
    }

    public boolean process(Exchange original, AsyncCallback callback) {
        Iterator<Processor> processors = getProcessors().iterator();
        Exchange nextExchange = original;
        boolean first = true;
        while (true) {
            boolean exceptionHandled = hasExceptionBeenHandled(nextExchange);
            if (nextExchange.isFailed() || exceptionHandled) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Message exchange has failed so breaking out of pipeline: " + nextExchange
                              + " exception: " + nextExchange.getException() + " fault: "
                              + nextExchange.getFault(false)
                              + (exceptionHandled ? " handled by the error handler" : ""));
                }
                break;
            }
            if (!processors.hasNext()) {
                break;
            }

            AsyncProcessor processor = AsyncProcessorTypeConverter.convert(processors.next());

            if (first) {
                first = false;
            } else {
                nextExchange = createNextExchange(processor, nextExchange);
            }

            boolean sync = process(original, nextExchange, callback, processors, processor);
            if (!sync) {
                return false;
            }
        }

        if (LOG.isTraceEnabled()) {
            LOG.trace("Processing compelete for exchangeId: " + original.getExchangeId() + " >>> " + nextExchange);
        }
        ExchangeHelper.copyResults(original, nextExchange);
        callback.done(true);
        return true;
    }

    private boolean process(final Exchange original, final Exchange exchange, final AsyncCallback callback, final Iterator<Processor> processors, AsyncProcessor processor) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Processing exchangeId: " + exchange.getExchangeId() + " >>> " + exchange);
        }
        return processor.process(exchange, new AsyncCallback() {
            public void done(boolean sync) {
                if (sync) {
                    return;
                }

                Exchange nextExchange = exchange;
                while (processors.hasNext()) {
                    AsyncProcessor processor = AsyncProcessorTypeConverter.convert(processors.next());

                    boolean exceptionHandled = hasExceptionBeenHandled(nextExchange);
                    if (nextExchange.isFailed() || exceptionHandled) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Message exchange has failed so breaking out of pipeline: " + nextExchange
                                      + " exception: " + nextExchange.getException() + " fault: "
                                      + nextExchange.getFault(false)
                                      + (exceptionHandled ? " handled by the error handler" : ""));
                        }
                        break;
                    }

                    nextExchange = createNextExchange(processor, nextExchange);
                    sync = process(original, nextExchange, callback, processors, processor);
                    if (!sync) {
                        return;
                    }
                }

                ExchangeHelper.copyResults(original, nextExchange);
                callback.done(false);
            }
        });
    }


    private static boolean hasExceptionBeenHandled(Exchange nextExchange) {
        return Boolean.TRUE.equals(nextExchange.getProperty(Exchange.EXCEPTION_HANDLED_PROPERTY));
    }

    /**
     * Strategy method to create the next exchange from the previous exchange.
     * <p/>
     * Remember to copy the original exchange id otherwise correlation of ids in the log is a problem
     *
     * @param producer         the producer used to send to the endpoint
     * @param previousExchange the previous exchange
     * @return a new exchange
     */
    protected Exchange createNextExchange(Processor producer, Exchange previousExchange) {
        Exchange answer = previousExchange.newInstance();
        answer.setExchangeId(previousExchange.getExchangeId());

        answer.getProperties().putAll(previousExchange.getProperties());

        Message previousOut = previousExchange.getOut(false);
        Message in = answer.getIn();
        if (previousOut != null) {
            in.copyFrom(previousOut);
        } else {
            in.copyFrom(previousExchange.getIn());
        }
        return answer;
    }

    @Override
    public String toString() {
        return "Pipeline" + getProcessors();
    }
}
