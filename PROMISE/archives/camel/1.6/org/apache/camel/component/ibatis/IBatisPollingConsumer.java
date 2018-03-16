package org.apache.camel.component.ibatis;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.impl.PollingConsumerSupport;

/**
 * @version $Revision: 647667 $
 */
public class IBatisPollingConsumer extends PollingConsumerSupport {
    private final IBatisEndpoint endpoint;

    public IBatisPollingConsumer(IBatisEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public Exchange receive(long timeout) {
        return receiveNoWait();
    }

    public Exchange receive() {
        return receiveNoWait();
    }

    public Exchange receiveNoWait() {
        try {
            Exchange exchange = endpoint.createExchange();
            Message in = exchange.getIn();
            endpoint.query(in);
            return exchange;
        } catch (Exception e) {
            throw new RuntimeCamelException("Failed to poll: " + endpoint + ". Reason: " + e, e);
        }
    }

    protected void doStart() throws Exception {
    }

    protected void doStop() throws Exception {
    }
}
