package org.apache.camel.component.timer;

import java.util.Date;
import java.util.Timer;

import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.impl.DefaultEndpoint;

/**
 * Represents a timer endpoint that can generate periodic inbound PojoExchanges.
 *
 * @version $Revision: 660275 $
 */
public class TimerEndpoint extends DefaultEndpoint<Exchange> {
    private String timerName;
    private Date time;
    private long period = 1000;
    private long delay;
    private boolean fixedRate;
    private boolean daemon = true;
    private Timer timer;

    public TimerEndpoint(String fullURI, TimerComponent component, String timerName) {
        super(fullURI, component);
        this.timer = component.getTimer(this);
        this.timerName = timerName;
    }

    public TimerEndpoint(String endpointUri, Timer timer) {
        this(endpointUri);
        this.timer = timer;
    }

    public TimerEndpoint(String endpointUri) {
        super(endpointUri);
    }

    public Producer<Exchange> createProducer() throws Exception {
        throw new RuntimeCamelException("Cannot produce to a TimerEndpoint: " + getEndpointUri());
    }

    public Consumer<Exchange> createConsumer(Processor processor) throws Exception {
        return new TimerConsumer(this, processor);
    }

    public String getTimerName() {
        if (timerName == null) {
            timerName = getEndpointUri();
        }
        return timerName;
    }

    public void setTimerName(String timerName) {
        this.timerName = timerName;
    }

    public boolean isDaemon() {
        return daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public boolean isFixedRate() {
        return fixedRate;
    }

    public void setFixedRate(boolean fixedRate) {
        this.fixedRate = fixedRate;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isSingleton() {
        return true;
    }

    public Timer getTimer() {
        if (timer == null) {
            timer = new Timer();
        }
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
}
