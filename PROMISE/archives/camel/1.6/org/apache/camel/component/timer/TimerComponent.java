package org.apache.camel.component.timer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link TimerEndpoint}.  It holds the
 * list of {@link TimerConsumer} objects that are started.
 *
 * @version $Revision: 640438 $
 */
public class TimerComponent extends DefaultComponent<Exchange> {
    private Map<String, Timer> timers = new HashMap<String, Timer>();

    public Timer getTimer(TimerEndpoint endpoint) {
        String key = endpoint.getTimerName();
        if (!endpoint.isDaemon()) {
            key = "nonDaemon:" + key;
        }

        Timer answer = timers.get(key);
        if (answer == null) {
            answer = new Timer(endpoint.getTimerName(), endpoint.isDaemon());
            timers.put(key, answer);
        }
        return answer;
    }

    @Override
    protected Endpoint<Exchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        TimerEndpoint answer = new TimerEndpoint(uri, this, remaining);
        setProperties(answer, parameters);
        return answer;
    }

    @Override
    protected void doStop() throws Exception {
        Collection<Timer> collection = timers.values();
        for (Timer timer : collection) {
            timer.cancel();
        }
        timers.clear();
    }
}
