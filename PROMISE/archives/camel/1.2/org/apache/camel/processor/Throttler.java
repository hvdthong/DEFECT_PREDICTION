package org.apache.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * will set a limit on the maximum number of message exchanges which can be sent
 * to a processor within a specific time period. <p/> This pattern can be
 * extremely useful if you have some external system which meters access; such
 * as only allowing 100 requests per second; or if huge load can cause a
 * particular systme to malfunction or to reduce its throughput you might want
 * to introduce some throttling.
 * 
 * @version $Revision: $
 */
public class Throttler extends DelayProcessorSupport {
    private long maximumRequestsPerPeriod;
    private long timePeriodMillis;
    private long startTimeMillis;
    private long requestCount;

    public Throttler(Processor processor, long maximumRequestsPerPeriod) {
        this(processor, maximumRequestsPerPeriod, 1000);
    }

    public Throttler(Processor processor, long maximumRequestsPerPeriod, long timePeriodMillis) {
        super(processor);
        this.maximumRequestsPerPeriod = maximumRequestsPerPeriod;
        this.timePeriodMillis = timePeriodMillis;
    }

    @Override
    public String toString() {
        return "Throttler[requests: " + maximumRequestsPerPeriod + " per: " + timePeriodMillis + " (ms) to: "
               + getProcessor() + "]";
    }

    public long getMaximumRequestsPerPeriod() {
        return maximumRequestsPerPeriod;
    }

    /**
     * Sets the maximum number of requests per time period
     */
    public void setMaximumRequestsPerPeriod(long maximumRequestsPerPeriod) {
        this.maximumRequestsPerPeriod = maximumRequestsPerPeriod;
    }

    public long getTimePeriodMillis() {
        return timePeriodMillis;
    }

    /**
     * Sets the time period during which the maximum number of requests apply
     */
    public void setTimePeriodMillis(long timePeriodMillis) {
        this.timePeriodMillis = timePeriodMillis;
    }

    /**
     * The number of requests which have taken place so far within this time
     * period
     */
    public long getRequestCount() {
        return requestCount;
    }

    /**
     * The start time when this current period began
     */
    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    protected void delay(Exchange exchange) throws Exception {
        long now = currentSystemTime();
        if (startTimeMillis == 0) {
            startTimeMillis = now;
        }
        if (now - startTimeMillis > timePeriodMillis) {
            requestCount = 1;
            startTimeMillis = now;
        } else {
            if (++requestCount > maximumRequestsPerPeriod) {
                long time = startTimeMillis + timePeriodMillis;
                waitUntil(time, exchange);
            }
        }
    }
}
