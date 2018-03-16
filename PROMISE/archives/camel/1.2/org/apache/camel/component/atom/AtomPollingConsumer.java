package org.apache.camel.component.atom;

import org.apache.abdera.model.Document;
import org.apache.abdera.model.Feed;
import org.apache.abdera.util.iri.IRISyntaxException;
import org.apache.camel.Exchange;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.impl.PollingConsumerSupport;

import java.io.IOException;

/**
 * @version $Revision: 1.1 $
 */
public class AtomPollingConsumer extends PollingConsumerSupport {
    private final AtomEndpoint endpoint;

    public AtomPollingConsumer(AtomEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    protected void doStart() throws Exception {
    }

    protected void doStop() throws Exception {
    }

    public Exchange receiveNoWait() {
        try {
            Document<Feed> document = endpoint.parseDocument();
            Exchange exchange = endpoint.createExchange();
            exchange.getIn().setBody(document);
            return exchange;
        }
        catch (IRISyntaxException e) {
            throw new RuntimeCamelException(e);
        }
        catch (IOException e) {
            throw new RuntimeCamelException(e);
        }
    }

    public Exchange receive() {
        return receiveNoWait();
    }

    public Exchange receive(long timeout) {
        return receiveNoWait();
    }
}
