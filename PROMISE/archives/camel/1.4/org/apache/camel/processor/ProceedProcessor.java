package org.apache.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * A {@link Processor} which proceeds on an {@link Interceptor}
 *
 * @version $Revision: 640438 $
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
