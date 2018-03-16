package org.apache.camel.component.atom;

import org.apache.abdera.model.Document;
import org.apache.abdera.model.Feed;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Consumer to poll atom feeds and return the full feed.
 *
 * @version $Revision: 656933 $
 */
public class AtomPollingConsumer extends AtomConsumerSupport {

    public AtomPollingConsumer(AtomEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
    }

    protected void poll() throws Exception {
        Document<Feed> document = AtomUtils.parseDocument(endpoint.getAtomUri());
        Feed feed = document.getRoot();
        Exchange exchange = endpoint.createExchange(feed);
        getProcessor().process(exchange);
    }

}
