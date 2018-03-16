package org.apache.camel.management;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description = "Counter", currencyTimeLimit = 15)
public class Counter {

    protected AtomicLong numExchanges = new AtomicLong(0L);

    @ManagedOperation(description = "Reset counters")
    public void reset() {
        numExchanges.set(0L);
    }

    @ManagedAttribute(description = "Total number of exchanges")
    public long getNumExchanges() throws Exception {
        return numExchanges.get();
    }

    public long increment() {
        return numExchanges.incrementAndGet();
    }
}
