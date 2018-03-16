package org.apache.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.util.ExpressionHelper;

/**
 * delays processing the exchange until the correct amount of time has elapsed
 * using an expression to determine the delivery time. <p/> For example if you
 * wish to delay JMS messages by 25 seconds from their publish time you could
 * create an instance of this class with the expression
 * <code>header("JMSTimestamp")</code> and a delay value of 25000L.
 * 
 * @version $Revision: 630591 $
 */
public class Delayer extends DelayProcessorSupport {
    private Expression<Exchange> timeExpression;
    private long delay;

    public Delayer(Processor processor, Expression<Exchange> timeExpression, long delay) {
        super(processor);
        this.timeExpression = timeExpression;
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "Delayer[on: " + timeExpression + " delay: " + delay + " to: " + getProcessor() + "]";
    }

    public long getDelay() {
        return delay;
    }

    /**
     * Sets the delay from the publish time; which is typically the time from
     * the expression or the current system time if none is available
     */
    public void setDelay(long delay) {
        this.delay = delay;
    }


    /**
     * Waits for an optional time period before continuing to process the
     * exchange
     */
    protected void delay(Exchange exchange) throws Exception {
        long time = 0;
        if (timeExpression != null) {
            Long longValue = ExpressionHelper.evaluateAsType(timeExpression, exchange, Long.class);
            if (longValue != null) {
                time = longValue.longValue();
            }
        }
        if (time <= 0) {
            time = defaultProcessTime(exchange);
        }

        time += delay;

        waitUntil(time, exchange);
    }

    /**
     * A Strategy Method to allow derived implementations to decide the current
     * system time or some other default exchange property
     * 
     * @param exchange
     */
    protected long defaultProcessTime(Exchange exchange) {
        return currentSystemTime();
    }

}
