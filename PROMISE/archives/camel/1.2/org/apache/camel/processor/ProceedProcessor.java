package org.apache.camel.processor;

import org.apache.camel.Processor;
import org.apache.camel.Exchange;
import org.apache.camel.processor.Interceptor;

/**
 * A {@link Processor} which proceeds on an {@link Interceptor}
 *
 * @version $Revision: 1.1 $
 */
public class ProceedProcessor implements Processor {
    private final Interceptor interceptor;

    public ProceedProcessor(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    public String toString() {
        return "Proceed[" + interceptor + "]";
    }

    public void process(Exchange exchange) throws Exception {
        interceptor.proceed(exchange);
    }
}
