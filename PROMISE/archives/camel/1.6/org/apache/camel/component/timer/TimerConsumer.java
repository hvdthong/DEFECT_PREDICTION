package org.apache.camel.component.timer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The timer consumer.
 *
 * @version $Revision: 688957 $
 */
public class TimerConsumer extends DefaultConsumer<Exchange> {
    private static final transient Log LOG = LogFactory.getLog(TimerConsumer.class);
    private final TimerEndpoint endpoint;
    private TimerTask task;

    public TimerConsumer(TimerEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
    }

    @Override
    protected void doStart() throws Exception {
        task = new TimerTask() {
            @Override
            public void run() {
                sendTimerExchange();
            }
        };

        Timer timer = endpoint.getTimer();
        configureTask(task, timer);
    }

    @Override
    protected void doStop() throws Exception {
        task.cancel();
    }

    protected void configureTask(TimerTask task, Timer timer) {
        if (endpoint.isFixedRate()) {
            if (endpoint.getTime() != null) {
                timer.scheduleAtFixedRate(task, endpoint.getTime(), endpoint.getPeriod());
            } else {
                timer.scheduleAtFixedRate(task, endpoint.getDelay(), endpoint.getPeriod());
            }
        } else {
            if (endpoint.getTime() != null) {
                if (endpoint.getPeriod() >= 0) {
                    timer.schedule(task, endpoint.getTime(), endpoint.getPeriod());
                } else {
                    timer.schedule(task, endpoint.getTime());
                }
            } else {
                if (endpoint.getPeriod() >= 0) {
                    timer.schedule(task, endpoint.getDelay(), endpoint.getPeriod());
                } else {
                    timer.schedule(task, endpoint.getDelay());
                }
            }
        }
    }

    protected void sendTimerExchange() {
        Exchange exchange = endpoint.createExchange();
        exchange.setProperty("org.apache.camel.timer.name", endpoint.getTimerName());
        exchange.setProperty("org.apache.camel.timer.time", endpoint.getTime());
        exchange.setProperty("org.apache.camel.timer.period", endpoint.getPeriod());

        Date now = new Date();
        exchange.setProperty("org.apache.camel.timer.firedTime", now);
        exchange.getIn().setHeader("firedTime", now);

        try {
            getProcessor().process(exchange);
        } catch (Exception e) {
            LOG.error("Caught: " + e, e);
        }
    }
}
