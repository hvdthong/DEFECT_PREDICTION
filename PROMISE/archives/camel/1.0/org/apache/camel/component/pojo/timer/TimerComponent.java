package org.apache.camel.component.pojo.timer;

import org.apache.camel.component.pojo.PojoExchange;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.Endpoint;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represents the component that manages {@link TimerEndpoint}.  It holds the
 * list of {@link TimerConsumer} objects that are started.
 *
 * @version $Revision: 519973 $
 */
public class TimerComponent extends DefaultComponent<PojoExchange> {
    protected final ArrayList<TimerConsumer> timers = new ArrayList<TimerConsumer>();

    boolean addConsumer(TimerConsumer consumer) {
        return timers.add(consumer);
    }

    boolean removeConsumer(TimerConsumer consumer) {
        return timers.remove(consumer);
    }

    @Override
    protected Endpoint<PojoExchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new TimerEndpoint(uri, this, remaining);
    }
}
