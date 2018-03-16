package org.apache.camel.management;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(
        description="PerformanceCounter", 
        currencyTimeLimit=15)
public class PerformanceCounter extends Counter {

	private AtomicLong numCompleted = new AtomicLong(0L);
	private long minProcessingTime = -1L;
	private long maxProcessingTime = -1L;
	private double totalProcessingTime = 0;
	
	@Override
	@ManagedOperation(description = "Reset counters")
	public synchronized void reset() {
		super.reset();
		numCompleted.set(0L);
		minProcessingTime = 0L;
		maxProcessingTime = 0L;
		totalProcessingTime = 0;
	}
	
	@ManagedAttribute(description = "Number of successful exchanges")
	public long getNumCompleted() throws Exception {
		return numCompleted.get();
	}

	@ManagedAttribute(description = "Number of failed exchanges")
	public long getNumFailed() throws Exception {
		return numExchanges.get() - numCompleted.get();
	}

	@ManagedAttribute(description = "Min Processing Time [usec]")
	public synchronized long getMinProcessingTime() throws Exception {
		return minProcessingTime;
	}

	@ManagedAttribute(description = "Mean Processing Time [usec]")
	public synchronized long getMeanProcessingTime() throws Exception {
		long count = numCompleted.get();
		return count > 0 ? (long)totalProcessingTime / count : 0L;
	}

	@ManagedAttribute(description = "Max Processing Time [usec]")
	public synchronized long getMaxProcessingTime() throws Exception {
		return maxProcessingTime;
	}
	
	public synchronized void completedExchange(long time) {
		increment();
		numCompleted.incrementAndGet();
		totalProcessingTime += time;
		if (minProcessingTime < 0 || time < minProcessingTime) {
			minProcessingTime = time;
		}
		if (time > maxProcessingTime) {
			maxProcessingTime = time;
		}
	}

	public void completedExchange() {
		numExchanges.incrementAndGet();
	}
}
