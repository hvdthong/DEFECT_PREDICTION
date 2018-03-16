package org.apache.camel.management;

import org.apache.camel.Exchange;
import org.apache.camel.processor.DelegateProcessor;

public class InstrumentationProcessor extends DelegateProcessor {

    private PerformanceCounter counter;
    
    InstrumentationProcessor(PerformanceCounter counter) {
    	this.counter = counter;
    }
    
    public void process(Exchange exchange) throws Exception {
    	long startTime = System.nanoTime();
        super.process(exchange);
        if (counter != null) {
            if (exchange.getException() == null) {
            	counter.completedExchange((System.nanoTime() - startTime) / 1000);
            }
            else {
            	counter.completedExchange();
            }
        }
    }
}
