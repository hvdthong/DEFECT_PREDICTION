package org.apache.camel.component.seda;

import java.util.List;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.PollingConsumerSupport;

/**
 * @deprecated not used. Will be removed in Camel 2.0.
 * @version $Revision: 663882 $
 */
@Deprecated
public class ListPollingConsumer extends PollingConsumerSupport {
    private final List<Exchange> exchanges;

    public ListPollingConsumer(Endpoint endpoint, List<Exchange> exchanges) {
        super(endpoint);
        this.exchanges = exchanges;
    }

    public Exchange receive() {
        return receiveNoWait();
    }

    public Exchange receiveNoWait() {
        if (exchanges.isEmpty()) {
            return null;
        } else {
            return exchanges.remove(0);
        }
    }

    public Exchange receive(long timeout) {
        return receiveNoWait();
    }

    protected void doStart() throws Exception {
    }

    protected void doStop() throws Exception {
    }
}
