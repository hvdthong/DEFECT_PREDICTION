package org.apache.camel.processor;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.impl.ServiceSupport;
import org.apache.camel.util.ExchangeHelper;
import org.apache.camel.util.ServiceHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implements try/catch/finally type processing
 *
 * @version $Revision: 660275 $
 */
public class TryProcessor extends ServiceSupport implements Processor {
    private static final transient Log LOG = LogFactory.getLog(TryProcessor.class);

    private final Processor tryProcessor;
    private final List<CatchProcessor> catchClauses;
    private final Processor finallyProcessor;

    public TryProcessor(Processor tryProcessor, List<CatchProcessor> catchClauses, Processor finallyProcessor) {
        this.tryProcessor = tryProcessor;
        this.catchClauses = catchClauses;
        this.finallyProcessor = finallyProcessor;
    }

    public String toString() {
        String finallyText = (finallyProcessor == null) ? "" : " Finally {" + finallyProcessor + "}";
        return "Try {" + tryProcessor + "} " + catchClauses + finallyText;
    }

    public void process(Exchange exchange) throws Exception {
        Throwable e = null;
        try {
            tryProcessor.process(exchange);
            e = exchange.getException();

            if (e != null && DeadLetterChannel.isFailureHandled(exchange)) {
                e = null;
            }
        } catch (Throwable ex) {
            e = ex;
            exchange.setException(e);
        }

        Exception unexpected = null;
        try {
            if (e != null) {
                LOG.info("Caught exception while processing exchange.", e);
                handleException(exchange, e);
            }
        } catch (Exception ex) {
            unexpected = ex;
        } catch (Throwable ex) {
            unexpected = new RuntimeCamelException(ex);
        } finally {
            try {
                processFinally(exchange);
            } catch (Exception ex) {
                unexpected = ex;
            } catch (Throwable ex) {
                unexpected = new RuntimeCamelException(ex);
            }
            if (unexpected != null) {
                LOG.warn("Caught exception inside processFinally clause.", unexpected);
                throw unexpected;
            }
        }

        if (unexpected != null) {
            LOG.warn("Caught exception inside handle clause.", unexpected);
            throw unexpected;
        }
    }

    protected void doStart() throws Exception {
        ServiceHelper.startServices(tryProcessor, catchClauses, finallyProcessor);
    }

    protected void doStop() throws Exception {
        ServiceHelper.stopServices(tryProcessor, catchClauses, finallyProcessor);
    }

    protected void handleException(Exchange exchange, Throwable e) throws Throwable {
        for (CatchProcessor catchClause : catchClauses) {
            if (catchClause.catches(e)) {
                Exchange localExchange = exchange.copy();
                localExchange.getIn().setHeader("caught.exception", e);
                localExchange.setException(null);

                catchClause.process(localExchange);
                localExchange.getIn().removeHeader("caught.exception");
                ExchangeHelper.copyResults(exchange, localExchange);
                return;
            }
        }
    }

    protected void processFinally(Exchange exchange) throws Throwable {
        if (finallyProcessor != null) {
            Throwable lastException = exchange.getException();
            exchange.setException(null);

            finallyProcessor.process(exchange);
            if (exchange.getException() == null) {
                exchange.setException(lastException);
            }
        }
    }
}
