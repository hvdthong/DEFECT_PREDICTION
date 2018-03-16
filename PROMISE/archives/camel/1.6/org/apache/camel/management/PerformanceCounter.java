package org.apache.camel.management;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description = "PerformanceCounter", currencyTimeLimit = 15)
public class PerformanceCounter extends Counter {

    private AtomicLong numCompleted = new AtomicLong(0L);
    private double minProcessingTime;
    private double maxProcessingTime;
    private double totalProcessingTime;
    private Date lastExchangeCompletionTime;
    private Date lastExchangeFailureTime;
    private Date firstExchangeCompletionTime;
    private Date firstExchangeFailureTime;

    @Override
    @ManagedOperation(description = "Reset counters")
    public synchronized void reset() {
        super.reset();
        numCompleted.set(0L);
        minProcessingTime = 0.0;
        maxProcessingTime = 0.0;
        totalProcessingTime = 0.0;
        lastExchangeCompletionTime = null;
        lastExchangeFailureTime = null;
        firstExchangeCompletionTime = null;
        firstExchangeFailureTime = null;
    }

    @ManagedAttribute(description = "Number of successful exchanges")
    public long getNumCompleted() throws Exception {
        return numCompleted.get();
    }

    @ManagedAttribute(description = "Number of failed exchanges")
    public long getNumFailed() throws Exception {
        return numExchanges.get() - numCompleted.get();
    }

    @ManagedAttribute(description = "Min Processing Time [milliseconds]")
    public synchronized double getMinProcessingTimeMillis() throws Exception {
        return minProcessingTime;
    }

    @ManagedAttribute(description = "Mean Processing Time [milliseconds]")
    public synchronized double getMeanProcessingTimeMillis() throws Exception {
        long count = numCompleted.get();
        return count > 0 ? totalProcessingTime / count : 0.0;
    }

    @ManagedAttribute(description = "Max Processing Time [milliseconds]")
    public synchronized double getMaxProcessingTimeMillis() throws Exception {
        return maxProcessingTime;
    }

    @ManagedAttribute(description = "Total Processing Time [milliseconds]")
    public synchronized double getTotalProcessingTimeMillis() throws Exception {
        return totalProcessingTime;
    }

    @ManagedAttribute(description = "Last Exchange Completed Timestamp")
    public synchronized Date getLastExchangeCompletionTime() {
        return lastExchangeCompletionTime;
    }

    @ManagedAttribute(description = "First Exchange Completed Timestamp")
    public synchronized Date getFirstExchangeCompletionTime() {
        return firstExchangeCompletionTime;
    }

    @ManagedAttribute(description = "Last Exchange Failed Timestamp")
    public synchronized Date getLastExchangeFailureTime() {
        return lastExchangeFailureTime;
    }

    @ManagedAttribute(description = "First Exchange Failed Timestamp")
    public synchronized Date getFirstExchangeFailureTime() {
        return firstExchangeFailureTime;
    }

    /**
     * This method is called when an exchange has been processed successfully.
     * 
     * @param time in milliseconds it spent on processing the exchange
     */
    public synchronized void completedExchange(double time) {
        increment();
        numCompleted.incrementAndGet();

        totalProcessingTime += time;
        if (minProcessingTime <= 0 || time < minProcessingTime) {
            minProcessingTime = time;
        }
        if (time > maxProcessingTime) {
            maxProcessingTime = time;
        }
        
        Date timestamp = new Date();
        if (firstExchangeCompletionTime == null) {
            firstExchangeCompletionTime = timestamp;
        }
        lastExchangeCompletionTime = timestamp;
    }

    /**
     * This method is called when an exchange has been processed and failed.
     */
    public synchronized void failedExchange() {
        increment();

        Date timestamp = new Date();
        if (firstExchangeFailureTime == null) {
            firstExchangeFailureTime = timestamp;
        }
        lastExchangeFailureTime = timestamp;
    }
}
